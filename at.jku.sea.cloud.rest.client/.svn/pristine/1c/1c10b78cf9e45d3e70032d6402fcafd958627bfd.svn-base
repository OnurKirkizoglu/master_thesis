package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoMapArtifactValueProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class MapArtifactValueProvider extends RestProvider<Object> {
  private final MapArtifact map;

  public MapArtifactValueProvider(MapArtifact map) {
    this.map = map;
  }

  public MapArtifact getMap() {
    return map;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    return new PojoMapArtifactValueProvider(pojoFactory.createPojo(map));
  }
}
