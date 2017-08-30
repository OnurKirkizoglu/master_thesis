package at.jku.sea.cloud.rest.server.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.exceptions.MetaModelDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoArtifactAndProperties;
import at.jku.sea.cloud.rest.pojo.PojoArtifactRepAndPropertiesRep;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class MetaModelHandler {

  @Autowired
  protected Cloud cloud;
  @Autowired
  protected PojoFactory pojoFactory;

  public PojoArtifact[] getArtifacts(long id, long version) throws MetaModelDoesNotExistException {
    MetaModel metamodel = cloud.getMetaModel(version, id);
    Artifact[] artifacts = metamodel.getArtifacts().toArray(new Artifact[0]);
    return pojoFactory.createPojoArray(artifacts);
  }

  public PojoArtifactAndProperties[] getArtifactAndProperties(long id, long version) {
    MetaModel metamodel = cloud.getMetaModel(version, id);
    Map<Artifact, Set<Property>> map = metamodel.getArtifactAndProperties();
    return pojoFactory.createPojoArray(map);
  }

  public PojoObject[][] getArtifactRepresentations(long id, long version) {
    MetaModel metamodel = cloud.getMetaModel(version, id);
    Collection<Object[]> collection = metamodel.getArtifactRepresentations();
    PojoObject[][] result = new PojoObject[collection.size()][];
    int i = 0;
    for (Object[] artifact : collection) {
      result[i] = pojoFactory.createPojoArray(artifact);
      i++;
    }
    return result;
  }

  public PojoArtifactRepAndPropertiesRep[] getArtifactPropertyMapRepresentations(long id, long version) {
    MetaModel metamodel = cloud.getMetaModel(version, id);
    Map<Object[], Set<Object[]>> map = metamodel.getArtifactPropertyMapRepresentations();
    PojoArtifactRepAndPropertiesRep[] result = pojoFactory.createPojoArrayRep(map);
    return result;
  }

  public PojoArtifactAndProperties[] getArtifactPropertyMap(long id, long version) {
    MetaModel metamodel = cloud.getMetaModel(version, id);
    Map<Artifact, Map<String, Object>> map = metamodel.getArtifactPropertyMap();
    return pojoFactory.createPojoMappings(map);
  }

  public PojoMetaModel getMetaModel(long id, long version) {
    MetaModel metaModel = cloud.getMetaModel(version, id);
    return pojoFactory.createPojo(metaModel.getMetaModel());
  }

}
