package org.simpleworkflow.web.config;

import java.io.IOException;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class DozerConfig {
  @Bean
  public DozerBeanMapperFactoryBean mapper() throws IOException {
    DozerBeanMapperFactoryBean mapper = new DozerBeanMapperFactoryBean();
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    Resource[] mappingFiles = resourcePatternResolver.getResources("classpath:META-INF/dozer-conf/*.xml");
    mapper.setMappingFiles(mappingFiles);

    return mapper;
  }

}
