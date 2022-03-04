package org.pipalo.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.pipalo.worker.service", "org.pipalo.worker.client"})
public class WorkerAppMain {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(WorkerAppMain.class, args);
    }
}
