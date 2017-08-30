package at.jku.sea.cloud.rest.client.handler;

import java.util.Collection;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoPackage;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class ProjectHandler
		extends CollectionArtifactHandler {
  
  private static ProjectHandler INSTANCE;
  
  public static ProjectHandler getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ProjectHandler();  
    }
    return INSTANCE;
  }
  
  protected ProjectHandler() {}

	public void addArtifact(
			long wsId,
			long proId,
			long proVersion,
			long id,
			long version)
			throws ArtifactDeadException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/projects/id=%d&v=%d/artifacts/id=%d&v=%d", wsId, proId, proVersion, id, version);
		try {
			template.put(url, null);
		}
		catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
				throw new ArtifactDeadException(version, id);
			}
			throw e;
		}
	}

	public void removeArtifact(
			long wsId,
			long proId,
			long proVersion,
			long id,
			long version)
			throws ArtifactDeadException {
		String url = String.format(WORKSPACE_ADDRESS + "/id=%d/projects/id=%d&v=%d/artifacts/id=%d&v=%d", wsId, proId, proVersion, id, version);
		try {
			template.delete(url);
		}
		catch (HttpClientErrorException e) {
			if (e.getResponseBodyAsString().equals(ArtifactDeadException.class.getSimpleName())) {
				throw new ArtifactDeadException(version, id);
			}
			throw e;
		}
	}

	public Collection<Package> getPackages(
			long id,
			long version) {
		String url = String.format(PROJECT_ADDRESS + "/id=%d&v=%d/packages", id, version);
		PojoPackage[] pojoPackages = template.getForEntity(url, PojoPackage[].class).getBody();
		Package[] packages = restFactory.createRestArray(pojoPackages);
		return list(packages);
	}

	public Collection<Artifact> getArtifacts(
			long id,
			long version) {
		String url = String.format(PROJECT_ADDRESS + "/id=%d&v=%d/artifacts", id, version);
		PojoArtifact[] pojoArtifacts = template.getForEntity(url, PojoArtifact[].class).getBody();
		Artifact[] artifacts = restFactory.createRestArray(pojoArtifacts);
		return list(artifacts);
	}
}
