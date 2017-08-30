package at.jku.sea.cloud.rest.server.handler;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.exceptions.PackageDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 * @author gkanos
 */
public class PackageHandler {

  @Autowired
  protected Cloud cloud;
  @Autowired
  protected PojoFactory pojoFactory;

  public PojoArtifact[] getArtifacts(long id, long version) throws PackageDoesNotExistException {
    Package _package = cloud.getPackage(version, id);
    Artifact[] artifacts = _package.getArtifacts().toArray(new Artifact[0]);
    return pojoFactory.createPojoArray(artifacts);
  }

  public PojoPackage[] getPackages(long version) {
    Collection<Package> x = cloud.getPackages(version);
    Package[] array = (Package[]) x.toArray();
    return pojoFactory.createPojoArray(array);
  }

  public PojoPackage[] getPackages(long id, long version) {
    Package pack = cloud.getPackage(version, id);
    
    Collection<Package> packs = pack.getPackages();

    return pojoFactory.createPojoArray(PojoPackage[]::new, packs, pojoFactory::createPojo);
  }
}
