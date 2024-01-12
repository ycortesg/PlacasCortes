/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.placascortes.DAO;
import es.placascortes.beans.Pedido;

/**
 *
 * @author _
 */
public interface ILineaPedidoDAO {
    public void crearLineasPedido(Pedido pedidoCarrito);
    public void actualizarLinea(Short idPedido, Byte cantidad, Short idProducto);
    public void insertarLinea(Short idPedido, Byte cantidad, Short idProducto);
    public void eliminarLinea(Short idPedido, Short idProducto);
    public void closeConnection();
}
