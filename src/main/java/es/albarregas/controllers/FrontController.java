/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.albarregas.controllers;

import es.albarregas.DAO.ILineaPedidoDAO;
import es.albarregas.DAO.IProductoDAO;
import es.albarregas.DAO.IUsuarioDAO;
import es.albarregas.DAO.LineaPedidoDAO;
import es.albarregas.DAOFactory.DAOFactory;
import es.albarregas.beans.Carrito;
import es.albarregas.beans.Producto;
import es.albarregas.beans.Usuario;
import es.albarregas.utilities.Utilities;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
@WebServlet(name = "FrontController", urlPatterns = {"/FrontController"})
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
        List<Carrito> listadoCarrito = null;
        List<Producto> listadoProducto = null;
        Usuario usuario = null;
        Boolean actualizado = null;
        DAOFactory daof = null;
        IProductoDAO pdao = null;
        IUsuarioDAO udao = null;
        ILineaPedidoDAO lpdao = null;

        switch (opcion) {
            case "inicioSesion":
                if (Utilities.formularioEstaRelleno(request.getParameterMap())) {
                    try {
                        usuario = new Usuario();
                        BeanUtils.populate(usuario, request.getParameterMap());
                        
                        daof = new DAOFactory();
                        udao = daof.getUsuarioDAO();
                        
                        usuario = udao.usuarioEsValido(usuario);
                        
                        if (usuario.getIdUsuario() != null){
                            request.getSession().setAttribute("usuarioEnSesion", usuario);
                            
                            lpdao = new LineaPedidoDAO();
                            listadoCarrito = lpdao.crearCarritoDeLineasPedido(usuario.getIdUsuario());
                            
                            request.getSession().setAttribute("carrito", listadoCarrito);
                        }
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                urlDispatcher = "index.jsp";
                break;
            case "registro":
                urlDispatcher = "JSP/registro.jsp";
                break;
            case "cuenta":
                urlDispatcher = "JSP/menuUsuario.jsp";
                break;
            case "cerrerSesion":
                if (Utilities.carritoEstaEnSesion(request.getSession()))request.getSession().removeAttribute("carrito");
                request.getSession().removeAttribute("usuarioEnSesion");
                urlDispatcher = "index.jsp";
                break;
            case "carrito":
                if (!Utilities.carritoEstaEnSesion(request.getSession())) {
                    Utilities.leerCoockie(request.getSession(), request);
                }
                if (Utilities.carritoEstaEnSesion(request.getSession())) {
                    daof = new DAOFactory();
                    pdao = daof.getProductoDAO();
                    listadoCarrito = (List) request.getSession().getAttribute("carrito");
                    listadoProducto = pdao.getDetallesCarrito(listadoCarrito);
                    for (Integer i = 0; i < listadoCarrito.size(); i++) {
                        actualizado = false;
                        for (Integer j = 0; j < listadoProducto.size() && !actualizado; j++) {
                            if (listadoProducto.get(j).getIdProducto().equals(listadoCarrito.get(i).getProducto().getIdProducto())) {
                                actualizado = true;
                                listadoCarrito.get(i).setProducto(listadoProducto.get(j));
                            }
                        }
                    }
                    request.setAttribute("carrito", listadoCarrito);
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
