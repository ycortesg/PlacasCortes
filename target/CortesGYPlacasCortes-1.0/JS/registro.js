
let inputNIF = document.querySelector("#nif");
let letraNIF = document.querySelector("input#letraNIF");
let inputCodigoPostal = document.querySelector("#codigoPostal");
let inputProvincia = document.querySelector("#provincia");
let inputEmail = document.querySelector("#email");
let inputPassword = document.querySelector("#password");
let inputPassword2 = document.querySelector("#password2");
let botonEnvio = document.querySelector("#btnRegistro");

const url = "Ajax";
let codigosPostal =
        {
            "01": "Álava",
            "02": "Albacete",
            "03": "Alicante",
            "04": "Almería",
            "05": "Ávila",
            "06": "Badajoz",
            "07": "Islas Baleares1",
            "08": "Barcelona",
            "09": "Burgos",
            "10": "Cáceres",
            "11": "Cádiz",
            "12": "Castellón",
            "13": "Ciudad Real",
            "14": "Córdoba",
            "15": "La Coruña",
            "16": "Cuenca",
            "17": "Gerona",
            "18": "Granada",
            "19": "Guadalajara",
            "20": "Guipúzcoa",
            "21": "Huelva",
            "22": "Huesca",
            "23": "Jaén",
            "24": "León",
            "25": "Lérida",
            "26": "La Rioja2",
            "27": "Lugo",
            "28": "Madrid",
            "29": "Málaga",
            "30": "Murcia",
            "31": "Navarra",
            "32": "Orense",
            "33": "Asturias3",
            "34": "Palencia",
            "35": "Las Palmas5",
            "36": "Pontevedra",
            "37": "Salamanca",
            "38": "Santa Cruz de Tenerife",
            "39": "Cantabria4",
            "40": "Segovia",
            "41": "Sevilla",
            "42": "Soria",
            "43": "Tarragona",
            "44": "Teruel",
            "45": "Toledo",
            "46": "Valencia",
            "47": "Valladolid",
            "48": "Vizcaya",
            "49": "Zamora",
            "50": "Zaragoza",
            "51": "Ceuta",
            "52": "Melilla"
        };

let listaCamposValidos = [false, false, false, false];

let letrasDNI = ['T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'];

let regex = {
    nif: /^[0-9]{8}$/,
    codigoPostal: /^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$/,
    email: /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/
};

function checkNIFInput() {
    listaCamposValidos[0] = regex.nif.test(inputNIF.value);
    if (regex.nif.test(inputNIF.value)) {
        letraNIF.value = letrasDNI[parseInt(inputNIF.value) % 23];
    } else {
        letraNIF.value = "";
    }
}

function checkCodigoPostalInput() {
    listaCamposValidos[1] = regex.codigoPostal.test(inputCodigoPostal.value);
    if (regex.codigoPostal.test(inputCodigoPostal.value)) {
        console.log(inputCodigoPostal.value);
        inputProvincia.value = codigosPostal[inputCodigoPostal.value.substr(0, 2)];
    } else {
        inputProvincia.value = "";
    }
}

function checkEmailInput() {
    if (regex.email.test(inputEmail.value)) {
        let request = new XMLHttpRequest();

        request.open('POST', url, true);
        request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        request.onreadystatechange = (e) => {
            if (request.readyState === 4 && request.status === 200) {
                let respuesta = JSON.parse(e.currentTarget.responseText);
                listaCamposValidos[2] = respuesta.correoValido;
                checkInputsValidos();
                console.log(respuesta);
            }
        };
        request.send(`accion=correoExistente&arreglo=${JSON.stringify(inputEmail.value)}`);
    } else {
        listaCamposValidos[2] = false;
    }
}

function checkPasswordInput() {
    listaCamposValidos[3] = inputPassword.value === inputPassword2.value;
}

function checkInputsValidos() {
    botonEnvio.disabled = !(listaCamposValidos.every(e => e) && Array.from(document.querySelectorAll("input")).every(e => e.value !== ""));
}

inputNIF.addEventListener("keyup", () => {
    checkNIFInput();
    checkInputsValidos();
});

inputCodigoPostal.addEventListener("keyup", () => {
    checkCodigoPostalInput();
    checkInputsValidos();
});

inputEmail.addEventListener("keyup", () => {
    console.log(inputEmail.value);
    checkEmailInput();
    checkInputsValidos();
});

inputPassword.addEventListener("keyup", () => {
    checkPasswordInput();
    checkInputsValidos();
});

inputPassword2.addEventListener("keyup", () => {
    checkPasswordInput();
    checkInputsValidos();
});

Array.from(document.querySelectorAll("input"))
        .forEach((e) => {
            e.addEventListener("keyup", () => {
                checkInputsValidos();
            });
        });

checkNIFInput();
checkCodigoPostalInput();
checkEmailInput();
checkPasswordInput();
checkInputsValidos();

