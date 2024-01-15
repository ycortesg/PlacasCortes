/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.beans;

import java.io.Serializable;

/**
 *
 * @author _
 */
public class LineaPedido implements Serializable{
    private Short idLineaPedido;
    private Producto Producto;
    private Byte cantidad;

    /**
     *
     * @return
     */
    public Short getIdLineaPedido() {
        return idLineaPedido;
    }

    /**
     *
     * @param idLineaPedido
     */
    public void setIdLineaPedido(Short idLineaPedido) {
        this.idLineaPedido = idLineaPedido;
    }

    /**
     *
     * @return
     */
    public Producto getProducto() {
        return Producto;
    }

    /**
     *
     * @param Producto
     */
    public void setProducto(Producto Producto) {
        this.Producto = Producto;
    }

    /**
     *
     * @return
     */
    public Byte getCantidad() {
        return cantidad;
    }

    /**
     *
     * @param cantidad
     */
    public void setCantidad(Byte cantidad) {
        this.cantidad = cantidad;
    }
    
}
