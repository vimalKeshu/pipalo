package org.pipalo.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan({"org.pipalo.worker.controller", "org.pipalo.worker.service", "org.pipalo.worker.client"})
@SpringBootApplication
public class WorkerAppMain {

    public static void main(String[] args) throws Exception{
        System.out.println("started....");
        SpringApplication.run(WorkerAppMain.class, args);
        System.out.println("ended....");
    }
}
