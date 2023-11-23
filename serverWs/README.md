# PIXELWAR YNOV LYON 2023 Client & Serveur

## Fonctionnement
Pixel war maison, on s'inscrit et ensuite on dessine sur un canvas.

## Déploiement

### Avec docker
Pour déployer le projet dans un conteneur docker, il vous suffit d'ouvrir un terminal et de vous mettre dans le dossier PixelWarWS du projet.
Lancez ensuite la commande : docker compose -f docker-compose.dev.yml up --build 

Le projet se lancera automatiquement dans le conteneur et vous pourrez y accéder ensuite via l'url :

→ http://localhost:8081/

### Sans docker
Le projet est compris dans un seul et même dossier et est à déployer sur un seul est même serveur, Tomcat Apache.
Le prochain est développé en Java via la JDK 1.8.
Il peut être lancé directement depuis un terminal ou un IDE.
Pour la base de données, elle se crée automatiquement. C'est une BDD Mysql, 
le j-connector est déjà compris dans l'application, il n'y a qu'à renseigner le nom de la bdd, votre nom d'utilisateur 
et votre mot de passe dans le fichier application.properties.

## Utilisation

Pour jouer à la pixel war, rentrez simplement un nom d'utilisateur, puis des valeurs dans les inputs des coordonnées et faites votre plus beau dessin pixel par pixel !