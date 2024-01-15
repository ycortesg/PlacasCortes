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
import es.placascortes.beans.LineaPedido;
import es.placascortes.beans.Pedido;
import es.placascortes.beans.Producto;
import es.placascortes.beans.Usuario;
import es.placascortes.utilities.Utilities;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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

        // Declaramos variables
        String accion = request.getParameter("accion");
        JSONObject objeto = null;
        JSONArray arrayJSON = null;
        List listadoProdusctosFiltrados = null;
        List listadoPedidos = null;

        Usuario usuario = null;
        Producto producto = null;
        Pedido pedido = null;
        LineaPedido lineaPedido = null;

        String correoElectronico = null;
        Boolean correoValido = null;
        Gson g = null;

        Byte cantidadProductoEnCarrito = null;

        Date fechaPedido = null;

        Short idPedido = null;

        Integer productosEnCarrito = null;
        Float precioTotalSinIVA = null;
        Float precioTotalConIVA = null;
        Float diferenciaDeIVA = null;

        Short idProducto = null;
        Iterator<Producto> iteratorProducto = null;
        Iterator<LineaPedido> iteratorLineaPedido = null;
        Iterator<Pedido> iteratorPedido = null;
        HttpSession sesion = null;

        DAOFactory daof = null;
        IUsuarioDAO udao = null;
        IProductoDAO pdao = null;
        IPedidoDAO pedao = null;
        ILineaPedidoDAO pldao = null;

        switch (accion) {
            case "buscador":
                // Recogemos los filtros introducidos por ajax
                g = new Gson();
                String[][] buscadorArr = g.fromJson(request.getParameter("arreglo"), String[][].class);

                arrayJSON = new JSONArray();

                // Declaramos los dao
                daof = new MySQLDAOFactory();
                pdao = daof.getProductoDAO();
                
                // Recogemos el listado de productos filtrados por los terminos introducidos
                listadoProdusctosFiltrados = pdao.getProductosFiltrados(buscadorArr);

                // Declaramos el iterador y lo recorremos
                iteratorProducto = listadoProdusctosFiltrados.iterator();
                while (iteratorProducto.hasNext()) {
                    producto = iteratorProducto.next();

                    // Creamos un objeto json para devolverlo al ajax con los datos que hemos recogido anteriormente
                    objeto = new JSONObject();
                    
                    // Lo rellenamos
                    objeto.put("idProducto", producto.getIdProducto());
                    objeto.put("nombreProducto", producto.getNombre());
                    objeto.put("precio", producto.getPrecio());
                    objeto.put("marca", producto.getMarca());
                    objeto.put("imagenProducto", producto.getImagen());
                    objeto.put("imagenCategoria", producto.getCategoria().getImagen());

                    // Lo anadimos al arrayJSON
                    arrayJSON.add(objeto);
                }

                // Mandamos el arrayjson
                response.setContentType("application/text");
                response.getWriter().print(arrayJSON);
                break;
            case "detallesProducto":
                // Recogemos los el id del pedido de ajax
                g = new Gson();
                idProducto = g.fromJson(request.getParameter("arreglo"), Short.class);

                // Declaramos los dao
                daof = new MySQLDAOFactory();
                pdao = daof.getProductoDAO();
                
                // Recogemos los detalles del producto por el id recogido
                producto = pdao.getProductoDetallesPorId(idProducto);

                // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                objeto = new JSONObject();
                objeto.put("nombreProducto", producto.getNombre());
                objeto.put("precio", producto.getPrecio());
                objeto.put("marca", producto.getMarca());
                objeto.put("descripcion", producto.getDescripcion());
                objeto.put("imagenProducto", producto.getImagen());
                objeto.put("imagenCategoria", producto.getCategoria().getImagen());
                objeto.put("nombreCategoria", producto.getCategoria().getNombre());

                // Mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "correoExistente":
                // Recogemos el correo introducido por ajax
                g = new Gson();
                correoElectronico = g.fromJson(request.getParameter("arreglo"), String.class);

                // Declaramos los dao
                daof = new MySQLDAOFactory();
                udao = daof.getUsuarioDAO();
                
                // Recogemos si el correo es valido o no
                correoValido = udao.correoEsValido(correoElectronico);

                // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                objeto = new JSONObject();
                objeto.put("correoValido", correoValido);

                // Mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "carritoAjax":
                // Recogemos el idProducto y la accion del ajax
                g = new Gson();
                String[] accionesACarritoArr = g.fromJson(request.getParameter("arreglo"), String[].class);

                // Declaramos el idProducto y la sesion
                idProducto = Short.valueOf(accionesACarritoArr[0]);
                sesion = request.getSession();

                // Si el carrito no esta en sesion y el usuario tampoco lee la cookie
                if (!Utilities.carritoEstaEnSesion(sesion) && !Utilities.usuarioEstaEnSesion(sesion)) {
                    Utilities.leerCookie(sesion, request);
                }
                
                // Recogemos el pedido de sesion
                pedido = (Pedido) sesion.getAttribute("carrito");

                // Si el pedido es nulo crea uno y le anade el listado de lineas
                if (pedido == null) {
                    pedido = new Pedido();
                    pedido.setListadoLineasPedido(new ArrayList());
                }

                // Hace la accion introducida por ajax al carrito y recoge la cantidad
                // de ese producto hay en el carrito
                cantidadProductoEnCarrito = Utilities.accionesCarrito(
                        sesion,
                        idProducto,
                        accionesACarritoArr[1]);
                
                // Recoge el numero de unidades de productos que hay en el carrito
                productosEnCarrito = Utilities.sumUnidadesCarrito(pedido.getListadoLineasPedido());

                // Si el usuario esta en sesion
                if (Utilities.usuarioEstaEnSesion(sesion)) {
                    usuario = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
                    
                    // Declaramos los dao
                    daof = new MySQLDAOFactory();
                    pedao = daof.getPedidoDAO();
                    pldao = daof.getLineaPedidoDAO();

                    // Si la cantidad de producto es 0 elimina la linea de la base de datos
                    if (cantidadProductoEnCarrito == 0) {
                        if (pedido.getIdPedido() != null){
                            pldao.eliminarLinea(pedido.getIdPedido(), idProducto);
                            
                            // Si la cantidad total es 0 elimina el pedido de la base de datos
                            if (productosEnCarrito == 0) pedao.eliminarPedido(pedido.getIdPedido());
                        }
                    } else {
                        // Si se anade uno desde 0 anade un pedido a la base de datos
                        if (productosEnCarrito == 1 && accionesACarritoArr[1].equals("anadirACarrito")) {
                            pedido.setIdPedido(pedao.crearPedido(usuario.getIdUsuario()));
                        }
                        
                        // Si la el producto no estaba en el pedido lo anade si si lo actualiza en la base de datos
                        if (cantidadProductoEnCarrito == 1 && accionesACarritoArr[1].equals("anadirACarrito")) {
                            pldao.insertarLinea(pedido.getIdPedido(), cantidadProductoEnCarrito, idProducto);
                        } else {
                            pldao.actualizarLinea(pedido.getIdPedido(), cantidadProductoEnCarrito, idProducto);
                        }

                    }
                } else {
                    // Actualiza la cookie
                    Utilities.crearCookie(sesion, response);
                }

                // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                objeto = new JSONObject();
                objeto.put("productosEnCarrito", productosEnCarrito);
                objeto.put("cantidadProductoEnCarrito", cantidadProductoEnCarrito);

                // Mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "productoEnCarrito":
                // Recogemos el idProducto por ajax
                g = new Gson();
                idProducto = g.fromJson(request.getParameter("arreglo"), Short.class);
                
                sesion = request.getSession();

                // Si el carrito no esta en sesion y el usuario tampoco lee la cookie
                if (!Utilities.usuarioEstaEnSesion(sesion) && !Utilities.usuarioEstaEnSesion(sesion)) {
                    Utilities.leerCookie(sesion, request);
                }
                
                // Recogemos el pedido de sesion
                pedido = (Pedido) sesion.getAttribute("carrito");

                // Si el pedido es nulo crea uno y le anade el listado de lineas
                if (pedido == null) {
                    pedido = new Pedido();
                    pedido.setListadoLineasPedido(new ArrayList());
                }

                // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                objeto = new JSONObject();
                objeto.put("cantidadProductoEnCarrito",
                        Utilities.productoEnCarrito(
                                pedido.getListadoLineasPedido(),
                                idProducto));

                // Mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "carritoFactura":
                // Recogemos el pedido de sesion
                pedido = (Pedido) request.getSession().getAttribute("carrito");

                // Declaramos los valores que luego pasaremos al ajax
                precioTotalSinIVA = Utilities.sumPreciosCarrito(pedido.getListadoLineasPedido());
                precioTotalConIVA = precioTotalSinIVA * 1.21f;
                diferenciaDeIVA = precioTotalSinIVA * .21f;

                // Declaramos el JSONArray y el iterator de lineasPedido y lo recorremos
                arrayJSON = new JSONArray();
                iteratorLineaPedido = pedido.getListadoLineasPedido().iterator();
                while (iteratorLineaPedido.hasNext()) {
                    lineaPedido = iteratorLineaPedido.next();

                    // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                    objeto = new JSONObject();
                    objeto.put("nombre", lineaPedido.getProducto().getNombre());
                    objeto.put("precio", lineaPedido.getProducto().getPrecio());
                    objeto.put("cantidad", lineaPedido.getCantidad());

                    // Anadimos el objeto al arrayJSON
                    arrayJSON.add(objeto);
                }

                // Creamos un objetoJSON y lo rellenamos con los datos recogidos y el JSONArray rellenado
                objeto = new JSONObject();
                objeto.put("precioTotalSinIVA", precioTotalSinIVA);
                objeto.put("precioTotalConIVA", precioTotalConIVA);
                objeto.put("diferenciaDeIVA", diferenciaDeIVA);
                objeto.put("listadoCarrito", arrayJSON);

                // Mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "pedidosPorFecha":
                // Recogemos la fecha introducida por ajax
                g = new Gson();
                fechaPedido = g.fromJson(request.getParameter("arreglo"), Date.class);
                usuario = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
                
                // Declaramos los dao
                daof = new MySQLDAOFactory();
                pedao = daof.getPedidoDAO();

                // Recogemos el listado de pedidos finalizados del usuario en sesion en la fecha introducida
                listadoPedidos = pedao.getPedidosDeFechaYUsuario(usuario.getIdUsuario(), fechaPedido);
                
                // Declaramos el iterator y el arrayJSOn
                iteratorPedido = listadoPedidos.iterator();
                arrayJSON = new JSONArray();
                while (iteratorPedido.hasNext()) {
                    pedido = iteratorPedido.next();
                    objeto = new JSONObject();

                    // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                    objeto.put("idPedido", pedido.getIdPedido());
                    objeto.put("importe", pedido.getImporte());
                    objeto.put("iva", pedido.getIva());

                    // Anadimos el objeto al arrayJSON
                    arrayJSON.add(objeto);
                }

                // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                objeto = new JSONObject();
                objeto.put("listadoPedido", arrayJSON);

                // Mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
                break;
            case "detallesPedidoFinalizado":
                // Recogemos el idPedido introducido por ajax
                g = new Gson();
                idPedido = g.fromJson(request.getParameter("arreglo"), Short.class);
                
                // Declaramos los dao
                daof = new MySQLDAOFactory();
                pedao = daof.getPedidoDAO();
                
                // Recogemos el pedido finalizado con los detalles por el idPedido
                pedido = pedao.getPedidoFinalizadoPorId(idPedido);
                
                // Declaramos el arrayJSON y el iterator
                arrayJSON = new JSONArray();
                iteratorLineaPedido = pedido.getListadoLineasPedido().iterator();
                while (iteratorLineaPedido.hasNext()) {
                    lineaPedido = iteratorLineaPedido.next();
                    // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                    objeto = new JSONObject();
                    
                    objeto.put("nombre", lineaPedido.getProducto().getNombre());
                    objeto.put("precio", lineaPedido.getProducto().getPrecio());
                    objeto.put("imagen", lineaPedido.getProducto().getImagen());
                    objeto.put("cantidad", lineaPedido.getCantidad());
                    
                    // Anadimos el objeto creado al arrayJSON
                    arrayJSON.add(objeto);
                }
                
                // Creamos un objetoJSON y lo rellenamos con los datos recogidos
                objeto = new JSONObject();
                objeto.put("listadoProductosPedido", arrayJSON);

                // Mandamos el objeto
                response.setContentType("application/text");
                response.getWriter().print(objeto);
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
