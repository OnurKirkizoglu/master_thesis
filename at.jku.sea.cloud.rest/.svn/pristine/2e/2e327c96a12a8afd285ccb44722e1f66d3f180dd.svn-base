package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ArtifactAndFilters")
public class PojoArtifactAndFilters {

  private PojoArtifact artifact;
  private PojoArtifact[] filters;

  public PojoArtifactAndFilters() {

  }

  public PojoArtifactAndFilters(PojoArtifact artifact, PojoArtifact[] filters) {
    this.artifact = artifact;
    this.filters = filters;
  }

  public PojoArtifact getArtifact() {
    return artifact;
  }

  public void setArtifact(PojoArtifact artifact) {
    this.artifact = artifact;
  }

  public PojoArtifact[] getFilters() {
    return filters;
  }

  public void setFilters(PojoArtifact[] filters) {
    this.filters = filters;
  }
}
