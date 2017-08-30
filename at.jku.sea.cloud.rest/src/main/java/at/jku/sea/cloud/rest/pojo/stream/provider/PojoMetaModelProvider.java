package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

import at.jku.sea.cloud.rest.pojo.PojoMetaModel;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "MetaModelProvider")
public class PojoMetaModelProvider extends PojoProvider {
  private PojoMetaModel metaModel;
  
  public PojoMetaModelProvider() {
  }
  
  public PojoMetaModelProvider(PojoMetaModel metaModel) {
    this.metaModel = metaModel;
  }
  
  public PojoMetaModel getMetaModel() {
    return metaModel;
  }
  
  public void setMetaModel(PojoMetaModel metaModel) {
    this.metaModel = metaModel;
  }
  
}
