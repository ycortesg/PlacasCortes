<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        <script id="main" src="${applicationScope.javascript}menuUsuario.js" data-json-route="${applicationScope.path}JSON/provincias.json" data-productos-route="${applicationScope.imagenes}PRODUCTOS/" defer type="module"></script>
        <title>Menu Usuario</title>
    </head>
    <body class="d-flex align-items-center justify-content-center flex-column gap-3 vh-100 menu-cuenta-usuario ">
        
        <c:if test="${requestScope.aviso != null}">
            <jsp:include page="/JSP/INCLUDES/avisos.jsp">
                <jsp:param name="error" value="${requestScope.error != null}"/>
                <jsp:param name="mensaje" value="${requestScope.aviso}"/>
            </jsp:include>
        </c:if>
        
        <div class="modal fade" id="pedidoModal" tabindex="-1" aria-labelledby="pedidoModalLabel" aria-hidden="true">
            <div class="modal-dialog position-relative ">
                <div class="modal-content" id="modalPedido">
                    <div class="modal-header">
                        <h5 class="modal-title" id="pedidoModalLabel">Información pedido</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <table class="table table-striped" id="tablaPedido">
                            <thead>
                                <tr>
                                    <th scope="col">Imagen</th>
                                    <th scope="col">Nombre</th>
                                    <th scope="col">Precio</th>
                                    <th scope="col">Unidades</th>
                                </tr>
                            </thead>
                            <tbody id="0">

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        
        <c:set value="justify-content-center" var="contenidoCentrado" ></c:set>
        <c:if test="${fn:length(fechasPedidos) > 0}">
            <c:set value="justify-content-between" var="contenidoCentrado" ></c:set>
        </c:if>

        <main class="d-flex align-items-center ${contenidoCentrado} flex-column rounded-4 h-75 w-75">
            <form action="CuentaController" method="POST"><button type="submit"  class="btn btn-primary bg-success" name="opcion" value="volver">Inicio</button></form>
            <h1>Menu Usuario</h1>
            <div class="d-flex align-items-center ${contenidoCentrado} w-100 h-75">
                <div class="d-flex align-items-center justify-content-start gap-3 flex-column h-100">
                    <h2>Editar datos</h2>
                    <div class="accordion" id="accordionActualizarDatos">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingOne">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne">
                                    Datos personales
                                </button>
                            </h2>
                            <div id="collapseOne" class="accordion-collapse collapse " aria-labelledby="headingOne" data-bs-parent="#accordionActualizarDatos">
                                <div class="accordion-body position-relative p-0 m-0">
                                    <form name="form" method="POST" action="CuentaController" class="py-3 m-0 row g-3 needs-validation top-0 start-0" novalidate>
                                        <div class="col-md-12">
                                            <label for="nombre" class="form-label">Nombre:</label>
                                            <input name="nombre" type="text" class="form-control" id="nombre" value="${sessionScope.usuarioEnSesion.nombre}" pattern="\S+.*" required>
                                            <div class="invalid-feedback">Introduce tu nombre</div>
                                        </div>

                                        <div class="col-md-12">
                                            <label for="apellidos" class="form-label">Apellidos</label>
                                            <input type="text" class="form-control" id="apellidos" name="apellidos" value="${sessionScope.usuarioEnSesion.apellidos}" required pattern="\S+.*\s+\S+.*" />
                                            <div class="invalid-feedback">Introduce tus apellidos</div>
                                        </div>

                                        <div class="col-md-12 ">
                                            <label for="telefono" class="form-label">Número de teléfono</label>
                                            <input type="text" class="form-control" id="telefono" name="telefono" value="${sessionScope.usuarioEnSesion.telefono}" required
                                                   pattern="^(9|8|7|6)[0-9]{8}$" />
                                            <div class="invalid-feedback">
                                                Introduce un número de telefono válido
                                            </div>
                                        </div>

                                        <div class="col-md-12">
                                            <label for="direccion" class="form-label">Dirección</label>
                                            <input type="text" class="form-control" id="direccion" name="direccion" value="${sessionScope.usuarioEnSesion.direccion}" required
                                                   pattern="^\S+.*" />
                                            <div class="invalid-feedback">Introduce una dirección válida</div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="codigoPostal" class="form-label">Código postal</label>
                                            <input type="text" class="form-control" id="codigoPostal" name="codigoPostal" value="${sessionScope.usuarioEnSesion.codigoPostal}" required
                                                   pattern="^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$" />
                                            <div class="invalid-feedback">Introduce un código postal válido</div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="provincia" class="form-label">Provincia</label>
                                            <input type="text" class="form-control" id="provincia" name="provincia" value="${sessionScope.usuarioEnSesion.provincia}" required readonly />
                                            <div class="invalid-feedback">Introduce un código postal válido</div>
                                        </div>

                                        <div class="col-md-12">
                                            <label for="localidad" class="form-label">Localidad</label>
                                            <input type="text" class="form-control" id="localidad" name="localidad" value="${sessionScope.usuarioEnSesion.localidad}" required pattern="\S+.*" />
                                            <div class="invalid-feedback">Introduce tu localidad</div>
                                        </div>
                                        <div class="col-md-12 d-flex align-items-center justify-content-between" id="containerBotones">
                                            <button id="actualizarDatosPersonales" type="submit" value="actualizarDatosPersonales" name="opcion" class="btn btn-primary no-confirmado col-md-12">Actualizar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingTwo">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                    Contraseña
                                </button>
                            </h2>
                            <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionActualizarDatos">

                                <div class="accordion-body position-relative p-0 m-0">
                                    <form name="form" method="POST" action="CuentaController" class="row px-4 g-3 needs-validation top-0 start-0 py-3 m-0" novalidate 
                                          oninput='password2.setCustomValidity(password2.value != password.value ? "La password no es igual" : "");
                                          password.setCustomValidity(password.value == passwordOld.value ? "Tiene que ser una password nueva" : "")'>

                                        <div class="col-md-12">
                                            <label for="passwordOld" class="form-label">Contraseña antigua</label>
                                            <input type="password" class="form-control" id="passwordOld" name="passwordOld" required/>
                                            <div class="invalid-feedback">Introduce una contraseña válida</div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="password" class="form-label">Contraseña nueva</label>
                                            <input type="password" class="form-control" id="password" name="password" required  />
                                            <div class="invalid-feedback">Repite la contraseña</div>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="password2" class="form-label">Repite la contraseña nueva</label>
                                            <input type="password" class="form-control" id="password2" name="password2" required  />
                                            <div class="invalid-feedback">Repite la contraseña</div>
                                        </div>
                                        <div class="col-md-12 d-flex align-items-center justify-content-between" id="containerBotones">

                                            <button id="actualizarPassword" type="submit" value="actualizarPassword" name="opcion" class="btn btn-primary no-confirmado col-md-12">Actualizar</button>
                                        </div>

                                    </form>
                                </div>

                            </div>
                        </div>
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingThree">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                                    Avatar
                                </button>
                            </h2>
                            <div id="collapseThree" class="accordion-collapse collapse" aria-labelledby="headingThree" data-bs-parent="#accordionActualizarDatos">
                                <div class="accordion-body d-flex align-items-center justify-content-between">
                                    
                                    <img src="${applicationScope.imagenes}AVATARS/${sessionScope.usuarioEnSesion.avatar}" width="120" height="120" alt="alt"/>
                                    
                                    <form  name="form" action="CuentaController" class="row g-3" method="POST" enctype="multipart/form-data">
                                        <div class="col-md-12">
                                            <label for="avatar" class="form-label">Avatar nuevo (png o jpg como máximo 100KB)</label>
                                            <input type="file" class="form-control" id="avatar" name="avatar" required  />
                                        </div>
                                        <div class="col-md-12 d-flex align-items-center justify-content-between" id="containerBotones">
                                            <button id="actualizarAvatar" type="submit" value="actualizarAvatar" name="opcion" class="btn btn-primary no-confirmado col-md-12">Actualizar</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <c:if test="${fn:length(fechasPedidos) > 0}">

                    <div class="d-flex align-items-center justify-content-start flex-column  h-100">
                        <h2>Compras realizadas</h2>
                        <select id="fechasPedidos" class="form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                            <option selected disabled>Seleccciona una fecha</option>
                            <c:forEach var="fecha" items="${fechasPedidos}">
                                <option value="${fecha}">${fecha}</option>
                            </c:forEach>
                        </select>
                        <div class="d-flex align-items-center justify-content-center flex-column w-100" id="listaPedidos">
                        </div>
                    </div>
                </c:if>
            </div>
        </main>
    </body>
</html>
