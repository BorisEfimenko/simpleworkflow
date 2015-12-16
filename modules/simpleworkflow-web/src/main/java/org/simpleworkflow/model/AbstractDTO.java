package org.simpleworkflow.model;

import java.io.Serializable;

@SuppressWarnings("serial")
  public abstract class AbstractDTO implements Serializable{
          protected Long id;
          protected Integer version;

          public Integer getVersion() {
                  return version;
          }

          public void setVersion(Integer version) {
                  this.version = version;
          }

          
          public Long getId() {
            return id;
          }

          
          public void setId(Long id) {
            this.id = id;
          }

  }
