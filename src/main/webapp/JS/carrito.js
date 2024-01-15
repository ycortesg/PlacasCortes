// Declaramos varibles
const url = "Ajax";
let botonFacturaModal = document.querySelector("button#botonFacturaModal");
let tablaFactura = document.querySelector("table#tablaFactura tbody");
let botonFinalizarCompra = document.querySelector("button#finalizarCompra");
let inputImporte = document.querySelector("input#importe");
let inputIVA = document.querySelector("input#iva");

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
// Manda peticion al controlador de ajax con el id del producto y la accion
function manejoCarrito(id, accion, elementoCantidadProducto, carta) {
    let request = new XMLHttpRequest();

    request.open('POST', url, true);
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    request.onreadystatechange = (e) => {
        if (request.readyState === 4 && request.status === 200) {
            let respuesta = JSON.parse(e.currentTarget.responseText);
            console.log(respuesta);
            if (accion === "eliminarDeCarritoEntero" || respuesta.cantidadProductoEnCarrito === 0) {
                carta.remove();
            }
            if (respuesta.productosEnCarrito === 0) {
                botonFacturaModal.removeAttribute("data-bs-target");
                botonFacturaModal.disabled = true;
            }
            elementoCantidadProducto.innerText = respuesta.cantidadProductoEnCarrito;
        }
    };
    // Manda peticion al controlador de ajax con el id del producto y la accion
    request.send(`accion=carritoAjax&arreglo=${JSON.stringify([id, accion])}`);
}

// Limpia la tabla de la factura de la informacion anterior
function limpiarTabla() {
    tablaFactura.replaceChildren();
}

// Anade un producto a la factura con los datos introducidos
function anadirProductoFactura(nombre, precio, cantidad, indx) {
    tablaFactura.innerHTML += `
<tr>
    <th scope="row">${indx + 1}</th>
    <td>${nombre}</td>
    <td>${formateoMoneda.format(precio)}</td>
    <td>${cantidad}</td>
    <td>${formateoMoneda.format(precio * cantidad)}</td>
</tr>
`;
}

// Introduce las ultimas 3 lineas de la factura con los datos introducidos
function anadirInfoDeFactura(precioSinIVA, diferenciaIVA, precioConIVA) {
    inputImporte.value = precioSinIVA;
    inputIVA.value = diferenciaIVA;
    tablaFactura.innerHTML += `
<tr>
    <th scope="row" colspan="4">Total sin IVA</th>
    <td id="totalSinIVA">${formateoMoneda.format(precioSinIVA)}</td>
</tr>
<tr>
    <th scope="row" colspan="4">IVA</th>
    <td id="IVA">${formateoMoneda.format(diferenciaIVA)}</td>
</tr>
<tr>
    <th scope="row" colspan="4">Total con IVA</th>
    <td id="totalConIVA">${formateoMoneda.format(precioConIVA)}</td>
</tr>
`;
}

// Anadimos las acciones a los botones de los productos
document.querySelectorAll("section")
        .forEach((e) => {
            e.querySelectorAll("button")
                    .forEach((boton) => {
                        boton.addEventListener("click", () => {
                            manejoCarrito(e.id, boton.id, e.querySelector("div#cantidadProducto b"), e);
                        });
                    });
        });
        
if (botonFacturaModal){
    // Recoge la informacion de la factura
    botonFacturaModal.addEventListener("click", () => {
        if (!botonFacturaModal.disabled) {
            let request = new XMLHttpRequest();

            request.open('POST', url, true);
            request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

            request.onreadystatechange = (e) => {
                if (request.readyState === 4 && request.status === 200) {
                    let respuesta = JSON.parse(e.currentTarget.responseText);

                    botonFinalizarCompra.type = "button";
                    botonFinalizarCompra.innerText = "Finalizar compra";
                    limpiarTabla();
                    respuesta.listadoCarrito.forEach((e, indx) => {
                        anadirProductoFactura(e.nombre, e.precio, e.cantidad, indx);
                    });
                    anadirInfoDeFactura(respuesta.precioTotalSinIVA, respuesta.diferenciaDeIVA, respuesta.precioTotalConIVA);
                }
            };
            request.send(`accion=carritoFactura`);
        }
    });
}


botonFinalizarCompra.addEventListener("click", () => {
    botonFinalizarCompra.innerText = "Confirmar";

    setTimeout(() => botonFinalizarCompra.type = "submit", 100);
});