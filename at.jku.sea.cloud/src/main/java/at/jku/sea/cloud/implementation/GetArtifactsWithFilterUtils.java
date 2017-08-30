package at.jku.sea.cloud.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.DataStorage.Columns;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;

public class GetArtifactsWithFilterUtils {

  static Collection<Artifact> getArtifactsWithProperty(final DataStorage dataStorage, final long version, final Map<String, Object> propertyToValue, final boolean alive, final Artifact... filters) {
    final Iterator<Entry<String, Object>> iterator = propertyToValue.entrySet().iterator();
    if (!iterator.hasNext()) {
      return Collections.emptySet();
    }
    final Entry<String, Object> entry = iterator.next();
    final Collection<Artifact> result = new HashSet<Artifact>(getArtifactsWithProperty(dataStorage, version, entry.getKey(), entry.getValue(), alive, filters));
    while (iterator.hasNext()) {
      final Entry<String, Object> next = iterator.next();
      final String key = next.getKey();
      final Object value = next.getValue();
      final Collection<Artifact> artifacts = getArtifactsWithProperty(dataStorage, version, key, value, alive, filters);
      result.retainAll(artifacts);
    }
    return result;
  }

  static Collection<Artifact> getArtifactsWithProperty(final DataStorage dataStorage, final long version, final String propertyName, final Object propertyValue, final boolean alive,
      final Artifact... filters) {
    Collection<Object[]> resultIds = getArtifactIdsWithProperty(dataStorage, version, propertyName, propertyValue, alive, filters);
    final Collection<Artifact> result = new ArrayList<Artifact>(resultIds.size());
    for (final Object[] id : resultIds) {
      result.add(ArtifactFactory.getArtifact(dataStorage, version, id));
    }
    return result;
  }

  private static Collection<Object[]> getArtifactIdsWithProperty(final DataStorage dataStorage, final long version, final String propertyName, final Object propertyValue, final boolean alive,
      final Artifact... filters) {
    Long containerId = null;
    Long typeId = null;
    Long ownerId = null;
    Long toolId = null;
    Set<Long> projectIds = null;
    Set<Long> metaModelIds = null;
    for (final Artifact artifact : filters) {
      if (artifact == null) {
        continue;
      }
      if (artifact instanceof Project) {
        if (projectIds != null) {
          throw new UnsupportedOperationException("can only filter one Project at a time so far");
        }
        projectIds = new HashSet<Long>();
        projectIds.addAll((Collection<? extends Long>) dataStorage.getElementsFromCollection(artifact.getVersionNumber(), artifact.getId()));
// final Set<Long> checkedIds = new HashSet<Long>();
// final Set<Long> additionalIds = new HashSet<Long>();
// boolean continueLoop = false;
// do {
// additionalIds.clear();
// for (final Long id : projectIds) {
// if (!checkedIds.contains(id) && (this.dataStorage.getArtifactType(artifact.getVersionNumber(), id) == DataStorage.PACKAGE_TYPE_ID)) {
// additionalIds.addAll(ObjectArrayRepresentationUtils.getArtifactIds(this.dataStorage.getArtifactsByPackage(artifact.getVersionNumber(), id)));
// }
// checkedIds.add(id);
// }
// continueLoop = !additionalIds.isEmpty();
// projectIds.addAll(additionalIds);
// } while (continueLoop);
      } else if (artifact instanceof MetaModel) {
        if (metaModelIds != null) {
          throw new UnsupportedOperationException("can only filter one MetaModel at a time so far");
        }
        metaModelIds = new HashSet<Long>();
        metaModelIds.addAll((Collection<? extends Long>) dataStorage.getElementsFromCollection(artifact.getVersionNumber(), artifact.getId()));
// final Set<Long> checkedIds = new HashSet<Long>();
// final Set<Long> additionalIds = new HashSet<Long>();
// boolean continueLoop = false;
// do {
// additionalIds.clear();
// for (final Long id : metaModelIds) {
// if (!checkedIds.contains(id) && (this.dataStorage.getArtifactType(artifact.getVersionNumber(), id) == DataStorage.PACKAGE_TYPE_ID)) {
// additionalIds.addAll(ObjectArrayRepresentationUtils.getArtifactIds(this.dataStorage.getArtifactsByPackage(artifact.getVersionNumber(), id)));
// }
// checkedIds.add(id);
// }
// continueLoop = !additionalIds.isEmpty();
// metaModelIds.addAll(additionalIds);
// } while (continueLoop);
      } else if (artifact instanceof Container) {
        if (containerId != null) {
          throw new UnsupportedOperationException("can only filter one Package at a time so far");
        }
        containerId = artifact.getId();
      } else if (artifact instanceof Owner) {
        if (ownerId != null) {
          throw new UnsupportedOperationException("can only filter one Owner at a time so far");
        }
        ownerId = artifact.getId();
      } else if (artifact instanceof Tool) {
        if (toolId != null) {
          throw new UnsupportedOperationException("can only filter one Tool at a time so far");
        }
        toolId = artifact.getId();
      } else {
        if (typeId != null) {
          throw new UnsupportedOperationException("can only filter one Type at a time so far");
        }
        typeId = artifact.getId();
      }
    }
    final Collection<Object[]> ids;
    if ((propertyName == null) && (propertyValue == null)) {
      ids = dataStorage.getArtifacts(version, alive, toolId, ownerId, typeId, containerId);
    } else {
      if ((filters.length == 0) && (propertyValue == null)) {
        ids = dataStorage.getArtifactsWithProperty(version, alive, propertyName);
      } else {
        if (propertyValue instanceof Artifact) {
          ids = dataStorage.getArtifacts(version, alive, toolId, ownerId, typeId, containerId, propertyName, ((Artifact) propertyValue).getId(), true);
        } else {
          ids = dataStorage.getArtifacts(version, alive, toolId, ownerId, typeId, containerId, propertyName, propertyValue, false);
        }
      }
    }
    final Set<Object[]> resultIds = new HashSet<>(ids);

    if (projectIds != null) {
      ObjectArrayRepresentationUtils.retainAll(resultIds, projectIds);// TODO
    }
    if (metaModelIds != null) {
      ObjectArrayRepresentationUtils.retainAll(resultIds, metaModelIds);// TODO
    }
    return resultIds;

  }

