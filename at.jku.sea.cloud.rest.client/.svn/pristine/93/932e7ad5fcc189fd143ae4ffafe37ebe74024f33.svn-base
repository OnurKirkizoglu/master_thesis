package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoMapArtifactKeyProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class MapArtifactKeyProvider extends RestProvider<Object> {
  private final MapArtifact map;

  public MapArtifactKeyProvider(MapArtifact map) {
    this.map = map;
  }

  public MapArtifact getMap() {
    return map;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoMapArtifactKeyProvider(pojoFactory.createPojo(map));
  }

}
