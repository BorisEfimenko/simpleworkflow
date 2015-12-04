package org.simpleworkflow.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.engine.rest.dto.ResourceOptionsDto;
import org.camunda.bpm.engine.rest.dto.authorization.AuthorizationDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;



public class HalTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(HalTest.class);

	private static final String username = "redfox";
	private static final String password = "11111111";
	private String login = "http://localhost:8080/simpleworkflow-camunda/api/admin/auth/user/default/login/tasklist";

	@Test
	public void testLogin() {
		login();
	}

	@Test
	public void testFilterRequests() {
		ResponseEntity<ResourceOptionsDto> resourceOptionsEntity = testFilterRequest("application/json; q=0.5");
		assertEquals(HttpStatus.OK, resourceOptionsEntity.getStatusCode());
		assertNotNull("body must not be null.", resourceOptionsEntity.getBody());
		resourceOptionsEntity = testFilterRequest("application/json+hal; application/json; q=0.5");
		assertEquals(HttpStatus.OK, resourceOptionsEntity.getStatusCode());
		assertNotNull("body must not be null. Wrong version Jackson ", resourceOptionsEntity.getBody());
	}

	private String getJSessionID(ResponseEntity<AuthorizationDto> entity) {
		List<String> cookies = entity.getHeaders().get("Cookie");
		// assuming only one cookie with jsessionid as the only value
		if (cookies == null) {
			cookies = entity.getHeaders().get("Set-Cookie");
		}

		LOGGER.debug("cookies: " + cookies.toString());
		String jsessionid = "";
		for (String cookie : cookies) {
			if (cookie.startsWith("JSESSIONID")) {
				int end = cookie.indexOf(";");
				jsessionid = cookie.substring("JSESSIONID=".length(), end);
			}
		}
		LOGGER.debug(cookies.toString());
		return jsessionid;
	}

	private ResponseEntity<AuthorizationDto> login() {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
		MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
		bodyMap.add("username", username);
		bodyMap.add("password", password);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap,
				headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AuthorizationDto> entity = restTemplate.postForEntity(login, request, AuthorizationDto.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		AuthorizationDto authorization = entity.getBody();
		assertEquals(authorization.getUserId(), username);
		LOGGER.debug("logged as " + authorization.getUserId());
		return entity;
	}

	private ResponseEntity<ResourceOptionsDto> testFilterRequest(String mediaTypes) {
		ResponseEntity<AuthorizationDto> entity = login();

		String jsessionid = getJSessionID(entity);
		LOGGER.debug("jsessionid: " + jsessionid);

		HttpHeaders headers = new HttpHeaders();

		//headers.setAccept(MediaType.parseMediaTypes(mediaTypes));
		headers.set(HttpHeaders.ACCEPT, mediaTypes);
		headers.add("Cookie", "JSESSIONID=" + jsessionid);

		HttpEntity<Object> request = new HttpEntity<Object>(headers);
		String url = "http://localhost:8080/simpleworkflow-camunda/api/engine/engine/default/filter";

		RestTemplate restTemplate =getRestTemplate();
		//restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		return restTemplate.exchange(url, HttpMethod.OPTIONS, request, ResourceOptionsDto.class);

	}
	
	public RestTemplate getRestTemplate() {
		 RestTemplate restTemplate = new RestTemplate();
		 List<HttpMessageConverter<?>> existingConverters = restTemplate.getMessageConverters();
		 List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
		 newConverters.add(getHalMessageConverter());
		 newConverters.addAll(existingConverters);
		 restTemplate.setMessageConverters(newConverters);
		 return restTemplate;
		}

		private HttpMessageConverter<?> getHalMessageConverter() {
		 ObjectMapper objectMapper = new ObjectMapper();
		 objectMapper.registerModule(new Jackson2HalModule());
		 MappingJackson2HttpMessageConverter halConverter = new TypeConstrainedMappingJackson2HttpMessageConverter(ResourceSupport.class);
		 halConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
		 halConverter.setObjectMapper(objectMapper);
		 return halConverter;
		}
  
}  
