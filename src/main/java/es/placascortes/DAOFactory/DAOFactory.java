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
    
    public abstract ICategoriaDAO getCategoriaDAO();
    public abstract IProductoDAO getProductoDAO();
    public abstract IUsuarioDAO getUsuarioDAO();
    public abstract IPedidoDAO getPedidoDAO();
    public abstract ILineaPedidoDAO getLineaPedidoDAO();
    
    public static DAOFactory getDAOFactory(){
        return new MySQLDAOFactory();
    }
}
