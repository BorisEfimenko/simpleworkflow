package org.simpleworkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-prod.properties", "classpath:/application-service.properties",
    "classpath:/application-service-prod.properties" })
public class ServiceApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(ServiceApplication.class, args);
  }
}
