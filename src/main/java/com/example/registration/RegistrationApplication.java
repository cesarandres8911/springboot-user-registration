package com.example.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class RegistrationApplication {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationApplication.class);

    public static void main(String[] args) {
        logger.info("Starting RegistrationApplication...");
        try {
            SpringApplication.run(RegistrationApplication.class, args);
            logger.info("RegistrationApplication started successfully.");
        } catch (Exception e) {
            logger.error("Error starting RegistrationApplication", e);
        }
    }

}
