package at.jku.sea.cloud.rest.client;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.rest.client.handler.ProjectHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestProject
		extends RestCollectionArtifact
		implements Project {

	private static final long serialVersionUID = 1L;

	private ProjectHandler handler;

	protected RestProject(
			long id,
			long version) {
		super(id, version);
		handler = ProjectHandler.getInstance();
	}

	@Override
	public void addArtifact(
			Workspace workspace,
			Artifact artifact)
			throws ArtifactDeadException {
		handler.addArtifact(workspace.getId(), id, version, artifact.getId(), artifact.getVersionNumber());
	}

	@Override
	public void removeArtifact(
			Workspace workspace,
			Artifact artifact)
			throws ArtifactDeadException {
		handler.removeArtifact(workspace.getId(), id, version, artifact.getId(), artifact.getVersionNumber());
	}

	@Override
	public Collection<Package> getPackages() {
		return handler.getPackages(id, version);
	}

	@Override
	public Collection<Artifact> getArtifacts() {
		return handler.getArtifacts(id, version);
	}

  @Override
  public boolean containsArtifact(Artifact artifact) {
    return handler.existsElement(id, version, artifact);
  }
}
