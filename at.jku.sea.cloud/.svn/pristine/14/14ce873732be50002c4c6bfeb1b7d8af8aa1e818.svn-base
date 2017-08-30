package at.jku.sea.cloud.mmm;

import java.util.ArrayList;
import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Workspace;

public class MMMTypeCreator {
  private Workspace workspace;
  private MetaModel metamodel;
  
  /**
   * Constructor
   * 
   * @param workspace Workspace new instances should be created in
   * @param metamodel Meta model to look for complex types and features 
   */
  public MMMTypeCreator(Workspace workspace, MetaModel metamodel) {
    this.workspace = workspace;
    this.metamodel = metamodel;
  }
  
  /**
   * Create an instance named "name" of the meta model complex type named "typeName"
   * 
   * @param typeName Name of the complex type
   * @param name Name of the instance
   * @return
   * @throws Exception thrown when a type with the specified name does not exist
   */
  public Artifact createObject(String typeName, String name) throws Exception {
    return MMMTypesFactory.createComplexTypeInstance(workspace, name, getComplexType(typeName));
  }
  
  /**
   * Sets the feature named "propertyName" of an instance "object" to the value "value"
   * 
   * @param object Instance of a complex type
   * @param propertyName Name of the meta model feature
   * @param value New value of the instance feature
   * @throws Exception thrown when a property with the specified name does not exist
   */
  public void setProperty(Artifact object, String propertyName, Object value) throws Exception {
    Artifact type = getFeature(object.getType(), propertyName);
    MMMTypesFactory.createFeatureInstance(workspace, value, type, object);
  }
  
  private Artifact getComplexType(String typeName) throws Exception {
    Collection<Artifact> artifacts = getComplexTypes(typeName);
    
    Artifact result = null;
    if (artifacts.size() == 1) {
      result = artifacts.iterator().next();
    }
    
    if (result == null) {
      // TODO use appropriate exception
      throw new Exception("ComplexType (" + typeName + ")not found");
    }
    return result;
  }

  private Artifact getFeature(Artifact type, String featureName) throws Exception {
    Collection<Artifact> features = getFeatures(type);
    
    Artifact result = null;
    for (Artifact feature : features) {
      if (feature.getPropertyValue(MMMTypeProperties.NAME).equals(featureName)) {
        result = feature;
        break;
      }
    }
    
    if (result == null) {
      // TODO use appropriate exception
      throw new Exception("Property not found");
    }
    return result;
  }
  
  private Collection<Artifact> getComplexTypes(String typeName) {
    return workspace.getArtifactsWithProperty(MMMTypeProperties.NAME, typeName, metamodel);
  }
  
  @SuppressWarnings("unchecked")
  private Collection<Artifact> getFeatures(Artifact type) {
    Collection<Artifact> features = new ArrayList<Artifact>();
    
    for (Artifact superType : (Collection<Artifact>) ((CollectionArtifact) type.getPropertyValue(MMMTypeProperties.ALLSUPER_TYPES)).getElements()) {
      features.addAll(getFeatures(superType));
    }
    features.addAll((Collection<Artifact>) ((CollectionArtifact) type.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES)).getElements());
    
    return features;
  }
}