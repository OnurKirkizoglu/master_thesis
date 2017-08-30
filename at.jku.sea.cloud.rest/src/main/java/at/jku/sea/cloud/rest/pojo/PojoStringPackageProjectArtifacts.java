package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "StringPackageProjectArtifacts")
public class PojoStringPackageProjectArtifacts {

	private String fullQualifiedName;
	private PojoPackage pkg;
	private PojoProject project;
	private PojoArtifact[] artifacts;
	
	public PojoStringPackageProjectArtifacts() { }
	
	public PojoStringPackageProjectArtifacts(String fullQualifiedName, PojoPackage pkg, PojoProject project,
			PojoArtifact[] artifacts) {
		super();
		this.fullQualifiedName = fullQualifiedName;
		this.pkg = pkg;
		this.project = project;
		this.artifacts = artifacts;
	}

	public String getFullQualifiedName() {
		return fullQualifiedName;
	}
	public void setFullQualifiedName(String fullQualifiedName) {
		this.fullQualifiedName = fullQualifiedName;
	}
	public PojoPackage getPkg() {
		return pkg;
	}
	public void setPkg(PojoPackage pkg) {
		this.pkg = pkg;
	}
	public PojoProject getProject() {
		return project;
	}
	public void setProject(PojoProject project) {
		this.project = project;
	}
	public PojoArtifact[] getArtifacts() {
		return artifacts;
	}
	public void setArtifacts(PojoArtifact[] artifacts) {
		this.artifacts = artifacts;
	}
}
