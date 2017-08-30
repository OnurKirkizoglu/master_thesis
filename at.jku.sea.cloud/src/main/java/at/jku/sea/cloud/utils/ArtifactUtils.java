package at.jku.sea.cloud.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;

public class ArtifactUtils {

  public static Package createPackage(final Workspace ws, final String name, final String fullName) {
    final Package pckg = ws.createPackage();
    pckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, name);
    pckg.setPropertyValue(ws, DataStorage.PROPERTY_FULL_QUALIFIED_NAME, fullName);
    return pckg;
  }

  public static CollectionArtifact createCollectionArtifact(final Workspace ws, final Collection<?> collection, final boolean onlyArtifacts) {
    return createCollectionArtifact(ws, collection, onlyArtifacts, null);
  }

  public static CollectionArtifact createCollectionArtifact(final Workspace ws, final Collection<?> collection, final boolean onlyArtifacts, Package pckg) {
    if (!onlyAllowedTypes(collection) || isTypeMix(collection)) {
      return null;
    }
    return createCollectionArtifact(ws, collection, new HashMap<String, Object>(), onlyArtifacts, pckg);
  }

  public static CollectionArtifact createCollectionArtifact(final Workspace ws, final Collection<?> collection, final Map<String, Object> properties, final boolean onlyArtifacts, Package pckg) {
    if (!onlyAllowedTypes(collection) || isTypeMix(collection)) {
      return null;
    }
    final CollectionArtifact aCollection = ws.createCollection(onlyArtifacts, pckg, collection, properties);
    return aCollection;
  }

  private static boolean onlyAllowedTypes(final Collection<?> collection) {
    for (final Object obj : collection) {
      if (!CloudUtils.isSupportedType(obj)) {
        return false;
      }
    }
    return true;
  }

  private static boolean isTypeMix(final Collection<?> collection) {
    boolean containsArtifact = false;
    boolean containsBasicType = false;
    for (final Object obj : collection) {
      if (obj instanceof Artifact) {
        containsArtifact = true;
      }
      if ((obj == null) || (obj instanceof Number) || (obj instanceof Character) || (obj instanceof String) || (obj instanceof Boolean)) {
        containsBasicType = true;
      }
    }
    return containsArtifact && containsBasicType;
  }

  public static Collection<Artifact> getArtifactsInWs(final Workspace ws, final Collection<Long> artifactIds) {
    final Collection<Artifact> artifacts = new ArrayList<Artifact>();
    for (final Long artifactId : artifactIds) {
      artifacts.add(ws.getArtifact(artifactId));
    }
    return artifacts;
  }

  public static Set<Artifact> getArtifactsInWs(final Workspace ws, final Set<Long> artifactIds) {
    final Set<Artifact> artifacts = new HashSet<Artifact>();
    for (final Long artifactId : artifactIds) {
      artifacts.add(ws.getArtifact(artifactId));
    }
    return artifacts;
  }

  public static Artifact getArtifact(final Workspace ws, final Long type) {
    return ws.getArtifact(type);
  }

  public static Artifact convertJavaObject(final Workspace workspace, final Object object, final Package package_) {
    final Class<?> clazz = object.getClass();
    Artifact type = null;
    for (final Artifact artifact : workspace.getMetaModel(DataStorage.JAVA_METAMODEL).getArtifacts()) {
      if (clazz.getCanonicalName().equals(artifact.getPropertyValue(DataStorage.PROPERTY_FULL_QUALIFIED_NAME))) {
        type = artifact;
        break;
      }
    }
    if (type == null) {
      type = workspace.createArtifact(workspace.getArtifact(DataStorage.JAVA_OBJECT_TYPE));
      workspace.getMetaModel(DataStorage.JAVA_METAMODEL).addArtifact(workspace, type);
      type.setPropertyValue(workspace, DataStorage.PROPERTY_FULL_QUALIFIED_NAME, clazz.getCanonicalName());
    }
    final Artifact artifact = workspace.createArtifact(type, package_);
    for (final Field field : getAllFields(new HashSet<Field>(), clazz)) {
      final boolean accessible = field.isAccessible();
      if (!accessible) {
        field.setAccessible(true);
      }
      try {
        Object value = field.get(object);
        if (!(value instanceof Serializable)) {
          value = convertJavaObject(workspace, value, package_);
        }
        artifact.setPropertyValue(workspace, field.getName(), value);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      } finally {
        field.setAccessible(accessible);
      }
    }
    return artifact;
  }

  public static Object getJavaObject(final Artifact artifact, final ClassLoader loader) throws ClassNotFoundException, PropertyDoesNotExistException, IllegalAccessException, InstantiationException {
    final Artifact type = artifact.getType();
    final Class<?> clazz = Class.forName((String) type.getPropertyValue(DataStorage.PROPERTY_FULL_QUALIFIED_NAME), true, loader);
    final Object object = clazz.newInstance();
    for (final Field field : getAllFields(new HashSet<Field>(), clazz)) {
      final boolean accessible = field.isAccessible();
      if (!accessible) {
        field.setAccessible(true);
      }
      try {
        Object value = artifact.getPropertyValue(field.getName());
        if (value instanceof Artifact) {
          value = getJavaObject(artifact, loader);
        }
        field.set(object, value);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      } finally {
        field.setAccessible(accessible);
      }
    }
    return object;
  }

  private static Set<Field> getAllFields(final Set<Field> fields, final Class<?> clazz) {
    if (clazz != null) {
      for (final Field field : clazz.getDeclaredFields()) {
        final int modifiers = field.getModifiers();
        if (!(Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isTransient(modifiers))) {
          fields.add(field);
        }
      }
      getAllFields(fields, clazz.getSuperclass());
    }
    return fields;
  }
}
