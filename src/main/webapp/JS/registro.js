// Declaramos variables
let inputNIF = document.querySelector("#nif");
let letraNIF = document.querySelector("input#letraNIF");
let inputCodigoPostal = document.querySelector("#codigoPostal");
let inputProvincia = document.querySelector("#provincia");
let inputEmail = document.querySelector("#email");
let inputPassword = document.querySelector("#password");
let inputPassword2 = document.querySelector("#password2");
let botonEnvio = document.querySelector("#btnRegistro");

const url = "Ajax";
const rutaJSONProvincias = document.querySelector("script#main").getAttribute("data-json-route");

let listaCamposValidos = [false, false, false];

let letrasDNI = ['T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'];

let codigosPostal;

let regex = {
    nif: /^[0-9]{8}$/,
    codigoPostal: /^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$/
};

// Carga el JSON de las provincias
async function cargarProvincias() {
    let response = await fetch(rutaJSONProvincias);
    codigosPostal = await response.json();
}

// Comprueba si el nif es valido, de ser asi introduce su numero al input de letranif
function checkNIFInput() {
    listaCamposValidos[0] = regex.nif.test(inputNIF.value);
    if (regex.nif.test(inputNIF.value)) {
        letraNIF.value = letrasDNI[parseInt(inputNIF.value) % 23];
    } else {
        letraNIF.value = "";
    }
}

// Comprueba si el codigo postal es valido y si es asi actualiza la provincia
function checkCodigoPostalInput() {
    listaCamposValidos[1] = regex.codigoPostal.test(inputCodigoPostal.value);
    if (regex.codigoPostal.test(inputCodigoPostal.value)) {
        console.log(inputCodigoPostal.value);
        inputProvincia.value = codigosPostal[inputCodigoPostal.value.substr(0, 2)];
    } else {
        inputProvincia.value = "";
    }
}

// Combrueba si el correo electronico esta en uso
function checkEmailInput(valido) {
    if (valido) {
        let request = new XMLHttpRequest();

        request.open('POST', url, true);
        request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        request.onreadystatechange = (e) => {
            if (request.readyState === 4 && request.status === 200) {
                let respuesta = JSON.parse(e.currentTarget.responseText);
                listaCamposValidos[2] = respuesta.correoValido;
                console.log(respuesta);
                if (document.querySelector(".needs-validation").classList.contains("was-validated")){
                    if (respuesta.correoValido){
                        if (!inputEmail.classList.contains("is-valid"))inputEmail.classList.add("is-valid");
                        if (inputEmail.classList.contains("is-invalid"))inputEmail.classList.remove("is-invalid");
                    }else{
                        if (inputEmail.classList.contains("is-valid"))inputEmail.classList.remove("is-valid");
                        if (!inputEmail.classList.contains("is-invalid"))inputEmail.classList.add("is-invalid");
                    }
                }
            }
        };
        request.send(`accion=correoExistente&arreglo=${JSON.stringify(inputEmail.value)}`);
    } else {
        listaCamposValidos[2] = false;
    }
}

// Comprueba si todos los campos son validos
function checkInputsValidos() {
    return (listaCamposValidos.every(e => e) && Array.from(document.querySelectorAll("input")).every(e => e.value !== ""));
}

cargarProvincias().then(() => {

    inputNIF.addEventListener("keyup", () => {
        checkNIFInput();
    });

    inputCodigoPostal.addEventListener("keyup", () => {
        checkCodigoPostalInput();
    });

    inputEmail.addEventListener("blur", () => {
        checkEmailInput(inputEmail.checkValidity());
                
    });

    checkNIFInput();
    checkCodigoPostalInput();
    checkEmailInput();

    // Comprueba los campos de los formularios de la pagina cuando se intenta hacer submit
    (function () {
        "use strict";

        let forms = document.querySelectorAll(".needs-validation");

        Array.prototype.slice.call(forms).forEach(function (form) {
            form.addEventListener(
                    "submit",
                    function (event) {
                        if (event.submitter.value !== "volver") {
                            console.log(!form.checkValidity() || !checkInputsValidos());
                            if (!form.checkValidity() || !checkInputsValidos()) {
                                event.preventDefault();
                                event.stopPropagation();
                            }
                            form.classList.add("was-validated");
                        }

                    },
                    false
                    );
        });
    })();

});
