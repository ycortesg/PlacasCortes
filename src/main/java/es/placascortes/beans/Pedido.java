/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author _
 */
public class Pedido implements Serializable{
    private Short idPedido;
    private Date fecha;
    public enum tipoEstado {
        c,
        f
    };
    private tipoEstado estado;
    private Usuario usuario;
    private Float importe;
    private Float iva;
    private List<LineaPedido> listadoLineasPedido;

    public Short getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Short idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public tipoEstado getEstado() {
        return estado;
    }

    public void setEstado(tipoEstado estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public List<LineaPedido> getListadoLineasPedido() {
        return listadoLineasPedido;
    }

    public void setListadoLineasPedido(List listadoLineasPedido) {
        this.listadoLineasPedido = listadoLineasPedido;
    }
}
