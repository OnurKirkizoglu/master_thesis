package at.jku.sea.cloud.rest.client.navigator;

import java.util.ArrayList;
import java.util.List;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.navigator.TerminalManeuverable;
import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.rest.client.handler.navigator.NavigatorHandler;
import at.jku.sea.cloud.rest.client.navigator.actions.Access;
import at.jku.sea.cloud.rest.client.navigator.actions.OwnerAccess;
import at.jku.sea.cloud.rest.client.navigator.actions.PropertyArtifactAccess;
import at.jku.sea.cloud.rest.client.navigator.actions.PropertyCharacterAccess;
import at.jku.sea.cloud.rest.client.navigator.actions.PropertyNumberAccess;
import at.jku.sea.cloud.rest.client.navigator.actions.PropertyStringAccess;
import at.jku.sea.cloud.rest.client.navigator.actions.ToolAccess;
import at.jku.sea.cloud.stream.Context;

public class RestNavigator<T> implements TerminalManeuverable<T> {

  private final RestStartNavigator<T> start;
  private final List<Access> paths;
  private final NavigatorHandler handler;

  public RestNavigator(RestStartNavigator<T> start) {
    this.start = start;
    this.paths = new ArrayList<>();
    this.handler = new NavigatorHandler();
  }

  @Override
  public T get() throws PropertyDoesNotExistException {
    return handler.get(this);
  }

  @Override
  public T get(Context context) throws PropertyDoesNotExistException {
    throw new UnsupportedOperationException();
  }

  @Override
  public TerminalManeuverable<Artifact> to(String path) {
    return addAndTransform(new PropertyArtifactAccess(path));
  }

  @Override
  public TerminalManeuverable<CollectionArtifact> toCollection(String path) {
    return addAndTransform(new PropertyArtifactAccess(path));
  }

  @Override
  public TerminalManeuverable<MapArtifact> toMap(String path) {
    return addAndTransform(new PropertyArtifactAccess(path));
  }

  @Override
  public TerminalManeuverable<MetaModel> toMetaModel(String path) {
    return addAndTransform(new PropertyArtifactAccess(path));
  }

  @Override
  public TerminalManeuverable<Project> toProject(String path) {
    return addAndTransform(new PropertyArtifactAccess(path));
  }

  @Override
  public TerminalManeuverable<Container> toContainer(String path) {
    return addAndTransform(new PropertyArtifactAccess(path));
  }

  @Override
  public TerminalManeuverable<Owner> toOwner() {
    return addAndTransform(new OwnerAccess());
  }

  @Override
  public TerminalManeuverable<Tool> toTool() {
    return addAndTransform(new ToolAccess());
  }

  @Override
  public TerminalOperation<Number> toNumber(String path) {
    return addAndTransform(new PropertyNumberAccess(path));
  }

  @Override
  public TerminalOperation<Character> toCharacter(String path) {
    return addAndTransform(new PropertyCharacterAccess(path));
  }

  @Override
  public TerminalOperation<String> toString(String path) {
    return addAndTransform(new PropertyStringAccess(path));
  }

  private <S> S addAndTransform(Access access) {
    paths.add(access);
    @SuppressWarnings("unchecked")
    S result = (S) this;
    return result;
  }

  public RestStartNavigator<T> getStart() {
    return start;
  }

  public List<Access> getPaths() {
    return paths;
  }

  public NavigatorHandler getHandler() {
    return handler;
  }
}
