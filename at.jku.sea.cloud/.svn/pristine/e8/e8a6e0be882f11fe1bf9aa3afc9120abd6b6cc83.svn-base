package at.jku.sea.cloud.implementation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Workspace;

public class TestWegerPerformance {
  public static void main(String[] args) {
    new TestWegerPerformance();
  }

  public static final int ITERATIONS = 10_000;

  final Cloud cloud;
  final Workspace ws;

  public TestWegerPerformance() {
    // this.cloud = RestCloud.getInstance();
    this.cloud = CloudFactory.getInstance();
    this.ws = cloud.createWorkspace(cloud.createOwner(), cloud.createTool(), "benchmark");
    /** Worst cases */
    // ws.rollbackAll();
    // testWorstCaseGetArtifactWithProperty();
    // ws.rollbackAll();
    // testWorstCaseGetAllProperties();
    // ws.rollbackAll();
    // testWorstCaseGetArtifacts();
    // testWorstCaseGetElements();
    // ws.rollbackAll();

    /** Normal cases */
    // ws.rollbackAll();
    // testWorkspaceTimings();
    // ws.rollbackAll();
    // testArtifactTimings();
    // ws.rollbackAll();
    // testPropertyTimings();
    // ws.rollbackAll();
    testCollectionArtifactTimings();
    ws.rollbackAll();
    // testMapArtifactTimings();
    // ws.rollbackAll();
    // testMapArtifactEntryTimings();
    // ws.rollbackAll();

  }

  /**
   * WORST 4 tests:
   */

  public void testWorstCaseGetArtifactWithProperty() {
    /**
     * WORKSPACE: getArtifactsWithProperty(String, Object, Artifact...)
     */
    final Artifact type = ws.createArtifact();
    final String property = "property";
    final Artifact value = ws.createArtifact();
    final Artifact zeroValue = ws.createArtifact();
    System.out.println("WORKSPACE: getArtifactsWithProperty(String, Object, Artifact...)");
    for (int i = 1; i < 1_000_000; i *= 10) {
      for (int j = 0; j < i; j++) {
        Artifact artifact = ws.createArtifact(type);
        artifact.setPropertyValue(ws, property, j == 0 ? zeroValue : value);
      }
      System.out.println(i + " elements: " + benchmarkMethod(new Method() {
        @Override
        public void doMethod() {
          ws.getArtifactsWithProperty(property, zeroValue, type);
        }
      }, 100));
    }
  }

  public void testWorstCaseGetAllProperties() {
    /**
     * ARTIFACT: getAllProperties()
     */
    final Artifact type = ws.createArtifact();
    final Artifact artifact = ws.createArtifact(type);
    final String property = "property";
    System.out.println("ARTIFACT: getAllProperties()");
    for (int i = 100_000, cnt = 0; i < 1_000_000; i *= 10) {
      Map<String, Object> properties = new HashMap<>();
      for (int j = 0; j < i; j++) {
        properties.put(property + cnt++, j);
      }
      artifact.setPropertyValues(ws, properties);
      System.out.println(i + " elements: " + benchmarkMethod(new Method() {
        @Override
        public void doMethod() {
          artifact.getAllProperties();
        }
      }, 50));
    }
  }

  public void testWorstCaseGetArtifacts() {
    /**
     * WORKSPACE: getArtifacts(Artifact...)
     */
    System.out.println("WORKSPACE: getArtifacts(Artifact...)");
    for (int i = 1; i < 1_000_000; i *= 10) {
      final Artifact type = ws.createArtifact();
      for (int j = 0; j < i; j++) {
        ws.createArtifact(type);
      }
      System.out.println(i + " elements: " + benchmarkMethod(new Method() {
        @Override
        public void doMethod() {
          ws.getArtifacts(type);
        }
      }, 100));
    }
  }

  public void testWorstCaseGetElements() {
    /**
     * COLLECTIONARTIFACT: getElements()
     */
    System.out.println("COLLECTIONARTIFACT: getElements()");
    for (int i = 1; i < 1_000_000; i *= 10) {
      final CollectionArtifact col = ws.createCollection(false);
      for (int j = 0; j < i; j++) {
        col.addElement(ws, /* ws.createArtifact() */j);
      }

      long start = System.nanoTime();
      for (int s = 0; s < 1; s++) {
        // m.doMethod();
        col.getElements();
      }
      long duration = System.nanoTime() - start;
      System.out.println(i + " elements: " + (duration / Math.pow(10, 6)));
    }
  }

