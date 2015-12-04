package org.simpleworkflow.camunda.config;

import java.io.IOException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProcessEngineSpringConfiguration {

	@Inject
	private DataSource dataSource;

	@Inject
	private PlatformTransactionManager transactionManager;

	@Inject
	private ResourcePatternResolver resourceResolver;

	@Bean
	public ProcessEngineConfigurationImpl processEngineConfiguration() {
		SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
		config.setDataSource(dataSource);
		config.setTransactionManager(transactionManager);
		config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		config.setJobExecutorActivate(false);
		try {
			config.setDeploymentResources(resourceResolver
					.getResources("classpath*:autodeploy/*.bpmn"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return config;
	}
}
