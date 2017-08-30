package at.jku.sea.cloud.rest.pojo.listener.property;

import at.jku.sea.cloud.rest.pojo.PojoProperty;
import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeName(value = "PropertyChange")
@JsonSubTypes({ @Type(value = PojoPropertyAliveSet.class), @Type(value = PojoPropertyCommited.class), @Type(value = PojoPropertyRollBack.class), @Type(value = PojoPropertyValueSet.class) })
public class PojoPropertyChange extends PojoChange {

  private PojoProperty property;
  private long origin;

  public PojoPropertyChange() {
  }

  public PojoPropertyChange(long origin, PojoProperty property) {
    this.property = property;
    this.origin = origin;
  }

  public PojoProperty getProperty() {
    return property;
  }

  public void setProperty(PojoProperty property) {
    this.property = property;
  }

  public long getOrigin() {
    return origin;
  }

  public void setOrigin(long origin) {
    this.origin = origin;
  }
}
