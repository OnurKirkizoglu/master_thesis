package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;
import java.util.Set;

public class PropertyMapSetEvent implements Serializable {
  public final Set<PropertyValueSetEvent> valueSet;
  public final Set<PropertyReferenceSetEvent> referenceSet;

  PropertyMapSetEvent(final Set<PropertyValueSetEvent> valueSet, final Set<PropertyReferenceSetEvent> referenceSet) {
    this.valueSet = valueSet;
    this.referenceSet = referenceSet;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((referenceSet == null) ? 0 : referenceSet.hashCode());
    result = prime * result + ((valueSet == null) ? 0 : valueSet.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PropertyMapSetEvent other = (PropertyMapSetEvent) obj;
    if (referenceSet == null) {
      if (other.referenceSet != null)
        return false;
    } else if (!referenceSet.equals(other.referenceSet))
      return false;
    if (valueSet == null) {
      if (other.valueSet != null)
        return false;
    } else if (!valueSet.equals(other.valueSet))
      return false;
    return true;
  }

}
