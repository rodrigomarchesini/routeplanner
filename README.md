# routeplanner

Author: Rodrigo Marchesini

## Installation and running

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
