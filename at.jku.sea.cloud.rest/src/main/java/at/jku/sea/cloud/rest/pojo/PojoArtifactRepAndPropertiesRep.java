package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ArtifactRepAndPropertiesRep")
public class PojoArtifactRepAndPropertiesRep {
  private PojoObject[] artifact;
  private PojoObject[][] properties;

  public PojoArtifactRepAndPropertiesRep() {
  }

  public PojoArtifactRepAndPropertiesRep(
      PojoObject[] artifact,
      PojoObject[][] properties) {
    this.artifact = artifact;
    this.properties = properties;
  }
  
  public PojoObject[] getArtifact() {
    return artifact;
  }

  public void setArtifact(PojoObject[] artifact) {
    this.artifact = artifact;
  }

  public PojoObject[][] getProperties() {
    return properties;
  }

  public void setProperties(PojoObject[][] properties) {
    this.properties = properties;
  }
}
