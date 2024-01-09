/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.placascortes.DAO;

/**
 *
 * @author _
 */
public interface IPedidoDAO {
    public Short crearPedido(Short idUsuario);
    public Short getPedidoIdDeCarritoUsuario(Short idUsuario);
    public void closeConnection();
}
