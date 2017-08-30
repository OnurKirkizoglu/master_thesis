package at.jku.sea.cloud.javaobjectstorage;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;

public class ObjectLoader {
  public ObjectLoader() {
    this.knownArtifacts = new HashMap<Artifact, Object>();
  }

  public Object loadObject(final Artifact artifact) throws ClassNotFoundException {
    if (artifact == null) {
      return null;
    }

    Object obj = this.knownArtifacts.get(artifact);
    if (obj != null) {
      return obj;
    }

    final Class<?> type = this.getType(artifact.getType());

    if (type.isArray()) {
      obj = Array.newInstance(type.getComponentType(), (int) artifact.getProperty(Constants.ARRAYSIZE_NAME).getValue());
      this.loadArray(artifact, obj);
      this.knownArtifacts.put(artifact, obj);
    } else {
      if (Number.class.equals(type.getSuperclass()) || Boolean.class.equals(type) || Character.class.equals(type) || String.class.equals(type)) {
        obj = this.loadPrimitiveObject(artifact, type);
        this.knownArtifacts.put(artifact, obj);
      } else {
        obj = SilentObjectCreator.create(type);
        this.knownArtifacts.put(artifact, obj);
        this.loadProperties(artifact, obj, type);
      }
    }

    return obj;
  }

  private final Map<Artifact, Object> knownArtifacts;

  private Object loadPrimitiveObject(final Artifact artifact, final Class<?> type) {
    Constructor<?> constructor;
    Object obj = null;
    try {
      if (Byte.class.equals(type)) {
        constructor = type.getConstructor(byte.class);
      } else if (Short.class.equals(type)) {
        constructor = type.getConstructor(short.class);
      } else if (Integer.class.equals(type)) {
        constructor = type.getConstructor(int.class);
      } else if (Long.class.equals(type)) {
        constructor = type.getConstructor(long.class);
      } else if (Float.class.equals(type)) {
        constructor = type.getConstructor(float.class);
      } else if (Double.class.equals(type)) {
        constructor = type.getConstructor(double.class);
      } else if (Boolean.class.equals(type)) {
        constructor = type.getConstructor(boolean.class);
      } else if (Character.class.equals(type)) {
        constructor = type.getConstructor(char.class);
      } else if (String.class.equals(type)) {
        constructor = type.getConstructor(String.class);
      } else {
        throw new UnsupportedOperationException("Unsupported type: " + type.getName()); //$NON-NLS-1$
      }

      obj = constructor.newInstance(artifact.getProperty(Constants.PRIMITIVEVALUE_NAME).getValue());
    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      // TODO throw exception (should be dead code)
      e.printStackTrace();
    }

    return obj;
  }

  private void loadArray(final Artifact artifact, final Object array) throws ClassNotFoundException {
    for (int i = 0; i < Array.getLength(array); i++) {
      final Object value = this.getValue(artifact.getProperty(String.valueOf(i)).getValue());
      Array.set(array, i, value);
    }
  }

  private void loadProperties(final Artifact artifact, final Object obj, final Class<?> type) throws ClassNotFoundException {
    for (final Field field : ReflectionUtils.getAllFields(type)) {
      final boolean accessible = field.isAccessible();
      field.setAccessible(true);
      try {
        final Object value = this.getValue(artifact.getProperty(field.getName()).getValue());
        field.set(obj, value);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        // TODO throw exception (should be dead code)
        e.printStackTrace();
      } finally {
        field.setAccessible(accessible);
      }
    }
  }

  private Object getValue(final Object value) throws ClassNotFoundException {
    if (value == null) {
      return null;
    }
    if ((value instanceof Number) || (value instanceof Boolean) || (value instanceof Character) || (value instanceof String)) {
      return value;
    }
    if (value instanceof Artifact) {
      return this.loadObject((Artifact) value);
    }

    throw new IllegalArgumentException("Can only load objects of the following types: Number, Boolean, Charachter, IArtifact"); //$NON-NLS-1$
  }

  private Class<?> getType(final Artifact artifact) throws ClassNotFoundException {
    return Class.forName((String) artifact.getPropertyValue(DataStorage.PROPERTY_FULL_QUALIFIED_NAME));
  }
}
