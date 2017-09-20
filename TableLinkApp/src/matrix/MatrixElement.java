package matrix;

import java.util.HashMap;
import java.util.List;

import application.setup.Triple;
import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import init.setup.Link;

public class MatrixElement {
	// only for header elements
	private Artifact headerComplexType;
	private Artifact headerInstance;

	// only for single links, true = create link, false = delete link (if
	// linkInstance != null)
	private boolean linkExists;
	private Artifact linkInstance;

	// only for multiple links
	private List<Link> listOfDefinedLinks;
	private List<Link> listOfLinkInstances;
	// key = defLink, value = Pair (instance, available)
	private HashMap<Link, Triple> linksToBeProcessed = new HashMap<>();

	// needed for saving matrix via button "save link"
	private boolean toBeProcessed = false;

	private MatrixElementType type;
	public MatrixElement() {
		type = MatrixElementType.NONE;
	}

	// header element
	public MatrixElement(Artifact headerComplexType, Artifact headerInstance) {
		type = MatrixElementType.HEADER;
		this.headerComplexType = headerComplexType;
		this.headerInstance = headerInstance;
	}

	// single link constructor
	public MatrixElement(Link linkInstance) {
		type = MatrixElementType.SINGLE_LINK;
		// TODO switch to link.getInstance instead of link.getComplexType()
		this.linkInstance = linkInstance != null ? linkInstance.getComplexType() : null;
	}

	// multiple links (needed for creation)
	public MatrixElement(List<Link> listOfDefinedLinks, MatrixElementType elemType) {
		type = elemType;
		this.listOfDefinedLinks = listOfDefinedLinks;
	}

	// element to be processed
	public MatrixElement(int row, int col, boolean linkChecked, Artifact existingLink) {
		this.linkExists = linkChecked;
		this.linkInstance = existingLink;
		toBeProcessed = true;
	}

	public List<Link> getDefinedLinks() {
		return listOfDefinedLinks;
	}

	public Artifact getLinkInstance() {
		return linkInstance;
	}

	public MatrixElementType getMatrixElementType() {
		return type;
	}

	public Artifact getHeaderComplexType() {
		return headerComplexType;
	}

	public Artifact getHeaderInstance() {
		return headerInstance;
	}

	public boolean isLinkPresent() {
		return linkInstance != null;
	}

	public void setToBeProcessed(boolean process) {
		toBeProcessed = process;
	}

	public boolean isToBeProcessed() {
		return toBeProcessed;
	}

	public boolean isLinkExists() {
		return linkExists;
	}

	public List<Link> getListOfLinkInstances() {
		return listOfLinkInstances;
	}

	public void setListOfLinkInstances(List<Link> listOfLinkInstances) {
		this.listOfLinkInstances = listOfLinkInstances;

		boolean foundInstance;
		for (Link defLink : listOfDefinedLinks) {
			foundInstance = false;
			for (Link curInstance : listOfLinkInstances) {
				// TODO switch to curInstance.getInstance() instead of curInstance.getComplexType()
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

	public HashMap<Link, Triple> getLinksToBeProcessed() {
		return linksToBeProcessed;
	}

	@Override
	public String toString() {
		if (type == MatrixElementType.NONE) {
			return "";
		} else if (type == MatrixElementType.HEADER) {
			return (String) headerComplexType.getPropertyValueOrNull(MMMTypeProperties.NAME) + "/"
					+ (String) headerInstance.getPropertyValueOrNull(MMMTypeProperties.NAME);
		} else if (type == MatrixElementType.SINGLE_LINK) {
			return linkInstance == null ? "false" : "true";
		} else { // multiple links
			return listOfDefinedLinks.size() + "";
		}
	}
}