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

    /**
     *
     * @param idUsuario
     * @return
     */
    public Pedido crearCarritoDeLineasPedido(Short idUsuario);

    /**
     *
     * @param idPedido
     * @return
     */
    public Pedido getPedidoFinalizadoPorId(Short idPedido);

    /**
     *
     * @param idUsuario
     * @return
     */
    public Short crearPedido(Short idUsuario);

    /**
     *
     * @param idUsuario
     * @return
     */
    public Short getPedidoIdDeCarritoUsuario(Short idUsuario);

    /**
     *
     * @param idUsuario
     * @return
     */
    public List<String> getFechasPedidos(Short idUsuario);

    /**
     *
     * @param idUsuario
     * @param fecha
     * @return
     */
    public List<Pedido> getPedidosDeFechaYUsuario(Short idUsuario, Date fecha);

    /**
     *
     * @param idPedido
     */
    public void eliminarPedido(Short idPedido);

    /**
     *
     * @param pedido
     */
    public void finalizarPedido(Pedido pedido);

    /**
     *
     */
    public void closeConnection();
}
