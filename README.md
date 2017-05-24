# Summary

Flex Poker is a poker-playing (Texas hold 'em) web app.  It was originally written with Flex on the front-end, hence the name.  It's been rewritten many times since using various technologies.

# Motivation

It would be nice to actually make a fully usable product at some point, but so far the project has mostly served as a non-trivial project to learn new technologies and try out different design patterns.  That said, any contributions that move toward the goal of making something useful are most definitely welcomed.

# Brief tech/design history

### Back-end
* 2009 - BlazeDS, Spring, Hibernate, MySQL
* 2013 - BlazeDS replaced with Spring WebSocket
* 2014 - Rewrote to incorporate DDD, CQRS, and ES

### Front-end
* 2009 - Flex
* 2013 - Angular, WebSocket
* 2015 - ES6, WebComponents
* 2016 - React

# Persistence

To keep development simple, the use of a database has been removed in the default Spring profile.  The in-memory "database" (HashMaps and such) will be the main/first implementation, so no datastore will be required to run locally.  As time/interest allows, new implementations of the repositories will be added for various datastores.  When currently using the "prod" Spring profile, Redis is used and will be required to be running on startup.  Redis was chosen not because of fitness for a particular feature, but just for learning purposes.

The app generally allows each domain, and the command/query pieces within those domains, to use whatever persistence storage they like.  The command-side of one domain might store your data in memory while the query side of the same domain might be in-memory by default, but can be switched on app startup to use Redis instead.  As best as possible, the choice of persistence should not infect the rest of the application.

Since the app uses Event Sourcing, Greg Young's [Event Store](https://github.com/EventStore/EventStore) will be considered on the command side in the future.  On the query side, some sort of NoSQL document database will probably be considered.  Really happy with using in-memory structures and Redis for the time being.

# Security

Spring Security is being used, but only the user role at the moment.  Four players are hard-coded (loaded on app startup) in both the in-memory and Redis implementations.  Their logins are player1/player1, player2/player2, etc.

# Heroku

Thanks to Heroku for making it so simple (and free) to get a WebSocket-enabled Java app up and running on the public cloud.

Node.js: The Heroku config had to be changed to add a heroku/nodejs buildpack as well as the existing heroku/java buildpack.  The pack by default just runs npm install, so the webpack build has been added to the postinstall script.

Java: A Procfile is included that contains the jetty-runner command that Heroku uses to launch the app.  In addition to the Procfile, a small chunk of jetty-runner config was added to the pom.

Heroku auto-deploys whenever changes are made to master: [http://flex-poker.herokuapp.com/](http://flex-poker.herokuapp.com/)

NOTE: Since the app uses in-memory persistence, the entire state of the app is essentially reset after Heroku puts it to sleep from inactivity.  Feel free to hit that URL and try it out.

# Current status

## What works

* Login
* Sign-up/registration (w/o email)
* Creating a new game
* Joining a game
* Call, check, fold
* Time-out and countdown timer for check/fold
* Timed blind increment

## Not done

* Extremely ugly
* No sounds
* Sign-up/registration (w/ email)
* Awarding chips/points for winning a game
* Shortcut buttons to bet the min/max/pot
* Logic to show more than just the winning hand
* Ability to muck winning/losing hands
* Winning hand determination, including split pots

# Technologies

## Required

* Node.js/(npm|yarn)
* Java 8
* Maven

## Optional

* Redis - only required if using the "prod" Spring profile

## Front-end libraries/frameworks

* SockJS
* STOMP
* Web Components

## Back-end libraries/frameworks

* Spring - MVC/WebSocket/etc.
* Spring Data Redis
* Spring Security

# Running the app

1. Run the front-end build: `npm install` or `yarn`
2. Run the server:
   * Default version: `mvn jetty:run`
   * Prod version (Redis required): `mvn jetty:run -Dspring.profiles.active=prod`
3. Hit [http://localhost:8080/](http://localhost:8080/)

# Dev environment setup

* Just been using Eclipse, so to generate the .classpath file, from the top-level directory: `mvn eclipse:eclipse`
* While developing, running `npm start` or `yarn start` launches webpack in watch mode

# Testing

The basic unit tests run as part of a normal Maven lifecycle.  No external systems are hit.

Been recently playing around with mutation testing.  The PIT plugin has been included.  To run (after building): `mvn org.pitest:pitest-maven:mutationCoverage`
