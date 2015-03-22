Summary
======

Flex Poker is a web app originally written with Flex on the front-end, hence the name Flex Poker.  Since Flex is no longer the future, the project has been revived using Angular on the front-end and sticking with Java on the back-end.  In addition to the change in front-end, the back-end has been upgraded to use Spring 4 and WebSocket.  Even more recently, the back-end has gone through a large redesign in an attempt to incorporate DDD, CQRS, and ES.

Persistence
===========

To keep development simple, the use of a database has been removed in the default Spring profile.  The in-memory "database" (HashMaps and such) will be the main/first implementation, so no datastore will be required to run locally.  As time/interest allows, new implementations of the repositories will be added for various datastores.  When currently using the "prod" profile, Redis is used and will be required to be running on startup.

Since the app uses Event Sourcing, Greg Young's [Event Store](https://github.com/EventStore/EventStore) will be considered on the Command side in the future.  On the Query side, some sort of NoSQL document database will probably be looked at.  Really happy with using in-memory structures and Redis for the time being.

The goal for the foreseeable future is to get the main features working, make it look better, and then start adding in more persistence.  No reason to get hung-up on persistence when everything can be done in memory.

Security
========

Spring Security is being used, but only the user role is being used, so no admin role at the moment.  Four players are hard-coded (loaded on app startup) in both the in-memory and Redis implementations.  Their logins are player1/player1, player2/player2, etc.

Current status
==============

What works
----------

* Login
* Sign-up/registration (w/o email)
* Creating a new game
* Joining a game
* Call, check, fold
* Time-out and countdown timer for check/fold

Not done
--------

* Timed blind increment
* Extremely ugly
* No sounds
* Sign-up/registration (w/ email)
* Awarding chips/points for winning a game
* Shortcut buttons to bet the min/max/pot
* Logic to show more than just the winning hand
* Ability to muck winning/losing hands
* Winning hand determination, including split pots

Technologies
============

Required
--------

* Java 8
* Maven
* Redis - only required if using the "prod" Spring profile

Front-end libraries/frameworks
------------------------------

* AngularJS
* jQuery
* ng-grid from the AngularUI project
* SockJS
* jQuery UI
* STOMP.js - (https://github.com/jmesnil/stomp-websocket/)

Back-end libraries/frameworks
-----------------------------

* Spring 4.1 - MVC/WebSocket/etc.
* Spring Data Redis
* Spring Security 3.2
* A couple of basic pages (non-Angular) use basic JSP/JSTL

Running the app
===============

* Default version - from the top-level directory: `mvn jetty:run`
* Prod version (Redis required) - from the top-level directory:`mvn jetty:run -Dspring.profiles.active=prod`
* Hit (http://localhost:8080/)

Dev environment setup
=====================

Just been using Eclipse, so to generate the .classpath file, from the top-level directory: `mvn eclipse:eclipse`
