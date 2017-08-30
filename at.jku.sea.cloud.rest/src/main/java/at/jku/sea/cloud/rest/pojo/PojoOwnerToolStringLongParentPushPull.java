package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import at.jku.sea.cloud.PropagationType;

@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "OwnerToolStringLongParentPushPull")
public class PojoOwnerToolStringLongParentPushPull {
  private PojoOwner owner;
  private PojoTool tool;
  private String string;
  private Long _long;
  private Long parent;
  private PropagationType push;
  private PropagationType pull;

  public PojoOwnerToolStringLongParentPushPull() {
  }

  public PojoOwnerToolStringLongParentPushPull(PojoOwner owner, PojoTool tool, String string, Long _long, Long parent, PropagationType push, PropagationType pull) {
    super();
    this.owner = owner;
    this.tool = tool;
    this.string = string;
    this._long = _long;
    this.setParent(parent);
    this.setPush(push);
    this.setPull(pull);
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

  public PropagationType getPush() {
    return push;
  }

  public void setPush(PropagationType push) {
    this.push = push;
  }

  public PropagationType getPull() {
    return pull;
  }

  public void setPull(PropagationType pull) {
    this.pull = pull;
  }

  public Long getParent() {
    return parent;
  }

  public void setParent(Long parent) {
    this.parent = parent;
  }
}
