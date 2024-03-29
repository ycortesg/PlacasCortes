/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.placascortes.controllers;

import es.placascortes.DAO.ILineaPedidoDAO;
import es.placascortes.DAO.IPedidoDAO;
import es.placascortes.DAO.IUsuarioDAO;
import es.placascortes.DAOFactory.DAOFactory;
import es.placascortes.DAOFactory.MySQLDAOFactory;
import es.placascortes.beans.Pedido;
import es.placascortes.beans.Usuario;
import es.placascortes.utilities.Utilities;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author _
 */
@WebServlet(name = "RegistroController", urlPatterns = {"/RegistroController"})
public class RegistroController extends HttpServlet {

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
        String letraNIF = null;
        String opcion = request.getParameter("opcion");
        DAOFactory daof = null;
        IUsuarioDAO udao = null;
        IPedidoDAO pedao = null;
        ILineaPedidoDAO lpdao = null;
        Usuario usuario = null;
        Pedido pedido = null;
        Short estadoRegistro = null;

        switch (opcion) {
            case "registrate":
                if (Utilities.formularioEstaRelleno(request.getParameterMap())) {
                    try {
                        // Rellenamos un objeto Usuario con los datos introducidos
                        letraNIF = request.getParameter("letraNIF");

                        usuario = new Usuario();
                        BeanUtils.populate(usuario, request.getParameterMap());
                        usuario.setNIF(usuario.getNIF() + letraNIF);

                        // Declaramos los dao
                        daof = new MySQLDAOFactory();
                        udao = daof.getUsuarioDAO();

                        // Intentamos llevar a cabo el registro
                        estadoRegistro = udao.registrarUsuario(usuario);
                        if (estadoRegistro > -1) {
                            throw new IllegalAccessException();
                        }
                        
                        // Lee la cookie y meta la informacion en sesion
                        if (!Utilities.carritoEstaEnSesion(request.getSession())) {
                            Utilities.leerCookie(request.getSession(), request);
                        }

                        // Declaramos los dao
                        pedao = daof.getPedidoDAO();
                        lpdao = daof.getLineaPedidoDAO();
                        
                        // Recogemos el pedido de sesion
                        pedido = (Pedido) request.getSession().getAttribute("carrito");
                        
                        // Si el pedido es nulo crea uno y le anade el listado de lineas
                        if (pedido == null) {
                            pedido = new Pedido();
                            pedido.setListadoLineasPedido(new ArrayList());
                        }
                        
                        usuario.setIdUsuario((short) (estadoRegistro * -1));

                        // Si el carrito no esta vacio
                        if (Utilities.sumUnidadesCarrito(pedido.getListadoLineasPedido()) > 0){
                            
                            // Crea el pedido y las lineasPedido en sesion
                            pedido.setIdPedido(pedao.crearPedido(usuario.getIdUsuario()));
                            lpdao.crearLineasPedido(pedido);
                        }
                        
                        // Elimina el carrito de sesion y de la cookie
                        request.getSession().removeAttribute("carrito");
                        Utilities.eliminarCookie(response);
                        
                        // Enviamos mensaje 
                        Utilities.enviarAvisoRequest(request, 
                                "Te has registrado correctamente", 
                                false);
                        urlDispatcher = "index.jsp";

                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        // Enviamos mensaje de error
                        Utilities.enviarAvisoRequest(request, 
                                "Ha ocurrido algun error", 
                                true);
                        urlDispatcher = "JSP/registro.jsp";
                        Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    urlDispatcher = "JSP/registro.jsp";
                    // Enviamos mensaje de error
                    Utilities.enviarAvisoRequest(request, 
                                "Todos los campos son necesarios", 
                                true);
                }
                break;
            case "volver":
                urlDispatcher = "index.jsp";
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
