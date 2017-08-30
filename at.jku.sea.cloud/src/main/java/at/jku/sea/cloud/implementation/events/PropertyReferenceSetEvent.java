package at.jku.sea.cloud.implementation.events;

import java.io.Serializable;

public class PropertyReferenceSetEvent extends PropertyEvent implements Serializable {

  public final Long oldReferenceId, newReferenceId;


   PropertyReferenceSetEvent(final Long origin, final Long version, final long owner, final long tool, final long id, final String property, final Long oldReferenceId, final Long newReferenceId) {
    super(origin, version, owner, tool, id, property);
    this.oldReferenceId = oldReferenceId;
    this.newReferenceId = newReferenceId;
  }
}
