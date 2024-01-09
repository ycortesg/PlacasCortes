/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.Carrito;
import java.util.List;

/**
 *
 * @author _
 */
public interface ILineaPedidoDAO {
    public void crearLineasPedido(List<Carrito> listadoCarrito, Short idPedido);
    public List<Carrito> crearCarritoDeLineasPedido(Short idUsuario);
    public void actualizarLinea(Short idPedido, Short cantidad, Short idProducto);
    public void insertarLinea(Short idPedido, Short cantidad, Short idProducto);
    public void eliminarLinea(Short idPedido, Short idProducto);
    public void closeConnection();
}
