# Microservices - Group 6 

The results of the microservice project of group 6 can be found in different repositories.
The repositories contain microservices which are used to build a ticket system. With this system you are
able to create projects and this projects can contain issues. Issues and projects can be managed.\
In the current orga repository is the overall docker organisation and the documentation.

All the other necessary repositories can be found [here](https://git.thm.de/microservicesss21).


## Getting started
Create a directory and clone every [repository](https://git.thm.de/microservicesss21).
The easiest way to start all services is to use docker compose.
Use the following commands to do so:
 ```
 cd orga
 gradlew buildAll
 docker compose build
 docker compose up
 ```
All services should start. You can test the services by using their api. The apis are documented [here](https://git.thm.de/microservicesss21/orga/-/tree/master/doc/apis).

## Architecture
![architecture](https://git.thm.de/microservicesss21/orga/-/blob/master/doc/diagrams/Architecture_Diagram.pdf "Architecure of the ticket system")
![events](https://git.thm.de/microservicesss21/orga/-/blob/master/doc/diagrams/Event_Diagram.pdf "Event management of the ticket system")

