package at.jku.sea.cloud.navigator;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.stream.Context;
import at.jku.sea.cloud.stream.Stream;

/**
 * Establish the navigation towards the property of an Artifact.
 * 
 * @author Florian Weger
 */
public interface NavigatorProvider {

  /**
   * Builds a {@link TerminalManeuverable} with a given Artifact.
   * 
   * @param <T>
   *          <code>T</code> must be a an Artifact type
   * @param artifact
   *          start point of property navigation
   * @return start of navigation
   * 
   * @throws NullPointerException
   *           if artifact is <code>null</code>
   */
  <T extends Artifact> TerminalManeuverable<T> from(T artifact);

  /**
   * Builds a {@link TerminalManeuverable} from a given {@link Context}. This is used for
   * {@link Stream#map(String, at.jku.sea.cloud.navigator.TerminalOperation)}.
   * 
   * @param <T>
   *          <code>T</code> must be a an Artifact type
   * @param context
   *          wrapped name of context
   * @return start of navigation in a {@link Stream#map(String, at.jku.sea.cloud.navigator.TerminalOperation)}
   * @throws NullPointerException
   *           if <code>context</code> is <code>null</code>
   */
  <T extends Artifact> TerminalManeuverable<T> from(Context.Path context);
}
