package org.springframework.hateoas.hal;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class IdResourceSupportMixin extends ResourceSupportMixin {
//http://www.howtobuildsoftware.com/index.php/how-do/jSP/java-jackson-spring-data-rest-spring-hateoas-hal-null-id-property-when-deserialize-json-with-jackson-and-jackson2halmodule-of-spring-hateoas
  @Override
  @JsonIgnore(false)
  public abstract Link getId();
}
