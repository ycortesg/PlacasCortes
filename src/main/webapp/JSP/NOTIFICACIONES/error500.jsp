<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
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
        <title>Error 500</title>
    </head>
    <body class="d-flex align-items-center justify-content-center bg-black bg-opacity-10 vh-100">
        <div class="w-50 h-50 d-flex align-items-center justify-content-between position-relative p-3 rounded-4 error " style="background-image: url('${applicationScope.imagenes}NOTIFICACIONES/500.jpg')">
            <c:choose>
                <c:when test="${sessionScope.usuarioEnSesion != null}">
                    <img class="img-fluid d-block mx-auto h-75" src="${applicationScope.imagenes}AVATARS/${sessionScope.usuarioEnSesion.avatar}">
                </c:when>
                <c:otherwise>
                    <svg xmlns="http://www.w3.org/2000/svg" fill="black" class="bi bi-person img-fluid d-block mx-auto h-75" viewBox="0 0 16 16">
                    <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0m4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4m-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664z"/>
                    </svg>
                </c:otherwise>
            </c:choose>
            <div class="h-50 w-50 d-flex align-items-center justify-content-center flex-column gap-5 bg-light bg-opacity-75 rounded-4">
                <h1>
                    Error
                </h1>
                <c:if test="${sessionScope.usuarioEnSesion != null}">
                    <h3>
                        ¡Rompieste algo <b>${sessionScope.usuarioEnSesion.nombre}</b>!
                    </h3>
                </c:if>
                <form action="Volver" method="POST"><input type="submit" name="volver" value="Volver"></form>
            </div>
        </div>
    </body>
</html>