  static Collection<Artifact> getArtifactsWithReference(final DataStorage dataStorage, long version, Artifact artifact, Artifact... filters) {
    Map<Long, Set<String>> aidToProperties = dataStorage.getPropertiesByReference(version, artifact.getId());
    Set<Long> artifactIds = aidToProperties.keySet();
    Set<Long> projectIds = null;
    Set<Long> metaModelIds = null;
    Long containerId = null;
    Long typeId = null;
    Long ownerId = null;
    Long toolId = null;
    for (final Artifact filter : filters) {
      if (filter == null) {
        continue;
      }
      if (filter instanceof Project) {
        if (projectIds != null) {
          throw new UnsupportedOperationException("can only filter one Project at a time so far");
        }
        projectIds = new HashSet<Long>();
        projectIds.addAll((Collection<? extends Long>) dataStorage.getElementsFromCollection(filter.getVersionNumber(), filter.getId()));
      } else if (filter instanceof MetaModel) {
        if (metaModelIds != null) {
          throw new UnsupportedOperationException("can only filter one MetaModel at a time so far");
        }
        metaModelIds = new HashSet<Long>();
        metaModelIds.addAll((Collection<? extends Long>) dataStorage.getElementsFromCollection(filter.getVersionNumber(), filter.getId()));
      } else if (filter instanceof Container) {
        if (containerId != null) {
          throw new UnsupportedOperationException("can only filter one Package at a time so far");
        }
        containerId = filter.getId();
      } else if (filter instanceof Owner) {
        if (ownerId != null) {
          throw new UnsupportedOperationException("can only filter one Owner at a time so far");
        }
        ownerId = filter.getId();
      } else if (filter instanceof Tool) {
        if (toolId != null) {
          throw new UnsupportedOperationException("can only filter one Tool at a time so far");
        }
        toolId = filter.getId();
      } else {
        if (typeId != null) {
          throw new UnsupportedOperationException("can only filter one Type at a time so far");
        }
        typeId = filter.getId();
      }
    }

    Set<Object[]> artifacts = dataStorage.getArtifacts(version, true, toolId, ownerId, typeId, containerId);
    if (artifactIds.size() > 0) {
      ObjectArrayRepresentationUtils.retainAll(artifacts, artifactIds);// TODO
    }
    if (projectIds != null) {
      ObjectArrayRepresentationUtils.retainAll(artifacts, projectIds);// TODO
    }
    if (metaModelIds != null) {
      ObjectArrayRepresentationUtils.retainAll(artifacts, metaModelIds);// TODO
    }
    Collection<Artifact> result = new ArrayList<>();
    for (final Object[] id : artifacts) {
      result.add(ArtifactFactory.getArtifact(dataStorage, version, id));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  static <T extends Artifact> Collection<T> getArtifactsWithProperty(final DataStorage dataStorage, Class<T> cls, final long version, final String propertyName, final Object propertyValue,
      final boolean alive, final Artifact... filters) {
    Collection<Object[]> resultIds = getArtifactIdsWithProperty(dataStorage, version, propertyName, propertyValue, alive, filters);
    final Collection<T> result = new ArrayList<>(resultIds.size());
    for (final Object[] id : resultIds) {
      result.add((T) ArtifactFactory.getArtifact(dataStorage, version, id));
    }
    return result;
  }

  static Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(DataStorage dataStorage, long version, String propertyName, Object propertyValue, boolean alive, Artifact... filters) {
    Collection<Artifact> artifacts = getArtifactsWithProperty(dataStorage, version, propertyName, propertyValue, alive, filters);
    return createArtifactAndStringObjectMap(dataStorage, version, artifacts);
  }

  static Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(DataStorage dataStorage, long version, Map<String, Object> propertyToValue, boolean alive, Artifact... filters) {
    Collection<Artifact> artifacts = getArtifactsWithProperty(dataStorage, version, propertyToValue, alive, filters);
    return createArtifactAndStringObjectMap(dataStorage, version, artifacts);
  }

  static Map<Artifact, Set<Property>> createArtifactAndPropertyMap(DataStorage dataStorage, long version, Collection<Artifact> artifacts) {
    Map<Artifact, Set<Property>> map = new HashMap<>();
    for (Artifact artifact : artifacts) {
      Collection<String> properties = dataStorage.getAllPropertyNames(version, artifact.getId());
      Set<Property> props = new HashSet<>();
      for (String property : properties) {
        props.add(new DefaultProperty(dataStorage, artifact.getId(), version, property));
      }
      map.put(artifact, props);
    }
    return map;
  }

  static Map<Artifact, Map<String, Object>> createArtifactAndStringObjectMap(DataStorage dataStorage, long version, Collection<Artifact> artifacts) {
    Map<Artifact, Map<String, Object>> map = new HashMap<Artifact, Map<String, Object>>();
    for (Artifact artifact : artifacts) {
      Collection<Object[]> properties = dataStorage.getAllPropertyRepresentations(version, null, artifact.getId(), true);
      Map<String, Object> values = new HashMap<>();
      for (Object[] property : properties) {
        String name = (String) property[Columns.PROPERTY_NAME.getIndex()];
        Object value = property[Columns.PROPERTY_VALUE.getIndex()];
        Long reference = (Long) property[Columns.PROPERTY_REFERENCE.getIndex()];
        if (value != null || (value == null && reference == null)) {
          values.put(name, value);
        } else {
          values.put(name, ArtifactFactory.getArtifact(dataStorage, artifact.getVersionNumber(), reference));
        }
      }
      map.put(artifact, values);
    }
    return map;
  }

  static Map<Object[], Set<Object[]>> createArtifactAndPropertyObjectMap(DataStorage dataStorage, long version, Collection<Object[]> artifacts) {
    Map<Object[], Set<Object[]>> map = new HashMap<>();
    for (Object[] artifact : artifacts) {
      Collection<Object[]> representations = dataStorage.getAllPropertyRepresentations(version, null, (Long) artifact[DataStorage.Columns.ARTIFACT_ID.getIndex()], true);
      map.put(artifact, new HashSet<>(representations));
    }
    return map;
  }
}
