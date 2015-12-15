package org.simpleworkflow.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "org.simpleworkflow.camunda.config" })
public class Application extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

  public static void main(String args[]) throws Exception {
    SpringApplication.run(Application.class, args);
  }
  /*
   * @Bean InitializingBean usersAndGroupsInitializer(final IdentityService
   * identityService) {
   * 
   * return new InitializingBean() { public void afterPropertiesSet() throws
   * Exception { User admin = identityService.newUser("redfox");
   * admin.setPassword("11111111"); admin.setFirstName("redfox");
   * admin.setLastName("redfox"); identityService.saveUser(admin); Group group =
   * identityService.newGroup("admins"); group.setName("admins");
   * group.setType("SYSTEM"); identityService.saveGroup(group);
   * identityService.createMembership(admin.getId(), group.getId()); } }; }
   */

}
