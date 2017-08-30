package at.jku.sea.cloud.rest.pojo.navigator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Navigator")
@JsonSubTypes({ @Type(value = PojoArtifactNavigator.class), @Type(value = PojoStreamNavigator.class) })
public abstract class PojoNavigator {
  private PojoAccess[] access;
  
  public PojoNavigator() {
  }
  
  public PojoNavigator(PojoAccess[] access) {
    this.access = access;
  }
  
  public PojoAccess[] getAccess() {
    return access;
  }
  
  public void setAccess(PojoAccess[] access) {
    this.access = access;
  }
}
