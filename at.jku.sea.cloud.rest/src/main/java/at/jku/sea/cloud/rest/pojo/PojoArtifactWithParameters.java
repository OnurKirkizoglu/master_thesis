package at.jku.sea.cloud.rest.pojo;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "ArtifactWithParameters")
public class PojoArtifactWithParameters {
	/**
	 * @author Dominik Muttenthaler
	 * @author Florian Weger
	 */
	
	private PojoArtifact type;
	private PojoContainer container;
	private PojoMetaModel metamodel;
	private PojoProject project;
	private Map<String, PojoObject> propertyMap;
	
	public PojoArtifactWithParameters() {}
	
	public PojoArtifactWithParameters(PojoArtifact type, PojoContainer container, PojoMetaModel metamodel, PojoProject project, Map<String, PojoObject> propertyMap) {
	  this.type = type;
	  this.container = container;
	  this.metamodel = metamodel;
	  this.project = project;
	  this.propertyMap = propertyMap;
  }
	
	public PojoArtifact getType() {
	  return type;
  }
	public void setType(PojoArtifact type) {
	  this.type = type;
  }
	public PojoContainer getContainer() {
		return container;
	}
	public void setContainer(PojoContainer container) {
		this.container = container;
	}
	public PojoMetaModel getMetamodel() {
		return metamodel;
	}
	public void setMetamodel(PojoMetaModel metamodel) {
		this.metamodel = metamodel;
	}
	public PojoProject getProject() {
		return project;
	}
	public void setProject(PojoProject project) {
		this.project = project;
	}
	public Map<String, PojoObject> getPropertyMap() {
	  return propertyMap;
  }
	public void setPropertyMap(Map<String, PojoObject> propertyMap) {
	  this.propertyMap = propertyMap;
  }
}
