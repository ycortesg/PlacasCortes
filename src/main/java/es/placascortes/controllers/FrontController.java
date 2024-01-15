/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.placascortes.controllers;

import es.placascortes.DAO.IPedidoDAO;
import es.placascortes.DAO.IProductoDAO;
import es.placascortes.DAO.IUsuarioDAO;
import es.placascortes.DAO.PedidoDAO;
import es.placascortes.DAOFactory.DAOFactory;
import es.placascortes.DAOFactory.MySQLDAOFactory;
import es.placascortes.beans.Pedido;
import es.placascortes.beans.Producto;
import es.placascortes.beans.Usuario;
import es.placascortes.utilities.Utilities;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author _
 */
public class FrontController extends HttpServlet {

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
        request.getRequestDispatcher("index.jsp").forward(request, response);
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
        String urlDispatcher = null;
        String opcion = request.getParameter("opcion");
        List<Producto> listadoProducto = null;
        List<String> listadoFechasPedido = null;
        Usuario usuario = null;
        Pedido pedido = null;
        Boolean actualizado = null;
        DAOFactory daof = null;
        IProductoDAO pdao = null;
        IUsuarioDAO udao = null;
        IPedidoDAO pedao = null;

        switch (opcion) {
            case "inicioSesion":
                
                // Si el formulario esta relleno
                if (Utilities.formularioEstaRelleno(request.getParameterMap())) {
                    try {
                        
                        // Rellenamos un usuario nuevo con los datos introducidos
                        usuario = new Usuario();
                        BeanUtils.populate(usuario, request.getParameterMap());

                        // Declaramos los dao
                        daof = new MySQLDAOFactory();
                        udao = daof.getUsuarioDAO();

                        usuario = udao.usuarioEsValido(usuario);

                        // Si el correo y la contrasena coinciden
                        if (usuario.getIdUsuario() != null) {
                            
                            // Actualiza el ultimo acceso del usuario
                            udao.actualizarUltimoAcceso(usuario.getIdUsuario());
                            
                            // Introduce el usuario a un atributo de sesion
                            request.getSession().setAttribute("usuarioEnSesion", usuario);

                            // Creamos el carrito a partir de la base de datos
                            pedao = new PedidoDAO();
                            pedido = pedao.crearCarritoDeLineasPedido(usuario.getIdUsuario());

                            // Si el pedido no esta vacio introduce el idPedido
                            if (!pedido.getListadoLineasPedido().isEmpty()) {
                                pedao = daof.getPedidoDAO();
                                pedido.setIdPedido(pedao.getPedidoIdDeCarritoUsuario(usuario.getIdUsuario()));
                            }

                            // Enviamos mensaje
                            Utilities.enviarAvisoRequest(request,
                                    "Has iniciado sesion correctamente",
                                    false);
                            request.getSession().setAttribute("carrito", pedido);
                        } else {
                            // Enviamos mensaje de error
                            Utilities.enviarAvisoRequest(request,
                                    "El usuario o la contrasena son incorrectos",
                                    true);
                        }
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        // Enviamos mensaje de error
                        Utilities.enviarAvisoRequest(request,
                                "Ha ocurrido algun error al intentar iniciar sesion",
                                true);
                        Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    // Enviamos mensaje de error
                    Utilities.enviarAvisoRequest(request,
                            "Para iniciar sesion necesitas rellenar el email y la contrasena",
                            true);
                }
                urlDispatcher = "index.jsp";
                break;
            case "registro":
                urlDispatcher = "JSP/registro.jsp";
                break;
            case "cuenta":
                // Si el usuario esta en sesion
                usuario = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
                if (usuario != null) {
                    
                    // Declaramos los dao
                    daof = new MySQLDAOFactory();
                    udao = daof.getUsuarioDAO();
                    pedao = daof.getPedidoDAO();

                    // Introducimos detalles al usuario en sesion y el listado de las fechas de pedidos finalizados del usaurio en sesion
                    usuario = udao.anadirDetallesAUsuario(usuario);
                    listadoFechasPedido = pedao.getFechasPedidos(usuario.getIdUsuario());
                    request.getSession().setAttribute("usuarioEnSesion", usuario);
                    request.getSession().setAttribute("fechasPedidos", listadoFechasPedido);
                    
                    urlDispatcher = "JSP/menuUsuario.jsp";
                } else {
                    // Enviamos mensaje de error
                    Utilities.enviarAvisoRequest(request, 
                                "Para acceder al menu de la cuenta necesitas iniciar sesion", 
                                true);
                    urlDispatcher = "index.jsp";
                }
                break;
            case "cerrerSesion":
                // Eliminamos el carrito de la sesion
                if (Utilities.carritoEstaEnSesion(request.getSession())) {
                    request.getSession().removeAttribute("carrito");
                }
                
                // Eliminamos el usuario de sesion
                request.getSession().removeAttribute("usuarioEnSesion");
                
                // Enviamos mensaje 
                Utilities.enviarAvisoRequest(request, 
                                "Has cerrado sesion correctamente", 
                                false);
                urlDispatcher = "index.jsp";
                break;
            case "carrito":
                
                // Si el carrito no esta en sesion lee la cookie
                if (!Utilities.carritoEstaEnSesion(request.getSession())) {
                    Utilities.leerCookie(request.getSession(), request);
                }
                
                if (Utilities.carritoEstaEnSesion(request.getSession())) {
                    // Declaramos los dao
                    daof = new MySQLDAOFactory();
                    pdao = daof.getProductoDAO();
                    
                    // Recogemos el pedido carrito de sesion y le anadimos detalles a los productos
                    pedido = (Pedido) request.getSession().getAttribute("carrito");
                    listadoProducto = pdao.getDetallesCarrito(pedido.getListadoLineasPedido());
                    
                    // Actualiza los datos de la lista del carrito en sesion
                    for (Integer i = 0; i < pedido.getListadoLineasPedido().size(); i++) {
                        actualizado = false;
                        for (Integer j = 0; j < listadoProducto.size() && !actualizado; j++) {
                            if (listadoProducto.get(j).getIdProducto().equals(pedido.getListadoLineasPedido().get(i).getProducto().getIdProducto())) {
                                actualizado = true;
                                pedido.getListadoLineasPedido().get(i).setProducto(listadoProducto.get(j));
                            }
                        }
                    }
                    
                    request.setAttribute("carrito", pedido);
                }
                urlDispatcher = "JSP/carrito.jsp";
                break;
            default:
                throw new AssertionError();
        }

        request.getRequestDispatcher(urlDispatcher).forward(request, response);

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
