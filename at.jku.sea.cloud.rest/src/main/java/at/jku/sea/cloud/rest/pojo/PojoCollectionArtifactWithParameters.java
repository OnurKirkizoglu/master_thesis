package at.jku.sea.cloud.rest.pojo;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "CollectionArtifactWithParameters")
public class PojoCollectionArtifactWithParameters {
  /**
   * @author Dominik Muttenthaler
   * @author Florian Weger
   */

  private PojoPackage pckg;
  private PojoObject[] elements;
  private Map<String, PojoObject> propertyMap;
  private boolean onlyContainsArtifacts;

  public PojoCollectionArtifactWithParameters() {
  }

  public PojoCollectionArtifactWithParameters(PojoPackage pckg, PojoObject[] elements, Map<String, PojoObject> propertyMap, boolean onlyContainsArtifacts) {
    this.pckg = pckg;
    this.elements = elements;
    this.propertyMap = propertyMap;
    this.onlyContainsArtifacts = onlyContainsArtifacts;
  }

  public PojoPackage getPackage() {
    return pckg;
  }

  public void setPackage(PojoPackage pckg) {
    this.pckg = pckg;
  }

  public Map<String, PojoObject> getPropertyMap() {
    return propertyMap;
  }

  public void setPropertyMap(Map<String, PojoObject> propertyMap) {
    this.propertyMap = propertyMap;
  }

  public PojoObject[] getElements() {
    return elements;
  }

  public void setElements(PojoObject[] elements) {
    this.elements = elements;
  }

  public boolean isOnlyContainsArtifacts() {
    return onlyContainsArtifacts;
  }

  public void setOnlyContainsArtifacts(boolean onlyContainsArtifacts) {
    this.onlyContainsArtifacts = onlyContainsArtifacts;
  }
}
