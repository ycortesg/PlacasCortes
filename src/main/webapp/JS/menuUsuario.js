let codigosPostal;
const rutaJSONProvincias = document.querySelector("script#main").getAttribute("data-json-route");

let inputNombre = document.querySelector("input#nombre");
let inputCodigoPostal = document.querySelector("input#codigoPostal");
let inputProvincia = document.querySelector("input#provincia");
let botonEnvioDatosPersonales = document.querySelector("button[value=\"editarDatosPersonales\"]");

let botonModalPassword = document.querySelector("button#botonModalPassword");
let inputPasswordAntigua = document.querySelector("#passwordAntigua");
let inputPasswordNueva = document.querySelector("#passwordNueva");
let inputPasswordNueva2 = document.querySelector("#passwordNueva2");
let listInputsPassword = [inputPasswordAntigua, inputPasswordNueva, inputPasswordNueva2];
let botonEnvioPassword = document.querySelector("button[value='editarPassword']");

let regexcodigoPostal = /^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$/;

async function cargarProvincias() {
    let response = await fetch(rutaJSONProvincias);
    codigosPostal = await response.json();
}

function checkCodigoPostalInput() {
    botonEnvioDatosPersonales.disabled = !regexcodigoPostal.test(inputCodigoPostal.value);
    if (regexcodigoPostal.test(inputCodigoPostal.value)) {
        inputProvincia.value = codigosPostal[inputCodigoPostal.value.substr(0, 2)];
    } else {
        inputProvincia.value = "";
    }
}

function passwordFormCorrecto(){
    return (
            (inputPasswordAntigua.value !== inputPasswordNueva.value) && 
            (inputPasswordNueva.value === inputPasswordNueva2.value) && 
            (listInputsPassword.every(e=>e.value.replace(" ", "") !== "")));
}

cargarProvincias().then(() => {
    inputCodigoPostal.addEventListener("keyup", () => {
        checkCodigoPostalInput();
    });
});

inputPasswordAntigua.addEventListener("keyup", () => {
    if (passwordFormCorrecto()){
        botonEnvioPassword.disabled = false;
    }else{
        botonEnvioPassword.disabled = true;
    }
});

inputPasswordNueva.addEventListener("keyup", () => {
    if (passwordFormCorrecto()){
        botonEnvioPassword.disabled = false;
    }else{
        botonEnvioPassword.disabled = true;
    }
});

inputPasswordNueva2.addEventListener("keyup", () => {
        console.log(passwordFormCorrecto());
    if (passwordFormCorrecto()){
        botonEnvioPassword.disabled = false;
    }else{
        botonEnvioPassword.disabled = true;
    }
});

botonModalPassword.addEventListener("click", () => {
    listInputsPassword.forEach(e=>e.readOnly = false);
    botonEnvioPassword.type = "button";
    botonEnvioPassword.innerText = "Actualizar";
});

botonEnvioPassword.addEventListener("click", () => {
    if (botonEnvioPassword.type === "button" && !botonEnvioPassword.disabled){
        listInputsPassword.forEach(e=>e.readOnly = true);
        setTimeout(() => {
            botonEnvioPassword.type = "submit";
            botonEnvioPassword.innerText = "Confirmar";
        }, 100);
    }
});