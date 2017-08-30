package at.jku.sea.cloud.navigator;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;

/**
 * This is an extension to {@link TerminalOperation} to maneuver through the properties of an Artifact. So to say this
 * is a navigator to help the user to go type-safe towards Artifact properties.
 * 
 * @author Florian Weger
 *
 * @param <T>
 *          the type of the result
 */
public interface TerminalManeuverable<T> extends TerminalOperation<T> {

  /**
   * Navigates from an {@link Artifact} to another {@link Artifact} with a given property name.
   * 
   * @param path
   *          property name
   * @return a TerminalManeuverable that resolves to an {@link Artifact}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalManeuverable<Artifact> to(String path);

  /**
   * Navigates from an {@link Artifact} to another {@link CollectionArtifact} with a given property name.
   * 
   * @param path
   *          property name
   * @return a TerminalManeuverable that resolves to a {@link CollectionArtifact}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalManeuverable<CollectionArtifact> toCollection(String path);

  /**
   * Navigates from an {@link Artifact} to another {@link MapArtifact} with a given property name.
   * 
   * @param path
   *          property name
   * @return a TerminalManeuverable that resolves to a {@link MapArtifact}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalManeuverable<MapArtifact> toMap(String path);

  /**
   * Navigates from an {@link Artifact} to another {@link MetaModel} with a given property name.
   * 
   * @param path
   *          property name
   * @return a TerminalManeuverable that resolves to a {@link MetaModel}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalManeuverable<MetaModel> toMetaModel(String path);

  /**
   * Navigates from an {@link Artifact} to another {@link Project} with a given property name.
   * 
   * @param path
   *          property name
   * @return a TerminalManeuverable that resolves to a {@link Project}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalManeuverable<Project> toProject(String path);

  /**
   * Navigates from an {@link Artifact} to another {@link Container} with a given property name.
   * 
   * @param path
   *          property name
   * @return a TerminalManeuverable that resolves to a {@link Container}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalManeuverable<Container> toContainer(String path);

  /**
   * Navigates from an {@link Artifact} to its {@link Owner}.
   * 
   * @return a TerminalOperation that resolves to an {@link Owner}
   */
  TerminalManeuverable<Owner> toOwner();

  /**
   * Navigates from an {@link Artifact} to its {@link Tool}.
   * 
   * @return a TerminalOperation that resolves to a {@link Tool}
   */
  TerminalManeuverable<Tool> toTool();

  /**
   * Navigates from an {@link Artifact} to a {@link Number} property.
   * 
   * @param path
   *          property name
   * @return a TerminalOperation that resolves to a {@link Number}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalOperation<Number> toNumber(String path);

  /**
   * Navigates from an {@link Artifact} to a {@link Character} property.
   * 
   * @param path
   *          property name
   * @return a TerminalOperation that resolves to a {@link Character}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalOperation<Character> toCharacter(String path);

  /**
   * Navigates from an {@link Artifact} to a {@link String} property.
   * 
   * @param path
   *          property name
   * @return a TerminalOperation that resolves to a {@link String}
   * @throws PropertyDoesNotExistException
   *           if the property does not exist
   */
  TerminalOperation<String> toString(String path);

}
