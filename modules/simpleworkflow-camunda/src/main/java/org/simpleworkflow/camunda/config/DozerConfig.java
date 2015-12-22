package org.simpleworkflow.camunda.config;

import org.dozer.FixDozerBeanMapperFactoryBean;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class DozerConfig {

  @Autowired
  private ApplicationContext context;

  @Bean(destroyMethod = "destroy")
  public Mapper mapper() throws Exception {
    FixDozerBeanMapperFactoryBean mapperFactory = new FixDozerBeanMapperFactoryBean();
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    Resource[] mappingFiles = resourcePatternResolver.getResources("classpath:META-INF/dozer-conf/*.xml");
    mapperFactory.setApplicationContext(context);
    mapperFactory.setMappingFiles(mappingFiles);
    mapperFactory.afterPropertiesSet();

    return mapperFactory.getObject();
  }

}
