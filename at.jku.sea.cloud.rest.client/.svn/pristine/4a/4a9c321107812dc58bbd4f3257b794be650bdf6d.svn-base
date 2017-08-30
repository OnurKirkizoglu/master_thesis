package at.jku.sea.cloud.rest.client.navigator;

import java.util.Objects;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.navigator.NavigatorProvider;
import at.jku.sea.cloud.navigator.TerminalManeuverable;
import at.jku.sea.cloud.stream.Context;

public class RestNavigatorProvider implements NavigatorProvider {
  @Override
  public <T extends Artifact> TerminalManeuverable<T> from(T artifact) {
    Objects.requireNonNull(artifact);
    return new RestNavigator<>(new RestStartArtifact<T>(artifact));
  }

  @Override
  public TerminalManeuverable<Artifact> from(Context.Path context) {
    Objects.requireNonNull(context);
    return new RestNavigator<>(new RestStartStream(context.get()));
  }
}
