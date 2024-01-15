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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author _
 */
public class PedidoDAO implements IPedidoDAO {

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
    public Short crearPedido(Short idUsuario) {
        ResultSet resultado = null;
        Short idGenerado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "insert into pedidos (idUsuario) values (?)";

        try {
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            sentenciaPreparada.setShort(1, idUsuario);

            sentenciaPreparada.executeUpdate();

            resultado = sentenciaPreparada.getGeneratedKeys();
            resultado.next();

            idGenerado = resultado.getShort(1);
            conexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return idGenerado;
    }

    @Override
    public Short getPedidoIdDeCarritoUsuario(Short idUsuario) {
        ResultSet resultado = null;
        Short idPedido = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select idPedido from pedidos where idUsuario = ? and estado = \'c\'";
        try {
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);
            
            sentenciaPreparada.setShort(1, idUsuario);
            
            resultado = sentenciaPreparada.executeQuery();
            if (resultado.next()){
                idPedido = resultado.getShort("idPedido");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return idPedido;
    }

    @Override
    public void finalizarPedido(Pedido pedido) {
        PreparedStatement sentenciaPreparada = null;
        String sql = "update pedidos set fecha = ?, estado = \'f\', importe = ?, iva = ? where  idPedido = ? ";

        try {
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql);

            sentenciaPreparada.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            sentenciaPreparada.setFloat(2, pedido.getImporte());
            sentenciaPreparada.setFloat(3, pedido.getIva());
            sentenciaPreparada.setShort(4, pedido.getIdPedido());
            
            sentenciaPreparada.executeUpdate();
            conexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public void eliminarPedido(Short idPedido) {
        PreparedStatement sentenciaPreparada = null;
        String sql = "delete from pedidos where idPedido = ?";

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql);

            sentenciaPreparada.setShort(1, idPedido);

            sentenciaPreparada.executeUpdate();
            conexion.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public List<String> getFechasPedidos(Short idUsuario) {
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select distinct fecha from pedidos where idUsuario = ? and estado = \'f\'";
        List<String> listadoFechas = null;
        
        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            // Llenamos la sentencia preparada con los datos introducidos
            sentenciaPreparada.setShort(1, idUsuario);

            // Recogemos el resultado
            resultado = sentenciaPreparada.executeQuery();
            
            listadoFechas = new ArrayList<>();
            while (resultado.next()) {
                listadoFechas.add(resultado.getString("fecha"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return listadoFechas;    
    }

    @Override
    public List<Pedido> getPedidosDeFechaYUsuario(Short idUsuario, Date fecha) {
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select idPedido, importe, iva from pedidos where idUsuario = ? and fecha = ?";
        List<Pedido> listadoPedidosDeFecha = null;
        Pedido pedido = null;
        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            // Llenamos la sentencia preparada con los datos introducidos
            sentenciaPreparada.setShort(1, idUsuario);
            sentenciaPreparada.setDate(2, new java.sql.Date(fecha.getTime()));

            // Recogemos el resultado
            resultado = sentenciaPreparada.executeQuery();
            
            listadoPedidosDeFecha = new ArrayList<>();
            while (resultado.next()) {
                pedido = new Pedido();

                pedido.setIdPedido(resultado.getShort("idPedido"));
                pedido.setImporte(resultado.getFloat("importe"));
                pedido.setIva(resultado.getFloat("iva"));

                listadoPedidosDeFecha.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return listadoPedidosDeFecha;
    }

    @Override
    public Pedido getPedidoFinalizadoPorId(Short idPedido) {
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select productos.nombre, productos.precio, productos.imagen, lineaspedidos.cantidad from lineaspedidos inner join pedidos on lineaspedidos.idPedido = pedidos.idPedido inner join productos on lineaspedidos.idProducto = productos.idProducto where pedidos.idPedido = ?";
        Pedido pedidoFinalizao = null;
        LineaPedido lineaPedido = null;
        Producto producto = null;
        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            // Llenamos la sentencia preparada con los datos introducidos
            sentenciaPreparada.setShort(1, idPedido);

            // Recogemos el resultado
            resultado = sentenciaPreparada.executeQuery();
            pedidoFinalizao = new Pedido();
            pedidoFinalizao.setListadoLineasPedido(new ArrayList<>());
            while (resultado.next()) {
                lineaPedido = new LineaPedido();
                producto = new Producto();

                producto.setNombre(resultado.getString("nombre"));
                producto.setPrecio(resultado.getFloat("precio"));
                producto.setImagen(resultado.getString("imagen"));
                lineaPedido.setCantidad(resultado.getByte("cantidad"));

                lineaPedido.setProducto(producto);

                pedidoFinalizao.getListadoLineasPedido().add(lineaPedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return pedidoFinalizao;
    }
    
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
}
