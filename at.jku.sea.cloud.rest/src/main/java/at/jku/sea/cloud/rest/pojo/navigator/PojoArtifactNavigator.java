package at.jku.sea.cloud.rest.pojo.navigator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoArtifact;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ArtifactNavigator")
public class PojoArtifactNavigator extends PojoNavigator {
  
  private PojoArtifact startElement;
  
  public PojoArtifactNavigator() {
  }
  
  public PojoArtifactNavigator(PojoArtifact startElement, PojoAccess[] access) {
    super(access);
    this.startElement = startElement;
  }
  
  public PojoArtifact getStartElement() {
    return startElement;
  }
  
  public void setStartElement(PojoArtifact startElement) {
    this.startElement = startElement;
  }
}
