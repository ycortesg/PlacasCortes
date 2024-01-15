/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.DAOFactory;

import es.placascortes.DAO.CategoriaDAO;
import es.placascortes.DAO.ICategoriaDAO;
import es.placascortes.DAO.ILineaPedidoDAO;
import es.placascortes.DAO.IPedidoDAO;
import es.placascortes.DAO.IProductoDAO;
import es.placascortes.DAO.IUsuarioDAO;
import es.placascortes.DAO.LineaPedidoDAO;
import es.placascortes.DAO.PedidoDAO;
import es.placascortes.DAO.ProductoDAO;
import es.placascortes.DAO.UsuarioDAO;

/**
 * 
 * @author _
 */
public class MySQLDAOFactory extends DAOFactory{

    /**
     *
     * @return
     */
    @Override
    public ICategoriaDAO getCategoriaDAO(){
        return new CategoriaDAO();
    }
    
    /**
     *
     * @return
     */
    @Override
    public IProductoDAO getProductoDAO(){
        return new ProductoDAO();
    }
    
    /**
     *
     * @return
     */
    @Override
    public IUsuarioDAO getUsuarioDAO(){
        return new UsuarioDAO();
    }
    
    /**
     *
     * @return
     */
    @Override
    public IPedidoDAO getPedidoDAO(){
        return new PedidoDAO();
    }
    
    /**
     *
     * @return
     */
    @Override
    public ILineaPedidoDAO getLineaPedidoDAO(){
        return new LineaPedidoDAO();
    }
}
