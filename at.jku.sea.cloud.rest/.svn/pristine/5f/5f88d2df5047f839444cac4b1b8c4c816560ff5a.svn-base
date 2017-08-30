package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifact;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "CollectionArtifactProvider")
public class PojoCollectionArtifactProvider extends PojoProvider {
  private PojoCollectionArtifact collectionArtifact;
  
  public PojoCollectionArtifactProvider() {
  }
  
  public PojoCollectionArtifactProvider(PojoCollectionArtifact collectionArtifact) {
    this.collectionArtifact = collectionArtifact;
  }
  
  public PojoCollectionArtifact getCollectionArtifact() {
    return collectionArtifact;
  }
  
  public void setCollectionArtifact(PojoCollectionArtifact collectionArtifact) {
    this.collectionArtifact = collectionArtifact;
  }
  
}
