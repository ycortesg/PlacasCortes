/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.albarregas.DAO;

import es.albarregas.beans.Usuario;

/**
 *
 * @author _
 */
public interface IUsuarioDAO{
    public Boolean correoEsValido(String correo);
    public Usuario usuarioEsValido(Usuario usuario);
    public Short registrarUsuario(Usuario usuario);
    public void closeConnection();
}
