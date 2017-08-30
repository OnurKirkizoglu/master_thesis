package at.jku.sea.cloud.rest.pojo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */

@JsonTypeInfo(use=Id.NAME, property = "__type")
@JsonTypeName(value = "Property")
public class PojoProperty {

	private long id;
	private long version;
	private String name;
	private PojoObject object;

	public PojoProperty() {
	}

	public PojoProperty(long id, long version, String name, PojoObject object) {
		this.id = id;
		this.version = version;
		this.name = name;
		this.object = object;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public PojoObject getObject() {
		return object;
	}

	public void setObject(PojoObject object) {
		this.object = object;
	}
}
