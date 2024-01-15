// Declaramos variables
const url = "Ajax";

let burbujaCarrito = document.querySelector("span#burbujaCarrito b");
let buscador = document.querySelector("#buscador");
let burbujaCategorias = document.querySelector("span#burbujaCategorias");
let burbujaMarcas = document.querySelector("span#burbujaMarcas");

let modalInicioSesion = document.querySelector("#modalInicioDeSesion");
let modalProducto = document.querySelector("#modalProducto");

let modalBurbujaProducto = modalProducto.querySelector("span#burbujaProducto b");
let modalProductoNombreProducto = modalProducto.querySelector("#exampleModalLabel");
let modalProductoNombreCategoria = modalProducto.querySelector("#nombreCategoriaProducto");
let modalProductoImagenProducto = modalProducto.querySelector("#imagenProducto");
let modalProductoPrecio = modalProducto.querySelector("#precioProducto");
let modalProductoDescripcion = modalProducto.querySelector("#descripcionProducto");
let modalProductoMarca = modalProducto.querySelector("#marcaProducto");
let modalProductoImagenCategoria = modalProducto.querySelector("#imagenCategoria");

let botonAnadirACarrito = modalProducto.querySelector("button#anadirACarrito");
let botonEliminarUnidadDeCarrito = modalProducto.querySelector("button#eliminarDeCarrito");
let botonEliminarProductoDeCarrito = modalProducto.querySelector("button#eliminarDeCarritoEntero");

let botonInicioSesion = document.querySelector("button#inicioSesion");
let containerProductos = document.querySelector("#containerProductos");

// URLS de las carpetas de categorias y marcas
const defaultURLProducto = document.querySelector("script#main").getAttribute("data-productos-route");
const defaultURLCategoria = document.querySelector("script#main").getAttribute("data-categorias-route");

// Formateo para las monedas
const formateoMoneda = new Intl.NumberFormat('es-ES', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
    useGrouping: true,
    notation: 'standard',
    currencyDisplay: 'narrowSymbol'
});

// Controla en el caso de haber algun error en elguna imagen de categoria no ensena
// el mensaje de error sino que cambia la imagen por default
function controlarErroresImagen() {
    Array.from(document.querySelectorAll("img#imagenCategoriaCarta"))
            .filter((imagen) => !imagen.classList.contains("control"))
            .forEach((element) => {
                element.classList.add("control");
                element.addEventListener("error", () => {
                    element.src = `${element.src.substr(0, element.src.lastIndexOf('/') + 1)}default.jpg`;
                });
            });
}