  /**
   * WORKSPACE
   */

  public void testWorkspaceTimings() {
    System.out.println("WORKSPACE");
    /** Workspace: getArtifacts() */
    System.out.println("getArtifacts(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.getArtifacts();
      }
    }, ITERATIONS));

    /** Workspace: createArtifact() */
    System.out.println("createArtifact: " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.createArtifact();
      }
    }, ITERATIONS));

    /** Workspace: createArtifact(Artifact) */
    final Artifact type = ws.createArtifact();
    System.out.println("createArtifact(Artifact): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.createArtifact(type);
      }
    }, ITERATIONS));

    /** Workspace: createCollection(boolean) */
    System.out.println("createCollection(boolean): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.createCollection(true);
      }
    }, ITERATIONS));

    /** Workspace: createMap() */
    System.out.println("createMap(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.createMap();
      }
    }, ITERATIONS));

    /** Workspace: createMetaModel() */
    System.out.println("createMetaModel(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.createMetaModel();
      }
    }, ITERATIONS));

    /** Workspace: createPackage() */
    System.out.println("createPackage(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.createPackage();
      }
    }, ITERATIONS));

    /** Workspace: createProject() */
    System.out.println("createProject(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.createProject();
      }
    }, ITERATIONS));

    /** Workspace: getArtifact(long) */
    final long artifactId = ws.createArtifact().getId();
    System.out.println("getArtifact(long): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.getArtifact(artifactId);
      }
    }, ITERATIONS));

    /** Workspace: getArtifacts(Artifact...) */
    final List<Artifact> artifacts = new ArrayList<>(1_000);
    for (int i = 0; i < 100; i++) {
      artifacts.add(ws.createArtifact(type));
    }
    System.out.println("getArtifacts(Artifact...): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.getArtifacts(type);
      }
    }, 1_000));

    /** Workspace: getArtifactsWithProperty(String, Object, Artifact...) */
    final String propertyKey = "Property";
    final Artifact propertyValue = ws.createArtifact();
    for (int i = 0; i < artifacts.size() >> 1; i++) {
      artifacts.get(i).setPropertyValue(ws, propertyKey, propertyValue);
    }
    System.out.println("getArtifactsWithProperty(String, Object, Artifact...): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        ws.getArtifactsWithProperty(propertyKey, propertyValue, type);
      }
    }, 1_000));
  }

  /**
   * ARTIFACT
   */

  public void testArtifactTimings() {
    System.out.println("ARTIFACT");
    final Artifact type = ws.createArtifact();
    final Artifact artifact = ws.createArtifact(type);

    /** Artifact: addToProject(Workspace, Project) */
    final Project project = ws.createProject();
    System.out.println("addToProject(Workspace, Project): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        artifact.addToProject(ws, project);
      }
    }, ITERATIONS));

    /** Artifact: createProperty(Workspace, String) */
    final String propertyName = "Property";
    System.out.println("createProperty(Workspace, String): " + benchmarkMethod(new Method() {
      private long i = 0L;

      @Override
      public void doMethod() {
        artifact.createProperty(ws, propertyName + "" + i++);
      }
    }, ITERATIONS));

    /** Artifact: delete(Workspace) */
    final Queue<Artifact> queue = new ArrayDeque<Artifact>(ITERATIONS);
    for (int i = 0; i < ITERATIONS; i++) {
      queue.offer(ws.createArtifact());
    }
    System.out.println("delete(Workspace): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        if (queue.isEmpty()) {
          throw new IllegalArgumentException();
        } else {
          queue.poll().delete(ws);
        }
      }
    }, ITERATIONS));

    /** Artifact: deleteProperty(Workspace, String) */
    for (int i = 0; i < ITERATIONS; i++) {
      final Artifact aArtifact = ws.createArtifact();
      aArtifact.createProperty(ws, propertyName);
      queue.offer(aArtifact);
    }
    System.out.println("deleteProperty(Workspace, String): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        if (queue.isEmpty()) {
          throw new IllegalArgumentException();
        } else {
          queue.poll().deleteProperty(ws, propertyName);
        }
      }
    }, ITERATIONS));

    /** Artifact: getAliveProperties() */
    final List<Property> properties = new ArrayList<Property>(artifact.getAllProperties());
    for (int i = 0; i < properties.size() >> 1; i++) {
      properties.get(i).delete(ws);
    }
    System.out.println("getAliveProperties(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        artifact.getAliveProperties();
      }
    }, 1_000));

    /** Artifact: getAllProperties() */
    System.out.println("getAllProperties(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        artifact.getAllProperties();
      }
    }, 500));

    /** Artifact: getPackage() */
    final Package pkg = ws.createPackage();
    artifact.setPackage(ws, pkg);
    System.out.println("getPackage(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        artifact.getPackage();
      }
    }, ITERATIONS));

    /** Artifact: getType() */
    System.out.println("getType(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        artifact.getType();
      }
    }, ITERATIONS));

    /** Artifact: setPropertyValue(Workspace, String, Object) */
    final String propertyNameSet = "PerfTest";
    artifact.createProperty(ws, propertyNameSet);
    System.out.println("setPropertyValue(Workspace, String, Object): " + benchmarkMethod(new Method() {
      private long i = 0;

      @Override
      public void doMethod() {
        artifact.setPropertyValue(ws, propertyNameSet, i++);
      }
    }, ITERATIONS));
  }

  /**
   * PROPERTY
   */

  public void testPropertyTimings() {
    System.out.println("PROPERTY");
    final Artifact artifact = ws.createArtifact();
    final String propertyName = "Property";
    final Long propertyValue = 10L;
    final Property property = artifact.createProperty(ws, propertyName);
    property.setValue(ws, propertyValue);

    /** Property: getArtifact() */
    System.out.println("getArtifact(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        property.getArtifact();
      }
    }, ITERATIONS));

    /** Property: getName() */
    System.out.println("getName(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        property.getName();
      }
    }, ITERATIONS));

    /** Property: getValue() */
    System.out.println("getValue(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        property.getValue();
      }
    }, ITERATIONS));

    /** Property: isReference() */
    System.out.println("isReference(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        property.isReference();
      }
    }, ITERATIONS));
  }

  /**
   * COLLECTIONARTIFACT
   */

  public void testCollectionArtifactTimings() {
    System.out.println("COLLECTIONARTIFACT");
    final CollectionArtifact collectionArtifact = ws.createCollection(false);

    /** CollectionArtifact: addElement(Workspace, Object) */
    System.out.println("addElement(Workspace, Object): " + benchmarkMethod(new Method() {

      private long i = 0;

      @Override
      public void doMethod() {
        collectionArtifact.addElement(ws, i++);
      }
    }, ITERATIONS)/ Math.pow(10, 6));

    /** CollectionArtifact: existsElement(Object) */
    System.out.println("existsElement(Object): " + benchmarkMethod(new Method() {
      private long i = 0;

      @Override
      public void doMethod() {
        collectionArtifact.existsElement(i++);
      }
    }, 1_000) / Math.pow(10, 6));

    /** CollectionArtifact: getElementAt(long) */
    System.out.println("getElementAt(long): " + benchmarkMethod(new Method() {
      private long i = 0;

      @Override
      public void doMethod() {
        collectionArtifact.getElementAt(i++);
      }
    }, ITERATIONS) / Math.pow(10, 6));

    /** CollectionArtifact: getElements() */
    System.out.println("getElements(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        collectionArtifact.getElements();
      }
    }, 1_000) / Math.pow(10, 6));

    /** CollectionArtifact: isContainingOnlyArtifacts() */
    System.out.println("isContainingOnlyArtifacts(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        collectionArtifact.isContainingOnlyArtifacts();
      }
    }, ITERATIONS) / Math.pow(10, 6));

    /** CollectionArtifact: removeElement(Workspace, Object) */
    System.out.println("removeElement(Workspace, Object): " + benchmarkMethod(new Method() {
      private long i = 0;

      @Override
      public void doMethod() {
        collectionArtifact.removeElement(ws, i++);
      }
    }, ITERATIONS));

    /** CollectionArtifact: removeElementAt(Workspace, long) */
    for (int i = 0; i < ITERATIONS; i++) {
      collectionArtifact.addElement(ws, i);
    }
    System.out.println("removeElementAt(Workspace, long): " + benchmarkMethod(new Method() {
      private long i = collectionArtifact.size();

      @Override
      public void doMethod() {
        collectionArtifact.removeElementAt(ws, --i);
      }
    }, ITERATIONS) / Math.pow(10, 6));

    /** CollectionArtifact: size() */
    System.out.println("size(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        collectionArtifact.size();
      }
    }, ITERATIONS)/ Math.pow(10, 6));

    /** CollectionArtifact: insertElementAt(Workspace, Object, long) */
    collectionArtifact.addElement(ws, 1L);
    collectionArtifact.addElement(ws, 1L);
    System.out.println("insertElementAt(Workspace, Object, long): " + benchmarkMethod(new Method() {
      private long i = 2;

      @Override
      public void doMethod() {
        collectionArtifact.insertElementAt(ws, i++, 1);
      }
    }, ITERATIONS) / Math.pow(10, 6));
  }

  /**
   * MAPARTIFACT
   */

  public void testMapArtifactTimings() {
    System.out.println("MAPARTIFACT");
    final MapArtifact mapArtifact = ws.createMap();

    /** MapArtifact: clear(Workspace) */
    final Queue<MapArtifact> maps = new ArrayDeque<MapArtifact>(ITERATIONS);
    for (int i = 0; i < 500; i++) {
      MapArtifact ma = ws.createMap();
      for (int j = 0; j < 100; j++) {
        ma.put(ws, j, j);
      }
      maps.offer(ma);
    }
    System.out.println("clear(Workspace): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        if (maps.isEmpty()) {
          throw new IllegalArgumentException();
        } else {
          maps.poll().clear(ws);
        }
      }
    }, 500));

    /** MapArtifact: containsKey(Object) */
    for (int i = 0; i < 100; i++) {
      mapArtifact.put(ws, i, i);
    }
    System.out.println("containsKey(Object): " + benchmarkMethod(new Method() {
      private int i = 0;

      @Override
      public void doMethod() {
        mapArtifact.containsKey(i++);
      }
    }, 500));

    /** MapArtifact: containsValue(Object) */
    System.out.println("containsValue(Object): " + benchmarkMethod(new Method() {
      private int i = 0;

      @Override
      public void doMethod() {
        mapArtifact.containsValue(i);
      }
    }, 500));

    /** MapArtifact: entrySet() */
    System.out.println("entrySet(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        mapArtifact.entrySet();
      }
    }, 500));

    /** MapArtifact: get(Object) */
    System.out.println("get(Object): " + benchmarkMethod(new Method() {
      private int i = 0;

      @Override
      public void doMethod() {
        mapArtifact.get(i);
      }
    }, 500));

    /** MapArtifact: keySet() */
    System.out.println("keySet(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        mapArtifact.keySet();
      }
    }, 500));

    /** MapArtifact: put(Workspace, Object, Object) */
    System.out.println("put(Workspace, Object, Object): " + benchmarkMethod(new Method() {
      private int i = ITERATIONS;

      @Override
      public void doMethod() {
        mapArtifact.put(ws, i, i++);
      }
    }, 500));

    /** MapArtifact: remove(Workspace, Object) */
    System.out.println("remove(Workspace, Object): " + benchmarkMethod(new Method() {
      private int i = ITERATIONS;

      @Override
      public void doMethod() {
        mapArtifact.remove(ws, i++);
      }
    }, 500));

    /** MapArtifact: size() */
    System.out.println("size(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        mapArtifact.size();
      }
    }, 500));

    /** MapArtifact: values() */
    System.out.println("values(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        mapArtifact.values();
      }
    }, 500));
  }

  /**
   * MapArtifactEntry
   */

  public void testMapArtifactEntryTimings() {
    System.out.println("MAPARTIFACTENTRY");
    final MapArtifact mapArtifact = ws.createMap();
    mapArtifact.put(ws, 1, 1);
    final MapArtifact.Entry entry = mapArtifact.entrySet().iterator().next();

    /** MapEntry: getKey() */
    System.out.println("getKey(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        entry.getKey();
      }
    }, 500));

    /** MapEntry: getValue() */
    System.out.println("getValue(): " + benchmarkMethod(new Method() {
      @Override
      public void doMethod() {
        entry.getValue();
      }
    }, 500));

    /** MapEntry: setValue(Workspace, Object) */
    System.out.println("setValue(Workspace, Object): " + benchmarkMethod(new Method() {
      private int i = 0;

      @Override
      public void doMethod() {
        entry.setValue(ws, i++);
      }
    }, 500));
  }

  public long benchmarkMethod(Method m, int iterations) {
    long start = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      m.doMethod();
    }
    long duration = System.nanoTime() - start;
    return duration / iterations;
  }

  interface Method {
    void doMethod();
  }
}
