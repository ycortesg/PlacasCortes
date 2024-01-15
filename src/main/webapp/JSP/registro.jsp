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
        <script id="main" src="${applicationScope.javascript}registro.js" data-json-route="${applicationScope.path}JSON/provincias.json" defer type="module"></script>
        <title>Registro</title>
    </head>
    <body class="vh-100 d-flex align-items-center justify-content-center overflow-hidden">
        <c:if test="${requestScope.aviso != null}">
            <jsp:include page="JSP/INCLUDES/avisos.jsp">
                <jsp:param name="error" value="${requestScope.error != null}"/>
                <jsp:param name="mensaje" value="${requestScope.aviso}"/>
            </jsp:include>
        </c:if>
        <div class="circulo-fondo"></div>
        <main class="w-75 p-4 bg-white shadow-lg d-flex align-items-center justify-content-center flex-column rounded-4 overflow-auto">

            <h1 class="text-success">Registro</h1>

            <form name="form" method="POST" action="RegistroController" class="row g-3 needs-validation" novalidate oninput='password2.setCustomValidity(password2.value != password.value ? "La password no es igual" : "")'>
                <div class="col-md-4">
                    <label for="nombre" class="form-label">Nombre</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" required pattern="\S+.*" />
                    <div class="invalid-feedback">Introduce tu nombre</div>
                </div>
                <div class="col-md-8">
                    <label for="apellidos" class="form-label">Apellidos</label>
                    <input type="text" class="form-control" id="apellidos" name="apellidos" required pattern="\S+.*\s+\S+.*" />
                    <div class="invalid-feedback">Introduce tus apellidos</div>
                </div>

                <div class="col-md-8 ">
                    <label for="nif" class="form-label">NIF</label>
                    <div class="input-group has-validation">
                        <input type="text" class="form-control" id="nif" name="NIF" pattern="[0-9]{8}"
                               aria-describedby="inputGroupPrepend" required />
                        <input style="width: 5rem" readonly required name="letraNIF" id="letraNIF" class="input-group-text" />
                        <div class="invalid-feedback">Introduce un NIF válido</div>
                    </div>
                </div>

                <div class="col-md-4">
                    <label for="telefono" class="form-label">Número de teléfono</label>
                    <input type="text" class="form-control" id="telefono" name="telefono" required
                           pattern="^(9|8|7|6)[0-9]{8}$" />
                    <div class="invalid-feedback">
                        Introduce un número de telefono válido
                    </div>
                </div>

                <div class="col-md-12">
                    <label for="direccion" class="form-label">Dirección</label>
                    <input type="text" class="form-control" id="direccion" name="direccion" required
                           pattern="^\S+.*" />
                    <div class="invalid-feedback">Introduce una dirección válida</div>
                </div>

                <div class="col-md-6">
                    <label for="codigoPostal" class="form-label">Código postal</label>
                    <input type="text" class="form-control" id="codigoPostal" name="codigoPostal" required
                           pattern="^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$" />
                    <div class="invalid-feedback">Introduce un código postal válido</div>
                </div>
                <div class="col-md-6">
                    <label for="provincia" class="form-label">Provincia</label>
                    <input type="text" class="form-control" id="provincia" name="provincia" required readonly />
                    <div class="invalid-feedback">Introduce un código postal válido</div>
                </div>

                <div class="col-md-4">
                    <label for="localidad" class="form-label">Localidad</label>
                    <input type="text" class="form-control" id="localidad" name="localidad" required pattern="\S+.*" />
                    <div class="invalid-feedback">Introduce tu localidad</div>
                </div>
                <div class="col-md-8">
                    <label for="email" class="form-label">Correo electrónico</label>
                    <input type="email" class="form-control" id="email" name="email" required />
                    <div class="invalid-feedback">Introduce un correo electrónico valido</div>
                </div>

                <div class="col-md-6">
                    <label for="password" class="form-label">Contraseña</label>
                    <input type="password" class="form-control" id="password" name="password" required/>
                    <div class="invalid-feedback">Introduce una contraseña válida</div>
                </div>
                <div class="col-md-6">
                    <label for="password2" class="form-label">Repite la contraseña</label>
                    <input type="password" class="form-control" id="password2" name="password2" required  />
                    <div class="invalid-feedback">Repite la contraseña</div>
                </div>
                <div class="col-md-12 d-flex align-items-center justify-content-center">
                    <div class="col-md-6 d-flex align-items-center justify-content-center">
                        <button type="submit" name="opcion" value="registrate" class="btn btn-primary col-md-6 bg-success" id="btnRegistro">Registrate</button>
                    </div>
                    <div class="col-md-6 d-flex align-items-center justify-content-center">
                        <button id="btnVolver" type="submit" name="opcion" value="volver" class="btn btn-primary col-md-6 bg-success">Volver</button>
                    </div>
                </div>
            </form>
        </main>
    </body>
</html>
