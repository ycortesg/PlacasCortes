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
        <div class="w-75 h-75 overflow-auto d-flex justify-content-center align-items-start flex-column gap-3">
            <fmt:parseNumber var="productosEnCarrito" value="0" type="number" integerOnly="true"></fmt:parseNumber>
            <c:forEach var="carrito" items="${sessionScope.carrito.listadoLineasPedido}">
                <fmt:parseNumber var="cantidadProducto" value="${carrito.cantidad}"  type="number" integerOnly="true"></fmt:parseNumber>
                <fmt:parseNumber var="productosEnCarrito" value="${productosEnCarrito + cantidadProducto}"></fmt:parseNumber>
                <c:if test="${carrito.cantidad > 0}">
                    <c:set var="id" value="id=\"${carrito.producto.idProducto}\""></c:set>
                    <section class="w-100 d-flex justify-content-between align-items-center rounded-4 border border-dark border-opacity-10" ${id} >
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
                            <div class="d-flex justify-content-between align-items-center flex-column h-100 w-25">
                                <div id="cantidadProducto" class="d-flex justify-content-between align-items-center h-50 w-50 text-center">
                                    <div class="d-flex align-items-center justify-content-between flex-column opacity-75">
                                        <button class="bg-opacity-0 border-0 text-info" id="anadirACarrito" ><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-arrow-up-circle-fill" viewBox="0 0 16 16">
                                            <path d="M16 8A8 8 0 1 0 0 8a8 8 0 0 0 16 0m-7.5 3.5a.5.5 0 0 1-1 0V5.707L5.354 7.854a.5.5 0 1 1-.708-.708l3-3a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 5.707z"></path>
                                            </svg>
                                        </button>
                                        <button class="bg-opacity-0 border-0 text-danger" id="eliminarDeCarrito">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-arrow-down-circle-fill" viewBox="0 0 16 16">
                                            <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0M8.5 4.5a.5.5 0 0 0-1 0v5.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293z"></path>
                                            </svg>
                                        </button>
                                    </div>
                                    <b class="fs-3">${carrito.cantidad}</b>
                                </div>
                                <div class="d-flex justify-content-center align-items-center h-50 w-75">
                                    <button type="button" class="btn btn-outline-danger" id="eliminarDeCarritoEntero">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
                                        <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"></path>
                                        <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"></path>
                                        </svg>
                                        Eliminar
                                    </button>
                                </div>
                            </div>
                        </div>
                    </section>
                </c:if>
            </c:forEach>
        </div>

        <c:set var="modalHabilitado" value="data-bs-target=\'#facturaModal\'"></c:set>
        <c:set var="eliminarCarritoHabilitado" value=""></c:set>
        <c:if test="${productosEnCarrito == 0}">
            <c:set var="modalHabilitado" value="disabled"></c:set>
            <c:set var="eliminarCarritoHabilitado" value="disabled"></c:set>
        </c:if>

        <form method="POST" action="CarritoController" class="d-flex align-items-center justify-content-between w-75">
            <c:choose>
                <c:when test="${sessionScope.usuarioEnSesion == null}">
                    <button type="submit" name="opcion" value="registrate" class="btn btn-primary bg-success">Registrate</button>
                </c:when>
                <c:otherwise>
                    <button id="botonFacturaModal" type="button" data-bs-toggle="modal" ${modalHabilitado} class="btn btn-primary bg-success">Finalizar compra</button>
                </c:otherwise>
            </c:choose>
            <button type="submit" name="opcion" value="eliminarCarrito" ${eliminarCarritoHabilitado} class="btn btn-primary bg-success">Eliminar carrito</button>
            <button type="submit" name="opcion" value="volver" class="btn btn-primary bg-success">Inicio</button>
        </form>
    </body>
</html>
