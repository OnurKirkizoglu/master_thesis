package at.jku.sea.cloud.rest.client.handler;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoPackage;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class PackageHandler extends AbstractHandler {
  
  private static PackageHandler INSTANCE;
  
  public static PackageHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PackageHandler();  
    }
    return INSTANCE;
  }
  
  protected PackageHandler() {}

  public Collection<Artifact> getArtifacts(long id, long version) {
    String url = String.format(CONTAINER_ADDRESS + "/id=%d&v=%d/artifacts", id, version);
    PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
    Artifact[] artifacts = restFactory.createRestArray(pojoArtifacts);
    return list(artifacts);
  }
  public Collection<Package> getPackages(long id, long version) {
    String url = String.format(PACKAGE_ADDRESS + "/id=%d&v=%d",id, version);
    PojoPackage[] pojoPack = template.getForEntity(url, PojoPackage[].class).getBody();
    Package[] packs = restFactory.createRestArray(pojoPack);
    return list(packs);

  }
}
