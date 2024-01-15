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

    /**
     *
     * @return
     */
    public List<Producto> getMarcas();

    /**
     *
     * @return
     */
    public List<Producto> getProductosEscaparate();

    /**
     *
     * @param listaFiltros
     * @return
     */
    public List<Producto> getProductosFiltrados(String listaFiltros[][]);

    /**
     *
     * @param id
     * @return
     */
    public Producto getProductoDetallesPorId(Short id);

    /**
     *
     * @param listadoListaPedido
     * @return
     */
    public List<Producto> getDetallesCarrito(List<LineaPedido> listadoListaPedido);

    /**
     *
     */
    public void closeConnection();
}
