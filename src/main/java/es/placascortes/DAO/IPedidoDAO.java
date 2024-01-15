/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.placascortes.DAO;
import es.placascortes.beans.Pedido;
import java.util.Date;
import java.util.List;

/**
 *
 * @author _
 */
public interface IPedidoDAO {
    public Pedido crearCarritoDeLineasPedido(Short idUsuario);
    public Pedido getPedidoFinalizadoPorId(Short idPedido);
    public Short crearPedido(Short idUsuario);
    public Short getPedidoIdDeCarritoUsuario(Short idUsuario);
    public List<String> getFechasPedidos(Short idUsuario);
    public List<Pedido> getPedidosDeFechaYUsuario(Short idUsuario, Date fecha);
    public void eliminarPedido(Short idPedido);
    public void finalizarPedido(Pedido pedido);
    public void closeConnection();
}
