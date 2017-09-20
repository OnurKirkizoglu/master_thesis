package graph;

import java.util.HashMap;
import java.util.List;

import application.setup.Triple;
import at.jku.sea.cloud.Artifact;
import init.setup.Link;

public class LinkShape {
	private ArtifactShape source;
	private ArtifactShape target;
	private Artifact complexType;
	private Artifact linkInstance;

	// single link
	private boolean toBeProcessed = false;
	private boolean linkShouldBeCreated = false;
	private boolean linkShouldBeDeleted = false;

	// multiple link
	private List<Link> listOfDefinedLinks;
	private List<Link> listOfInstanceLinks;
	private HashMap<Link, Triple> linksToBeProcessed = new HashMap<>();;

	// dummy
	public LinkShape() {

	}

	// single link between source and target
	public LinkShape(ArtifactShape sourceShape, ArtifactShape targetShape, Artifact complexType,
			Artifact linkInstance) {
		this.source = sourceShape;
		this.target = targetShape;
		this.complexType = complexType;
		this.linkInstance = linkInstance;
	}

	// multiple link
	public LinkShape(ArtifactShape sourceShape, ArtifactShape targetShape, List<Link> listOfDefinedLinks) {
		this.source = sourceShape;
		this.target = targetShape;
		this.listOfDefinedLinks = listOfDefinedLinks;
	}

	public HashMap<Link, Triple> getLinksToBeProcessed() {
		return linksToBeProcessed;
	}

	public List<Link> getListOfInstanceLinks() {
		return listOfInstanceLinks;
	}

	public void setListOfInstanceLinks(List<Link> listOfInstanceLinks) {
		this.listOfInstanceLinks = listOfInstanceLinks;

		boolean foundInstance;
		for (Link defLink : listOfDefinedLinks) {
			foundInstance = false;
			for (Link curInstance : listOfInstanceLinks) {
				// TODO switch to curInstance.getInstance() instead of
				// curInstance.getComplexType()
				if (defLink.getComplexType().equals(curInstance.getComplexType().getType())) {
					linksToBeProcessed.put(defLink, new Triple(curInstance, true, true));
					foundInstance = true;
				}
			}
			if (foundInstance == false) {
				linksToBeProcessed.put(defLink, new Triple(null, false, false));
			}
		}
	}

	public List<Link> getListOfDefinedLinks() {
		return listOfDefinedLinks;
	}

	public void setListOfDefinedLinks(List<Link> listOfDefinedLinks) {
		this.listOfDefinedLinks = listOfDefinedLinks;
	}

	public boolean isLinkShouldBeCreated() {
		return linkShouldBeCreated;
	}

	public void setLinkShouldBeCreated(boolean linkShouldBeCreated) {
		this.linkShouldBeCreated = linkShouldBeCreated;
	}

	public boolean isLinkShouldBeDeleted() {
		return linkShouldBeDeleted;
	}

	public void setLinkShouldBeDeleted(boolean linkShouldBeDeleted) {
		this.linkShouldBeDeleted = linkShouldBeDeleted;
	}

	public Artifact getComplexType() {
		return complexType;
	}

	public void setComplexType(Artifact complexType) {
		this.complexType = complexType;
	}

	public boolean isToBeProcessed() {
		return toBeProcessed;
	}

	public void setToBeProcessed(boolean toBeProcessed) {
		this.toBeProcessed = toBeProcessed;
	}

	public ArtifactShape getSource() {
		return source;
	}

	public Artifact getLinkInstance() {
		return linkInstance;
	}

	public void setLinkInstance(Artifact link) {
		this.linkInstance = link;
	}

	public void setSource(ArtifactShape source) {
		this.source = source;
	}

	public ArtifactShape getTarget() {
		return target;
	}

	public void setTarget(ArtifactShape target) {
		this.target = target;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkShape other = (LinkShape) obj;
		if (linkInstance == null) {
			if (other.linkInstance != null)
				return false;
		} else if (!linkInstance.equals(other.linkInstance))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	public void setDefaultParameter() {
		toBeProcessed = false;
		linkShouldBeCreated = false;
		linkShouldBeDeleted = false;
	}

}
