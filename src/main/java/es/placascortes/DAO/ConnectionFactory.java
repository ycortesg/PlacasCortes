/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.albarregas.DAO;

import java.sql.Connection;

import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory {

    static DataSource ds = null;
    static Connection conexion = null;
    static final String DATASOURCE_NAME = "java:comp/env/jdbc/jndi-tienda";
    
    public static Connection getConnection() {
        try {
            Context contextoInicial = new InitialContext();
            ds = (DataSource)contextoInicial.lookup(DATASOURCE_NAME);
            conexion = ds.getConnection();
        } catch (NamingException | SQLException e) {
            System.out.println("Error en la conexion contra MySQL");
            e.printStackTrace();
        }
        
        return conexion;
    }
    
    public static void closeConnection(){
        try {
            conexion.close();
        } catch(SQLException e){
            System.out.println("Error al cerrar la conexion a la BD");
            e.printStackTrace();
        }
    }
    
}