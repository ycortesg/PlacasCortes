<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <%@include file="/INCLUDE/metas.inc"%>
        <link rel="stylesheet" href="${applicationScope.estilo}"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
              integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="icon" type="image/x-icon" href="${applicationScope.imagenes}LOGOS/logo.ico">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${applicationScope.javascript}carrito.js" defer type="module"></script>
        <title>Carrito</title>
    </head>
    <body class="d-flex justify-content-start align-items-center vh-100 flex-column gap-3">
        <div class="modal fade top-100 overflow-visible" id="facturaModal" tabindex="-1" aria-labelledby="facturaModalLabel" aria-hidden="true">
            <div class="modal-dialog position-relative overflow-visible">
                <div class="modal-content position-absolute bottom-0" id="modalFacturaModal">
                    <div class="modal-header">
                        <h5 class="modal-title" id="facturaModalLabel">Factura</h5>
                        <!--<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>-->
                    </div>
                    <div class="modal-body">
                        <table class="table table-striped" id="tablaFactura">
                            <thead>
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">Nombre</th>
                                    <th scope="col">Precio</th>
                                    <th scope="col">Unidades</th>
                                    <th scope="col">Total</th>
                                </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                        <form method="POST" action="CarritoController">
                            <input type="hidden" id="importe" name="importe">
                            <input type="hidden" id="iva" name="iva">
                            <button id="finalizarCompra" type="button" value="finalizarCompra" name="opcion" class="btn btn-primary">Finalizar compra</button>
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal" aria-label="Close">Cancelar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <h1>Carrito</h1>
        <div class="w-75 h-75 overflow-auto d-flex justify-content-center align-items-start flex-column">
            <fmt:parseNumber var="productosEnCarrito" value="0" type="number" integerOnly="true"></fmt:parseNumber>
            <c:forEach var="carrito" items="${sessionScope.carrito.listadoLineasPedido}">
                <fmt:parseNumber var="cantidadProducto" value="${carrito.cantidad}"  type="number" integerOnly="true"></fmt:parseNumber>
                <fmt:parseNumber var="productosEnCarrito" value="${productosEnCarrito + cantidadProducto}"></fmt:parseNumber>
                <c:if test="${carrito.cantidad > 0}">
                    <c:set var="id" value="id=\"${carrito.producto.idProducto}\""></c:set>
                    <section class="w-100 d-flex justify-content-between align-items-center shadow-lg" ${id} >
                        <img id="imagenProducto" class="p-3" height="140" width="140" src="${applicationScope.imagenes}PRODUCTOS/${carrito.producto.imagen}.jpg" alt="${carrito.producto.nombre}">
                        <div class="w-75 d-flex justify-content-center align-items-center h-100">
                            <div class="d-flex justify-content-center align-items-start flex-column h-100 w-25">
                                <b id="precioProducto"><fmt:formatNumber type="currency" value="${carrito.producto.precio}" pattern="#,##0.00 €"/></b>
                                <h5 id="nombreProducto">${carrito.producto.nombre}</h5>
                            </div>
                            <div class="d-flex justify-content-center align-items-start flex-column h-100 w-50">
                                <h4 id="marcaProducto">${carrito.producto.marca}</h4>
                                <h4 id="categoriaProducto">${carrito.producto.categoria.nombre}</h4>
                            </div>
                            <div class="d-flex justify-content-center align-items-center flex-column h-100 w-25">
                                <div id="cantidadProducto" class="d-flex justify-content-center align-items-center h-50 w-50 text-center">
                                    <b>${carrito.cantidad}</b>
                                </div>
                                <div class="d-flex justify-content-center align-items-center h-50 w-50">
                                    <button class="btn btn-primary" id="anadirACarrito" >+</button>
                                    <button class="btn btn-primary" id="eliminarDeCarrito">-</button>
                                    <button class="btn btn-primary" id="eliminarDeCarritoEntero">Eliminar</button>
                                </div>
                            </div>
                        </div>
                    </section>
                </c:if>
            </c:forEach>
        </div>

        <form method="POST" action="CarritoController" class="d-flex align-items-center justify-content-center">
            <c:choose>
                <c:when test="${sessionScope.usuarioEnSesion == null}">
                    <button type="submit" name="opcion" value="registrate" class="btn btn-primary">Registrate</button>
                </c:when>
                <c:otherwise>
                    <c:set var="modalHabilitado" value="data-bs-target=\'#facturaModal\'"></c:set>
                    <c:if test="${productosEnCarrito == 0}">
                        <c:set var="modalHabilitado" value="disabled"></c:set>
                    </c:if>
                    <button id="botonFacturaModal" type="button" data-bs-toggle="modal" ${modalHabilitado} class="bg-light p-2 rounded-4 border-login px-3 shadow-sm">Finalizar compra</button>
                </c:otherwise>
            </c:choose>
            <button type="submit" name="opcion" value="volver" class="btn btn-primary">Volver</button>
        </form>
    </body>
</html>
