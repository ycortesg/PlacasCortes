/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


//TODO: arreglar session para que coja la suma suma al iniciar la aplicacion
const url = "Ajax";

let burbujaCarrito = document.querySelector("span#burbujaCarrito b");
let buscador = document.querySelector("#buscador");
let selectCategorias = document.querySelector("#categorias");
let selectMarcas = document.querySelector("#marcas");

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

let partesURL = modalProductoImagenProducto.src.split('/');
const defaultURLProducto = partesURL.slice(0, -2).join('/');
const defaultURLCategoria = modalProductoImagenCategoria.src.substr(0, modalProductoImagenCategoria.src.lastIndexOf('/') + 1);

const formateoMoneda = new Intl.NumberFormat('es-ES', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
    useGrouping: true,
    notation: 'standard',
    currencyDisplay: 'narrowSymbol'
});


function mandarFiltro() {

    let request = new XMLHttpRequest();

    request.open('POST', url, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    request.onreadystatechange = (e) => {
        if (request.readyState === 4 && request.status === 200) {
            let respuesta = JSON.parse(e.currentTarget.responseText);
            limpiarProductos();
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
                                    <img src="${defaultURLCategoria}${producto.imagenCategoria}" alt="${producto.imagenCategoria}" class="img-fluid d-block mx-auto mb-3 h-100 w-25 p-3">
                                </div>
                            </div>
                        </div>
                    </div>
`;
            });
            anadirEventosAProductos();
        }
    };

    request.send(`accion=buscador&arreglo=${JSON.stringify([
        Array.from(selectCategorias.selectedOptions).map(option => option.value),
        Array.from(selectMarcas.selectedOptions).map(option => option.value),
        [buscador.value.replace(" ", "").length > 3 ? buscador.value : ""]
    ]) }`);
}

function limpiarProductos() {
    Array.from(containerProductos.querySelectorAll("div.card-product")).forEach(e => e.remove());
}

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
            modalBurbujaProducto.innerText = respuesta.cantidadProductoEnCarrito;
            burbujaCarrito.innerText = respuesta.productosEnCarrito;
        }
    };
    request.send(`accion=carritoAjax&arreglo=${JSON.stringify([id, accion])}`);
}

function productoEnCarrito(id) {
    let request = new XMLHttpRequest();

    request.open('POST', url, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    request.onreadystatechange = (e) => {
        if (request.readyState === 4 && request.status === 200) {
            let respuesta = JSON.parse(e.currentTarget.responseText);
            console.log(respuesta);
            modalBurbujaProducto.innerText = respuesta.cantidadProductoEnCarrito;
            botonEliminarProductoDeCarrito.disabled = respuesta.cantidadProductoEnCarrito == 0;
            botonEliminarUnidadDeCarrito.disabled = respuesta.cantidadProductoEnCarrito == 0;
        }
    };
    request.send(`accion=productoEnCarrito&arreglo=${JSON.stringify(id)}`);
}

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
                            console.log(respuesta);
                            if (modalProducto.classList.contains("d-none"))
                                modalProducto.classList.remove("d-none");
                            if (!modalInicioSesion.classList.contains("d-none"))
                                modalInicioSesion.classList.add("d-none");

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

buscador.addEventListener("keyup", (e) => {
    if (buscador.value.replace(" ", "").length > 3 || (e.key === "Backspace" && buscador.value.replace(" ", "").length <= 3 && containerProductos.querySelectorAll("div.card-product").length === 0)) {
        console.log("succ");
        mandarFiltro();
    }
});

selectCategorias.addEventListener("click", () => {
    console.log(Array.from(selectCategorias.selectedOptions).map(option => option.value));
    mandarFiltro();
});

selectMarcas.addEventListener("click", () => {
    console.log(Array.from(selectCategorias.selectedOptions).map(option => option.value));
    mandarFiltro();
});

if (botonInicioSesion){
    botonInicioSesion.addEventListener("click", () => {
        if (!modalProducto.classList.contains("d-none"))
            modalProducto.classList.add("d-none");
        if (modalInicioSesion.classList.contains("d-none"))
            modalInicioSesion.classList.remove("d-none");
    });
}

anadirEventosAProductos();