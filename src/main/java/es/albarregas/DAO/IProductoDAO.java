/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.albarregas.DAO;

import es.albarregas.beans.Carrito;
import es.albarregas.beans.Producto;
import java.util.List;

/**
 *
 * @author _
 */
public interface IProductoDAO {
    public List<Producto> getMarcas();
    public List<Producto> getProductosEscaparate();
    public List<Producto> getProductosFiltrados(String listaFiltros[][]);
    public Producto getProductoDetallesPorId(Short id);
    public List<Producto> getDetallesCarrito(List<Carrito> listaCarrito);
    public void closeConnection();
}
