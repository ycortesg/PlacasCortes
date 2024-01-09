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

    public Categoria(Byte idCategoria, String nombre, String imagen) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public Categoria() {}

    public Byte getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Byte idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
