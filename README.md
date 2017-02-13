UIDRA
User Interactions' Data Retriever for Android
## Auteur
- [Nathan Bacquet](https://github.com/Apolloch)

## Table of Contents
- **[Introduction](#introduction)**   
- **[Travail Technique](#travail-technique)**   
- **[Evaluation](#evaluation)**
- **[Limitations](#limitation)**  

## Introduction

Le développement d'un logiciel passe par quatre étapes : spécification des besoins , conception , réalisation et test.Les applications mobiles ne font pas exception.Il est possible de tester les fonctionnalités d'une application via un framework de de test type Junit , de tester l’adéquation de l'interface graphique via d'autres frameworks spécifiques , mais pour tester la pertinence d'une interface graphique pour l'utilisateur lambda , il n'y a pas d'autre choix que de lui mettre entre les mains.Pour cela on recrute des utilisateurs le temps d'un test , et on doit , soit mobiliser du personnel pour noter chacune de ses actions ,ce qui est coûteux , soit se fier au retour de l'utilisateur après coup (avec un formulaire par exemple), ce qui est d'une grande imprécision. C'est pour palier à ce problème qu'est né UIDRA.

UIDRA est une librairie développé pour Android , permettant de récupérer ces si coûteuses informations , et de les envoyer sur un serveur distant.Ainsi il est possible de centraliser un grand nombre de données de façons précises et à un coût réduit .De plus grâce à la programmation par aspect ,elle permet de ne pas surcharger le code l'application avec de nombreuses ligne de code sans intérêt en terme de fonctionnalité.

UIDRA à été testé sur un projet open source ,[owncloud](https://github.com/owncloud/android) et a permis de récupérer efficacement 3 cas d'utilisation et le nombre de clics correspondants dans une base de donnée distante.

## Travail technique

### Overview

UIDRA permet de récupérer des données d'utilisation d'une application Android , et de les envoyer à un serveur pour être traiter ultérieurement.
Par le biais d'un système d'actions,délimitées au préalable dans le code , les interactions de l'utilisateur sur l'interface graphique sont enregistrées , avec le contexte de l'application correspondant .Sont ainsi récupérés le nombre de clicks par actions et les enchainements d'actions, .Une fois la séquence d'actions terminée , elle est envoyé sur l'URL indiqué.


### Architecture
Uidra utilise la programmation par aspect pour attraper les clicks à l’exécution.Pour cela il utilise le plugin gradle [com.uphyca.android-aspectj](https://github.com/uPhyca/gradle-android-aspectj-plugin "com.uphyca.android-aspectj") qui permet d'utiliser aspectJ sous Android.Cet outil est composé d'un ensemble de sources à inclure dans votre projet.


###Setup
Pour utiliser cet outil :

-  Inclure les sources dans le projet.

-  rajouter le plugin suivant dans build.gradle du module app : `apply plugin: 'com.uphyca.android-aspectj'`.
 
### Use
Cette outil fonctionne à base de délimitation d'actions grâce aux annotations @ActionBegin,@ActionEnd:
ex : 

    @ActionBegin(name="auth")
    protected void onCreate(Bundle icicle) {
    }
    
    @ActionEnd(name="auth")
        public void finish() {

pour que uidra puisse capter les clicks , vous devez surcharger la méthode dispatchTouchEvent dans votre activity :

    
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


## Evaluation
Uidra a été testé sur l'application owncloud et à permis de récolter des données d'utilisations de 3 cas : clique d'une image , authentification ,supprimer user.
Voici pour exemple le code nécessaire pour l'authentification.

    	@ActionBegin(name="auth")
        protected void onCreate(Bundle icicle) {
            super.onCreate(icicle);
		    ...
    }
    @ActionEnd(name="auth")
        public void finish() {
            if (mAccountAuthenticatorResponse != null) {
            ...
    }
    
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

et le résultat envoyé :

    {
	    "success":true,
	    "sub_action":[],
	    "clicks":4,
	    "name":"auth"
    }

## Limitation
L'utilisation de cet outil comporte l'obligation de surcharger des méthode sans raison apparente , ce qui peut être un soucis en terme de lisibilité.
Les données récoltées se limitants au nombre de clicks , les comportements que l'on peut en déduire sont somme toute assez limités. 


