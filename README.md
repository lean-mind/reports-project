# README #

Reports project is a sample project developed with Outside-in TDD, 
writing end2end tests from the very beginning. Backend is in Java with SpringBoot and frontend in JavaScript with React and Redux.

### Setup ###

- build the project:
  - `mvn clean install`

- Docker compose:

  - move to `src/main/docker`
  - run `docker-compose up --build`, it will start a postgresql container with a table *user* and a demo user. Also, it will start a rabbitmq service.
  - Verify that postgresql and rabbitmq are running: `docker ps`
  - Verify that the environment variables in **Dockerfile** are the same in your **application.properties** file 
      - spring.datasource.url= jdbc:postgresql://localhost:5432/reports
      - spring.datasource.username=postgres
      - spring.datasource.password=mysecretpassword
  
  ### Troubleshooting
  
  - Sometimes, there are zombie containers, run to remove them:
  
      `docker rm $(docker ps -a -q)`
      
### Production ###

#### Requirements ####
- Maven 3.5.0
- Java 8
- PostgreSQL 9 (Local or Remote)
- Docker (optional)

#### Steps (Without Docker) ####
- clone the repository
- run `mvn clean install`
- set your postgresql configuration. It can be locally or a remote DB. Set JDBC_DATABASE_URL environment variable with your configuration
> Note: remember run the src/main/docker/postgres/init.sql schema
- Example of a JDBC_DATABASE_URL: jdbc:postgresql://host:5432/batabasename?user=user&password=password

- run `java -jar -Dspring.profiles.active=prod target/*.jar`, the app will run on port 8080 and ftp port 8021

#### Steps (With Docker) ####

TO DO. Create a docker image to run the project. 