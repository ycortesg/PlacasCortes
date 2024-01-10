
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:url var="path" value="/" scope="application"></c:url>
<c:set var="estilo" value="${applicationScope.path}CSS/style.css" scope="application"></c:set>
<c:set var="imagenes" value="${applicationScope.path}IMAGES/" scope="application"></c:set>
<c:set var="javascript" value="${applicationScope.path}JS/" scope="application"></c:set>
    <!DOCTYPE html>
    <html lang="es">
        <head>
        <%@include file="INCLUDE/metas.inc"%>
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
        <script src="${applicationScope.javascript}inicio.js" defer type="module"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <title>Tienda virtual</title>
    </head>
    <body class="d-flex align-items-center justify-content-center flex-column bg-black bg-opacity-10">
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog position-relative">
                <div class="modal-content position-absolute d-none" id="modalInicioDeSesion">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Inicio de sesión</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form method="POST" action="FrontController">
                            <div class="form-group py-2">
                                <label for="email">Correo electrónico:</label>
                                <input name="email" type="email" class="form-control" id="email" required 
                                       pattern="/(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|&quot;(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*&quot;)@(?:(?:a-z0-9?\.)+a-z0-9?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/" 
                                       placeholder="Ingresa tu correo electrónico" 
                                       oninvalid="this.setCustomValidity('Correo electrónico válido')"
                                       oninput="this.setCustomValidity('')">
                            </div>
                            <div class="form-group py-2">
                                <label for="password">Contraseña:</label>
                                <input name="password" type="password" class="form-control" pattern="\S+.*" required id="password" placeholder="Ingresa tu contraseña">
                            </div>
                            <button type="submit" name="opcion" value="inicioSesion" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm my-2">Iniciar Sesion</button>
                        </form>
                    </div>
                </div>
                <div class="modal-content position-absolute d-none" id="modalProducto">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Nombre del Producto</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <span id="burbujaProducto" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                            <b class="fs-2">0</b>
                            <span class="visually-hidden">productos en carrito</span>
                        </span>
                        <img id="imagenProducto" src="${applicationScope.imagenes}PRODUCTOS/placasbase/14259007383287952.jpg" alt="" class="img-fluid d-block mx-auto mb-3 img-producto">
                        <div class="d-flex align-items-center justify-content-between w-100">
                            <div class="d-flex align-items-start flex-column w-75 ">
                                <h4 class="fw-bolder" id="precioProducto"></h4>
                                <p id="descripcionProducto"></p>
                                <h5 id="marcaProducto"></h5>
                                <h5 id="nombreCategoriaProducto"></h5>
                            </div>
                            <img src="${applicationScope.imagenes}CATEGORIAS/default.jpg" id="imagenCategoria" alt="" class="img-fluid d-block mx-auto mb-3 h-100 w-25 p-3">
                        </div>
                        <div class="form-group py-2">
                            <button id="anadirACarrito">Añadir a carrito</button>
                            <button id="eliminarDeCarrito">Eliminar unidad de carrito</button>
                            <button id="eliminarDeCarritoEntero">Eliminar de carrito</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="JSP/INCLUDES/header.jsp">
            <jsp:param name="hayCuentaEnSession" value="${sessionScope.usuarioEnSesion != null}"/>
            <jsp:param name="carritoEnSesion" value="${sessionScope.carrito}"/>
            <jsp:param name="coockieCarrito" value="${cookie.carritoPlacasCortes.value}"/>
        </jsp:include>
        <main class="d-flex align-items-start justify-content-center  h-100 w-100 py-4">
            <aside class="h-100 w-25 flex-column p-3 d-flex justify-content-start align-items-center gap-3">

                <div class="input-box position-relative">
                    <input type="text" class="form-control" id="buscador" placeholder="Buscar . . .">
                    <i class="fa fa-search"></i>                    
                </div>

                <select name="categoria" id="categorias" class="form-select m-0" aria-label="Default select example" multiple>
                    <c:forEach var="categoria" items="${applicationScope.listaCategorias}">
                        <option value="${categoria.idCategoria}">${categoria.nombre}</option>
                    </c:forEach>
                </select>
                <select name="marca" class="form-select m-0" id="marcas" aria-label="Default select example" multiple>
                    <c:forEach var="marca" items="${applicationScope.listaMarcas}">
                        <option value="${marca.marca}">${marca.marca}</option>
                    </c:forEach>
                </select>
            </aside>
            <div class="d-flex align-items-center justify-content-start h-100 w-75 row pb-5 mb-4" id="containerProductos">

                <c:forEach var="producto" items="${applicationScope.listaProductosEscaparate}">
                    <c:set var="id" value="id=\"${producto.idProducto}\""></c:set>
                    <div class="col-lg-4 col-md-6 mb-3 mb-lg-0 card-product gap-2" ${id} role="button" data-bs-toggle="modal" data-bs-target="#exampleModal">
                        <div class="card rounded shadow-sm border-0">
                            <div class="card-body p-4">
                                <img src="${applicationScope.imagenes}PRODUCTOS/${producto.imagen}.jpg" alt="${producto.nombre}" class="img-fluid d-block mx-auto mb-3 img-producto">
                                <div class="d-flex align-items-center justify-content-between w-100">
                                    <div class="d-flex align-items-start flex-column w-75 ">
                                        <h5 class="fs-6">${producto.nombre}</h5>
                                        <h4 class="fw-bolder "><fmt:formatNumber type="currency" value="${producto.precio}" pattern="#,##0.00 €"/></h4>
                                        <h5>${producto.marca}</h5>
                                    </div>
                                    <img src="${applicationScope.imagenes}CATEGORIAS/${producto.categoria.imagen}" alt="${producto.nombre}" class="img-fluid d-block mx-auto mb-3 h-100 w-25 p-3">
                                </div>
                            </div>
                        </div>
                    </div>

                </c:forEach>

            </div>
        </main>
        <footer class="d-flex align-items-center justify-content-center flex-column w-100 p-4 bottom-0 bg-white ">
            <div class="d-flex align-items-center justify-content-start gap-5 w-75 ">
                <div class="d-flex align-items-start justify-content-start flex-column h-100 gap-1 ">
                    <img src="${applicationScope.imagenes}LOGOS/logo.png" alt="logo" class="logo mb-2">
                    <h5>Contactanos</h5>
                    <div class="d-flex align-items-center justify-content-center gap-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-whatsapp" viewBox="0 0 16 16">
                        <path d="M13.601 2.326A7.854 7.854 0 0 0 7.994 0C3.627 0 .068 3.558.064 7.926c0 1.399.366 2.76 1.057 3.965L0 16l4.204-1.102a7.933 7.933 0 0 0 3.79.965h.004c4.368 0 7.926-3.558 7.93-7.93A7.898 7.898 0 0 0 13.6 2.326zM7.994 14.521a6.573 6.573 0 0 1-3.356-.92l-.24-.144-2.494.654.666-2.433-.156-.251a6.56 6.56 0 0 1-1.007-3.505c0-3.626 2.957-6.584 6.591-6.584a6.56 6.56 0 0 1 4.66 1.931 6.557 6.557 0 0 1 1.928 4.66c-.004 3.639-2.961 6.592-6.592 6.592zm3.615-4.934c-.197-.099-1.17-.578-1.353-.646-.182-.065-.315-.099-.445.099-.133.197-.513.646-.627.775-.114.133-.232.148-.43.05-.197-.1-.836-.308-1.592-.985-.59-.525-.985-1.175-1.103-1.372-.114-.198-.011-.304.088-.403.087-.088.197-.232.296-.346.1-.114.133-.198.198-.33.065-.134.034-.248-.015-.347-.05-.099-.445-1.076-.612-1.47-.16-.389-.323-.335-.445-.34-.114-.007-.247-.007-.38-.007a.729.729 0 0 0-.529.247c-.182.198-.691.677-.691 1.654 0 .977.71 1.916.81 2.049.098.133 1.394 2.132 3.383 2.992.47.205.84.326 1.129.418.475.152.904.129 1.246.08.38-.058 1.171-.48 1.338-.943.164-.464.164-.86.114-.943-.049-.084-.182-.133-.38-.232z"/>
                        </svg>
                        <div>
                            <h6>Whats App</h6>
                            <h6>+34 666 333 444</h6>
                        </div>
                    </div>
                    <div class="d-flex align-items-center justify-content-center gap-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-telephone" viewBox="0 0 16 16">
                        <path d="M3.654 1.328a.678.678 0 0 0-1.015-.063L1.605 2.3c-.483.484-.661 1.169-.45 1.77a17.568 17.568 0 0 0 4.168 6.608 17.569 17.569 0 0 0 6.608 4.168c.601.211 1.286.033 1.77-.45l1.034-1.034a.678.678 0 0 0-.063-1.015l-2.307-1.794a.678.678 0 0 0-.58-.122l-2.19.547a1.745 1.745 0 0 1-1.657-.459L5.482 8.062a1.745 1.745 0 0 1-.46-1.657l.548-2.19a.678.678 0 0 0-.122-.58L3.654 1.328zM1.884.511a1.745 1.745 0 0 1 2.612.163L6.29 2.98c.329.423.445.974.315 1.494l-.547 2.19a.678.678 0 0 0 .178.643l2.457 2.457a.678.678 0 0 0 .644.178l2.189-.547a1.745 1.745 0 0 1 1.494.315l2.306 1.794c.829.645.905 1.87.163 2.611l-1.034 1.034c-.74.74-1.846 1.065-2.877.702a18.634 18.634 0 0 1-7.01-4.42 18.634 18.634 0 0 1-4.42-7.009c-.362-1.03-.037-2.137.703-2.877L1.885.511z"/>
                        </svg>
                        <div>
                            <h6>Llamanos</h6>
                            <h6>+34 666 333 444</h6>
                        </div>
                    </div>
                </div>
                <div class="d-flex align-items-start justify-content-start flex-column h-100 gap-1 border-sections-footer px-5 ">
                    <h3 class="fs-4">Servicio al consumidor</h3>
                    <button class="bg-transparent border-0" type="submit" name="opcion" value="sobre-nosotros">Sobre nosotros</button>
                    <button class="bg-transparent border-0" type="submit" name="opcion" value="terminos">Terminos y condiciones</button>
                    <button class="bg-transparent border-0" type="submit" name="opcion" value="privacidad">Politica de privacidad</button>
                </div>
            </div>
            <div class="d-flex align-items-center justify-content-center p-3 w-75 border-top border-2 my-3 ">
                <div>
                    &copy; Yeray Cortés Grande
                </div>
            </div>
        </footer>
    </body>
</html>