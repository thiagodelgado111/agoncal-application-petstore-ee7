package org.agoncal.application.petstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application class for Petstore application
 * Migrated from Java EE 7 to Spring Boot 3.x with Java 21
 * 
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@SpringBootApplication
public class PetstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetstoreApplication.class, args);
    }
}
