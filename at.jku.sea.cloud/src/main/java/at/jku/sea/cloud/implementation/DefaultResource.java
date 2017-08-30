package at.jku.sea.cloud.implementation;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Workspace;

class DefaultResource extends DefaultContainer implements Resource {

  private static final long serialVersionUID = 1L;

  public DefaultResource(DataStorage dataStorage, long id, long version) {
    super(dataStorage, id, version);
  }

  @Override
  public String getFullQualifiedName() {
    return (String) this.getPropertyValue(DataStorage.PROPERTY_FULL_QUALIFIED_NAME);
  }

  @Override
  public void setFullQualifiedName(Workspace workspace, String fullQualifiedName) {
    this.setPropertyValue(workspace, DataStorage.PROPERTY_FULL_QUALIFIED_NAME, fullQualifiedName);
  }
}
