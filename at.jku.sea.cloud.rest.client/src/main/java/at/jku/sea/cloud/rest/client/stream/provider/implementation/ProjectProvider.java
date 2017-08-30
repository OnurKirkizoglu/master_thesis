package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProjectProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class ProjectProvider extends RestProvider<Artifact> {
  private final Project project;

  public ProjectProvider(Project project) {
    this.project = project;
  }

  public Project getProject() {
    return project;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoProjectProvider(pojoFactory.createPojo(project));
  }
}
