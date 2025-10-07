# Application - Petstore Migrated to Spring Boot 3.x and Java 17

* *Author* : [Antonio Goncalves](http://www.antoniogoncalves.org)
* *Level* : Intermediate
* *Technologies* : **Spring Boot 3.x**, Java 17, JPA (Jakarta Persistence 3.0), Bean Validation, Spring Data JPA, Spring REST, H2 Database, OpenAPI/Swagger
* *Previous Technologies* : Java EE 7 (JPA 2.1, CDI 1.1, Bean Validation 1.1, EJB Lite 3.2, JSF 2.2, JAX-RS 2.0)
* *Summary* : A Petstore-like REST API application migrated from Java EE 7 to Spring Boot 3.x

[Original Repository](https://github.com/agoncal/agoncal-application-petstore-ee7)

## Migration Summary

This application has been **successfully migrated** from Java EE 7 (deployed on WildFly) to **Spring Boot 3.x** with **Java 17**. The migration includes:

### What Was Migrated
- ✅ **Java Version**: Upgraded from Java 1.8 to Java 17 (LTS)
- ✅ **Framework**: Migrated from Java EE 7 to Spring Boot 3.2.0
- ✅ **JPA Entities**: All entity classes migrated from javax.persistence to jakarta.persistence
- ✅ **Validation**: Bean Validation migrated from javax.validation to jakarta.validation
- ✅ **Services**: EJB @Stateless services converted to Spring @Service components
- ✅ **REST API**: JAX-RS endpoints converted to Spring REST @RestController
- ✅ **Persistence**: persistence.xml replaced with Spring Boot application.properties
- ✅ **Database**: Uses H2 in-memory database (configurable)
- ✅ **API Documentation**: Swagger v2 upgraded to OpenAPI v3 (springdoc)
- ✅ **Transactions**: JTA transactions replaced with Spring @Transactional
- ✅ **Logging**: CDI interceptors migrated to Spring AOP

### What Was Removed/Deferred
- ⏸️ **JSF View Layer**: Removed temporarily (can be replaced with Thymeleaf or modern frontend)
- ⏸️ **Security**: JAAS security removed (can be reimplemented with Spring Security)
- ⏸️ **CDI Producers**: Removed (not needed in Spring)

## Purpose of this application

This is a modernized version of the classic Java Petstore application, originally created to demonstrate Java EE capabilities. Now it showcases:
* Modern Spring Boot 3.x development
* RESTful API design with Spring MVC
* JPA with Spring Data
* OpenAPI/Swagger documentation
* Embedded server deployment (no WAR file needed)

## Build and Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Compile and Package

```bash
mvn clean package -Dmaven.test.skip=true
```

This creates an executable JAR file: `target/applicationPetstore.jar`

### Run the Application

```bash
java -jar target/applicationPetstore.jar
```

The application will start on port 8080 with:
- REST API endpoints at: `http://localhost:8080/rest/`
- OpenAPI documentation at: `http://localhost:8080/swagger-ui.html`
- H2 Console at: `http://localhost:8080/h2-console` (for database inspection)

### Available REST Endpoints

- **Categories**: `GET/POST/PUT/DELETE /rest/categories`
- **Products**: `GET/POST/PUT/DELETE /rest/products`
- **Items**: `GET/POST/PUT/DELETE /rest/items`
- **Customers**: `GET/POST/PUT/DELETE /rest/customers`
- **Countries**: `GET/POST/PUT/DELETE /rest/countries`

### Testing

#### Unit Testing

```bash
mvn clean test
```

Tests validate entity equals/hashcode methods.

#### Integration Testing

Integration tests have been disabled during migration. They can be re-enabled using Spring Boot Test framework.

## Configuration

Edit `src/main/resources/application.properties` to customize:
- Database settings (currently H2 in-memory)
- Server port
- JPA/Hibernate settings
- Logging levels

## Technology Stack

    mvn clean verify -Parquillian-wildfly-managed

## Execute the sample

To execute the application, you can either use the WildFly admin console or the Maven WildFly plugin.

### Setup WildFly

Before starting Wildfly we need to create a new user. In the `$WILDFLY_HOME/bin` directory, run the following command `./add-user.sh` to create a new _Management User_. Choose a username and password.

### Start WildFly

In the `$WILDFLY_HOME/bin` directory, run the following command `./standalone.sh` to start WildFly. Then go to the 
WildFly admin console on http://localhost:9990 and enter your usename and password.

### Build and Deploy the application using the WildFly admin console

Build the application with `mvn clean package -Dmaven.test.skip=true`. You get the `applicationPetstore.war` war file in the `target` directory.

In the WildFly admin console, deploy the `applicationPetstore.war` file. For that, go to the _Deployments_ tab and click on _Add_.

### Build and Deploy the application using the WildFly Maven Plugin

With WildFly up and running, deploy the application using the Maven plugin `mvn clean wildfly:deploy`

### Run the application

Once deployed go to the following URL and start buying some pets: [http://localhost:8080/applicationPetstore](http://localhost:8080/applicationPetstore).

The admin [REST interface](http://localhost:8080/applicationPetstore/swagger.json) allows you to create/update/remove items in the catalog, orders or customers. You can run the following [curl](http://curl.haxx.se/) commands :

* `curl -X GET http://localhost:8080/applicationPetstore/rest/categories`
* `curl -X GET http://localhost:8080/applicationPetstore/rest/products`
* `curl -X GET http://localhost:8080/applicationPetstore/rest/items`
* `curl -X GET http://localhost:8080/applicationPetstore/rest/countries`
* `curl -X GET http://localhost:8080/applicationPetstore/rest/customers`

You can also get a JSON representation as follow :

* `curl -X GET -H "accept: application/json" http://localhost:8080/applicationPetstore/rest/items`

Check the Swagger contract on : [http://localhost:8080/applicationPetstore/swagger.json]()

## Databases

The `persistence.xml` defines a persistence unit called `applicationPetstorePU` that uses the default JBoss database :

```
<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>
```

### H2
 
By default, the application uses the in-memory H2 database. If you log into the WildFly [Admin Console](http://localhost:9990/), go to [http://localhost:9990/console/App.html#profile/datasources;name=ExampleDS]() and you will see the H2 Driver as well as the Connection URL pointing at the in-memory H2 database `jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`

### Postgresql

If instead a H2 in-memory database you want to use PostgreSQL, you need to do the following steps.

#### Install the PostgreSQL driver into Wildfly

This [good article](http://ralph.soika.com/wildfly-install-postgresql-jdbc-driver-as-a-module/) explains you how. 

1) Go to `$WILDFLY_HOME/modules/system/layers/base/` and create the folder `org/postgresql/main`
2) Copy the Postgresql [JDBC driver jar](https://jdbc.postgresql.org/download.html) file (eg. `postgresql-42.1.4.jar`) to the new folder `$WILDFLY_HOME/modules/system/layers/base/org/postgresql/main`
3) Create the file `$WILDFLY_HOME/modules/system/layers/base/org/postgresql/main/module.xml` with the following content:

```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.1.4.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

4) Reference the module as a driver in WildFly configuration

```
WILDFLY_HOME/bin $ ./jboss-cli.sh
You are disconnected at the moment. Type 'connect' to connect to the server or 'help' for the list of supported commands.
[disconnected /] connect
[standalone@localhost:9990 /] /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql, driver-class-name=org.postgresql.Driver)
{"outcome" => "success"}
```

#### Modify the default Datasource

In the Wildfly [Admin Console](http://localhost:9990) check the default datasource [ExampleDS](http://localhost:9990/console/App.html#profile/ds-finder/datasources;name=ExampleDS). As you can see, it points to an in-memory H2 database. Make the following changes so it points at Postgres:

1) Attribute Tab: Change the driver to postgresql
2) Connection Tab: Change the Connection URL to `jdbc:postgresql://localhost:5432/postgres`
3) Security Tab: Change User name to `postgres` and no password

Once Postgres is up and running, you can hit the button `Test Connection`. It should be ok.

#### Startup PostgreSQL

The easiest is to use the Docker file to start Postgres

```
$ docker-compose -f src/main/docker/postgresql.yml up -d
```

## Test this application on CloudBees

<a href="https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/cyrille-leclerc/agoncal-application-petstore-ee7/master/clickstart.json"><img src="https://d3ko533tu1ozfq.cloudfront.net/clickstart/deployInstantly.png"/></a>

## Third Party Tools & Frameworks

### Twitter Bootstrap

When, like me, you have no web designer skills at all and your web pages look ugly, you use [Twitter Bootstrap](http://twitter.github.com/bootstrap/) ;o)

## Icons

I use:
 
* [Font Awesome](http://fontawesome.io/)
* [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) which are in Creative Commons

### Arquillian

[Arquillian](http://arquillian.org/) for the integration tests.

## Developpers

Some people who worked on this project :

* [Antoine Sabot-Durand](https://twitter.com/#!/antoine_sd)
* [Brice Leporini](https://twitter.com/#!/blep)
* Hervé Le Morvan

## Bugs & Workaround


## Licensing

<a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-sa/3.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/">Creative Commons Attribution-ShareAlike 3.0 Unported License</a>.

<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>
