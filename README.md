# LevelOne

Le jeu est un platformer, voici les contrôles :
Q : Aller à gauche
D : Aller à droite
Z : Interagir avec le monde (PNJ, coffre, ...)
Shift : Courir
E : Ouvrir l'inventaire
Clic gauche : Taper
Clic droit : Utiliser l'objet

## Lancer le jeu
Pour le lancer le jeu, utiliser au minimum Java 17 et JavaFX 17 en spécifiant le chemin vers JavaFX dans la commande
```
java --module-path {CHEMIN VERS JAVAFX} --add-modules javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics,javafx.media,javafx.web -jar LevelOne.jar
```

Le jeu vient avec JavaFX 17 pour Linux, pour le lancer avec cette version :
```
java --module-path JavaFX17 --add-modules javafx.controls,javafx.fxml,javafx.swing,javafx.base,javafx.graphics,javafx.media,javafx.web -jar LevelOne.jar
```

## Les items
Le jeu vient avec deux items :
- Apple : Elle vous redonne 1 PV a chaque utilisation, sans couldown. Attention, si vous en consommez avec la vie pleine, vous perdrez une pomme pour rien
- Ring of Life : Disponible dans le coffre, augmente votre vie maximal par 1.4 (+ 4 P. Attention, en la déplacent de votre hotbar vers votre inventaire et vis versa, vous perdez l'effet le temps du déplacement, vous faisant perdre de la vie si vous étiez au dessus de la vie max de base.

## Les monstres
Ils peuvent vous attaquer, vous suivre si vous êtes assez proche, et vous font des dégâts passivement si vous leur rentrez dedans
- Ghost : Ne vous attaque pas, mais vous suis en traversant le monde
- Zombie : Vous attaque si vous êtes a proximité, moins rapide que vous, mais possèdent beaucoup de vie.

## Victoires et defaites
Par défaut, la défaite se fait à la mort du joueur (il perd de la vie s'il tombe dans le vide). La victoire se fait en atteignant l'extrémité droite de la carte.
