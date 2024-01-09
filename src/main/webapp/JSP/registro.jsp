<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <script src="${applicationScope.javascript}registro.js" defer type="module"></script>
        <title>Registro</title>
    </head>
    <body class="vh-100 d-flex align-items-center justify-content-center flex-column">
        <h1>Registro</h1>

        <form method="POST" action="RegistroController">
            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <input name="nombre" type="text" class="form-control" id="nombre" pattern="\S+.*" required>
            </div>
            <div class="form-group">
                <label for="apellidos">Apellidos:</label>
                <input name="apellidos" type="text" class="form-control" id="apellidos" pattern="\S+.*\s+\S+.*" required
                       oninvalid="this.setCustomValidity('Dos palabras separadas por espacio')"
                       oninput="this.setCustomValidity('')">
            </div>
            <div class="form-group">
                <label for="telefono">Número de teléfono:</label>
                <input name="telefono" type="tel" class="form-control" id="telefono" pattern="[0-9]{9}" required
                       oninvalid="this.setCustomValidity('9 digitos')"
                       oninput="this.setCustomValidity('')">
            </div>
            <div class="form-group">
                <label for="nif">NIF:</label>
                <input name="NIF" type="text" class="form-control" id="nif" pattern="[0-9]{8}" required
                       oninvalid="this.setCustomValidity('8 digitos')"
                       oninput="this.setCustomValidity('')">
                <input readonly name="letraNIF" id="letraNIF">
            </div>
            <div class="form-group">
                <label for="direccion">Dirección:</label>
                <input name="direccion" type="text" class="form-control" id="direccion" pattern="\S+.*" required>
            </div>
            <div class="form-group">
                <label for="codigoPostal">Código postal:</label>
                <input name="codigoPostal" type="text" class="form-control" id="codigoPostal" required
                       oninvalid="this.setCustomValidity('5 digitos válidos')"
                       oninput="this.setCustomValidity('')">
            </div>
            <div class="form-group">
                <label for="provincia">Provincia:</label>
                <input name="provincia" type="text" class="form-control" id="provincia" readonly pattern="\S+.*" required>
            </div>
            <div class="form-group">
                <label for="localidad">Localidad:</label>
                <input name="localidad" type="text" class="form-control" id="localidad" pattern="\S+.*" required>
            </div>
            <div class="form-group">
                <label for="email">Correo electrónico:</label>
                <input name="email" type="email" class="form-control" id="email" required
                       oninvalid="this.setCustomValidity('Correo electrónico válido')"
                        oninput="this.setCustomValidity('')">
            </div>
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input name="password" type="password" class="form-control" id="password" required>
            </div>
            <div class="form-group">
                <label for="password2">Repite la contraseña:</label>
                <input type="password" class="form-control" id="password2" required>
            </div>
            <button type="submit" name="opcion" value="registrate" class="btn btn-primary" disabled id="btnRegistro">Registrate</button>
            <button type="submit" name="opcion" value="volver" class="btn btn-primary" formnovalidate>Volver</button>
        </form>
    </body>
</html>
