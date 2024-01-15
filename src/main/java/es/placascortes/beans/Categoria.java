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
public class Categoria implements Serializable{
    private Byte idCategoria;
    private String nombre;
    private String imagen;

    /**
     *
     * @param idCategoria
     * @param nombre
     * @param imagen
     */
    public Categoria(Byte idCategoria, String nombre, String imagen) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    /**
     *
     */
    public Categoria() {}

    /**
     *
     * @return
     */
    public Byte getIdCategoria() {
        return idCategoria;
    }

    /**
     *
     * @param idCategoria
     */
    public void setIdCategoria(Byte idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public String getImagen() {
        return imagen;
    }

    /**
     *
     * @param imagen
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
