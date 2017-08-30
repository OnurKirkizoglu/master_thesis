package at.jku.sea.cloud.rest.pojo.predicate;

import at.jku.sea.cloud.rest.pojo.PojoObject;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "HasPropertyValuePredicate")
public class PojoPredicateHasPropertyValue extends PojoPredicate {
  private String context;
  private String name;
  private PojoObject value;

  public PojoPredicateHasPropertyValue() {
  }

  public PojoPredicateHasPropertyValue(String context, String name, PojoObject value) {
    this.context = context;
    this.name = name;
    this.value = value;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PojoObject getValue() {
    return value;
  }

  public void setValue(PojoObject value) {
    this.value = value;
  }
}
