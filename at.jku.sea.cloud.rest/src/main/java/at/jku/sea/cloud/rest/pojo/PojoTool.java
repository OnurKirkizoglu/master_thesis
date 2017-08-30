package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "Tool")
public class PojoTool
		extends PojoArtifact {
	
	@JsonProperty private String name;
	@JsonProperty private String toolVersion;

	public PojoTool() {
		super();
	}

	public PojoTool(
			@JsonProperty("id") long id,
			@JsonProperty("version") long version,
			@JsonProperty("name") String name,
			@JsonProperty("toolVersion") String toolVersion) {
		super(id, version);
		this.name = name;
		this.toolVersion = toolVersion;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the toolVersion
	 */
	public String getToolVersion() {
		return toolVersion;
	}
	
	
}
