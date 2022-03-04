package org.pipalo.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"org.pipalo.master.service", "org.pipalo.master.client"})
public class MasterAppMain {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(MasterAppMain.class, args);
    }
}
