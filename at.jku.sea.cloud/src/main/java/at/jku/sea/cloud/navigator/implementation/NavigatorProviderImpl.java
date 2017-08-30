package at.jku.sea.cloud.navigator.implementation;

import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.navigator.NavigatorProvider;
import at.jku.sea.cloud.navigator.TerminalManeuverable;
import at.jku.sea.cloud.stream.Context;

/**
 * Establish the navigation towards the property of an Artifact.
 * 
 * @author Florian Weger
 */
public final class NavigatorProviderImpl implements NavigatorProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Artifact> TerminalManeuverable<T> from(T artifact) {
    Objects.requireNonNull(artifact);

    return new StartNavigator<T>(artifact);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Artifact> TerminalManeuverable<T> from(Context.Path context) {
    Objects.requireNonNull(context);

    return new StreamStartNavigator<>(context);
  }
}
