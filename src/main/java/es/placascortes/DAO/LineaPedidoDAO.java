/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.LineaPedido;
import es.placascortes.beans.Pedido;
import es.placascortes.beans.Producto;
import es.placascortes.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author _
 */
public class LineaPedidoDAO implements ILineaPedidoDAO {

    @Override
    public void crearLineasPedido(Pedido pedidoCarrito, Short idPedido) {
        PreparedStatement sentenciaPreparada = null;
        String sql = "insert into lineaspedidos (idPedido, idProducto, cantidad) values (?, ?, ?)";

        try {
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);

            for (LineaPedido lineaPedido : pedidoCarrito.getListadoLineasPedido()) {
                sentenciaPreparada = conexion.prepareStatement(sql);

                sentenciaPreparada.setShort(1, idPedido);
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
    public Pedido crearCarritoDeLineasPedido(Short idUsuario) {
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select lineaspedidos.idProducto, lineaspedidos.cantidad from lineaspedidos inner join pedidos on lineaspedidos.idPedido = pedidos.idPedido where pedidos.idUsuario = ?  and pedidos.estado = \'c\'";
        Pedido pedidoCarrito = null;
        Usuario usuario = null;
        LineaPedido lineaPedido = null;
        Producto producto = null;
        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            // Llenamos la sentencia preparada con los datos introducidos
            sentenciaPreparada.setShort(1, idUsuario);

            // Recogemos el resultado
            resultado = sentenciaPreparada.executeQuery();
            pedidoCarrito = new Pedido();
            pedidoCarrito.setListadoLineasPedido(new ArrayList<>());
            
            usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            
            pedidoCarrito.setUsuario(usuario);
            while (resultado.next()) {
                lineaPedido = new LineaPedido();
                producto = new Producto();

                producto.setIdProducto(resultado.getShort("idProducto"));
                lineaPedido.setCantidad(resultado.getByte("cantidad"));

                lineaPedido.setProducto(producto);

                pedidoCarrito.getListadoLineasPedido().add(lineaPedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return pedidoCarrito;
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
