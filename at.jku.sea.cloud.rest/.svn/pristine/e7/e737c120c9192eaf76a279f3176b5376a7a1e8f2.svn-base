package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoMapArtifact;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "MapArtifactKeyProvider")
public class PojoMapArtifactKeyProvider extends PojoProvider {
  private PojoMapArtifact mapArtifact;
  
  public PojoMapArtifactKeyProvider() {
  }
  
  public PojoMapArtifactKeyProvider(PojoMapArtifact mapArtifact) {
    this.mapArtifact = mapArtifact;
  }
  
  public PojoMapArtifact getMapArtifact() {
    return mapArtifact;
  }
  
  public void setMapArtifact(PojoMapArtifact mapArtifact) {
    this.mapArtifact = mapArtifact;
  }
  
}
