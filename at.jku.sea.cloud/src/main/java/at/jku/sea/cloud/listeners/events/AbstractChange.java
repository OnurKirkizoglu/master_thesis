package at.jku.sea.cloud.listeners.events;

import java.io.Serializable;

public abstract class AbstractChange implements Serializable, Change {
  private static final long serialVersionUID = 1L;
  protected ChangeType type;

  @Override
  public int compareTo(final Change other) {
    return this.type.getPriority() > other.getType().getPriority() ? 1 : this.type.getPriority() < other.getType().getPriority() ? -1 : 0;
  }

  @Override
  public ChangeType getType() {
    return this.type;
  }
}
