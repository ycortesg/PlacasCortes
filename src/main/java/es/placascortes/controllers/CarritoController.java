/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.placascortes.controllers;

import es.placascortes.DAO.ILineaPedidoDAO;
import es.placascortes.DAO.IPedidoDAO;
import es.placascortes.DAOFactory.DAOFactory;
import es.placascortes.DAOFactory.MySQLDAOFactory;
import es.placascortes.beans.LineaPedido;
import es.placascortes.beans.Pedido;
import es.placascortes.beans.Usuario;
import es.placascortes.utilities.Utilities;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author _
 */
@WebServlet(name = "CarritoController", urlPatterns = {"/CarritoController"})
public class CarritoController extends HttpServlet {

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
        Pedido pedido = null;
        Usuario usuario = null;
        LineaPedido lineaPedido = null;
        Iterator<LineaPedido> iteratorLineaPedido = null;
        DAOFactory daof = null;
        IPedidoDAO pedao = null;
        ILineaPedidoDAO pldao = null;
        HttpSession sesion = null;
        
        switch (opcion) {
            case "registrate":
                urlDispatcher = "JSP/registro.jsp";
                break;
            case "finalizarCompra":
                sesion = request.getSession();
                
                // Si el usuario y el carrito esta en sesion
                if (Utilities.usuarioEstaEnSesion(sesion) && Utilities.carritoEstaEnSesion(sesion)) {
                    try {
                        urlDispatcher = "index.jsp";
                        
                        // Declaramos los dao
                        daof = new MySQLDAOFactory();
                        pedao = daof.getPedidoDAO();
                        
                        // Recogemos el usuario de sesion
                        usuario = (Usuario) sesion.getAttribute("usuarioEnSesion");
                        
                        // Rellenamos un pedido nuevo con la informacion introductida que es el importe y el iva
                        pedido = new Pedido();
                        BeanUtils.populate(pedido, request.getParameterMap());
                        pedido.setIdPedido(pedao.getPedidoIdDeCarritoUsuario(usuario.getIdUsuario()));
                        pedido.setFecha(new Date());
                        
                        // Finalizamos el pedido en la base de datos
                        pedao.finalizarPedido(pedido);
                        
                        // Eliminamos el carrito de la sesion
                        Utilities.eliminarCarrito(sesion);
                        
                        // Enviamos mensaje
                        Utilities.enviarAvisoRequest(request, 
                                "Has realizado la compra por in valor de "+String.format("%.2f", pedido.getImporte())+" € + IVA", 
                                false);
                        
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        // Enviamos mensaje de error
                        Utilities.enviarAvisoRequest(request, 
                                "Ha ocurrido algun error al finalizar la compra", 
                                true);
                        urlDispatcher = "JSP/carrito.jsp";
                        Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    // Enviamos mensaje de error
                    Utilities.enviarAvisoRequest(request, 
                                "Necesitas iniciar sesion para finalizar la compra", 
                                true);
                    urlDispatcher = "JSP/carrito.jsp";
                }
                break;
            case "eliminarCarrito":
                sesion = request.getSession();
                urlDispatcher = "index.jsp";
                if (Utilities.carritoEstaEnSesion(sesion)){
                    if (Utilities.usuarioEstaEnSesion(sesion)) {
                        // Declaramos los dao
                        daof = new MySQLDAOFactory();
                        pldao = daof.getLineaPedidoDAO();
                        pedao = daof.getPedidoDAO();
                        
                        // Declaramos el pedido de carrito y el iterator
                        pedido = (Pedido) sesion.getAttribute("carrito");
                        iteratorLineaPedido = pedido.getListadoLineasPedido().iterator();
                        while (iteratorLineaPedido.hasNext()) {
                            lineaPedido = iteratorLineaPedido.next();
                            
                            // Elimina la linea de pedido de la base de datos
                            pldao.eliminarLinea(pedido.getIdPedido(), lineaPedido.getProducto().getIdProducto());
                        }
                        
                        // Elimina el pedido de la base de datos
                        pedao.eliminarPedido(pedido.getIdPedido());
                    }else{
                        // Elimina la cookie
                        Utilities.eliminarCookie(response);
                    }
                    
                    // Elimina el carrito de sesion
                    Utilities.eliminarCarrito(sesion);
                    
                    // Enviamos mensaje
                    Utilities.enviarAvisoRequest(request, 
                                "Se ha eliminado el carrito correctamente", 
                                false);
                }else if (!Utilities.usuarioEstaEnSesion(sesion)){
                    // Lee la cookie del carrito
                    Utilities.leerCookie(sesion, request);
                    if (Utilities.carritoEstaEnSesion(sesion)){
                        
                        // Elimina el carrito de sesion
                        Utilities.eliminarCarrito(sesion);
                        
                        // Enviamos mensaje
                        Utilities.enviarAvisoRequest(request, 
                                "Se ha eliminado el carrito correctamente", 
                                false);
                    }else{
                        // Enviamos mensaje de error
                        Utilities.enviarAvisoRequest(request, 
                                    "No hay ningun carrito que eliminar", 
                                    true);
                    }
                }
                break;
            case "volver":
                urlDispatcher = "index.jsp";
                break;
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
