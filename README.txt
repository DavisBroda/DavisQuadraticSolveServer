This is an app that returns real number solutions of quadratic equations, rounded to three decimal places of precision.

This app can be run from the Quadratic App class in an IDE or by building the app, and running the jar (see below).

When run this app will start a server on localhost:8080. To use this app, sue the quad endpoint, and have
the quadratic equation to be solved placed in the "str" parameter of the query (URL encoded). If needed,
a URL encoding site can be found here: https://www.urlencoder.org/. Response will be in text/plain(UTF-8) format.

Example:

equation to solve: x^2 - 3x + 2 = 0
URL encoded string: x%5E2%20-%203x%20%2B%202%20%3D%200

request: http://localhost:8080/quad?str=x%5E2%20-%203x%20%2B%202%20%3D%200
response: Quadratic had solutions x = 2.0, x = 1.0

Building and running from command line

To build run: maven package

To run do: <project root>/bin/run.sh

This should start the server, and you can then start submitting requests as normal



