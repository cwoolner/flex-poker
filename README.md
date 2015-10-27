#Summary

Flex Poker is a poker-playing (Texas hold 'em) web app.  It was originally written with Flex on the front-end, hence the name.  It's been rewritten many times since  using various technologies.

#Motivation

It would be nice to actually make a fully usable product at some point, but so far the project has mostly served as a non-trivial project to learn new technologies and try out different design patterns.  That said, any contributions that move toward the goal of making something useful are most definitely welcomed.

#Brief tech/design history

The project started off using Flex on the front-end and Spring BlazeDS on the back-end for object serialization and pseudo-push functionality.  Regular Spring/Hibernate/MySQL webapp otherwise.  Project went dormant for a few years until the front-end was rewritten in Angular and BlazeDS was replaced with Spring WebSocket for true push functionality.  The back-end was later rewritten in an attempt to incorporate Domain-Driven Design, Command Query Responsibility Segregation, and Event Sourcing.  In-memory repositories replaced the use of a database and Hibernate with some optional pieces done in Redis.  Angular and other front-end frameworks/libraries were removed and replaced with raw Web Components and ES6.  A small front-end build using npm/bower/Grunt/Babel/Browserify was added.

#Persistence

To keep development simple, the use of a database has been removed in the default Spring profile.  The in-memory "database" (HashMaps and such) will be the main/first implementation, so no datastore will be required to run locally.  As time/interest allows, new implementations of the repositories will be added for various datastores.  When currently using the "prod" Spring profile, Redis is used and will be required to be running on startup.  Redis was chosen not because of fitness for a particular feature, but just for learning purposes.

The app generally allows each domain, and the command/query pieces within those domains, to use whatever persistence storage they like.  The command-side of one domain might store your data in memory while the query side of the same domain might be in-memory by default, but can be switched on app startup to use Redis instead.  As best as possible, the choice of persistence should not infect the rest of the application.

Since the app uses Event Sourcing, Greg Young's [Event Store](https://github.com/EventStore/EventStore) will be considered on the command side in the future.  On the query side, some sort of NoSQL document database will probably be considered.  Really happy with using in-memory structures and Redis for the time being.

#Security

Spring Security is being used, but only the user role at the moment.  Four players are hard-coded (loaded on app startup) in both the in-memory and Redis implementations.  Their logins are player1/player1, player2/player2, etc.

#Heroku

Thanks to Heroku for making it so simple (and free) to get a WebSocket-enabled Java app up and running on the public cloud.

A Procfile is included in the repository that will allow the project to be run after pushing the git repo up to Heroku.  In addition to the Procfile, a small chunk of jetty-runner config was added to the pom.xml file.  Nothing else in the app has been specialized for Heroku.

My currently deployed version of the app is: [http://flex-poker.herokuapp.com/](http://flex-poker.herokuapp.com/)

NOTE: Since the app uses in-memory persistence, the entire state of the app is essentially reset after Heroku puts it to sleep from inactivity.  Feel free to hit that URL and try it out.

#Current status

##What works

* Login
* Sign-up/registration (w/o email)
* Creating a new game
* Joining a game
* Call, check, fold
* Time-out and countdown timer for check/fold

##Not done

* Timed blind increment
* Extremely ugly
* No sounds
* Sign-up/registration (w/ email)
* Awarding chips/points for winning a game
* Shortcut buttons to bet the min/max/pot
* Logic to show more than just the winning hand
* Ability to muck winning/losing hands
* Winning hand determination, including split pots

#Technologies

##Required

* Java 8
* Maven
* node
* npm
* grunt

##Optional

* Redis - only required if using the "prod" Spring profile

##Front-end libraries/frameworks

* SockJS
* STOMP
* Web Components

##Back-end libraries/frameworks

* Spring - MVC/WebSocket/etc.
* Spring Data Redis
* Spring Security

#Running the app

* Default version - from the top-level directory: `mvn jetty:run`
* Prod version (Redis required) - from the top-level directory: `mvn jetty:run -Dspring.profiles.active=prod`
* Hit [http://localhost:8080/](http://localhost:8080/)

#Dev environment setup

Just been using Eclipse, so to generate the .classpath file, from the top-level directory: `mvn eclipse:eclipse`
