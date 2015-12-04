package org.simpleworkflow;

import org.simpleworkflow.service.ReportService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
// @EntityScan(basePackages="org.simpleworkflow.domain")
// @EnableTransactionManagement
// @EnableJpaRepositories(basePackages = "org.simpleworkflow.repository")

public class JpaApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(JpaApplication.class, args);

	}

	@Bean
	InitializingBean testInitializer(final ReportService reportService) {

		return new InitializingBean() {
			public void afterPropertiesSet() throws Exception {
				reportService.findAll();
			}
		};
	}
}
