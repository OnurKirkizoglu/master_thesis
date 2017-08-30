package at.jku.sea.cloud.rest.client;

import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.ArtifactDoesNotExistException;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.client.handler.PropertyHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestProperty implements Property {

	private static final long serialVersionUID = 1L;

	private long id;
	private long version;
	private String name;

	private PropertyHandler handler;

	protected RestProperty(long id, long version, String name) {
		this.id = id;
		this.version = version;
		this.name = name;
		handler = PropertyHandler.getInstance();
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public long getVersionNumber() {
		return version;
	}

	@Override
	public Owner getOwner() {
		return handler.getOwner(id, version, name);
	}

	@Override
	public Tool getTool() {
		return handler.getTool(id, version, name);
	}

	@Override
	public boolean isAlive() {
		return handler.isAlive(id, version, name);
	}

	@Override
	public boolean isReference() {
		return handler.isReference(id, version, name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getValue() {
		return handler.getValue(id, version, name);
	}

	@Override
	public void setValue(Workspace workspace, Object value)
			throws ArtifactDoesNotExistException, ArtifactDeadException,
			PropertyDeadException, TypeNotSupportedException {
		handler.setValue(workspace.getId(), id, version, name, value);
	}

	 @Override
	  public Object[] getRepresentation() {
	    return handler.getRepresentation(id, version, name);
	  }
	
	@Override
	public void delete(Workspace workspace) {
		handler.delete(workspace.getId(), id, version, name);
	}

	@Override
	public void undelete(Workspace workspace) {
		handler.undelete(workspace.getId(), id, version, name);
	}

	@Override
	public Artifact getArtifact() {
		return handler.getArtifact(id, version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, id, name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RestProperty other = (RestProperty) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (version != other.version) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Property[" + id + ", " + version + ", " + name + "]";
	}

 
}
