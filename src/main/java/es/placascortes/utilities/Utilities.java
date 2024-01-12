/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.placascortes.utilities;

import es.placascortes.beans.LineaPedido;
import es.placascortes.beans.Pedido;
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

    public static Byte accionesCarrito(HttpSession sesion, Short idProducto, String accion) {
        
        Pedido pedidoCarrito = (Pedido) sesion.getAttribute("carrito");
        if (pedidoCarrito == null) {
            pedidoCarrito = new Pedido();
            pedidoCarrito.setListadoLineasPedido(new ArrayList<LineaPedido>());
        }

        List<LineaPedido> listadoLineas = pedidoCarrito.getListadoLineasPedido();
        Boolean carritoASidoModificado = false;
        LineaPedido lineaPedido = null;
        Producto producto = null;
        Usuario usuario = null;
        Byte cantidadProducto = null;
        for (Short i = 0; i < listadoLineas.size(); i++) {
            lineaPedido = listadoLineas.get(i);
            if (lineaPedido.getProducto().getIdProducto().equals(idProducto)) {
                switch (accion) {
                    case "anadirACarrito":
                        carritoASidoModificado = true;
                        listadoLineas.get(i).setCantidad((byte) (lineaPedido.getCantidad()+1));

                        break;
                    case "eliminarDeCarrito":
                        if (listadoLineas.get(i).getCantidad() > 0) {
                            carritoASidoModificado = true;
                            listadoLineas.get(i).setCantidad((byte) (lineaPedido.getCantidad()-1));
                        }

                        break;
                    case "eliminarDeCarritoEntero":
                        carritoASidoModificado = true;
                        listadoLineas.get(i).setCantidad((byte) 0);

                        break;
                }
                cantidadProducto = listadoLineas.get(i).getCantidad();
            }
        }
        if (!carritoASidoModificado) {
            lineaPedido = new LineaPedido();
            producto = new Producto();

            producto.setIdProducto(idProducto);

            lineaPedido.setCantidad((byte) 1);
            lineaPedido.setProducto(producto);

            listadoLineas.add(lineaPedido);
            cantidadProducto = 1;
        }

        pedidoCarrito.setListadoLineasPedido(listadoLineas);
        sesion.setAttribute("carrito", pedidoCarrito);
        return cantidadProducto;
    }

    public static Integer sumUnidadesCarrito(List<LineaPedido> listadoLineaPedido) {
        Integer sumUnidades = 0;
        for (LineaPedido lineaPedido : listadoLineaPedido) {
            sumUnidades += lineaPedido.getCantidad();
        }
        return sumUnidades;
    }
    
    public static Float sumPreciosCarrito(List<LineaPedido> listadoLineaPedido) {
        Float sumPrecios = 0f;
        for (LineaPedido lineaPedido : listadoLineaPedido) {
            sumPrecios += lineaPedido.getCantidad() * lineaPedido.getProducto().getPrecio();
        }
        return sumPrecios;
    }

    public static Byte productoEnCarrito(List<LineaPedido> listadoLineaPedido, Short idProducto) {
        byte resultado = 0;
        for (Integer i = 0; listadoLineaPedido != null && i < listadoLineaPedido.size() && resultado == 0; i++) {
            if (listadoLineaPedido.get(i).getProducto().getIdProducto().equals(idProducto) && listadoLineaPedido.get(i).getCantidad() != 0) {
                resultado = listadoLineaPedido.get(i).getCantidad();
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
        Pedido pedidoCarrito = (Pedido) sesion.getAttribute("carrito");
        List<LineaPedido> listadoLineas = null;
        if (pedidoCarrito != null) listadoLineas = pedidoCarrito.getListadoLineasPedido();
        
        for (Integer i = 0; pedidoCarrito != null && i < listadoLineas.size(); i++) {
            if (!listadoLineas.get(i).getCantidad().equals(new Byte("0"))) {
                if (i > 0) {
                    resultado.append("===");
                }
                resultado.append(listadoLineas.get(i).getProducto().getIdProducto() + "-" + listadoLineas.get(i).getCantidad());
            }
        }

        Cookie cookie = new Cookie("carritoPlacasCortes", resultado.toString());

        cookie.setMaxAge(pedidoCarrito == null || resultado.toString().isBlank() ? 0 : 172800);
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
        List<LineaPedido> listadoLineaPedido = null;
        Producto producto = null;
        Pedido pedidoCarrito = null;
        LineaPedido lineaPedido = null;
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
            listadoLineaPedido = new ArrayList();
            for (String carritoStr : decodedCoockieList.split("===")) {
                if (!carritoStr.isBlank()) {
                    carritoArr = carritoStr.split("-");

                    producto = new Producto();
                    lineaPedido = new LineaPedido();

                    producto.setIdProducto(Short.parseShort(carritoArr[0]));
                    lineaPedido.setCantidad(Byte.parseByte(carritoArr[1]));

                    lineaPedido.setProducto(producto);
                    if (lineaPedido.getCantidad() > 0) {
                        listadoLineaPedido.add(lineaPedido);
                    }

                }
            }
            pedidoCarrito = new Pedido();
            pedidoCarrito.setListadoLineasPedido(listadoLineaPedido);
            sesion.setAttribute("carrito", pedidoCarrito);
        }
    }

    /**
     * devulve si el carrito esta en sesion true y si no false
     *
     * @param sesion
     * @return
     */
    public static Boolean carritoEstaEnSesion(HttpSession sesion) {
        Pedido pedidoCarrito = (Pedido) sesion.getAttribute("carrito");
        return pedidoCarrito != null;
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
    
    public static HttpServletRequest enviarAvisoRequest(HttpServletRequest request, String mensaje, Boolean esError){
        request.setAttribute("aviso", mensaje);
        if (esError) request.setAttribute("error", "error");
        return request;
    }
}
