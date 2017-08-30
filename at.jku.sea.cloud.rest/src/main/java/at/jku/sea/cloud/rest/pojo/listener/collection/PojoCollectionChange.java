package at.jku.sea.cloud.rest.pojo.listener.collection;

import at.jku.sea.cloud.rest.pojo.PojoVersion;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;
import at.jku.sea.cloud.rest.pojo.listener.property.PojoPropertyMapSet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(value = "CollectionChange")
@JsonSubTypes({ @Type(value = PojoPropertyMapSet.class) })
public abstract class PojoCollectionChange<T> extends PojoChange {
  protected T[] changes;
  private long origin;

  public PojoCollectionChange() {
  }

  public PojoCollectionChange(long origin, T[] changes) {
    this.changes = changes;
    this.origin = origin;
  }

  public void setChanges(T[] changes) {
    this.changes = changes;
  }

  public T[] getChanges() {
    return changes;
  }

  public long getOrigin() {
    return origin;
  }

  public void setOrigin(long origin) {
    this.origin = origin;
  }
}
