package org.change.hippo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations =
        {
                "classpath*:context/context-*.xml",
                "classpath*:servlet-config.xml",
                "classpath*:service/context-service*.xml"
        })
@EnableHystrix
@EnableHystrixDashboard
public class PortalApplication {


    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }
}
