# CoffeeServer


## Build
- go to root of project
- on Linux/Mac: 
    ```bash
    ./gradle build
    ```
- On Windows:
    ```dos
    gradlew.bat build
    ```
## Start
- Built Project in `./build/libs/CoffeeServer2-1.0-SNAPSHOT.jar`
    ```bash
    cd ./build/libs/
    ```
- Start project:
    ```bash
   java ./CoffeeServer2-1.0-SNAPSHOT.jar -jar
    ```
- Now Project run on port `25565`
## Usable 
>[!Warning]
> not usable since 2.0.0
- Now exist only three main command, all of them work with JSON:
    - GET
      - `/getFood` - get menu of all food
      - `/getOrders` - get current order queue
    - POST
      - `/addOrder` - add new order in queue
- Example, with linux like client: 
  - `$ curl -X GET http://localhost:25565/getFood | jq`
  - `$ curl -X GET http://localhost:25565/getOrders | jq`
  - `$ curl -X POST http://localhost:25565/addOrder -H "Content-Type: application/json" -d '{"id":[1,2]}'`

# TODO
- [X] Remake [Requests.java](src/main/java/controller/Requests.java), must be more divided 
- [ ] Make Admin and seller command _(with special key)_, for change food menu and orders queue
- [ ] Replace [StubData.java](src/main/java/database/repository/StubData.java) with full database _(maybe SQL, idk yet)_
- [ ] Create Tests _(JUnit?)_
- [ ] 
