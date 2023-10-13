'use strict';

/*
Déclaration des tags de la partie html possédant un id
 */
var usernameForm = document.querySelector("#usernameForm");
var drawingForm = document.querySelector("#drawingForm");
var canvas = document.querySelector("#canvas");
var btnInscription = document.querySelector("#btn-inscription");
var btnInscriptionTexte = "S'Inscrire";

/*
Déclaration des variables qui vont être utilisées pour la connexion ws.
 */
var stompClient = null;
var username = null;
var coorY = null;
var coorX = null;
var col = null;
var socket = null;

/*
Déclaraton des variables pour l'implémentation du canvas
 */
var ctx = canvas.getContext('2d');
canvas.height = 700;
canvas.width = 1000;
var canvasWidth = canvas.width;
var canvasHeight = canvas.height;
ctx.clearRect(0, 0, canvasWidth, canvasHeight);
let x = 0;
let y = 0;

//Méthode de connection à la webSocket.
function connect(event) {
    username = document.querySelector('#name').value.trim();
    if (username) {
        socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnect);
    }
    event.preventDefault();
}

//Ce qu'il se passe au moment du clic sur le bouton de connexion/déconnexion.
function onConnect(payload) {
    stompClient.subscribe('/pixel/public', onMessageReceived);
        stompClient.send('/app/pixelWar.addUser', {}, JSON.stringify({
            sender: username,
            messageType: 'REJOINDRE'
        }));
}

//La connexion de la ws se ferme lorsque la page web est quittée.
window.addEventListener("unload", function () {
    if (socket.readyState === WebSocket.OPEN) {
        socket.close();
    }
})

//Gestion des erreurs par l'affichage d'une alert.
function onError() {
    window.alert('Could not connect to websocket server. Refresh and try again.');
}

//Différentes implémentations de ce qu'il se passe au niveau du front lorsque le serveur envoie un message.
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var userElement = document.createElement('li');

    //On affiche le formulaire de dessin si le nom de l'utilisateur est disponible, sinon on affiche une alerte.
    if (message.pixelDrawing !== undefined) {
        if (message.pixelDrawing.messageType === "REJOINDRE") {
                userElement.appendChild(document.createTextNode(message.pixelDrawing.sender + ' a rejoint la pw !'));
                drawingForm.classList.add('drawingForm-visible');
                usernameForm.classList.add('namefield-hidden');
                stompClient.send('/app/pixelWar.getPixels', {}, JSON.stringify({messageType: 'AFFICHER_CANVAS'}));
        }

        //On dessine sur le canvas tous les pixels en temps réel.
    } else if (message.messageType === "DESSINER") {
        console.log('DESSINE')
        drawInCanvas(message.coordoX, message.coordoY, message.color);

        //On se déconnecte de la ws et on cache le formulaire de dessin.
    } else if (message.messageType === "QUITTER") {
        btnInscription.textContent = btnInscriptionTexte;
        drawingForm.classList.add('.drawingForm-hidden');
        socket.close();

        //On dessine sur le canvas tous les pixels déjà enregistrés en bdd.
    } else {
        let pixels = message;
        for (let i = 0; i < pixels.length; i++) {
            drawInCanvas(pixels[i].coordoX, pixels[i].coordoY, pixels[i].color);
        }
    }
}

/*Méthode pour récupérer les valeurs du formulaire de dessin et envoyer le message à la partie serveur que l'on dessine.
Affichage d'une alerte si les valeurs des coordonnées du pixel sont fausses.
 */
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

//Méthode de dessin/de remplissage du canvas.
function drawInCanvas(coorX, coorY, col) {
    ctx.fillStyle = col;
    ctx.fillRect(coorX, coorY, 1, 1);
}

//Association des méthodes avec les formulaires correspondants.
drawingForm.addEventListener('submit', drawPixel, true);
usernameForm.addEventListener('submit', connect, true);