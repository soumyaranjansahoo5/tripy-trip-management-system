package com.tripy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TripyApplication {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("  Starting Tripy Application...");
        System.out.println("========================================");

        SpringApplication.run(TripyApplication.class, args);

        System.out.println("========================================");
        System.out.println("  Tripy - Trip Management System");
        System.out.println("  Running on: http://localhost:8080");
        System.out.println("========================================");
    }
}