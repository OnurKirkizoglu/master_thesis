package at.jku.sea.cloud.rest.pojo.stream;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.stream.action.PojoStreamAction;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Stream")
@JsonSubTypes(@Type(value = PojoMatchStream.class))
public class PojoStream {
  private PojoProvider provider;
  private PojoStreamAction[] actions;
  
  public PojoStream() {
  }
  
  public PojoStream(PojoProvider provider, PojoStreamAction[] actions) {
    this.provider = provider;
    this.actions = actions;
  }
  
  public PojoProvider getProvider() {
    return provider;
  }
  
  public void setProvider(PojoProvider provider) {
    this.provider = provider;
  }
  
  public PojoStreamAction[] getActions() {
    return actions;
  }
  
  public void setActions(PojoStreamAction[] actions) {
    this.actions = actions;
  }
  
}
