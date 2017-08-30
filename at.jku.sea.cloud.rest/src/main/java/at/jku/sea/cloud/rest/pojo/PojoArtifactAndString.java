package at.jku.sea.cloud.rest.pojo;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ArtifactAndString")
public class PojoArtifactAndString {
  private PojoArtifact[] artifacts;
  private Set<String> properties;

  public PojoArtifactAndString() {
  }

  public PojoArtifactAndString(PojoArtifact[] artifacts, Set<String> properties) {
    this.artifacts = artifacts;
    this.properties = properties;
  }

  public PojoArtifact[] getArtifacts() {
    return artifacts;
  }

  public void setArtifacts(PojoArtifact[] artifacts) {
    this.artifacts = artifacts;
  }

  public Set<String> getProperties() {
    return properties;
  }

  public void setProperties(Set<String> properties) {
    this.properties = properties;
  }
}
