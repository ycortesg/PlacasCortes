/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.Carrito;
import es.placascortes.beans.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author _
 */
public class LineaPedidoDAO implements ILineaPedidoDAO {

    @Override
    public void crearLineasPedido(List<Carrito> listadoCarrito, Short idPedido) {
        PreparedStatement sentenciaPreparada = null;
        String sql = "insert into lineaspedidos (idPedido, idProducto, cantidad) values (?, ?, ?)";

        try {
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);

            for (Carrito carrito : listadoCarrito) {
                sentenciaPreparada = conexion.prepareStatement(sql);

                sentenciaPreparada.setShort(1, idPedido);
                sentenciaPreparada.setShort(2, carrito.getProducto().getIdProducto());
                sentenciaPreparada.setByte(3, Byte.parseByte(carrito.getCantidad().toString()));

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
    public List<Carrito> crearCarritoDeLineasPedido(Short idUsuario) {
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select lineaspedidos.idProducto, lineaspedidos.cantidad from lineaspedidos inner join pedidos on lineaspedidos.idPedido = pedidos.idPedido where pedidos.idUsuario = ?  and pedidos.estado = \'c\'";
        List<Carrito> listadoCarrito = null;
        Carrito carrito = null;
        Producto producto = null;
        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            // Llenamos la sentencia preparada con los datos introducidos
            sentenciaPreparada.setShort(1, idUsuario);

            // Recogemos el resultado
            resultado = sentenciaPreparada.executeQuery();
            listadoCarrito = new ArrayList<>();
            while (resultado.next()) {
                carrito = new Carrito();
                producto = new Producto();

                producto.setIdProducto(resultado.getShort("idProducto"));
                carrito.setCantidad((short) resultado.getByte("cantidad"));

                carrito.setProducto(producto);

                listadoCarrito.add(carrito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return listadoCarrito;
    }

    @Override
    public void actualizarLinea(Short idPedido, Short cantidad, Short idProducto) {
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "update lineaspedidos set cantidad = ? where  idPedido = ? and idProducto = ?";

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql);

            sentenciaPreparada.setByte(1, Byte.parseByte(cantidad.toString()));
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
    public void insertarLinea(Short idPedido, Short cantidad, Short idProducto) {
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
            sentenciaPreparada.setByte(3, Byte.parseByte(cantidad.toString()));

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
