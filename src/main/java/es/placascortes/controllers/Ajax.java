/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.placascortes.controllers;

import com.google.gson.Gson;
import es.placascortes.DAO.ILineaPedidoDAO;
import es.placascortes.DAO.IPedidoDAO;
import es.placascortes.DAO.IProductoDAO;
import es.placascortes.DAO.IUsuarioDAO;
import es.placascortes.DAOFactory.DAOFactory;
import es.placascortes.DAOFactory.MySQLDAOFactory;
import es.placascortes.beans.Carrito;
import es.placascortes.beans.Producto;
import es.placascortes.beans.Usuario;
import es.placascortes.utilities.Utilities;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author _
 */
@WebServlet(name = "Ajax", urlPatterns = {"/Ajax"})
public class Ajax extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map map = request.getParameterMap();
        String accion = request.getParameter("accion");
        JSONObject objeto = null;
        JSONArray arrayJSON = null;
        List listadoProdusctosFiltrados = null;
        List listadoCarrito = null;

        Usuario usuario = null;
        Producto producto = null;
        Carrito carrito = null;

        DAOFactory daof = new MySQLDAOFactory();
        String correoElectronico = null;
        Boolean correoValido = null;
        String aviso = null;
        Gson g = null;

        Short estado = null;
        Short cantidadProductoEnCarrito = null;
        Short idPedido = null;

        Integer productosEnCarrito = null;
        Float precioTotalSinIVA = null;
        Float precioTotalConIVA = null;
        Float diferenciaDeIVA = null;

        Short idProducto = null;
        List retornoObj = null;
        Iterator<Producto> iteratorProducto = null;
        Iterator<Carrito> iteratorCarrito = null;
        HttpSession sesion = null;

        IUsuarioDAO udao = null;
        IProductoDAO pdao = null;
        IPedidoDAO pedao = null;
        ILineaPedidoDAO pldao = null;

        switch (accion) {
            case "buscador":
                g = new Gson();
                String[][] buscadorArr = g.fromJson(request.getParameter("arreglo"), String[][].class);

                arrayJSON = new JSONArray();

                pdao = daof.getProductoDAO();
                listadoProdusctosFiltrados = pdao.getProductosFiltrados(buscadorArr);

                iteratorProducto = listadoProdusctosFiltrados.iterator();
                while (iteratorProducto.hasNext()) {
                    producto = iteratorProducto.next();

                    // creamos un objeto json para devolverlo al ajax con el aviso que tiene que mostrar
                    objeto = new JSONObject();
                    objeto.put("idProducto", producto.getIdProducto());
                    objeto.put("nombreProducto", producto.getNombre());
                    objeto.put("precio", producto.getPrecio());
                    objeto.put("marca", producto.getMarca());
                    objeto.put("imagenProducto", producto.getImagen());
                    objeto.put("imagenCategoria", producto.getCategoria().getImagen());

                    arrayJSON.add(objeto);
                }

                // mandamos el json
                response.setContentType("application/text");
                response.getWriter().print(arrayJSON);
                break;
            case "detallesProducto":
                g = new Gson();
                idProducto = g.fromJson(request.getParameter("arreglo"), Short.class);

                pdao = daof.getProductoDAO();
                producto = pdao.getProductoDetallesPorId(idProducto);

                objeto = new JSONObject();
                objeto.put("nombreProducto", producto.getNombre());
                objeto.put("precio", producto.getPrecio());
                objeto.put("marca", producto.getMarca());
                objeto.put("descripcion", producto.getDescripcion());
                objeto.put("imagenProducto", producto.getImagen());
                objeto.put("imagenCategoria", producto.getCategoria().getImagen());
                objeto.put("nombreCategoria", producto.getCategoria().getNombre());

                // mandamos el json
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "correoExistente":
                g = new Gson();
                correoElectronico = g.fromJson(request.getParameter("arreglo"), String.class);

                udao = daof.getUsuarioDAO();
                correoValido = udao.correoEsValido(correoElectronico);

                objeto = new JSONObject();
                objeto.put("correoValido", correoValido);

                // mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "carritoAjax":
                g = new Gson();
                String[] accionesACarritoArr = g.fromJson(request.getParameter("arreglo"), String[].class);

                idProducto = Short.parseShort(accionesACarritoArr[0]);
                sesion = request.getSession();

                if (!Utilities.carritoEstaEnSesion(sesion) && !Utilities.usuarioEstaEnSesion(sesion)) {
                    Utilities.leerCoockie(sesion, request);
                }

                cantidadProductoEnCarrito = Utilities.accionesCarrito(
                        sesion,
                        idProducto,
                        accionesACarritoArr[1]);
                productosEnCarrito = Utilities.sumUnidadesCarrito((List<Carrito>) sesion.getAttribute("carrito"));
                sesion.setAttribute("sumaProductosEnCarrito", productosEnCarrito);

                if (Utilities.usuarioEstaEnSesion(sesion)) {
                    usuario = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
                    pedao = daof.getPedidoDAO();
                    pldao = daof.getLineaPedidoDAO();
                    idPedido = pedao.getPedidoIdDeCarritoUsuario(usuario.getIdUsuario());

                    if (productosEnCarrito == 0) {
                        pldao.eliminarLinea(idPedido, idProducto);
                        pedao.eliminarPedido(idPedido);
                    } else {
                        if (cantidadProductoEnCarrito == 0) {
                            pldao.eliminarLinea(idPedido, idProducto);
                        } else {
                            if (productosEnCarrito == 1 && accionesACarritoArr[1].equals("anadirACarrito")) {
                                idPedido = pedao.crearPedido(usuario.getIdUsuario());
                            }
                            if (cantidadProductoEnCarrito == 1 && accionesACarritoArr[1].equals("anadirACarrito")) {
                                pldao.insertarLinea(idPedido, cantidadProductoEnCarrito, idProducto);
                            } else {
                                pldao.actualizarLinea(idPedido, cantidadProductoEnCarrito, idProducto);
                            }
                        }
                    }

                } else {
                    Utilities.crearCookie(sesion, response);
                }

                objeto = new JSONObject();
                objeto.put("productosEnCarrito", productosEnCarrito);
                objeto.put("cantidadProductoEnCarrito", cantidadProductoEnCarrito);

                // mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "productoEnCarrito":
                g = new Gson();
                idProducto = g.fromJson(request.getParameter("arreglo"), Short.class);

                sesion = request.getSession();

                if (!Utilities.usuarioEstaEnSesion(sesion) && !Utilities.usuarioEstaEnSesion(sesion)) {
                    Utilities.leerCoockie(sesion, request);
                }

                objeto = new JSONObject();
                objeto.put("cantidadProductoEnCarrito",
                        Utilities.productoEnCarrito(
                                (List<Carrito>) request.getSession().getAttribute("carrito"),
                                idProducto));

                // mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "carritoFactura":
                listadoCarrito = (List) request.getSession().getAttribute("carrito");

                precioTotalSinIVA = Utilities.sumPreciosCarrito(listadoCarrito);
                precioTotalConIVA = precioTotalSinIVA * 1.21f;
                diferenciaDeIVA = precioTotalSinIVA * .21f;

                arrayJSON = new JSONArray();
                iteratorCarrito = listadoCarrito.iterator();
                while (iteratorCarrito.hasNext()) {
                    carrito = iteratorCarrito.next();

                    objeto = new JSONObject();
                    objeto.put("nombre", carrito.getProducto().getNombre());
                    objeto.put("precio", carrito.getProducto().getPrecio());
                    objeto.put("cantidad", carrito.getCantidad());

                    arrayJSON.add(objeto);
                }

                objeto = new JSONObject();
                objeto.put("precioTotalSinIVA", precioTotalSinIVA);
                objeto.put("precioTotalConIVA", precioTotalConIVA);
                objeto.put("diferenciaDeIVA", diferenciaDeIVA);
                objeto.put("listadoCarrito", arrayJSON);

                // mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "2":

                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
