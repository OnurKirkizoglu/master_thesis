package at.jku.sea.cloud.rest.pojo.stream.action;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "StreamMapAction")
public class PojoMapAction extends PojoStreamAction {
  private PojoNavigator navigator;
  
  public PojoMapAction() {
  }
  
  public PojoMapAction(String context, PojoNavigator navigator) {
    super(context);
    this.navigator = navigator;
  }
  
  public PojoNavigator getNavigator() {
    return navigator;
  }
  
  public void setNavigator(PojoNavigator navigator) {
    this.navigator = navigator;
  }
  
}
