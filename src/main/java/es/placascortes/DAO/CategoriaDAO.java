/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.Categoria;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author _
 */
public class CategoriaDAO implements ICategoriaDAO {

    @Override
    public List<Categoria> getAllCategorias() {
        ResultSet resultado = null;
        Categoria categoria = null;

        Statement sentencia = null;
        String sql = "select idCategoria, nombre, imagen from categorias";
        List<Categoria> listadoCategorias = null;

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentencia = conexion.createStatement();

            // recogemos el resultado y creamos un arrayList
            resultado = sentencia.executeQuery(sql);
            listadoCategorias = new ArrayList<>();

            while (resultado.next()) {
                categoria = new Categoria();

                categoria.setIdCategoria(resultado.getByte("idCategoria"));
                categoria.setNombre(resultado.getString("nombre"));
                categoria.setImagen(resultado.getString("imagen"));

                listadoCategorias.add(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return listadoCategorias;
    }

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
    
}
