package matrix;

import java.util.HashMap;
import java.util.List;

import application.setup.Triple;
import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import init.setup.Link;

/**
 * 
 * Represents an element of the matrix.
 */
public class MatrixElement {
	// Only for header elements x/y = 0/y or x/0
	private Artifact headerComplexType;
	private Artifact headerInstance;

	// Only for specific links, true = create link, false = delete link (if
	// linkInstance != null)
	private boolean linkExists;
	private Artifact linkInstance;

	// Only for non-specific links ("multiple links")
	private List<Link> listOfDefinedLinks;
	private List<Link> listOfLinkInstances;
	// Key = defLink, Value = triple (instance, available, to be created)
	private HashMap<Link, Triple> linksToBeProcessed = new HashMap<>();

	// If true, this element will be processed during saving process.
	private boolean toBeProcessed = false;
	private MatrixElementType type;

	/**
	 * Constructor for Empty Elements (e.g. x/y = 0/0)
	 */
	public MatrixElement() {
		type = MatrixElementType.NONE;
	}

	/**
	 * Constructor for Header Elements.
	 * 
	 * @param headerComplexType
	 *            ComplexType of HeaderInstance
	 * @param headerInstance
	 *            HeaderInstance
	 */
	public MatrixElement(Artifact headerComplexType, Artifact headerInstance) {
		type = MatrixElementType.HEADER;
		this.headerComplexType = headerComplexType;
		this.headerInstance = headerInstance;
	}

	/**
	 * Constructor if <b>specific link</b> is selected.
	 * 
	 * @param linkInstance
	 *            LinkInstance between given source and target.
	 */
	public MatrixElement(Link linkInstance) {
		type = MatrixElementType.SPECIFIC_LINK;
		this.linkInstance = linkInstance != null ? linkInstance.getComplexType() : null;
	}

	/**
	 * Constructor if <b>multiple (non-specific) link</b> is selected.
	 * 
	 * @param listOfDefinedLinks
	 *            A list of Defined Links between given source and target.
	 * @param elemType
	 *            Type of Element (common type =
	 *            MatrixElementType.MULTIPLE_LINK).
	 */
	public MatrixElement(List<Link> listOfDefinedLinks, MatrixElementType elemType) {
		type = elemType;
		this.listOfDefinedLinks = listOfDefinedLinks;
	}

	/**
	 * Constructor if <b>specific link</b> is selected to process this element
	 * during saving process.
	 * 
	 * @param row
	 *            Row of selected element.
	 * @param col
	 *            Column of selected element.
	 * @param linkExists
	 *            If Link exists, this parameter is set to true.
	 * @param existingLink
	 *            Link between selected source/target element.
	 */
	public MatrixElement(int row, int col, boolean linkExists, Artifact existingLink) {
		this.linkExists = linkExists;
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

	/**
	 * 
	 * Only for <b>non-specific</b> links. <br>This method sets the corresponding
	 * process of all defined links by this given list of instances.
	 * 
	 * @param listOfLinkInstances
	 */
	public void setListOfLinkInstances(List<Link> listOfLinkInstances) {
		this.listOfLinkInstances = listOfLinkInstances;

		boolean foundInstance;
		for (Link defLink : listOfDefinedLinks) {
			foundInstance = false;
			for (Link curInstance : listOfLinkInstances) {
				// TODO: Interaction with design space .getType()
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
			// TODO: Interaction with design space .getPropertyValueOrNull()
			return (String) headerComplexType.getPropertyValueOrNull(MMMTypeProperties.NAME) + "/"
					+ (String) headerInstance.getPropertyValueOrNull(MMMTypeProperties.NAME);
		} else if (type == MatrixElementType.SPECIFIC_LINK) {
			return linkInstance == null ? "false" : "true";
		} else { // multiple links
			return listOfDefinedLinks.size() + "";
		}
	}
}