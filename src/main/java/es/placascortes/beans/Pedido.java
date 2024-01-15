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

    /**
     *
     */
    public enum tipoEstado {

        /**
         *
         */
        c,

        /**
         *
         */
        f
    };
    private tipoEstado estado;
    private Usuario usuario;
    private Float importe;
    private Float iva;
    private List<LineaPedido> listadoLineasPedido;

    /**
     *
     * @return
     */
    public Short getIdPedido() {
        return idPedido;
    }

    /**
     *
     * @param idPedido
     */
    public void setIdPedido(Short idPedido) {
        this.idPedido = idPedido;
    }

    /**
     *
     * @return
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     *
     * @param fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     *
     * @return
     */
    public tipoEstado getEstado() {
        return estado;
    }

    /**
     *
     * @param estado
     */
    public void setEstado(tipoEstado estado) {
        this.estado = estado;
    }

    /**
     *
     * @return
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     *
     * @return
     */
    public Float getImporte() {
        return importe;
    }

    /**
     *
     * @param importe
     */
    public void setImporte(Float importe) {
        this.importe = importe;
    }

    /**
     *
     * @return
     */
    public Float getIva() {
        return iva;
    }

    /**
     *
     * @param iva
     */
    public void setIva(Float iva) {
        this.iva = iva;
    }

    /**
     *
     * @return
     */
    public List<LineaPedido> getListadoLineasPedido() {
        return listadoLineasPedido;
    }

    /**
     *
     * @param listadoLineasPedido
     */
    public void setListadoLineasPedido(List listadoLineasPedido) {
        this.listadoLineasPedido = listadoLineasPedido;
    }
}
