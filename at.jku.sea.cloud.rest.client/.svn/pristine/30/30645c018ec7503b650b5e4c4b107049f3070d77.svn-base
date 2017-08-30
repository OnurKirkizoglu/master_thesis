package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProjectPackageProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class ProjectPackageProvider extends RestProvider<Package> {
  private final Project project;

  public ProjectPackageProvider(Project project) {
    this.project = project;
  }

  public Project getProject() {
    return project;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoProjectPackageProvider(pojoFactory.createPojo(project));
  }

}
