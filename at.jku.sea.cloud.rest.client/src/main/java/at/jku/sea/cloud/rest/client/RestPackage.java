package at.jku.sea.cloud.rest.client;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.rest.client.handler.PackageHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestPackage extends RestContainer implements Package {

  private static final long serialVersionUID = 1L;

  private PackageHandler handler;

  protected RestPackage(long id, long version) {
    super(id, version);
    handler = PackageHandler.getInstance();
  }

  @Override
  public Collection<Artifact> getArtifacts() {
    return handler.getArtifacts(id, version);
  }

  @Override
  public Collection<Package> getPackages() {
    Collection<Package> arts = handler.getPackages(id, version);
    return arts;
  }
}
