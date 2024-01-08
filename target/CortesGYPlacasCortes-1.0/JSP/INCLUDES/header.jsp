<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<header class="d-flex align-items-center justify-content-between w-75 py-3 " >
    <img src="${applicationScope.imagenes}LOGOS/logo.png" alt="logo" class="logo">

        <form method="POST" action="FrontController" class="d-flex align-items-center justify-content-center gap-3">
            <div>                
                <c:choose>
                    <c:when test="${param.hayCuentaEnSession}">
                        <img src="${applicationScope.imagenes}AVATARS/${sessionScope.usuarioEnSesion.avatar}" width="23" height="23" alt="${sessionScope.usuarioEnSesion.nombre}"/>
                        <button type="submit" name="opcion" value="cuenta" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm">Cuenta</button>
                        <button type="submit" name="opcion" value="cerrerSesion" class="border-0 text-white  p-2 rounded-4 fondo-login px-3 shadow-sm">Cerrer sesión</button>
                    </c:when>
                    <c:otherwise>
                        <svg xmlns="http://www.w3.org/2000/svg" width="23" height="23" fill="currentColor" class="bi bi-person" viewBox="0 0 16 16">
                            <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0m4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4m-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664z"/>
                        </svg>
                        <button id="inicioSesion" name="opcion" value="inicioSesion" type="button" data-bs-toggle="modal" data-bs-target="#exampleModal" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm">Iniciar Sesion</button>
                        <button type="submit" name="opcion" value="registro" class="border-0 text-white  p-2 rounded-4 fondo-login px-3 shadow-sm">Registrarse</button>
                    </c:otherwise> 
                </c:choose>
            </div>
            <div>
                <button type="submit" name="opcion" value="carrito" class="bg-light rounded-4 border-login  carrito-boton shadow-sm position-relative">
                    <svg xmlns="http://www.w3.org/2000/svg" width="23" height="23" fill="currentColor" class="bi bi-cart2 carrito-logo z-2" viewBox="0 0 16 16">
                        <path d="M0 2.5A.5.5 0 0 1 .5 2H2a.5.5 0 0 1 .485.379L2.89 4H14.5a.5.5 0 0 1 .485.621l-1.5 6A.5.5 0 0 1 13 11H4a.5.5 0 0 1-.485-.379L1.61 3H.5a.5.5 0 0 1-.5-.5M3.14 5l1.25 5h8.22l1.25-5H3.14zM5 13a1 1 0 1 0 0 2 1 1 0 0 0 0-2m-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0m9-1a1 1 0 1 0 0 2 1 1 0 0 0 0-2m-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0"/>
                    </svg>
                    Carrito
                    <span id="burbujaCarrito" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                        <fmt:parseNumber var="productosEnCarrito" value="0" type="number" integerOnly="true"></fmt:parseNumber>
                        <c:choose>
                            <c:when test="${param.hayCuentaEnSession}">
                                <c:if test="${sessionScope.carrito != null}">
                                    <c:forEach var="carrito" items="${sessionScope.carrito}">
                                        <fmt:parseNumber var="cantidadProducto" value="${carrito.cantidad}"  type="number" integerOnly="true"></fmt:parseNumber>
                                        <fmt:parseNumber var="productosEnCarrito" value="${productosEnCarrito + cantidadProducto}"></fmt:parseNumber>
                                    </c:forEach>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <c:forTokens var="carrito" items="${param.coockieCarrito}" delims="===">
                                    <fmt:parseNumber var="cantidadProducto" value="${fn:split(carrito, '-')[1]}" type="number" integerOnly="true"></fmt:parseNumber>
                                    <fmt:parseNumber var="productosEnCarrito" value="${productosEnCarrito + cantidadProducto}"></fmt:parseNumber>
                                </c:forTokens>
                            </c:otherwise>
                        </c:choose>
                        <b>${productosEnCarrito}</b>
                        <span class="visually-hidden">productos en carrito</span>
                    </span>
                </button>
            </div>
        </form>
</header>