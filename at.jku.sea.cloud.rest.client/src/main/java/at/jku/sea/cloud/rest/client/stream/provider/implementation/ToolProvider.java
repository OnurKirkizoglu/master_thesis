package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoToolProvider;

public class ToolProvider extends RestProvider<Artifact> {
  private final Tool tool;

  public ToolProvider(Tool tool) {
    this.tool = tool;
  }

  public Tool getTool() {
    return tool;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoToolProvider(pojoFactory.createPojo(tool));
  }

}
