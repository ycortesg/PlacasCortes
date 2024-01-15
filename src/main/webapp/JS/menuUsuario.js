// Declaramos variables
let codigosPostal;
const rutaJSONProvincias = document.querySelector("script#main").getAttribute("data-json-route");

let inputCodigoPostal = document.querySelector("input#codigoPostal");
let inputProvincia = document.querySelector("input#provincia");

let selectFechasPedidos = document.querySelector("select#fechasPedidos");
let containerListaPedidos = document.querySelector("div#listaPedidos");
let tableBodyPedido = document.querySelector("#tablaPedido tbody");

const defaultURLProducto = document.querySelector("script#main").getAttribute("data-productos-route"); 
let regexcodigoPostal = /^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$/;

const url = "Ajax";

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

// Cargamos las provincias del JSON
async function cargarProvincias() {
    let response = await fetch(rutaJSONProvincias);
    codigosPostal = await response.json();
}

// Comprueba si el codigo postal es valido y si es asi actualiza la provincia
function checkCodigoPostalInput() {
    if (regexcodigoPostal.test(inputCodigoPostal.value)) {
        inputProvincia.value = codigosPostal[inputCodigoPostal.value.substr(0, 2)];
    } else {
        inputProvincia.value = "";
    }
}

// Anade los eventos a las cartas de pedidos
function anadirEventosACartas(){
    Array.from(containerListaPedidos.querySelectorAll("div.card"))
            .forEach((e)=>{
                e.addEventListener("click",()=> {
                    limpiarModal();
                    cargarDetallesPedido(e.id, 
                    parseFloat(e.getAttribute("data-importe")), 
                    parseFloat(e.getAttribute("data-iva")));
                });
    });     
}

// Anade un producto a los datos del pedido con los datos introducidos
function anadirProductoPedido(nombre, precio, cantidad, imagen) {
    tableBodyPedido.innerHTML += `
<tr>
    <td><img src="${defaultURLProducto}${imagen}.jpg" height="50" width="50" alt="${nombre}"></td>
    <td>${nombre}</td>
    <td>${formateoMoneda.format(precio)}</td>
    <td class="text-center">${cantidad}</td>
</tr>
`;
}

// Elimina las cartas de pedidos
function eliminarCartas(){
    containerListaPedidos.replaceChildren();
}

// Elimina los datos del pedido del modal
function limpiarModal(){
    tableBodyPedido.replaceChildren();
}

// Introduce las ultimas 3 lineas del pedido con los datos introducidos
function anadirInfoFinalAPedido(precioSinIVA, diferenciaIVA, precioConIVA) {
    tableBodyPedido.innerHTML += `
<tr>
    <th scope="row" colspan="3">Total sin IVA</th>
    <td id="totalSinIVA">${formateoMoneda.format(precioSinIVA)}</td>
</tr>
<tr>
    <th scope="row" colspan="3">IVA</th>
    <td id="IVA">${formateoMoneda.format(diferenciaIVA)}</td>
</tr>
<tr>
    <th scope="row" colspan="3">Total con IVA</th>
    <td id="totalConIVA">${formateoMoneda.format(precioConIVA)}</td>
</tr>
`;
}

// Recoge los detalles de un pedido del ajax por su id
function cargarDetallesPedido(idPedido, importe, iva){
    let request = new XMLHttpRequest();

    request.open('POST', url, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
    request.onreadystatechange = (e) => {
        if (request.readyState === 4 && request.status === 200) {
            let respuesta = JSON.parse(e.currentTarget.responseText);
            console.log(respuesta);
            // Anade la informacio introducida mas el importe y el iva al modal
            respuesta.listadoProductosPedido
                    .forEach(element=>anadirProductoPedido(element.nombre, element.precio, element.cantidad, element.imagen));
            
            anadirInfoFinalAPedido(importe, iva, importe+iva);
        }
    };
    request.send(`accion=detallesPedidoFinalizado&arreglo=${JSON.stringify(idPedido)}`);
}

// Carga los pedidos de una fecha introducida
function cargarPedidosPorFecha(fecha){
    let request = new XMLHttpRequest();

    request.open('POST', url, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    request.onreadystatechange = (e) => {
        if (request.readyState === 4 && request.status === 200) {
            let respuesta = JSON.parse(e.currentTarget.responseText);
            console.log(respuesta);
            respuesta.listadoPedido.forEach((pedido) => {
                
            containerListaPedidos.innerHTML += `
<div class="card w-100" id="${pedido.idPedido}" data-importe="${pedido.importe}" data-iva="${pedido.iva}" role="button" data-bs-toggle="modal" data-bs-target="#pedidoModal">
    <div class="card-body">
        <h5 class="card-title">${fecha}</h5>
            <div class="d-flex justify-content-between align-items-center">
                <p class="card-text">${formateoMoneda.format(pedido.importe)}</p>
                <p class="card-text">${formateoMoneda.format(pedido.iva)}</p>
            </div>
    </div>
</div>
`;
                anadirEventosACartas();
            });
        }
    };
    request.send(`accion=pedidosPorFecha&arreglo=${JSON.stringify(fecha)}`);
}

cargarProvincias().then(() => {
    inputCodigoPostal.addEventListener("keyup", () => {
        checkCodigoPostalInput();
    });
});

if (selectFechasPedidos){
    selectFechasPedidos.addEventListener("change", ()=>{
        eliminarCartas();
        cargarPedidosPorFecha(selectFechasPedidos.value);
    });
}

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
                    } else if (event.submitter.classList.contains("no-confirmado")) {
                        event.preventDefault();
                        event.stopPropagation();
                        event.submitter.innerText = "Confirmar";
                        let query = `input[type="${event.submitter.value === "actualizarPassword" ? "password" : "text"}"]`

                        Array.from(event.submitter.closest("form")
                                .querySelectorAll(query))
                                .forEach((element) => element.readOnly = true);


                        event.submitter.classList.remove("no-confirmado");
                        event.submitter.classList.remove("col-md-12");
                        event.submitter.classList.add("col-md-4");

                        let botonCancelar = document.createElement("button");
                        botonCancelar.type = "button";
                        botonCancelar.innerText = "Cancelar";
                        botonCancelar.classList.add("btn", "btn-primary", "col-md-4");

                        botonCancelar.addEventListener("click", () => {
                            botonCancelar.remove();
                            event.submitter.classList.add("no-confirmado");
                            event.submitter.classList.add("col-md-12");
                            event.submitter.classList.remove("col-md-4");
                            event.submitter.innerText = "Actualizar";
                            Array.from(event.submitter.closest("form")
                                    .querySelectorAll(query))
                                    .forEach((element) => element.readOnly = false);
                        });

                        event.submitter.closest("div#containerBotones").appendChild(botonCancelar);

                    }

                    form.classList.add('was-validated');
                }, false);
            });
})();