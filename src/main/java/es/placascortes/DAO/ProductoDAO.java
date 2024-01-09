/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.albarregas.DAO;

import es.albarregas.beans.Carrito;
import es.albarregas.beans.Categoria;
import es.albarregas.beans.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author _
 */
public class ProductoDAO implements IProductoDAO {

    @Override
    public List<Producto> getMarcas() {
        ResultSet resultado = null;
        Producto producto = null;

        Statement sentencia = null;
        String sql = "select distinct marca from productos";
        List<Producto> listadoMarcas = null;

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentencia = conexion.createStatement();

            // recogemos el resultado y creamos un arrayList
            resultado = sentencia.executeQuery(sql);
            listadoMarcas = new ArrayList<>();

            while (resultado.next()) {
                producto = new Producto();

                producto.setMarca(resultado.getString("marca"));

                listadoMarcas.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return listadoMarcas;
    }

    @Override
    public List<Producto> getProductosEscaparate() {
        ResultSet resultado = null;
        Producto producto = null;
        Categoria categoria = null;

        Statement sentencia = null;
        String sql = "select productos.idProducto, productos.nombre, productos.precio, productos.marca, productos.imagen as imagenProducto, categorias.imagen as imagenCategoria from productos inner join categorias on productos.idCategoria = categorias.idCategoria order by rand() limit 6";
        List<Producto> listadoProductos = null;

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentencia = conexion.createStatement();

            // recogemos el resultado y creamos un arrayList
            resultado = sentencia.executeQuery(sql);
            listadoProductos = new ArrayList<>();

            while (resultado.next()) {
                producto = new Producto();
                categoria = new Categoria();

                producto.setIdProducto(resultado.getShort("idProducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setPrecio(resultado.getFloat("precio"));
                producto.setMarca(resultado.getString("marca"));
                producto.setImagen(resultado.getString("imagenProducto"));

                categoria.setImagen(resultado.getString("imagenCategoria"));

                producto.setCategoria(categoria);
                listadoProductos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return listadoProductos;
    }

    @Override
    public Producto getProductoDetallesPorId(Short id) {
        ResultSet resultado = null;
        Producto producto = null;
        Categoria categoria = null;

        PreparedStatement sentenciaPreparada = null;
        String sql = "select productos.nombre as nombreProducto, productos.precio, productos.marca, productos.descripcion, productos.imagen as imagenProducto, categorias.imagen as imagenCategoria, categorias.nombre as nombreCategoria from productos inner join categorias on productos.idCategoria = categorias.idCategoria where productos.idProducto = ?";

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentenciaPreparada = conexion.prepareStatement(sql);

            sentenciaPreparada.setShort(1, id);

            // recogemos el resultado y creamos un arrayList
            resultado = sentenciaPreparada.executeQuery();

            while (resultado.next()) {
                producto = new Producto();
                categoria = new Categoria();

                producto.setNombre(resultado.getString("nombreProducto"));
                producto.setPrecio(resultado.getFloat("precio"));
                producto.setMarca(resultado.getString("marca"));
                producto.setDescripcion(resultado.getString("descripcion"));
                producto.setImagen(resultado.getString("imagenProducto"));

                categoria.setImagen(resultado.getString("imagenCategoria"));
                categoria.setNombre(resultado.getString("nombreCategoria"));

                producto.setCategoria(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return producto;
    }

    @Override
    public List<Producto> getProductosFiltrados(String[][] listaFiltros) {
        ResultSet resultado = null;
        Producto producto = null;
        Categoria categoria = null;

        Boolean tieneFiltro = false;
        StringBuilder filtrosSQL = null;
        Statement sentencia = null;
        String sql = "select productos.idProducto, productos.nombre, productos.precio, productos.marca, productos.imagen as imagenProducto, categorias.imagen as imagenCategoria from productos inner join categorias on productos.idCategoria = categorias.idCategoria ";

        List<Producto> listadoProductos = null;

        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();

            filtrosSQL = new StringBuilder();

            filtrosSQL.append(sql);
            if (listaFiltros[0].length > 0) {
                filtrosSQL.append("where productos.idCategoria in (" + String.join(", ", Arrays.asList(listaFiltros[0])) + ")");
                tieneFiltro = true;
            }
            if (listaFiltros[1].length > 0) {
                filtrosSQL.append((tieneFiltro ? " and" : "where") + " productos.marca in (\'" + String.join("\', \'", Arrays.asList(listaFiltros[1])) + "\')");
                tieneFiltro = true;
            }
            if (!listaFiltros[2][0].isBlank()) {
                filtrosSQL.append((tieneFiltro ? " and" : "where") + " productos.descripcion like \'%" + listaFiltros[2][0] + "%\'");
            }

            sentencia = conexion.createStatement();

            // recogemos el resultado y creamos un arrayList
            resultado = sentencia.executeQuery(filtrosSQL.toString());
            listadoProductos = new ArrayList<>();

            while (resultado.next()) {
                producto = new Producto();
                categoria = new Categoria();

                producto.setIdProducto(resultado.getShort("idProducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setPrecio(resultado.getFloat("precio"));
                producto.setMarca(resultado.getString("marca"));
                producto.setImagen(resultado.getString("imagenProducto"));

                categoria.setImagen(resultado.getString("imagenCategoria"));

                producto.setCategoria(categoria);
                listadoProductos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return listadoProductos;
    }

    @Override
    public List<Producto> getDetallesCarrito(List<Carrito> listaCarrito) {
        ResultSet resultado = null;
        Producto producto = null;
        Categoria categoria = null;

        StringBuilder listaProductos = new StringBuilder();
        Statement sentencia = null;
        List<Producto> listadoProductos = null;
        String sql = "select productos.idProducto, productos.nombre, productos.precio, productos.marca, productos.imagen as imagenProducto, categorias.nombre as nombreCategoria from productos inner join categorias on productos.idCategoria = categorias.idCategoria ";

        listaProductos.append(sql);
        listaProductos.append("where productos.idProducto in (");
        for (Integer i = 0 ; i<listaCarrito.size() ; i++){
            if (i > 0) listaProductos.append(",");
            listaProductos.append(listaCarrito.get(i).getProducto().getIdProducto());
        }
        listaProductos.append(")");
        
        try {
            // creamos conexion y sentencia
            Connection conexion = ConnectionFactory.getConnection();
            sentencia = conexion.createStatement();

            // recogemos el resultado y creamos un arrayList
            resultado = sentencia.executeQuery(listaProductos.toString());
            listadoProductos = new ArrayList<>();

            while (resultado.next()) {
                producto = new Producto();
                categoria = new Categoria();

                producto.setIdProducto(resultado.getShort("idProducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setPrecio(resultado.getFloat("precio"));
                producto.setMarca(resultado.getString("marca"));
                producto.setImagen(resultado.getString("imagenProducto"));

                categoria.setNombre(resultado.getString("nombreCategoria"));

                producto.setCategoria(categoria);
                listadoProductos.add(producto);
            }            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return listadoProductos;
    }
    
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
}
