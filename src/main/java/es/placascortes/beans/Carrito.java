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
public class Carrito implements Serializable{
    private Producto producto;
    private Short cantidad;

    public Carrito(Producto producto, Short cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Carrito() {
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Short getCantidad() {
        return cantidad;
    }

    public void setCantidad(Short cantidad) {
        this.cantidad = cantidad;
    }
}
