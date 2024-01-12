/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.Usuario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author _
 */
public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public Boolean correoEsValido(String correo) {
        ResultSet resultado = null;
        Boolean estaEnTabla = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select count(*) as resultado from usuarios where email = ?";

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            sentenciaPreparada.setString(1, correo);

            // recogemos el resultado y creamos un arrayList
            resultado = sentenciaPreparada.executeQuery();
            resultado.next();
            Integer prueba = resultado.getInt("resultado");

            estaEnTabla = resultado.getInt("resultado") > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return !estaEnTabla;
    }

    @Override
    public Short registrarUsuario(Usuario usuario) {
        ResultSet resultado = null;
        Short estado = -1;

        PreparedStatement sentenciaPreparada = null;
        String sql = usuario.getAvatar() == null
                ? "insert into usuarios (email, password, nombre, apellidos, NIF, telefono, direccion, codigoPostal, localidad, provincia) values (?, md5(?), ?, ?, ?, ?, ?, ?, ?, ?)"
                : "insert into usuarios (email, password, nombre, apellidos, NIF, telefono, direccion, codigoPostal, localidad, provincia, avatar) values (?, md5(?), ?, ? ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            sentenciaPreparada.setString(1, usuario.getEmail());
            sentenciaPreparada.setString(2, usuario.getPassword());
            sentenciaPreparada.setString(3, usuario.getNombre());
            sentenciaPreparada.setString(4, usuario.getApellidos());
            sentenciaPreparada.setString(5, usuario.getNIF());
            sentenciaPreparada.setString(6, usuario.getTelefono());
            sentenciaPreparada.setString(7, usuario.getDireccion());
            sentenciaPreparada.setString(8, usuario.getCodigoPostal());
            sentenciaPreparada.setString(9, usuario.getLocalidad());
            sentenciaPreparada.setString(10, usuario.getProvincia());

            if (usuario.getAvatar() != null) {
                sentenciaPreparada.setString(11, usuario.getAvatar());
            }

            sentenciaPreparada.executeUpdate();
            resultado = sentenciaPreparada.getGeneratedKeys();
            resultado.next();

            estado = (short) (resultado.getShort(1) * -1);
            conexion.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            estado = (short) e.getErrorCode();
        } finally {
            this.closeConnection();
        }

        return estado;
    }

    @Override
    public Usuario usuarioEsValido(Usuario usuario) {
        // Declaramos variables
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select idUsuario, nombre, avatar from usuarios where email = ? and password = MD5(?)";
        try {
            // Creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            // Llenamos la sentencia preparada con los datos introducidos
            sentenciaPreparada.setString(1, usuario.getEmail());
            sentenciaPreparada.setString(2, usuario.getPassword());

            // Recogemos el resultado
            resultado = sentenciaPreparada.executeQuery();

            // Si el resultado no es nulo se rellena el objeto usuario
            if (resultado.next()) {
                usuario.setIdUsuario(resultado.getShort("idUsuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setAvatar(resultado.getString("avatar"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return usuario;
    }

    @Override
    public Usuario anadirDetallesAUsuario(Usuario usuario) {
        // Declaramos variables
        ResultSet resultado = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select apellidos, telefono, direccion, codigoPostal, localidad, provincia from usuarios where idUsuario = ? ";
        try {
            // Creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            // Llenamos la sentencia preparada con los datos introducidos
            sentenciaPreparada.setShort(1, usuario.getIdUsuario());

            // Recogemos el resultado
            resultado = sentenciaPreparada.executeQuery();

            // Si el resultado no es nulo se rellena el objeto usuario
            if (resultado.next()) {
                usuario.setApellidos(resultado.getString("apellidos"));
                usuario.setTelefono(resultado.getString("telefono"));
                usuario.setDireccion(resultado.getString("direccion"));
                usuario.setCodigoPostal(resultado.getString("codigoPostal"));
                usuario.setLocalidad(resultado.getString("localidad"));
                usuario.setProvincia(resultado.getString("provincia"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return usuario;
    }

    @Override
    public void actualizarUltimoAcceso(Short idUsuario) {
        // Declaramos variables
        PreparedStatement sentenciaPreparada = null;
        String sql = "update usuarios set ultimoAcceso = ? where idUsuario = ?";
        try {
            // Creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            conexion.setAutoCommit(false);
            sentenciaPreparada = conexion.prepareStatement(sql);

            // Llenamos la sentencia preparada con los datos requeridos
            sentenciaPreparada.setDate(1, new Date(new java.util.Date().getTime()));
            sentenciaPreparada.setShort(2, idUsuario);

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
