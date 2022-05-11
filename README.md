# Dojo-Currencies

Dojo-Currencies is a simple application connecting to [NBP-API](http://api.nbp.pl/) for currencies exchange info.
Application allows us:
* Retrieving actual available currencies names with their currency code
* Retrieving actual exchange rates in context of Polish currency (PLN)
* Converting currencies based on exchange rates (at this moment only from PLN to available currency and vice versa)

## Requirements
To set up all the environments, it is required to have [Docker](https://www.docker.com/) installed.

## Installation
1. Clone repository.
2. To generate .jar file open terminal in project root path example: `desktop/dojo-currencies`:
    1. with installed [Maven](https://maven.apache.org/) on your local machine
   ```
    mvn clean package
   ```
   2. with pre-installed MavenWrapper in [IntelliJ IDEA](https://www.jetbrains.com/idea/)
   ```
    ./mvnw clean package 
   ```
3. In project root path type in terminal sequentially:
```
docker build -t dojo-currencies:latest -f docker/Dockerfile .
docker-compose -f docker/docker-compose.yml up
```
4. To close all containers:
```
docker-compose -f docker/docker-compose.yml down
```

## Additional information
* Look in `docker-compose.yml` file to get all needed database connection info.
* API documentation is generated with [Swagger](https://swagger.io/). Link: 
```
http://localhost:8080/swagger-ui/index.html
```