/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.placascortes.controllers;

import es.placascortes.DAO.IPedidoDAO;
import es.placascortes.DAOFactory.DAOFactory;
import es.placascortes.DAOFactory.MySQLDAOFactory;
import es.placascortes.beans.Pedido;
import es.placascortes.beans.Usuario;
import es.placascortes.utilities.Utilities;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
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
        DAOFactory daof = null;
        IPedidoDAO pedao = null;
        HttpSession sesion = null;
        
        switch (opcion) {
            case "registrate":
                urlDispatcher = "JSP/registro.jsp";
                break;
            case "volver":
                urlDispatcher = "index.jsp";
                break;
            case "finalizarCompra":
                sesion = request.getSession();
                if (Utilities.usuarioEstaEnSesion(sesion) && Utilities.carritoEstaEnSesion(sesion)) {
                    try {
                        daof = new MySQLDAOFactory();
                        pedao = daof.getPedidoDAO();
                        
                        usuario = (Usuario) sesion.getAttribute("usuarioEnSesion");
                        
                        pedido = new Pedido();
                        BeanUtils.populate(pedido, request.getParameterMap());
                        pedido.setIdPedido(pedao.getPedidoIdDeCarritoUsuario(usuario.getIdUsuario()));
                        pedido.setFecha(new Date());
                        
                        pedao.finalizarPedido(pedido);
                        sesion.removeAttribute("carrito");
                        
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        request.setAttribute("aviso", "Ha ocurrido algun error");
                        urlDispatcher = "JSP/registro.jsp";
                        Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
