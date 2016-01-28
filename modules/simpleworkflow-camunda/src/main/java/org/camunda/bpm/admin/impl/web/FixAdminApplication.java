package org.camunda.bpm.admin.impl.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.camunda.bpm.admin.Admin;
import org.camunda.bpm.admin.plugin.spi.AdminPlugin;
import org.camunda.bpm.engine.rest.exception.ExceptionHandler;
import org.camunda.bpm.engine.rest.exception.RestExceptionHandler;
import org.camunda.bpm.engine.rest.mapper.JacksonConfigurator;
import org.camunda.bpm.webapp.impl.security.auth.FixUserAuthenticationResource;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
public class FixAdminApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<Class<?>>();

    classes.add(JacksonConfigurator.class);
    classes.add(JacksonJsonProvider.class);
    classes.add(RestExceptionHandler.class);
    classes.add(ExceptionHandler.class);

    classes.add(FixUserAuthenticationResource.class);
    classes.add(SetupResource.class);

    addPluginResourceClasses(classes);

    return classes;
  }


  private void addPluginResourceClasses(Set<Class<?>> classes) {

    List<AdminPlugin> plugins = getPlugins();

    for (AdminPlugin plugin : plugins) {
      classes.addAll(plugin.getResourceClasses());
    }
  }

  private List<AdminPlugin> getPlugins() {
    return Admin.getRuntimeDelegate().getAppPluginRegistry().getPlugins();
  }

}

