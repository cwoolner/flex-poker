Summary
======

Web app originally written with Flex on the front-end, hence the name Flex Poker.  Since Flex is no longer the future, the project has been revived using Angular on the front-end and sticking with Java on the back-end.  In addition to the change in front-end, the back-end has been upgraded to use Spring 4 and WebSocket.  To keep development simple, the use of a database has been removed, so Spring Security logins are just hard-coded for the time being.  A small amount of Redis is being used, but not in the main part of the application as of yet, so no reason to install that if you do not want.

The goal for the foreseeable future is to get the main features working, make it look better, and then start adding in persistence.  No reason to get hung-up on persistence when everything can be done in memory.

Current status
==============

What works
----------

* Creating a new game
* Joining a game
* Call, check, fold
* Winning hand determination, including split pots
* Time-out and countdown timer for check/fold

Not done
--------

* Timed blind increment
* Extremely ugly
* No sounds
* Sign-up/registration
* Awarding chips/points for winning a game
* Shortcut buttons to bet the min/max/pot
* Logic to show more than just the winning hand
* Ability to muck winning/losing hands

Technologies
============

Required
--------

* Java 7 or higher (current: 1.7.0_25)
* Maven (current: 3.0.4 - probably not using any features that require Maven 3)

Front-end libraries/frameworks
------------------------------

* AngularJS
* jQuery
* ng-grid from the AngularUI project
* SockJS
* jQuery UI
* Lo-dash
* AngularStomp - (https://github.com/gtarcea/AngularStomp/)
* STOMP.js - (https://github.com/jmesnil/stomp-websocket/)

Back-end libraries/frameworks
-----------------------------

* Spring 4 - MVC/WebSocket/etc.
* Spring Data Redis
* Spring Security 3.2
* Redis - only required for sign-up functionality
* A couple of basic pages (non-Angular) use basic JSP/JSTL

Running the app
===============

* From the top-level directory: `mvn clean jetty:run`
* Hit (http://localhost:8080/flexpoker)

Dev environment setup
=====================

Just been using Eclipse, so to generate the .classpath file, from the top-level directory: `mvn eclipse:eclipse`