<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script id="main" src="${applicationScope.javascript}menuUsuario.js" data-json-route="${applicationScope.path}JSON/provincias.json" defer type="module"></script>
        <title>Menu Usuario</title>
    </head>
    <body class="d-flex align-items-center flex-column gap-3 vh-100">

        <div class="modal fade" id="modalDatosPersonales" tabindex="-1" aria-labelledby="modalDatosPersonalesLabel" aria-hidden="true">
            <div class="modal-dialog position-relative">
                <div class="modal-content position-absolute" >
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalDatosPersonalesLabel">Datos personales</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form method="POST" action="CuentaController">
                            <div class="form-group">
                                <label for="nombre">Nombre:</label>
                                <input name="nombre" type="text" class="form-control" id="nombre" value="${sessionScope.usuarioEnSesion.nombre}" pattern="\S+.*" required>
                            </div>
                            <div class="form-group">
                                <label for="apellidos">Apellidos:</label>
                                <input name="apellidos" type="text" class="form-control" id="apellidos" value="${sessionScope.usuarioEnSesion.apellidos}" pattern="\S+.*\s+\S+.*" required
                                       oninvalid="this.setCustomValidity('Dos palabras separadas por espacio')"
                                       oninput="this.setCustomValidity('')">
                            </div>
                            <div class="form-group">
                                <label for="telefono">Número de teléfono:</label>
                                <input name="telefono" type="tel" class="form-control" id="telefono" pattern="^(9|8|7|6)\\d{8}$" value="${sessionScope.usuarioEnSesion.telefono}" required
                                       oninvalid="this.setCustomValidity('9 digitos que empieza por 9, 8, 7 o 6')"
                                       oninput="this.setCustomValidity('')">
                            </div>
                            <div class="form-group">
                                <label for="direccion">Dirección:</label>
                                <input name="direccion" type="text" class="form-control" id="direccion" pattern="\S+.*" value="${sessionScope.usuarioEnSesion.direccion}" required>
                            </div>
                            <div class="form-group">
                                <label for="codigoPostal">Código postal:</label>
                                <input name="codigoPostal" type="text" class="form-control" id="codigoPostal" value="${sessionScope.usuarioEnSesion.codigoPostal}" required
                                       oninvalid="this.setCustomValidity('5 digitos válidos')"
                                       oninput="this.setCustomValidity('')">
                            </div>
                            <div class="form-group">
                                <label for="provincia">Provincia:</label>
                                <input name="provincia" type="text" class="form-control" id="provincia" readonly pattern="\S+.*" value="${sessionScope.usuarioEnSesion.provincia}" required>
                            </div>
                            <div class="form-group">
                                <label for="localidad">Localidad:</label>
                                <input name="localidad" type="text" class="form-control" id="localidad" pattern="\S+.*" value="${sessionScope.usuarioEnSesion.localidad}" required>
                            </div>
                            <button type="submit" name="opcion" value="editarDatosPersonales" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm my-2">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modalPassword" tabindex="-1" aria-labelledby="modalPasswordLabel" aria-hidden="true">
            <div class="modal-dialog position-relative">
                <div class="modal-content position-absolute" >
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalPasswordLabel">Contraseña</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form method="POST" action="CuentaController">
                            <div class="form-group">
                                <label for="passwordAntigua">Contraseña antigua:</label>
                                <input name="passwordAntigua" type="password" class="form-control" id="passwordAntigua" required>
                            </div>
                            <div class="form-group">
                                <label for="passwordNueva">Nueva contraseña:</label>
                                <input type="password" name="passwordNueva" class="form-control" id="passwordNueva" required>
                            </div>
                            <div class="form-group">
                                <label for="passwordNueva2">Repite la nueva contraseña:</label>
                                <input type="password" class="form-control" id="passwordNueva2" required>
                            </div>
                            <button type="button" name="opcion" value="editarPassword" disabled id="editarPassword" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm my-2">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modalAvatar" tabindex="-1" aria-labelledby="modalAvatarLabel" aria-hidden="true">
            <div class="modal-dialog position-relative">
                <div class="modal-content position-absolute" >
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalAvatarLabel">Avatar</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form method="POST" action="FrontController">
                            <button type="submit" name="opcion" value="inicioSesion" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm my-2">Iniciar Sesion</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${request.aviso != null}">
            <jsp:include page="JSP/INCLUDES/avisos.jsp">
                <jsp:param name="error" value="${sessionScope.error != null}"/>
                <jsp:param name="mensaje" value="${sessionScope.aviso}"/>
            </jsp:include>
        </c:if>

        <h1>Menu Usuario</h1>
        <main class="d-flex align-items-center justify-content-center">
            <div class="d-flex align-items-center justify-content-start gap-3 flex-column">
                <h2>Editar datos</h2>
                <button type="button" data-bs-toggle="modal" data-bs-target="#modalDatosPersonales" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm">Datos personales</button>
                <button id="botonModalPassword" type="button" data-bs-toggle="modal" data-bs-target="#modalPassword" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm">Contraseña</button>
                <button type="button" data-bs-toggle="modal" data-bs-target="#modalAvatar" class="bg-light p-2 rounded-4 border-login px-3 shadow-sm">Avatar</button>
            </div>
            <div class="d-flex align-items-center justify-content-start flex-column">
                <form action="CuentaController" method="POST"><button type="submit" name="opcion" value="visualizarPedidosFinalizados">Visualizar pedidos finalizados</button></form>
            </div>
        </main>
        <form action="CuentaController" method="POST"><button type="submit" name="opcion" value="volver">Volver</button></form>
    </body>
</html>
