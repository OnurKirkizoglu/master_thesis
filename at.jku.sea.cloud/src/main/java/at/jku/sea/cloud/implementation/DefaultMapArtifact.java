package at.jku.sea.cloud.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.utils.CloudUtils;

public final class DefaultMapArtifact extends DefaultArtifact implements MapArtifact {
  private static final long serialVersionUID = 1L;

  private final CollectionArtifact table;

  public DefaultMapArtifact(final DataStorage dataStorage, final long id, final long version) {
    super(dataStorage, id, version);
    final Long collectionId = dataStorage.getPropertyReference(version, id, DataStorage.PROPERTY_MAP_COLLECTION);
    this.table = (CollectionArtifact) ArtifactFactory.getArtifact(dataStorage, version, collectionId);
  }

  @Override
  public long size() {
    return this.table.size();
  }

  @Override
  public boolean containsKey(final Object key) {
    for (final MapArtifact.Entry entry : this.entrySet()) {
      final Object entryKey = entry.getKey();
      if (entryKey == null ? entryKey == key : entryKey.equals(key)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsValue(final Object value) {
    for (final MapArtifact.Entry entry : this.entrySet()) {
      final Object entryValue = entry.getValue();
      if (entryValue == null ? entryValue == value : entryValue.equals(value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Object get(final Object key) {
    for (final MapArtifact.Entry entry : this.entrySet()) {
      final Object entryKey = entry.getKey();
      if (entryKey == null ? entryKey == key : entryKey.equals(key)) {
        return entry.getValue();
      }
    }
    return null;
  }

  @Override
  public Object put(final Workspace workspace, final Object key, final Object value) {
    if (!CloudUtils.isSupportedType(key)) {
      throw new TypeNotSupportedException(key.getClass());
    }
    if (!CloudUtils.isSupportedType(value)) {
      throw new TypeNotSupportedException(value.getClass());
    }
    Object oldValue = null;

    for (final MapArtifact.Entry entry : this.entrySet()) {
      final Object entryKey = entry.getKey();
      if (entryKey == null ? entryKey == key : entryKey.equals(key)) {
        oldValue = entry.getValue();
        break;
      }
    }
    boolean isKeyReference = key instanceof Artifact;
    boolean isValueReference = value instanceof Artifact;
    dataStorage.putInMap(workspace.getVersionNumber(), id, workspace.getOwner().getId(), workspace.getTool().getId(), DefaultEntry.KEY, isKeyReference ? ((Artifact) key).getId() : key,
        isKeyReference, DefaultEntry.VALUE, isValueReference ? ((Artifact) value).getId() : value, isValueReference);
    return oldValue;
  }

  @Override
  public Object remove(final Workspace workspace, final Object key) {
    for (final MapArtifact.Entry entry : this.entrySet()) {
      final Object entryKey = entry.getKey();
      if (entryKey == null ? entryKey == key : entryKey.equals(key)) {
        final Object value = entry.getValue();
        boolean isKeyReference = key instanceof Artifact;
        dataStorage.removeFromMap(workspace.getVersionNumber(), id, workspace.getOwner().getId(), workspace.getTool().getId(), DefaultEntry.KEY, isKeyReference ? ((Artifact) key).getId() : key,
            isKeyReference, DefaultEntry.VALUE);
        return value;
      }
    }
    return null;
  }

  @Override
  public void clear(final Workspace workspace) {
    dataStorage.clearMap(workspace.getVersionNumber(), id, workspace.getOwner().getId(), workspace.getTool().getId());
  }

  @Override
  public Set<Object> keySet() {
    final Set<Object> result = new HashSet<Object>();
    for (final MapArtifact.Entry entry : this.entrySet()) {
      result.add(entry.getKey());
    }
    return Collections.unmodifiableSet(result);
  }

  @Override
  public Collection<Object> values() {
    final Collection<Object> result = new ArrayList<Object>();
    for (final MapArtifact.Entry entry : this.entrySet()) {
      result.add(entry.getValue());
    }
    return Collections.unmodifiableCollection(result);
  }

  @Override
  public Set<MapArtifact.Entry> entrySet() {
    final Set<MapArtifact.Entry> result = new HashSet<MapArtifact.Entry>();
    @SuppressWarnings("unchecked")
    final Collection<Artifact> elements = (Collection<Artifact>) this.table.getElements();
    for (final Artifact artifact : elements) {
      final DefaultEntry entry = new DefaultEntry(this.dataStorage, artifact.getId(), artifact.getVersionNumber());
      result.add(entry);
    }
    return Collections.unmodifiableSet(result);
  }

  @Override
  public void delete(final Workspace workspace) {
    for (final MapArtifact.Entry entry : this.entrySet()) {
      final DefaultMapArtifact.DefaultEntry dentry = (DefaultMapArtifact.DefaultEntry) entry;
      dentry.delete(workspace);
    }
    this.table.delete(workspace);
    super.delete(workspace);
  }

  @Override
  public void undelete(final Workspace workspace) {
    super.undelete(workspace);
    this.table.undelete(workspace);
    for (final MapArtifact.Entry entry : this.entrySet()) {
      final DefaultMapArtifact.DefaultEntry dentry = (DefaultMapArtifact.DefaultEntry) entry;
      dentry.undelete(workspace);
    }
  }

  public final class DefaultEntry extends DefaultArtifact implements MapArtifact.Entry {
    private static final long serialVersionUID = 1L;

    public static final String KEY = "key";
    public static final String VALUE = "value";

    DefaultEntry(final DataStorage dataStorage, final long id, final long version) {
      super(dataStorage, id, version);
    }

    @Override
    public Object getKey() {
      return this.getPropertyValue(DefaultEntry.KEY);
    }

    @Override
    public Object getValue() {
      return this.getPropertyValue(DefaultEntry.VALUE);
    }

    @Override
    public Object setValue(final Workspace workspace, final Object newValue) {
      if (!CloudUtils.isSupportedType(newValue)) {
        throw new TypeNotSupportedException(newValue.getClass());
      }
      final Object oldValue = this.getValue();
      this.setPropertyValue(workspace, DefaultEntry.VALUE, newValue);
      return oldValue;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (this.getClass() != obj.getClass()) {
        return false;
      }
      final DefaultEntry other = (DefaultEntry) obj;

      return ((Artifact) this).equals((Artifact) other);
    }

    @Override
    public String toString() {
      return "[" + this.getKey() + ": " + this.getValue() + "]";
    }
  }
}
