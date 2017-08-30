package at.jku.sea.cloud.rest.client;

import java.util.Collection;

import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.client.handler.CollectionArtifactHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestCollectionArtifact extends RestArtifact implements CollectionArtifact {

  private static final long serialVersionUID = 1L;

  private CollectionArtifactHandler handler;

  protected RestCollectionArtifact(long id, long version) {
    super(id, version);
    handler = CollectionArtifactHandler.getInstance();
  }

  @Override
  public boolean isContainingOnlyArtifacts() {
    return handler.isContainingOnlyArtifacts(id, version);
  }

  @Override
  public void addElement(Workspace workspace, Object element) throws ArtifactDeadException, TypeNotSupportedException {
    handler.addElement(workspace.getId(), id, version, element);
  }

  @Override
  public void addElements(Workspace workspace, Collection<?> elements) throws ArtifactDeadException, TypeNotSupportedException {
    handler.addElements(workspace.getId(), id, version, elements);
  }

  @Override
  public void removeElement(Workspace workspace, Object element) throws ArtifactDeadException {
    handler.removeElement(workspace.getId(), id, version, element);
  }

  @Override
  public void insertElementAt(Workspace workspace, Object element, long index) throws ArtifactDeadException, TypeNotSupportedException {
    handler.insertElementAt(workspace.getId(), id, version, index, element);
  }

  @Override
  public void removeElementAt(Workspace workspace, long index) throws ArtifactDeadException, IndexOutOfBoundsException {
    handler.removeElementAt(workspace.getId(), id, version, index);
  }

  @Override
  public Object getElementAt(long index) {
    return handler.getElementAt(id, version, index);
  }

  @Override
  public Collection<?> getElements() {
    return handler.getElements(id, version);
  }

  @Override
  public boolean existsElement(Object element) {
    return handler.existsElement(id, version, element);
  }

  @Override
  public Long size() {
    return handler.size(id, version);
  }
}
