package at.jku.sea.cloud.navigator.implementation;

import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.navigator.TerminalManeuverable;
import at.jku.sea.cloud.navigator.TerminalOperation;

abstract class AbstractNavigator<T extends Artifact> implements TerminalManeuverable<T> {
  
  @Override
  public TerminalManeuverable<Artifact> to(String path) {
    Objects.requireNonNull(path);
    
    return new IntermediateNavigator<Artifact>(this, path);
  }
  
  @Override
  public TerminalManeuverable<CollectionArtifact> toCollection(String path) {
    Objects.requireNonNull(path);
    
    return new IntermediateNavigator<CollectionArtifact>(this, path);
  }
  
  @Override
  public TerminalManeuverable<MapArtifact> toMap(String path) {
    Objects.requireNonNull(path);
    
    return new IntermediateNavigator<MapArtifact>(this, path);
  }
  
  @Override
  public TerminalManeuverable<Container> toContainer(String path) {
    Objects.requireNonNull(path);
    return new IntermediateNavigator<Container>(this, path);
  }
  
  @Override
  public TerminalManeuverable<MetaModel> toMetaModel(String path) {
    Objects.requireNonNull(path);
    
    return new IntermediateNavigator<MetaModel>(this, path);
  }
  
  @Override
  public TerminalManeuverable<Project> toProject(String path) {
    Objects.requireNonNull(path);
    
    return new IntermediateNavigator<Project>(this, path);
  }
  
  @Override
  public TerminalManeuverable<Owner> toOwner() {
    return new IntermediateOwnerNavigator(this);
  }
  
  @Override
  public TerminalManeuverable<Tool> toTool() {
    return new IntermediateToolNavigator(this);
  }
  
  @Override
  public TerminalOperation<Number> toNumber(String path) {
    Objects.requireNonNull(path);
    
    return new TerminalNavigator<Number>(this, path);
  }
  
  @Override
  public TerminalOperation<Character> toCharacter(String path) {
    Objects.requireNonNull(path);
    
    return new TerminalNavigator<Character>(this, path);
  }
  
  @Override
  public TerminalOperation<String> toString(String path) {
    Objects.requireNonNull(path);
    
    return new TerminalNavigator<String>(this, path);
  }
}