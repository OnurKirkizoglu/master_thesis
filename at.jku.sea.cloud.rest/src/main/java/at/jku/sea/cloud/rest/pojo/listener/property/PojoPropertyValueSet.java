package at.jku.sea.cloud.rest.pojo.listener.property;

import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoVersion;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "PropertyValueSet")
public class PojoPropertyValueSet extends PojoPropertyChange {

  private PojoObject value;
  private PojoObject oldValue;
  private boolean wasReference;

  public PojoPropertyValueSet() {
  }

  public PojoPropertyValueSet(long origin, PojoProperty property, PojoObject value, PojoObject oldValue, boolean wasReference) {
    super(origin, property);
    this.value = value;
    this.oldValue = oldValue;
    this.wasReference = wasReference;
  }

  public PojoObject getValue() {
    return value;
  }

  public void setValue(PojoObject value) {
    this.value = value;
  }

  public PojoObject getOldValue() {
    return oldValue;
  }

  public void setOldValue(PojoObject oldValue) {
    this.oldValue = oldValue;
  }

  public boolean isWasReference() {
    return wasReference;
  }

  public void setWasReference(boolean wasReference) {
    this.wasReference = wasReference;
  }
}
