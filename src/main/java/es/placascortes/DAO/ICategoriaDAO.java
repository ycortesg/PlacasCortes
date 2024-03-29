/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.placascortes.DAO;

import es.placascortes.beans.Categoria;
import java.util.List;

/**
 *
 * @author _
 */
public interface ICategoriaDAO {

    /**
     *
     * @return
     */
    public List<Categoria> getAllCategorias();

    /**
     *
     */
    public void closeConnection();
}
