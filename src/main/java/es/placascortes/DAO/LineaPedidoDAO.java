/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.LineaPedido;
import es.placascortes.beans.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author _
 */
public class LineaPedidoDAO implements ILineaPedidoDAO {

    @Override
    public void crearLineasPedido(Pedido pedidoCarrito) {
        PreparedStatement sentenciaPreparada = null;
        String sql = "insert into lineaspedidos (idPedido, idProducto, cantidad) values (?, ?, ?)";

        try {
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);

            for (LineaPedido lineaPedido : pedidoCarrito.getListadoLineasPedido()) {
                sentenciaPreparada = conexion.prepareStatement(sql);

                sentenciaPreparada.setShort(1, pedidoCarrito.getIdPedido());
                sentenciaPreparada.setShort(2, lineaPedido.getProducto().getIdProducto());
                sentenciaPreparada.setByte(3, Byte.parseByte(lineaPedido.getCantidad().toString()));

                sentenciaPreparada.executeUpdate();
            }

            conexion.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public void actualizarLinea(Short idPedido, Byte cantidad, Short idProducto) {

        PreparedStatement sentenciaPreparada = null;
        String sql = "update lineaspedidos set cantidad = ? where  idPedido = ? and idProducto = ?";

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql);

            sentenciaPreparada.setByte(1, cantidad);
            sentenciaPreparada.setShort(2, idPedido);
            sentenciaPreparada.setShort(3, idProducto);

            sentenciaPreparada.executeUpdate();
            conexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public void insertarLinea(Short idPedido, Byte cantidad, Short idProducto) {
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "insert into lineaspedidos (idPedido, idProducto, cantidad) values (?, ?, ?)";

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql);

            sentenciaPreparada.setShort(1, idPedido);
            sentenciaPreparada.setShort(2, idProducto);
            sentenciaPreparada.setByte(3, cantidad);

            sentenciaPreparada.executeUpdate();
            conexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public void eliminarLinea(Short idPedido, Short idProducto) {
        PreparedStatement sentenciaPreparada = null;
        String sql = "delete from lineaspedidos where idPedido = ? and idProducto = ?";

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql);

            sentenciaPreparada.setShort(1, idPedido);
            sentenciaPreparada.setShort(2, idProducto);

            sentenciaPreparada.executeUpdate();
            conexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
}
