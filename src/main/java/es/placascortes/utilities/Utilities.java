/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.utilities;

import es.placascortes.beans.Carrito;
import es.placascortes.beans.Producto;
import es.placascortes.beans.Usuario;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author _
 */
public class Utilities implements Serializable {

    /**
     * Comprueba si todos los campos del formulario estan rellenos
     *
     * @param valoresForulario
     * @return
     */
    public static boolean formularioEstaRelleno(Map<String, String[]> valoresForulario) {
        boolean faltaAlgunValor = false;
        for (Map.Entry<String, String[]> entry : valoresForulario.entrySet()) {
            if (entry.getValue().length == 0 || entry.getValue()[0].isBlank()) {
                faltaAlgunValor = true;
            }
        }
        return !faltaAlgunValor;
    }

    public static Short accionesCarrito(HttpSession sesion, Short idProducto, String accion) {
        
        List<Carrito> listadoCarrito = (List) sesion.getAttribute("carrito");
        if (listadoCarrito == null) {
            listadoCarrito = new ArrayList();
        }

        Boolean carritoASidoModificado = false;
        Carrito carrito = null;
        Producto producto = null;
        Usuario usuario = null;
        Short cantidadProducto = null;
        for (Short i = 0; i < listadoCarrito.size(); i++) {
            carrito = listadoCarrito.get(i);
            if (carrito.getProducto().getIdProducto().equals(idProducto)) {
                switch (accion) {
                    case "anadirACarrito":
                        carritoASidoModificado = true;
                        listadoCarrito.get(i).setCantidad((short) (carrito.getCantidad() + 1));

                        break;
                    case "eliminarDeCarrito":
                        if (listadoCarrito.get(i).getCantidad() > 0) {
                            carritoASidoModificado = true;
                            listadoCarrito.get(i).setCantidad((short) (carrito.getCantidad() - 1));
                        }

                        break;
                    case "eliminarDeCarritoEntero":
                        carritoASidoModificado = true;
                        listadoCarrito.get(i).setCantidad((short) 0);

                        break;
                }
                cantidadProducto = listadoCarrito.get(i).getCantidad();
            }
        }
        if (!carritoASidoModificado) {
            carrito = new Carrito();
            producto = new Producto();

            producto.setIdProducto(idProducto);

            carrito.setCantidad((short) 1);
            carrito.setProducto(producto);

            listadoCarrito.add(carrito);
            cantidadProducto = 1;
        }

        sesion.setAttribute("carrito", listadoCarrito);
        return cantidadProducto;
    }

    public static Integer sumUnidadesCarrito(List<Carrito> listadoCarrito) {
        Integer sumUnidades = 0;
        for (Carrito carrito : listadoCarrito) {
            sumUnidades += carrito.getCantidad();
        }
        return sumUnidades;
    }
    
    public static Float sumPreciosCarrito(List<Carrito> listadoCarrito) {
        Float sumPrecios = 0f;
        for (Carrito carrito : listadoCarrito) {
            sumPrecios += carrito.getCantidad() * carrito.getProducto().getPrecio();
        }
        return sumPrecios;
    }

    public static Short productoEnCarrito(List<Carrito> listadoCarrito, Short idCarrito) {
        Short resultado = 0;
        for (Integer i = 0; listadoCarrito != null && i < listadoCarrito.size() && resultado == 0; i++) {
            if (listadoCarrito.get(i).getProducto().getIdProducto().equals(idCarrito) && listadoCarrito.get(i).getCantidad() != 0) {
                resultado = listadoCarrito.get(i).getCantidad();
            }
        }
        return resultado;
    }

    /**
     * pide response y la sesion y anade el listado de carrito formateado a una
     * coockie
     *
     * @param sesion
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void crearCookie(HttpSession sesion, HttpServletResponse response) throws UnsupportedEncodingException {
        StringBuilder resultado = new StringBuilder();
        List<Carrito> listadoCarrito = (List) sesion.getAttribute("carrito");

        for (Integer i = 0; listadoCarrito != null && i < listadoCarrito.size(); i++) {
            if (!listadoCarrito.get(i).getCantidad().equals((short) 0)) {
                if (i > 0) {
                    resultado.append("===");
                }
                resultado.append(listadoCarrito.get(i).getProducto().getIdProducto() + "-" + listadoCarrito.get(i).getCantidad());
            }
        }

        Cookie cookie = new Cookie("carritoPlacasCortes", resultado.toString());

        cookie.setMaxAge(listadoCarrito == null || resultado.toString().isBlank() ? 0 : 172800);
        response.addCookie(cookie);
    }

    /**
     * pide request y lee la cookie y actualiza la sesion
     *
     * @param sesion
     * @param request
     * @throws UnsupportedEncodingException
     */
    public static void leerCoockie(HttpSession sesion, HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        List<Carrito> listadoCarrito = null;
        Producto producto = null;
        Carrito carrito = null;
        String decodedCoockieList;
        String[] carritoArr = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length && cookie == null; i++) {
                if (cookies[i].getName().equals("carritoPlacasCortes")) {
                    cookie = cookies[i];
                }
            }
        }
        if (cookie != null) {
            decodedCoockieList = URLDecoder.decode(cookie.getValue(), "UTF-8");
            listadoCarrito = new ArrayList();
            for (String carritoStr : decodedCoockieList.split("===")) {
                if (!carritoStr.isBlank()) {
                    carritoArr = carritoStr.split("-");

                    producto = new Producto();
                    carrito = new Carrito();

                    producto.setIdProducto(Short.parseShort(carritoArr[0]));
                    carrito.setCantidad(Short.parseShort(carritoArr[1]));

                    carrito.setProducto(producto);
                    if (carrito.getCantidad() > 0) {
                        listadoCarrito.add(carrito);
                    }

                }

            }
            sesion.setAttribute("carrito", listadoCarrito);
        }
    }

    /**
     * devulve si el carrito esta en sesion true y si no false
     *
     * @param sesion
     * @return
     */
    public static Boolean carritoEstaEnSesion(HttpSession sesion) {
        List<Carrito> listado = (List) sesion.getAttribute("carrito");
        return listado != null;
    }

    /**
     * devulve si el usuario esta ensesion true y si no false
     *
     * @param sesion
     * @return
     */
    public static Boolean usuarioEstaEnSesion(HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioEnSesion");
        return usuario != null;
    }

    /**
     * pide response y elimina la cookie del carrito guardado
     *
     * @param response
     */
    public static void eliminarCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("carritoPlacasCortes", "");

        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
