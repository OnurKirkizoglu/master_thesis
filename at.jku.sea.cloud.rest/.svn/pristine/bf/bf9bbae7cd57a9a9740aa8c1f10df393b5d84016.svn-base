package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "OwnerToolStringLong")
public class PojoOwnerToolStringLong {
  private PojoOwner owner;
  private PojoTool tool;
  private String string;
  private Long _long;

  public PojoOwnerToolStringLong() {
  }

  public PojoOwnerToolStringLong(PojoOwner owner, PojoTool tool, String string, Long _long) {
    super();
    this.owner = owner;
    this.tool = tool;
    this.string = string;
    this._long = _long;
  }

  public PojoOwner getOwner() {
    return owner;
  }

  public void setOwner(PojoOwner owner) {
    this.owner = owner;
  }

  public PojoTool getTool() {
    return tool;
  }

  public void setTool(PojoTool tool) {
    this.tool = tool;
  }

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  public Long getLong() {
    return _long;
  }

  public void setLong(Long _long) {
    this._long = _long;
  }
}
