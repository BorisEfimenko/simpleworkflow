package org.simpleworkflow;

import static org.camunda.bpm.engine.authorization.Authorization.*;
import static org.camunda.bpm.engine.authorization.Permissions.*;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//@ComponentScan({ "org.simpleworkflow.camunda.config" })
@PropertySource(value = { "classpath:/application-dao.properties", "classpath:/application-dao-dev.properties", "classpath:/application-service.properties",
    "classpath:/application-service-prod.properties", "classpath:/application-web.properties" })
public class WebApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(WebApplication.class);
  }

  public static void main(String args[]) throws Exception {
    SpringApplication.run(WebApplication.class, args);
  }

  @Bean
  InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {

    return new InitializingBean() {
      @Autowired
      AuthorizationService authorizationService;
      @Override
      public void afterPropertiesSet() throws Exception {
        
       // create group 
       if(identityService.createGroupQuery().groupId(Groups.CAMUNDA_ADMIN).count() == 0) { 
         Group camundaAdminGroup = identityService.newGroup(Groups.CAMUNDA_ADMIN); 
         camundaAdminGroup.setName("camunda BPM Administrators"); 
         camundaAdminGroup.setType(Groups.GROUP_TYPE_SYSTEM); 
         identityService.saveGroup(camundaAdminGroup); 
         for (Resource resource : Resources.values()) { 
                    if(authorizationService.createAuthorizationQuery().groupIdIn(Groups.CAMUNDA_ADMIN).resourceType(resource).resourceId(ANY).count() == 0) { 
                      AuthorizationEntity userAdminAuth = new AuthorizationEntity(AUTH_TYPE_GRANT); 
                     userAdminAuth.setGroupId(Groups.CAMUNDA_ADMIN); 
                      userAdminAuth.setResource(resource); 
                      userAdminAuth.setResourceId(ANY); 
                      userAdminAuth.addPermission(ALL); 
                      authorizationService.saveAuthorization(userAdminAuth); 
                    } 
                  } 

       } 

        if (identityService.createUserQuery().memberOfGroup(Groups.CAMUNDA_ADMIN).count() == 0){
        User admin = identityService.newUser("admin");
        admin.setPassword("11111111");
        admin.setFirstName("admin");
        admin.setLastName("admin");
        identityService.saveUser(admin);               
        identityService.createMembership(admin.getId(),  Groups.CAMUNDA_ADMIN);
        }
      }
    
    };
  }

}
