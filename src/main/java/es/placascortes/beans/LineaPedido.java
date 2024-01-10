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

    public Short getIdLineaPedido() {
        return idLineaPedido;
    }

    public void setIdLineaPedido(Short idLineaPedido) {
        this.idLineaPedido = idLineaPedido;
    }

    public Producto getProducto() {
        return Producto;
    }

    public void setProducto(Producto Producto) {
        this.Producto = Producto;
    }

    public Byte getCantidad() {
        return cantidad;
    }

    public void setCantidad(Byte cantidad) {
        this.cantidad = cantidad;
    }
    
}
