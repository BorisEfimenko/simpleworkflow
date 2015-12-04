package org.simpleworkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = { "classpath:/application-dao.properties","classpath:/application-dao-prod.properties"}) 
public class RepositoryApplication {
	public static void main(String[] args) throws Exception {
		/*ConfigFileApplicationListener listener = new ConfigFileApplicationListener();
		listener.setSearchNames("application-dao, application-dao-prod");
		SpringApplication springApp = new SpringApplication(RepositoryApplication.class);
		springApp.addListeners(listener);
		springApp.run(args);
*/
		 SpringApplication.run(RepositoryApplication.class, args);
	}
}
