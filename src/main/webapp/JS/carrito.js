const url = "Ajax";
        
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
            elementoCantidadProducto.innerText = respuesta.cantidadProductoEnCarrito;
        }
    };
    request.send(`accion=carritoAjax&arreglo=${JSON.stringify([id, accion])}`);
}

document.querySelectorAll("section")
        .forEach((e) => {
            e.querySelectorAll("button")
                    .forEach((boton) => {
                        boton.addEventListener("click", () =>{
//                            console.log(e.id, boton.id)
                            manejoCarrito(e.id, boton.id, e.querySelector("div#cantidadProducto b"), e);
                        });
                    });
        });