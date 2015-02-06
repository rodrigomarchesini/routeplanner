# routeplanner

Author: Rodrigo Marchesini

## Problem and solution

### Problem
The problem consists in finding the shortest path in weighted graphs. Maps have to be persisted and interfaces to create maps and find shortest path must be exposed as webservices.

## Solution

Implemented a Java Web application to manage maps and find paths storing data in a graph database (Neo4J - embedded).

#### Three main components:
* Core: responsible for dealing with graph database
* Processor: responsible for dealing with map data, including map reading, parsing, validation, asynchronous creation in database and path retrieval.
* Web: webservices to deal with maps and paths

#### Main technologies:
* Neo4j: graph database that offers good support and API for dealing with graphs. Provides easy way to set properties in edges/vertexes and to use them in queries/operations, besides out-of-the box algorithms to deal with graphs. In this particular case Dijkstra was used to get the shortest weighted path between two map points.
* Spring: was chosen because offers good support and integration with Neo4j through Spring Data.
* RestEasy: JAX-RS implementation used to build the rest webservices. These two simple webservices could be implemented in pure Java Servlet, but JAX-RS/RestEasy was chosen to speed up development, make it easier to deal with request/response parsing and exception handling.
* Jackson: robust and well-known JSON parsing and generation library. There were few POJOs to map, with simple properties. No advanced configuration was necessary.

#### Implementation strategies:
* PoC with Spring/Neo4J to do the basic functions: create points with routes and get the shortest path.
* Created a core component to deal directly with Neo4j, extending the PoC. Testing with JUnit.
* Mechanism to create maps asynchronously. Maps are read from input (webservice), parsed, validated and then saved in temporary files. These files are processed asynchronously: routes are loaded to memory and the map is created/replaced in small transactions. First two transactions remove previous map data (if any): removes routes (relationships) and then points (nodes); next, one transaction creates all points; and finally, routes are created in transaction groups of 5k. Mechanism tested with JUnit/Mockito.
* Web component to provide map/path access through webservices.
* Performance testing: found issues. Application took 169s to create a map with 50k routes. Application crashed with a map composed by 10k points and 500k routes. Refactor!
* Refactoring the way maps were created due performance issues. Left some Spring Data stuff behind and created helping in-memory structures to keep saved nodes and reduce the number of lookups in database. Discarded usage of unique index in database to speed up transactions. This way, point uniqueness in a map is guaranteed by the application (in-memory helpers). Reduced the number of "DTOs" between layers. After that, the time spent to create a map with 50k routes dropped to 3s. And fortunately, application could now handle a 10k/500k map, taking 30s to create it.
* Finally, some improvements in naming, logging, error handling, validation, etc.

### Prerequisistes
* JDK 1.7+
* Maven 3+

### Running

In routeplanner directory:
```
mvn clean install
cd routeplanner-web
mvn jetty:run -DskipTests=true
```

## Usage

### Create map

Request:
```
PUT http://localhost:8080/routeplanner/map/{map-name}
```

Response:
* HTTP 200 ok
* HTTP 400 for invalid requests (detail in response JSON)

Example:
```
curl -v http://localhost:8080/routeplanner/map/simplemap -XPUT --data-binary "A B 10
B D 15
A C 20
C D 30
B E 50
D E 30"
{"detail":"OK: Map will be imported."}
```

### Find path and cost

Request:
```
GET http://localhost:8080/routeplanner/map/{map-name}?origin={origin-point}&destination={destination-point}&autonomy={autonomy}&fuelCost={fuelCost}"
```

Reponse:
* HTTP 200 - JSON with path/cost data
  * pathExists: boolean indicating if there's a path from origin to destination
  * length: sum of weights from origin to destination
  * cost: calculated based on length, autonomy and fuelCost
  * points: array containing path's points
* HTTP 400 for invalid requests (detail in response JSON)
* HTTP 404 for map/point not found

Example:
```
curl -v "http://localhost:8080/routeplanner/map/simplemap?origin=A&destination=D&autonomy=10&fuelCost=2.5"
{"length":25.0,"cost":6.25,"pathExists":true,"points":["A","B","D"]}
```

## Limitations and suggested improvements
* Concurrency control while creating a map (separated transactions to replace a map vs. path retrieval).
* Application recovery if crashes/stops while creating a map.
