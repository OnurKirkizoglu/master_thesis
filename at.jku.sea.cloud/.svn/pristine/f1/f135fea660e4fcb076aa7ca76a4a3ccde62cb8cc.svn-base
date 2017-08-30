package at.jku.sea.cloud.javaobjectstorage;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Workspace;

public class ObjectSaver {
  public ObjectSaver() {
    this.knownObjects = new IdentityHashMap<Object, Artifact>();
  }

  public Artifact saveObject(final Object obj, final Workspace workspace) {
    if (obj == null) {
      return null;
    }

    Artifact artifact = this.knownObjects.get(obj);
    if (artifact != null) {
      return artifact;
    }

    final Class<?> type = obj.getClass();
    artifact = workspace.createArtifact();
    artifact.setType(workspace, this.getOrCreateType(type, workspace));
    this.knownObjects.put(obj, artifact);

    if (type.isArray()) {
      this.saveArray(obj, artifact, workspace);
    } else {
      if (this.isPrimitiveObject(obj)) {
        this.savePrimitiveObject(obj, artifact, workspace);
      } else {
        this.saveProperties(obj, artifact, type, workspace);
      }
    }

    return artifact;
  }

  private final Map<Object, Artifact> knownObjects;

  private void saveArray(final Object obj, final Artifact artifact, final Workspace workspace) {
    final Map<String, Object> properties = new HashMap<String, Object>();
    properties.put(Constants.ARRAYSIZE_NAME, Array.getLength(obj));
    for (int i = 0; i < Array.getLength(obj); i++) {
      properties.put(String.valueOf(i), this.getValue(Array.get(obj, i), workspace));
    }
    artifact.setPropertyValues(workspace, properties);
  }

  private boolean isPrimitiveObject(final Object value) {
    return (value instanceof Number) || (value instanceof Boolean) || (value instanceof Character) || (value instanceof String);
  }

  private void savePrimitiveObject(final Object obj, final Artifact artifact, final Workspace workspace) {
    artifact.setPropertyValue(workspace, Constants.PRIMITIVEVALUE_NAME, obj);
  }

  private void saveProperties(final Object obj, final Artifact artifact, final Class<?> type, final Workspace workspace) {
    final Map<String, Object> properties = new HashMap<String, Object>();
    for (final Field field : ReflectionUtils.getAllFields(type)) {
      final boolean accessible = field.isAccessible();
      field.setAccessible(true);
      try {
        properties.put(field.getName(), this.getValue(field.get(obj), workspace));
      } catch (IllegalArgumentException | IllegalAccessException e) {
        // TODO throw exception (should be dead code)
        e.printStackTrace();
      } finally {
        field.setAccessible(accessible);
      }
    }
    artifact.setPropertyValues(workspace, properties);
  }

  private Object getValue(final Object value, final Workspace workspace) {
    if (this.isPrimitiveObject(value)) {
      return value;
    } else {
      return this.saveObject(value, workspace);
    }
  }

  private Artifact getOrCreateType(final Class<?> type, final Workspace workspace) {
    Artifact artifact = null;
    for (final Artifact knownType : workspace.getMetaModel(DataStorage.JAVA_METAMODEL).getArtifacts()) {
      if (type.getCanonicalName().equals(knownType.getPropertyValue(DataStorage.PROPERTY_FULL_QUALIFIED_NAME))) {
        return knownType;
      }
    }
    if (artifact == null) {
      artifact = workspace.createArtifact(workspace.getArtifact(DataStorage.JAVA_OBJECT_TYPE));
      workspace.getMetaModel(DataStorage.JAVA_METAMODEL).addArtifact(workspace, artifact);
      artifact.setPropertyValue(workspace, DataStorage.PROPERTY_FULL_QUALIFIED_NAME, type.getName());
    }
    return artifact;
  }
}
