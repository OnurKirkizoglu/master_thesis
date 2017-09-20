package graph;

import at.jku.sea.cloud.Artifact;

public class ArtifactShape {
	private double x;
	private double y;
	private double width;
	private double height;
	private Artifact complexType;
	private Artifact instance;
	
	// due to performance problems
	private String name;

	public ArtifactShape(double x, double y, double width, double height, String name, Artifact complexType,
			Artifact instance) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		this.complexType = complexType;
		this.instance = instance;
	}

	public Artifact getComplexType() {
		return complexType;
	}

	public void setComplexType(Artifact complexType) {
		this.complexType = complexType;
	}

	public Artifact getInstance() {
		return instance;
	}

	public void setInstance(Artifact instance) {
		this.instance = instance;
	}

	public String getName() {
		return name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArtifactShape other = (ArtifactShape) obj;
		if (complexType == null) {
			if (other.complexType != null)
				return false;
		} else if (!complexType.equals(other.complexType))
			return false;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArtifactShape [complexType=" + complexType + ", instance=" + instance + "]";
	}
	
}
