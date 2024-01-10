/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.LineaPedido;
import es.placascortes.beans.Producto;
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
    public List<Producto> getDetallesCarrito(List<LineaPedido> listadoListaPedido);
    public void closeConnection();
}
