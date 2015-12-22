package org.simpleworkflow.camunda.plugin.sampletask;


import java.util.HashSet;
import java.util.Set;

import org.camunda.bpm.tasklist.plugin.spi.impl.AbstractTasklistPlugin;
import org.simpleworkflow.camunda.plugin.sampletask.resources.SampleTaskRootResource;


public class SampleTaskPlugin extends AbstractTasklistPlugin {

  public static final String ID = "sampleTask";

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public Set<Class<?>> getResourceClasses() {
    HashSet<Class<?>> classes = new HashSet<Class<?>>();

    classes.add(SampleTaskRootResource.class);

    return classes;
  }

  @Override
  public String getAssetDirectory() {
    return "plugin/sampleTask";
  }

}