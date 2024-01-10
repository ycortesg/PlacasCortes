const url = "Ajax";
let botonFacturaModal = document.querySelector("button#botonFacturaModal");
let tablaFactura = document.querySelector("table#tablaFactura tbody");
let botonFinalizarCompra = document.querySelector("button#finalizarCompra");
let inputImporte = document.querySelector("input#importe");
let inputIVA = document.querySelector("input#iva");

const formateoMoneda = new Intl.NumberFormat('es-ES', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
    useGrouping: true,
    notation: 'standard',
    currencyDisplay: 'narrowSymbol'
});

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
    request.send(`accion=carritoAjax&arreglo=${JSON.stringify([id, accion])}`);
}

function limpiarTabla() {
    tablaFactura.replaceChildren();
}

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

document.querySelectorAll("section")
        .forEach((e) => {
            e.querySelectorAll("button")
                    .forEach((boton) => {
                        boton.addEventListener("click", () => {
                            manejoCarrito(e.id, boton.id, e.querySelector("div#cantidadProducto b"), e);
                        });
                    });
        });

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

botonFinalizarCompra.addEventListener("click", () => {
    botonFinalizarCompra.innerText = "Confirmar";

    setTimeout(() => botonFinalizarCompra.type = "submit", 100);
});