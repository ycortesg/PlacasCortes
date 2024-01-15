/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.DAOFactory;

import es.placascortes.DAO.ICategoriaDAO;
import es.placascortes.DAO.ILineaPedidoDAO;
import es.placascortes.DAO.IPedidoDAO;
import es.placascortes.DAO.IProductoDAO;
import es.placascortes.DAO.IUsuarioDAO;

/**
 *
 * @author _
 */
public abstract class DAOFactory {
    
    /**
     *
     * @return
     */
    public abstract ICategoriaDAO getCategoriaDAO();

    /**
     *
     * @return
     */
    public abstract IProductoDAO getProductoDAO();

    /**
     *
     * @return
     */
    public abstract IUsuarioDAO getUsuarioDAO();

    /**
     *
     * @return
     */
    public abstract IPedidoDAO getPedidoDAO();

    /**
     *
     * @return
     */
    public abstract ILineaPedidoDAO getLineaPedidoDAO();
    
    /**
     *
     * @return
     */
    public static DAOFactory getDAOFactory(){
        return new MySQLDAOFactory();
    }
}
