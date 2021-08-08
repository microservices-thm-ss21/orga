# Microservices - Group 6 

The results of the microservice project of group 6 can be found in different repositories.
The repositories contain microservices which are used to build a ticket system. With this system you are
able to create projects and this projects can contain issues. Issues and projects can be managed.\
In the current orga repository is the overall docker orchestration and the documentation.

All the other necessary repositories can be found [here](https://git.thm.de/microservicesss21).

## Getting started

The microservices use the programming languages Kotlin and Scala,
as well as gradle, maven and sbt to build and manage the dependencies.
Please make sure you have the required jvm-version installed.

Create a directory and clone this repository.
Use `./gradlew cloneAll` from inside the `orga`-directory to clone every [repository](https://git.thm.de/microservicesss21) automatically.
The easiest way to start all services is to use docker compose.
Use the following commands to do so:
 ```
 cd orga
 gradlew buildAll
 docker compose build
 docker compose up
 ```
All services should start.

The system is a closed community. Therefore, you need to log in as one of our default users:
1. Admin-Role: `Peter_Zwegat` with the password `password`.
2. Support-Role: `Kim-Jong-Dos` with the password `password`.
3. User-Role: `Kim-Jong-On` with the password `password`.

You may create new users and delete existing ones. Please make sure to change the passwords of the users before going in production.

## Documentation

You may find the [documentation](https://git.thm.de/microservicesss21/orga/-/blob/master/doc/) of all services within this repository.
This includes an [architecture](https://git.thm.de/microservicesss21/orga/-/blob/master/doc/diagrams/Architecture_Diagram.pdf) 
and a [MessageMQ Communication](https://git.thm.de/microservicesss21/orga/-/blob/master/doc/diagrams/Event_Diagram.pdf) overview about the microservices internal communication.

You can test the services by using their api. The apis are documented [here](https://git.thm.de/microservicesss21/orga/-/tree/master/doc/apis).

## Monitoring

The monitoring is realized by collecting statistics into an InfluxDB-Database in combination with a graphical display via Grafana.
By starting the services the Monitoring is also started.
1. You may access Grafana via `http://localhost:3000` and log in with the default credentials `admin / admin`. 
Please change the password when asked.\
2. You need to add the datasource to the influxDB. You may do so via the Gear-Wheel on the left bar -> Datasources.
You need to add a datasource for every of the 5 services and name the datasource and the `datasource`-field, using exactly the same string:\
`issueService`, `userService`, `gateway`, `projectService`, `newsService`\
The other variables should be filled with this information:
   - URL: `http://influx:8086`
   - No Auth boxes ticked
   - User: `admin`
   - Password: `PASSWORD` 
   - HTTP-Method: `GET`

   > Note: The InfluxDB is not exposed to the internet

3. You may import the dashboard when logged in via the Plus-sign in the left bar -> Import.
Please upload the Grafana-Dashboard json file [Grafana-Services-Overview.json](https://git.thm.de/microservicesss21/orga/-/blob/master/doc/Grafana-Services-Overview.json).
Choose a random datasource on the selection.
4. [View the Dashboard](http://localhost:3000/d/microservices_overview/microservices-overview?orgId=1) and scroll down to view every service.


## Stresstest

A Stresstest is included within the repository [stresstest-gatling](https://git.thm.de/microservicesss21/gatling-service).
This is a stand-alone service written in Scala and uses the framework Gatling.
It is required to have [Scala 2](https://docs.scala-lang.org/getting-started/index.html) installed.

To start the Stresstest please execute `sbt` from within the repository-directory.
Once sbt started, execute `Gatling/test` to start the test.
It will print a result of the stresstest on the commandline.

You may execute different scenarios by exchanging the scenario in the `setUp` method.
For different amounts of users please modify the injected number of users.
You may chain multiple scenarios or execute them simultaneous.


## Saga
