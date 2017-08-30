package at.jku.sea.cloud.rest.server.factory;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.Container.Filter;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoObject;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class DesignSpaceFactory {

  private static DesignSpaceFactory instance;

  public static DesignSpaceFactory getInstance() {
    if (instance == null) {
      instance = new DesignSpaceFactory();
    }
    return instance;
  }

  @Autowired
  protected Cloud cloud;

  public Object createDS(PojoObject pojoObject) {
    Object result;

    if (pojoObject == null) {
      return null;
    }

    if (pojoObject.getObject() instanceof PojoArtifact) {
      PojoArtifact pojoArtifact = (PojoArtifact) pojoObject.getObject();
      result = cloud.getArtifact(pojoArtifact.getVersion(), pojoArtifact.getId());
    } else {
      result = pojoObject.getObject();
    }

    return result;
  }

  public Filter createFilter(PojoArtifact[] filters) {
    Filter filter = new Filter();
    if (filters[0] != null) {
      filter.setOwner(cloud.getOwner(filters[0].getId()));
    }
    if (filters[1] != null) {
      filter.setTool(cloud.getTool(filters[1].getId()));
    }
    if (filters[2] != null) {
      filter.setArtifact(cloud.getArtifact(filters[2].getVersion(), filters[2].getId()));
    }
    if (filters[3] != null) {
      filter.setCollection(cloud.getCollectionArtifact(filters[3].getVersion(), filters[3].getId()));
    }
    if (filters[4] != null) {
      filter.setProject(cloud.getProject(filters[4].getVersion(), filters[4].getId()));
    }

    return filter;
  }
}
