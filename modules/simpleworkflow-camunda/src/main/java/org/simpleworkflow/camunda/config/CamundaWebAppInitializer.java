package org.simpleworkflow.camunda.config;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.camunda.bpm.admin.impl.web.FixAdminApplication;
import org.camunda.bpm.admin.impl.web.bootstrap.AdminContainerBootstrap;
import org.camunda.bpm.cockpit.impl.web.CockpitApplication;
import org.camunda.bpm.cockpit.impl.web.bootstrap.CockpitContainerBootstrap;
import org.camunda.bpm.engine.rest.filter.CacheControlFilter;
import org.camunda.bpm.tasklist.impl.web.TasklistApplication;
import org.camunda.bpm.tasklist.impl.web.bootstrap.TasklistContainerBootstrap;
import org.camunda.bpm.webapp.impl.engine.EngineRestApplication;
import org.camunda.bpm.webapp.impl.engine.ProcessEnginesFilter;
import org.camunda.bpm.webapp.impl.security.auth.AuthenticationFilter;
import org.camunda.bpm.webapp.impl.security.filter.SecurityFilter;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;



public class CamundaWebAppInitializer implements WebApplicationInitializer {// implements
																			// ServletContextInitializer
																			// {

	public static final Logger LOGGER = LoggerFactory.getLogger(CamundaWebAppInitializer.class);

	private static final EnumSet<DispatcherType> DISPATCHER_TYPES = EnumSet.of(DispatcherType.REQUEST);

	private ServletContext servletContext;

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		this.servletContext = servletContext;
		LOGGER.debug("xxx startup");
		servletContext.addListener(new CockpitContainerBootstrap());
		servletContext.addListener(new AdminContainerBootstrap());
		servletContext.addListener(new TasklistContainerBootstrap());
		registerFilter("Authentication Filter", AuthenticationFilter.class, "/*");

		HashMap<String, String> securityFilterParameters = new HashMap<>();
		securityFilterParameters.put("configFile", "/WEB-INF/securityFilterRules.json");
		registerFilter("Security Filter", SecurityFilter.class, securityFilterParameters, "/*");
		registerFilter("Engines Filter", ProcessEnginesFilter.class, "/app/*");

		registerFilter("CacheControlFilter", CacheControlFilter.class, "/api/*");
/*
		HashMap<String, String> resteasyParameters = new HashMap<>();
		resteasyParameters.put("javax.ws.rs.Application",RestProcessEngineDeployment.class.getName());
		registerFilter("RestEasy Filter", org.jboss.resteasy.plugins.server.servlet.FilterDispatcher.class, "/api/engine/*");
*/		
		
		HashMap<String, String> cockpitApiParameters = new HashMap<>();
		cockpitApiParameters.put("javax.ws.rs.Application", CockpitApplication.class.getName());
		cockpitApiParameters.put("resteasy.servlet.mapping.prefix", "/api/cockpit");
		registerServlet("Cockpit Api",/* ServletContainer.class,*/ cockpitApiParameters, "/api/cockpit/*");

		HashMap<String, String> adminApiParameters = new HashMap<>();
		adminApiParameters.put("javax.ws.rs.Application", FixAdminApplication.class.getName());
		adminApiParameters.put("resteasy.servlet.mapping.prefix", "/api/admin");
		registerServlet("Admin Api", /* ServletContainer.class,*/ adminApiParameters, "/api/admin/*");

		HashMap<String, String> tasklistApiParameters = new HashMap<>();
		tasklistApiParameters.put("javax.ws.rs.Application", TasklistApplication.class.getName());
		tasklistApiParameters.put("resteasy.servlet.mapping.prefix", "/api/tasklist");
		registerServlet("Tasklist Api", /* ServletContainer.class,*/ tasklistApiParameters, "/api/tasklist/*");

		HashMap<String, String> engineApiParameters = new HashMap<>();
		engineApiParameters.put("javax.ws.rs.Application", EngineRestApplication.class.getName());
		engineApiParameters.put("resteasy.servlet.mapping.prefix", "/api/engine");
		registerServlet("Engine Api", /* ServletContainer.class,*/ engineApiParameters, "/api/engine/*");
		
	}

	private FilterRegistration registerFilter(final String filterName, final Class<? extends Filter> filterClass,
			final String... urlPatterns) {
		return registerFilter(filterName, filterClass, null, urlPatterns);
	}

	private FilterRegistration registerFilter(final String filterName, final Class<? extends Filter> filterClass,
			final Map<String, String> initParameters, final String... urlPatterns) {
		FilterRegistration filterRegistration = servletContext.getFilterRegistration(filterName);

		if (filterRegistration == null) {
			filterRegistration = servletContext.addFilter(filterName, filterClass);
			filterRegistration.addMappingForUrlPatterns(DISPATCHER_TYPES, true, urlPatterns);

			if (initParameters != null) {
				filterRegistration.setInitParameters(initParameters);
			}

			LOGGER.debug("Filter {} for URL {} registered.", filterName, urlPatterns);
		}

		return filterRegistration;
	}

	
	private ServletRegistration registerServlet(final String servletName,
			final Map<String, String> initParameters, final String... urlPatterns) {
		ServletRegistration servletRegistration = servletContext.getServletRegistration(servletName);

		if (servletRegistration == null) {
			servletRegistration = servletContext.addServlet(servletName,new HttpServletDispatcher());
			servletRegistration.addMapping(urlPatterns);
			servletRegistration.setInitParameters(initParameters);

			LOGGER.debug("Servlet {} for URL {} registered.", servletName, urlPatterns);
		}

		return servletRegistration;
	}

}
