/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.Usuario;

/**
 *
 * @author _
 */
public interface IUsuarioDAO{

    /**
     *
     * @param correo
     * @return
     */
    public Boolean correoEsValido(String correo);

    /**
     *
     * @param usuario
     * @return
     */
    public Usuario usuarioEsValido(Usuario usuario);

    /**
     *
     * @param usuario
     * @return
     */
    public Usuario anadirDetallesAUsuario(Usuario usuario);

    /**
     *
     * @param usuario
     * @return
     */
    public Short registrarUsuario(Usuario usuario);

    /**
     *
     * @param usuario
     * @return
     */
    public Short actualizarDatosPersonalesUsuario(Usuario usuario);

    /**
     *
     * @param usuario
     * @param passwordNueva
     * @return
     */
    public Short actualizarPasswordUsuario(Usuario usuario, String passwordNueva);

    /**
     *
     * @param usuario
     */
    public void actualizarAvatar(Usuario usuario);

    /**
     *
     * @param idUsuario
     */
    public void actualizarUltimoAcceso(Short idUsuario);

    /**
     *
     */
    public void closeConnection();
}
