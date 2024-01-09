/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author _
 */
public class PedidoDAO implements IPedidoDAO {

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
        String sql = "select lineaspedidos.idPedido from lineaspedidos inner join pedidos on lineaspedidos.idPedido = pedidos.idPedido where pedidos.idUsuario = ?  and pedidos.estado = \'c\'";
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
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
}
