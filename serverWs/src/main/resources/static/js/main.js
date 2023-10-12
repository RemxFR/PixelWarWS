'use strict';

var usernameForm = document.querySelector("#usernameForm");
var drawingForm = document.querySelector("#drawingForm");
var userList = document.querySelector("#utilisateursListe");
var canvas = document.querySelector("#canvas");
var btnInscription = document.querySelector("#btn-inscription");
var userNameField = document.querySelector("#name");
var btnInscriptionTexte = "S'Inscrire";
var btnDesnscriptionTexte = "Se Désinscrire";
var stompClient = null;
var username = null;
var coorY = null;
var coorX = null;
var col = null;
var isIn = false;
var userLeft = false;


var ctx = canvas.getContext('2d');
canvas.height = 700;
canvas.width = 1000;
var canvasWidth = canvas.width;
var canvasHeight = canvas.height;
ctx.clearRect(0, 0, canvasWidth, canvasHeight);
var id = ctx.getImageData(0, 0, canvasWidth, canvasHeight);
var pixels = id.data;
let x = 0;
let y = 0;

function connect(event) {
    username = document.querySelector('#name').value.trim();
    if (username) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnect, onError);
        drawingForm.classList.add('drawingForm-visible');
        userNameField.classList.add('namefield-visible');
    }
    event.preventDefault();
}

function onConnect(payload) {
    stompClient.subscribe('/pixel/public', onMessageReceived);
    if (!isIn) {
        stompClient.send('/app/pixelWar.addUser', {}, JSON.stringify({
            pixel: 0,
            sender: username,
            messageType: 'REJOINDRE'
        }));
        isIn = true;
        btnInscription.textContent = btnDesnscriptionTexte;
        stompClient.send('/app/pixelWar.getPixels', {}, JSON.stringify({messageType: 'AFFICHER_CANVAS'}));
    } else {
        stompClient.send('/app/pixelWar.addUser', {}, JSON.stringify({
            pixel: 0,
            sender: username,
            messageType: 'QUITTER'
        }));
        isIn = false;
        btnInscription.textContent = btnInscriptionTexte;
        userNameField.classList.add('namefield-hidden');

    }
}

function onError() {
    window.alert('Could not connect to websocket server. Refresh and try again.');
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var userElement = document.createElement('li');
    if (message.messageType === "REJOINDRE") {
        userElement.appendChild(document.createTextNode(message.sender + ' a rejoint la pw !'));
        userLeft = false;
    } else if (message.messageType === "QUITTER" && !userLeft) {
        userLeft = true;
        userElement.appendChild(document.createTextNode(message.sender + ' a quitté la pw !'));
    } else if (message.messageType === "DESSINER" && !userLeft) {
            userElement.appendChild(document.createTextNode(message.sender + ' a dessiné !'));
        drawInCanvas(message.coordoX, message.coordoY, message.color);
    } else {
        let pixels = message;
        for (let i = 0; i < pixels.length; i++) {
            drawInCanvas(pixels[i].coordoX, pixels[i].coordoY, pixels[i].color);
        }
    }
    userList.appendChild(userElement);
}

function drawPixel(event) {
    coorX = document.querySelector("#coorXinput").value;
    coorY = document.querySelector("#coorYinput").value;
    col = document.querySelector("#couleur").value;
    if (coorY < canvasWidth && coorX < canvasHeight) {
        var pixelToDraw = {coordoX: coorX, coordoY: coorY, color: col, sender: username, messageType: 'DESSINER'}
        stompClient.send('/app/pixelWar.drawPixel', {}, JSON.stringify(pixelToDraw));
        drawInCanvas(coorX, coorY, col);
    } else {
        window.alert("Coordonnées X entre 0 et 700, coordonnées Y entre 0 et 1000");
    }
    event.preventDefault();
}

function drawInCanvas(coorX, coorY, col) {
    ctx.fillStyle = col;
    ctx.fillRect(coorX, coorY, 1, 1);
}

usernameForm.addEventListener('submit', connect, true);
drawingForm.addEventListener('submit', drawPixel, true);