// Manda al controlador ajax los filtros de la pagina
function mandarFiltro() {

    let request = new XMLHttpRequest();

    request.open('POST', url, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    request.onreadystatechange = (e) => {
        if (request.readyState === 4 && request.status === 200) {
            let respuesta = JSON.parse(e.currentTarget.responseText);
            limpiarProductos();

            // Recorre la respuesta y rellena la pagina con los productos filtrados
            respuesta.forEach((producto) => {
                containerProductos.innerHTML += `
<div class="col-lg-4 col-md-6 mb-3 mb-lg-0 card-product gap-2" id=\"${producto.idProducto}\" role="button" data-bs-toggle="modal" data-bs-target="#exampleModal">
    <div class="card rounded shadow-sm border-0">
        <div class="card-body p-4">
            <img src="${defaultURLProducto}/${producto.imagenProducto}.jpg" alt="${producto.nombreProducto}" class="img-fluid d-block mx-auto mb-3 img-producto">
            <div class="d-flex align-items-center justify-content-between w-100">
                <div class="d-flex align-items-start flex-column w-75 ">
                    <h5 class="fs-6">${producto.nombreProducto}</h5>
                    <h4 class="fw-bolder ">${formateoMoneda.format(producto.precio)}</h4>
                    <h5>${producto.marca}</h5>
                </div>
                <img src="${defaultURLCategoria}${producto.imagenCategoria}" alt="${producto.imagenCategoria}" id="imagenCategoriaCarta" class="img-fluid d-block mx-auto mb-3 h-100 w-25 p-3">
            </div>
        </div>
    </div>
</div>
`;
            });
            controlarErroresImagen();
            anadirEventosAProductos();
        }
    };

    // Manda los filtros al controlador ajax
    request.send(`accion=buscador&arreglo=${JSON.stringify([
        Array.from(document.querySelectorAll("input[name='categorias']")).filter(e => e.checked).map(seleccionados => seleccionados.value),
        Array.from(document.querySelectorAll("input[name='marcas']")).filter(e => e.checked).map(seleccionados => seleccionados.value),
        [buscador.value.replace(" ", "").length > 3 ? buscador.value : ""]
    ]) }`);
}

// Elimina todos los productos mostrados en pantalla
function limpiarProductos() {
    Array.from(containerProductos.querySelectorAll("div.card-product")).forEach(e => e.remove());
}

// Manda peticion al controlador de ajax con el id del producto y la accion
function manejoCarrito(id, accion) {
    let request = new XMLHttpRequest();

    request.open('POST', url, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    request.onreadystatechange = (e) => {
        if (request.readyState === 4 && request.status === 200) {
            let respuesta = JSON.parse(e.currentTarget.responseText);
            console.log(respuesta);

            if (accion !== "anadirACarrito" || modalBurbujaProducto.innerText === "0") {
                productoEnCarrito(id);
            }

            // Actualiza las burbujas del producto y del carrito
            modalBurbujaProducto.innerText = respuesta.cantidadProductoEnCarrito;
            burbujaCarrito.innerText = respuesta.productosEnCarrito;
        }
    };
    // Manda peticion al controlador de ajax con el id del producto y la accion
    request.send(`accion=carritoAjax&arreglo=${JSON.stringify([id, accion])}`);
}

// Manda peticion al controlador de ajax con el id del producto 
function productoEnCarrito(id) {
    let request = new XMLHttpRequest();

    request.open('POST', url, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    request.onreadystatechange = (e) => {
        if (request.readyState === 4 && request.status === 200) {
            let respuesta = JSON.parse(e.currentTarget.responseText);

            // Si la cantidad del producto en el carrito es 0 disabilita los botones de eliminar y actualiza la 
            // burbuja del producto
            modalBurbujaProducto.innerText = respuesta.cantidadProductoEnCarrito;
            botonEliminarProductoDeCarrito.disabled = respuesta.cantidadProductoEnCarrito == 0;
            botonEliminarUnidadDeCarrito.disabled = respuesta.cantidadProductoEnCarrito == 0;
        }
    };
    // Manda peticion al controlador de ajax con el id del producto 
    request.send(`accion=productoEnCarrito&arreglo=${JSON.stringify(id)}`);
}

// Elimina los eventListener de los botones del modal
function eliminarEventListeners() {

    let botonEliminarProductoDeCarritoClone = botonEliminarProductoDeCarrito.cloneNode(true);
    let botonEliminarUnidadDeCarritoClone = botonEliminarUnidadDeCarrito.cloneNode(true);
    let botonAnadirACarritoClone = botonAnadirACarrito.cloneNode(true);

    botonEliminarProductoDeCarrito.parentNode.replaceChild(botonEliminarProductoDeCarritoClone, botonEliminarProductoDeCarrito);
    botonEliminarUnidadDeCarrito.parentNode.replaceChild(botonEliminarUnidadDeCarritoClone, botonEliminarUnidadDeCarrito);
    botonAnadirACarrito.parentNode.replaceChild(botonAnadirACarritoClone, botonAnadirACarrito);

    botonEliminarProductoDeCarrito = botonEliminarProductoDeCarritoClone;
    botonEliminarUnidadDeCarrito = botonEliminarUnidadDeCarritoClone;
    botonAnadirACarrito = botonAnadirACarritoClone;
}

// Actualiza la burbuja de las categorias
function actualizarBurbujaCategorias() {
    let numFiltrosCategorias = Array.from(document.querySelectorAll("input[name='categorias']")).filter(e => e.checked).length;

    console.log(numFiltrosCategorias)
    if (!burbujaCategorias.classList.contains("d-none") && numFiltrosCategorias === 0) {
        burbujaCategorias.classList.add("d-none");
    } else if (burbujaCategorias.classList.contains("d-none") && numFiltrosCategorias > 0) {
        burbujaCategorias.classList.remove("d-none");
    }
    burbujaCategorias.querySelector("b").innerText = numFiltrosCategorias;
}

// Actualiza la burbuja de las marcas
function actualizarBurbujaMarcas() {
    let numFiltrosMarcas = Array.from(document.querySelectorAll("input[name='marcas']")).filter(e => e.checked).length;

    if (!burbujaMarcas.classList.contains("d-none") && numFiltrosMarcas === 0) {
        burbujaMarcas.classList.add("d-none");
    } else if (burbujaMarcas.classList.contains("d-none") && numFiltrosMarcas > 0) {
        burbujaMarcas.classList.remove("d-none");
    }
    burbujaMarcas.querySelector("b").innerText = numFiltrosMarcas;
}

function anadirEventosAProductos() {
    Array.from(containerProductos.querySelectorAll("div.card-product"))
            .forEach(e => {
                e.addEventListener("click", () => {
                    let request = new XMLHttpRequest();

                    request.open('POST', url, true);
                    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

                    request.onreadystatechange = (eResponse) => {
                        if (request.readyState === 4 && request.status === 200) {
                            let respuesta = JSON.parse(eResponse.currentTarget.responseText);

                            // muestra el modal de producto y no el de inicio de sesion
                            if (modalProducto.classList.contains("d-none"))
                                modalProducto.classList.remove("d-none");
                            if (!modalInicioSesion.classList.contains("d-none"))
                                modalInicioSesion.classList.add("d-none");

                            // Actualiza los datos del modal
                            modalProductoNombreProducto.innerText = respuesta.nombreProducto;
                            modalProductoNombreCategoria.innerText = respuesta.nombreCategoria;
                            modalProductoImagenProducto.src = `${defaultURLProducto}/${respuesta.imagenProducto}.jpg`;
                            modalProductoImagenProducto.alt = respuesta.nombreProducto;
                            modalProductoPrecio.innerText = formateoMoneda.format(respuesta.precio);
                            modalProductoDescripcion.innerText = respuesta.descripcion;
                            modalProductoMarca.innerText = respuesta.marca;
                            modalProductoImagenCategoria.src = `${defaultURLCategoria}${respuesta.imagenCategoria}`;
                            modalProductoImagenCategoria.alt = respuesta.nombreCategoria;

                            productoEnCarrito(e.id);
                            eliminarEventListeners();

                            // Anade los eventListener a los botones del modal
                            [
                                botonAnadirACarrito,
                                botonEliminarUnidadDeCarrito,
                                botonEliminarProductoDeCarrito
                            ].forEach((boton) => {
                                boton.addEventListener("click", () => {
                                    if (!boton.disabled) {
                                        manejoCarrito(e.id, boton.id);
                                    }
                                });
                            });
                        }
                    };

                    request.send(`accion=detallesProducto&arreglo=${JSON.stringify(e.id)}`);
                });
            });
}

// Manda el filtro cuando tiene mas de 3 letras el input de busqueda
buscador.addEventListener("keyup", (e) => {
    if (buscador.value.replace(" ", "").length > 3 || (e.key === "Backspace" && buscador.value.replace(" ", "").length <= 3 && containerProductos.querySelectorAll("div.card-product").length === 0)) {
        console.log("succ");
        mandarFiltro();
    }
});

// Cuando se hace click en un checkbox se envian los filtros
Array.from(document.querySelectorAll("input[type=checkbox]"))
        .forEach(e => {
            e.addEventListener("click", () => {
                mandarFiltro();
                if (e.name === "categorias")
                    actualizarBurbujaCategorias();
                else if (e.name === "marcas")
                    actualizarBurbujaMarcas();
            });
        });

// Anade evento para prevenir errores a la imagen de categoria del modal
modalProductoImagenCategoria.addEventListener("error", () => {
    modalProductoImagenCategoria.src = `${defaultURLCategoria}default.jpg`;
});

if (botonInicioSesion) {
    botonInicioSesion.addEventListener("click", () => {
        if (!modalProducto.classList.contains("d-none"))
            modalProducto.classList.add("d-none");
        if (modalInicioSesion.classList.contains("d-none"))
            modalInicioSesion.classList.remove("d-none");
    });
}

controlarErroresImagen();
anadirEventosAProductos();

// Comprueba los campos de los formularios de la pagina cuando se intenta hacer submit
(function () {
    'use strict';

    let forms = document.querySelectorAll('.needs-validation');

    Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }

                    form.classList.add('was-validated');
                }, false);
            });
})();