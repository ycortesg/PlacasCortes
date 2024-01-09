/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.albarregas.events;

import es.albarregas.DAO.ICategoriaDAO;
import es.albarregas.DAO.IProductoDAO;
import es.albarregas.DAOFactory.DAOFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author _
 */
@WebListener
public class Listener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Cuando se inicia la aplicación
        DAOFactory daof = new DAOFactory();
        ICategoriaDAO cdao = daof.getCategoriaDAO();
        IProductoDAO pdao = daof.getProductoDAO();
        
        List listaCategorias = cdao.getAllCategorias();
        List listaMarcas = pdao.getMarcas();
        List listaProductosEscaparate = pdao.getProductosEscaparate();
        
        sce.getServletContext().setAttribute("listaCategorias", listaCategorias);
        sce.getServletContext().setAttribute("listaMarcas", listaMarcas);
        sce.getServletContext().setAttribute("listaProductosEscaparate", listaProductosEscaparate);
        
        System.out.println("Se ha invocado contextInitialized ya que se ha inicado la aplicación");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cuando se para la aplicación
        System.out.println("Se ha invocado contextDestroyed ya que se ha detenido la aplicación");
    }
}
