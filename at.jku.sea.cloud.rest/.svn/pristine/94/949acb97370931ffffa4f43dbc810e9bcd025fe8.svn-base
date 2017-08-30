package at.jku.sea.cloud.rest.pojo.navigator;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Access")
@JsonSubTypes({
  @Type(value = PojoPropertyArtifactAccess.class),
  @Type(value = PojoPropertyNumberAccess.class),
  @Type(value = PojoPropertyStringAccess.class),
  @Type(value = PojoPropertyCharacterAccess.class),
  @Type(value = PojoOwnerAccess.class),
  @Type(value = PojoToolAccess.class)
})
public abstract class PojoAccess {
  
  public PojoAccess() {
  }

}
