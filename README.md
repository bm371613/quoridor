[![Build Status](https://secure.travis-ci.org/bm371613/quoridor.png?branch=master)](http://travis-ci.org/bm371613/quoridor)

Quoridor
===

This is an implementation of the [Quoridor strategy game](https://en.wikipedia.org/wiki/Quoridor).

The code is divided into three subprojects:

 - `quoridor-core` - game state and game rules
 - `quoridor-ai` - bots
 - `quoridor-gui` - desktop application
 - `quoridor-analysis` - CLI tool for running matches between bots

Quickstart
===

Run

    ./gradlew quoridor-gui:fatJar

to compile the desktop application. An executable JAR should appear in `quoridor-gui/build/distributions/`.


Development
===

To run checks (tests, linters), use

    ./gradlew check
