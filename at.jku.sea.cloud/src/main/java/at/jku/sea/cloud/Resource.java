package at.jku.sea.cloud;


public interface Resource extends Container {
  public String getFullQualifiedName();

  public void setFullQualifiedName(Workspace workspace, String fullQualifiedName);
}
