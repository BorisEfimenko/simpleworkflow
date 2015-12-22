package org.simpleworkflow.camunda.plugin.sampletask.resources;

import javax.ws.rs.Path;

import org.camunda.bpm.tasklist.resource.AbstractTasklistPluginRootResource;
import org.simpleworkflow.camunda.plugin.sampletask.SampleTaskPlugin;

@Path("plugin/" + SampleTaskPlugin.ID)
public class SampleTaskRootResource extends AbstractTasklistPluginRootResource {

  public SampleTaskRootResource() {
    super(SampleTaskPlugin.ID);
  }

}
