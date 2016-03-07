package com.andy.gsbc.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Created by 延泽 on 3/6 0006.
 * Demo
 */
@SpringBootApplication
public class DemoServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(DemoServer.class, args);
    }
}
