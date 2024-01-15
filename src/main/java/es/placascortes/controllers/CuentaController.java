/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package es.placascortes.controllers;

import es.placascortes.DAO.IUsuarioDAO;
import es.placascortes.DAOFactory.DAOFactory;
import es.placascortes.DAOFactory.MySQLDAOFactory;
import es.placascortes.beans.Usuario;
import es.placascortes.utilities.Utilities;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author _
 */
@WebServlet(name = "CuentaController", urlPatterns = {"/CuentaController"})
public class CuentaController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Declaramos variables
        String urlDispatcher = null;
        String opcion = request.getParameter("opcion") == null ? "actualizarAvatar" : request.getParameter("opcion");
        String passwordAntigua = null;
        String passwordNueva = null;
        String dirImagen = null;
        String filePath = null;
        String extension = null;
        StringBuilder nombreFichero = null;
        Short estado = null;
        Boolean error = null;
        FileItemFactory factory = null;
        ServletFileUpload upload = null;
        List<FileItem> items = null;
        Iterator<FileItem> iteratorFile = null;
        FileItem uploaded = null;
        File fichero = null;
        Usuario usuarioEnSesion = null;
        Usuario usuarioConDatosNuevos = null;
        DAOFactory daof = null;
        IUsuarioDAO udao = null;
        
        // Si el usuario no esta en sesion vuelve al inicio y manda mensaje de error
        if (!Utilities.usuarioEstaEnSesion(request.getSession()) && !opcion.equals("volver")) {
            urlDispatcher = "index.jsp";
            Utilities.enviarAvisoRequest(request,
                    "Necesitas iniciar sesion para hacer cosas en esta pagina",
                    true);
        } else {
            switch (opcion) {
                case "actualizarDatosPersonales":
                    urlDispatcher = "JSP/menuUsuario.jsp";
                    
                    // Declaramos el usuario en sesion
                    usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
                    if (Utilities.formularioEstaRelleno(request.getParameterMap())) {
                        try {
                            // Hacemos una caopia del usuario en sesion pero con los datos introducidos
                            usuarioConDatosNuevos = new Usuario();
                            BeanUtils.populate(usuarioConDatosNuevos, request.getParameterMap());

                            usuarioConDatosNuevos.setIdUsuario(usuarioEnSesion.getIdUsuario());
                            usuarioConDatosNuevos.setAvatar(usuarioEnSesion.getAvatar());
                            usuarioConDatosNuevos.setEmail(usuarioEnSesion.getEmail());

                            // Si los dos objetos son iguales
                            if (!usuarioConDatosNuevos.equals(usuarioEnSesion)) {
                                // Declaramos los dao
                                daof = new MySQLDAOFactory();
                                udao = daof.getUsuarioDAO();
                                
                                // Actualizamos los datos del usuario
                                estado = udao.actualizarDatosPersonalesUsuario(usuarioConDatosNuevos);
                                
                                // Si ha salido bien se actualiza el usuario en sesion
                                if (estado == -1) {
                                    request.getSession().setAttribute("usuarioEnSesion", usuarioConDatosNuevos);

                                    // Enviamos mensaje
                                    Utilities.enviarAvisoRequest(request,
                                            "Se han actualizado tus datos personales correctamente",
                                            false);
                                } else {
                                    // Enviamos mensaje de error
                                    Utilities.enviarAvisoRequest(request,
                                            "Algo ha salido mal",
                                            true);
                                }
                            } else {
                                // Enviamos mensaje de error
                                Utilities.enviarAvisoRequest(request,
                                        "Tienes que cambiar algun dato para actualizar los datos",
                                        true);
                            }
                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            // Enviamos mensaje de error
                            Utilities.enviarAvisoRequest(request,
                                    "Ha ocurrido algun error al intentar actualiar tus datos",
                                    true);
                            Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        // Enviamos mensaje de error
                        Utilities.enviarAvisoRequest(request,
                                "Todos los campos son obligatorios para cambiar tus datos",
                                true);
                    }
                    break;

                case "actualizarPassword":
                    urlDispatcher = "JSP/menuUsuario.jsp";
                    
                    // Declaramos el usuario en sesion
                    usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
                    if (Utilities.formularioEstaRelleno(request.getParameterMap())) {
                        // Declaramos los dao
                        daof = new MySQLDAOFactory();
                        udao = daof.getUsuarioDAO();
                        
                        // Recogemos la contrasena introducida y lo anadimos a un usuario nuevo
                        passwordAntigua = request.getParameter("passwordOld");
                        usuarioConDatosNuevos = new Usuario();
                        usuarioConDatosNuevos.setEmail(usuarioEnSesion.getEmail());
                        usuarioConDatosNuevos.setPassword(passwordAntigua);
                        usuarioConDatosNuevos = udao.usuarioEsValido(usuarioConDatosNuevos);
                        
                        // Si la contrasena es valida recogemos la contrasena nueva la actualizamos en la base de datos
                        if (usuarioConDatosNuevos.getNombre() != null) {
                            passwordNueva = request.getParameter("password");
                            estado = udao.actualizarPasswordUsuario(usuarioEnSesion, passwordNueva);
                            if (estado == -1) {
                                // Enviamos mensaje 
                                Utilities.enviarAvisoRequest(request,
                                        "Se ha actualizado la contrasena correctamente",
                                        false);
                            } else {
                                // Enviamos mensaje de error
                                Utilities.enviarAvisoRequest(request,
                                        "Algo ha salido mal",
                                        true);
                            }
                        } else {
                            // Enviamos mensaje de error
                            Utilities.enviarAvisoRequest(request,
                                    "La contrasena antigua no es la correcta",
                                    true);
                        }
                    } else {
                        // Enviamos mensaje de error
                        Utilities.enviarAvisoRequest(request,
                                "Todos los campos son obligatorios para cambiar la contrasena",
                                true);
                    }
                    break;
                case "actualizarAvatar":
                    urlDispatcher = "/JSP/menuUsuario.jsp";
                    error = false;
                    dirImagen = request.getServletContext().getRealPath("/IMAGES/AVATARS/");
                    nombreFichero = new StringBuilder();
                    
                    // Declaramos los dao
                    daof = new MySQLDAOFactory();
                    udao = daof.getUsuarioDAO();

                    // Declaramos la factoría
                    factory = new DiskFileItemFactory();
                    upload = new ServletFileUpload(factory);
                    
                    // Declaramos la lista de objetos FileItem
                    usuarioEnSesion = (Usuario) request.getSession().getAttribute("usuarioEnSesion");
                    try {
                        items = upload.parseRequest(request);
                        
                        // Recorremos el iterator de File
                        iteratorFile = items.iterator();

                        while (iteratorFile.hasNext() && !error) {
                            uploaded = iteratorFile.next();
                            
                            // En el caso de que no se un control normal
                            if (!uploaded.isFormField()) {
                                
                                // Comprobamos que el fichero tenga la extensión permitida
                                if (uploaded.getContentType().equals("image/png") || uploaded.getContentType().equals("image/jpeg")) {
                                    
                                    // Comprobamos que el tamano es valido
                                    if (uploaded.getSize() < 102400) {
                                        // Obtenemos la extension
                                        extension = uploaded.getContentType().equals("image/png") ? ".png" : ".jpg";
                                        
                                        // Obtenemos el nombre del fichero como AvatarN + identificativo del usuario
                                        nombreFichero.append("AvatarN").append(String.valueOf(usuarioEnSesion.getIdUsuario())).append(extension);
                                        filePath = dirImagen + nombreFichero.toString();
                                        
                                        // Obtenemos el objeto File a partir de la variable anterior
                                        fichero = new File(filePath);
                                        try {
                                            // Escribimos el fichero en el servidor
                                            uploaded.write(fichero);
                                            // Alcenamos el nombre en el objeto usuario
                                            usuarioEnSesion.setAvatar(nombreFichero.toString());
                                            
                                            // Actualizamos el usuario en sesion y en la base de datos
                                            request.getSession().setAttribute("usuarioEnSesion", usuarioEnSesion);
                                            udao.actualizarAvatar(usuarioEnSesion);
                                            
                                            // Enviamos mensaje
                                            Utilities.enviarAvisoRequest(request,
                                                    "Se ha podido almacenar la imagen en el servidor correctamente",
                                                    false);
                                        } catch (Exception ex) {
                                            // Enviamos mensaje de error
                                            Utilities.enviarAvisoRequest(request,
                                                    "No se ha podido almacenar la imagen en el servidor",
                                                    true);
                                            error = true;
                                        }
                                    } else {
                                        // Enviamos mensaje de error
                                        Utilities.enviarAvisoRequest(request,
                                                "La imagen sobrepasa el tamano permitido",
                                                true);
                                        error = true;
                                    }
                                } else {
                                    // Enviamos mensaje de error
                                    Utilities.enviarAvisoRequest(request,
                                            "La imagen no tiene el formato adecuado",
                                            true);
                                    error = true;
                                }
                            }
                        }
                    } catch (FileUploadException ex) {
                        // Enviamos mensaje de error
                        Utilities.enviarAvisoRequest(request,
                                "No se han podido leer los campos del formulario",
                                true);
                    }
                    break;
                case "volver":
                    urlDispatcher = "index.jsp";
                    break;
                default:
                    throw new AssertionError();
            }
        }

        request.getRequestDispatcher(urlDispatcher).forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
