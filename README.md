# Summary

Flex Poker is a poker-playing (Texas hold 'em) web app.  It was originally written with Flex on the front-end, hence the name.  It's been rewritten many times since using various technologies.

# Running the app

### w/ Docker
1. NOTE: Requires docker >= 17.05 due to using a multi-stage build.  This runs the default dev version, which just uses an in-memory database
1. `docker build -t flex-poker:1.0-SNAPSHOT .`
1. `docker run -d -p 8080:8080 flex-poker:1.0-SNAPSHOT`
1. Hit [http://localhost:8080/](http://localhost:8080/)
1. Users created by default: player1/player1, player2/player2, player3/player3, player4/player4

### w/o Docker
1. `npm install`
1. `npm run prod`
1. Run the server:
   * Default version (JDK 21 required): `mvn spring-boot:run`
   * Redis version (Redis and JDK 21 required): `mvn -Dspring-boot.run.profiles=redis spring-boot:run`
1. Hit [http://localhost:8080/](http://localhost:8080/)
1. Users created by default: player1/player1, player2/player2, player3/player3, player4/player4

# Technologies

## Build/dev tools
* Node.js/npm
* Java 21
* Maven
* Redis - only required if using the `redis` Spring profile
* Docker (>= 17.05) - if you want to use the provided Dockerfile to build/run

## Front-end libraries/frameworks
* SockJS
* STOMP
* React
* Redux
* React Router
* React-Bootstrap

## Back-end libraries/frameworks
* Spring - MVC/WebSocket/etc.
* Spring Data Redis
* Spring Security

# Testing

The basic unit tests, repository tests (redis using [testcontainers](https://testcontainers.com) and in-memory), and arch tests run as part of a normal Maven lifecycle. `mvn test` should just work after a fresh clone.  As mentioned, the integration tests will spin up docker containers for redis, but no other external systems are hit.

The test classes are tagged, so running a specific subset is possible using the `-Dgroups` and `-DexcludedGroups` user properties:

* Run just the fast unit tests: `mvn test -Dgroups=unit`
* Run all except for the redis tests: `mvn test -DexcludedGroups=repository-redis`

In addition to the tests above, the [PIT](https://pitest.org) plugin has been included for mutation testing.  To run: `mvn test-compile org.pitest:pitest-maven:mutationCoverage`

# Motivation

It would be nice to actually make a fully usable product at some point, but so far the project has mostly served as a non-trivial project to learn new technologies and try out different design patterns.  That said, any contributions that move toward the goal of making something useful are most definitely welcomed.

# Brief tech/design history

### Back-end
* 2009 - BlazeDS, Spring, Hibernate, MySQL
* 2013 - BlazeDS replaced with Spring WebSocket
* 2014 - Rewrote to incorporate DDD, CQRS, and ES
* 2020 - Converted to Kotlin

### Front-end
* 2009 - Flex
* 2013 - Angular, WebSocket
* 2015 - ES6, WebComponents
* 2016 - React
* 2023 - TypeScript

# Persistence

To keep development simple, the use of a database has been removed in the default Spring profile.  The in-memory "database" (HashMaps and such) will be the main/first implementation, so no datastore will be required to run locally.  As time/interest allows, new implementations of the repositories will be added for various datastores.  When currently using the `redis` Spring profile, Redis is used and will be required to be running on startup.  Redis was chosen not because of fitness for a particular feature, but just for learning purposes.

The app generally allows each domain, and the command/query pieces within those domains, to use whatever persistence storage they like.  The command-side of one domain might store your data in memory while the query side of the same domain might be in-memory by default, but can be switched on app startup to use Redis instead.  As best as possible, the choice of persistence should not infect the rest of the application.

Since the app uses Event Sourcing, Greg Young's [Event Store](https://github.com/EventStore/EventStore) will be considered on the command side in the future.  On the query side, some sort of NoSQL document database will probably be considered.  Really happy with using in-memory structures and Redis for the time being.

# Spring profiles

The two main profiles are `default` (when you don't specify one) and `redis`.  In addition, there are more specific profiles that allow you to mix and match different datastores.  For example, if you run this extremely long list of profiles: `mvn -Dspring-boot.run.profiles=login-inmemory,signup-redis,game-command-redis,game-query-redis,table-command-inmemory,table-query-redis spring-boot:run`, you'll use the in-memory db for login and the command side of the table domain, but redis for everything else.

This is overly complicated, and it's only being used to show that these different domains and sub-domains can be persisted in a truly independent manner.  It keeps details (how to store your data) away from the real logic of the application.  The query side of the domains can be made even more granular, but that seems a bit extreme at this point.

# Security

Spring Security is being used, but only the user role at the moment.  Four players are hard-coded (loaded on app startup) in both the in-memory and Redis implementations.  Their logins are player1/player1, player2/player2, etc.

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
