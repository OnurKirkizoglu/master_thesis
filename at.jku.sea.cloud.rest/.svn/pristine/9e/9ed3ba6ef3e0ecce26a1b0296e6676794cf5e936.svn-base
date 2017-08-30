package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "KeyValue")
public class PojoKeyValue {
	private PojoObject key;
	private PojoObject value;
	
	public PojoKeyValue() {}
	
	public PojoKeyValue(PojoObject key, PojoObject value) {
		this.key = key;
		this.value = value;
	}
	
	public void setKey(PojoObject key) {
	  this.key = key;
  }
	public PojoObject getKey() {
	  return key;
  }
	public void setValue(PojoObject value) {
	  this.value = value;
  }
	public PojoObject getValue() {
	  return value;
  }
	
}
