/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.albarregas.DAOFactory;

import es.albarregas.DAO.CategoriaDAO;
import es.albarregas.DAO.ICategoriaDAO;
import es.albarregas.DAO.ILineaPedidoDAO;
import es.albarregas.DAO.IPedidoDAO;
import es.albarregas.DAO.ProductoDAO;
import es.albarregas.DAO.IProductoDAO;
import es.albarregas.DAO.UsuarioDAO;
import es.albarregas.DAO.IUsuarioDAO;
import es.albarregas.DAO.LineaPedidoDAO;
import es.albarregas.DAO.PedidoDAO;

/**
 *
 * @author _
 */
public class DAOFactory {
    public ICategoriaDAO getCategoriaDAO(){
        return new CategoriaDAO();
    }
    
    public IProductoDAO getProductoDAO(){
        return new ProductoDAO();
    }
    
    public IUsuarioDAO getUsuarioDAO(){
        return new UsuarioDAO();
    }
    
    public IPedidoDAO getPedidoDAO(){
        return new PedidoDAO();
    }
    
    public ILineaPedidoDAO getLineaPedidoDAO(){
        return new LineaPedidoDAO();
    }
}
