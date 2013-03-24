# Summary

# Current status

## What works

* Creating a new game.
* Call, check, fold.
* Winning hand determination, including split pots.
* Time-out and countdown timer for check/fold.

## Not done

* Timed blind increment (in progress).
* Extremely ugly.
* No sounds.
* Sign-up/registration.
* Awarding chips/points for winning a game.
* Shortcut buttons to bet the min/max/pot.
* Logic to show more than just the winning hand.
* Ability to muck winning/losing hands.

# Dev environment setup

The following will list the technologies that have been used up until this point, along with how to use each.  Different technologies/tools can obviously be used, so feel free to contribute setup instructions for your environment.

Major technologies:

* Java 6
* Maven 2
* Tomcat 6
* MySQL 5
* Flex 3.4

Development tools:

* Gentoo Linux
* Eclipse
* Emacs

## Eclipse

Eclipse can be used for both the Flex and Java modules in the project.  [Adobe's Flex Builder](http://www.adobe.com/products/flex/features/flex_builder/) is a much better (for making things look good, anyway), non-free alternative for the Flex side, but until this point, the Flex code has been done purely using Eclipse and Emacs.

To generate the .classpath file, from the top-level directory: `mvn eclipse:eclipse`