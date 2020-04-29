# geoblocker
Simple proof of concept for geo blocking.

## Usage
All countries are blocked by default, to allow a country to access the index-page go to /admin
and add the country code (ex. NO or UK).

## Online example
To test an online example go to https://geobloker.neksa.no

## Prerequisites
You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running
To start a web server for the application, run:

    lein ring server

## Build for production
To build a runnable jar-file:

    lein ring uberjar
