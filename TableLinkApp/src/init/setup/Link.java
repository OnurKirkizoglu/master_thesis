package init.setup;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.mmm.MMMTypeProperties;

/**
 * 
 * This class represents a link between two artifacts.
 *
 */
public class Link implements Comparable<Object> {

    private String linkName;
    private Artifact source;
    private Artifact target;
    private Artifact description;
    private Artifact complexType;
    private Artifact instanceOfComplexType;
    
    /** @deprecated */
    private boolean isMultipleLink = false;
    
    /**
     * 
     * This constructor is thought to represent a defined link.
     * 
     * @param linkName
     * @param source Source of Defined Link (ComplexType Level)
     * @param target Target of Defined Link (ComplexType Level)
     * @param description Description of Link (Instance)
     * @param complexType Defined link itself.
     */
    public Link(String linkName, Artifact source, Artifact target, Artifact description, Artifact complexType) {
        this.linkName = linkName;
        this.source = source;
        this.target = target;
        this.description = description;
        this.complexType = complexType;
    }

    /**
     * This constructor is thought to represent an instance link.
     * Please notice that source/target are also instances.
     * 
     * @param linkName Name of the given link instance.
     * @param source Source (instance) of given link instance.
     * @param target Target (instance) of given link instance.
     * @param description Description of the link instance.
     * @param complexType Type of given link instance.
     * @param instanceOfComplexType The instance link itself.
     */
    public Link(String linkName, Artifact source, Artifact target, Artifact description, Artifact complexType, Artifact instanceOfComplexType) {
        this.linkName = linkName;
        this.source = source;
        this.target = target;
        this.description = description;
        this.complexType = complexType;
        this.instanceOfComplexType = instanceOfComplexType;
    }
    
    public Link(String name){
    	this.linkName = name;
    	isMultipleLink = true;
    }
    public Link(String linkName, Artifact source, Artifact target) {
        this(linkName, source, target, null, null);
    }

    public void setInstanceOfComplexType(Artifact linkInstance){
    	instanceOfComplexType = linkInstance;
    }
    public Artifact getInstanceOfComplexType(){
    	return instanceOfComplexType;
    }
    public String getName() {
        return linkName;
    }

    public Artifact getSource() {
        return source;
    }

    public Artifact getTarget() {
        return target;
    }

    public Artifact getDescription() {
        return description;
    }

    public Artifact getComplexType() {
        return complexType;
    }

    public void setName(String linkName) {
        this.linkName = linkName;
    }

    public void setSource(Artifact source) {
        this.source = source;
    }

    public void setTarget(Artifact target) {
        this.target = target;
    }

    public void setDescription(Artifact description) {
        this.description = description;
    }

    public void setComplexType(Artifact link) {
        this.complexType = link;
    }

    @Override
    public boolean equals(Object obj) {
        Link other = (Link) obj;
        if (other == null) {
            return false;
        }
        if (linkName == null) {
            if (other.linkName != null)
                return false;
        } else if (!linkName.equals(other.linkName))
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

    @Override
    public int compareTo(Object o) {
        Link l = (Link) o;
        return this.getName().compareTo(l.getName());
    }
    public boolean isMultipleLink(){
    	return isMultipleLink;
    }

	@Override
	public String toString() {
		if(linkName != null)
		return "Complextype [linkName=" + linkName + "]";
		else
		// TODO: Interaction with design space
		return "ComplexType of Instance" + (String)instanceOfComplexType.getType().getPropertyValueOrNull(MMMTypeProperties.NAME);
	}
    
}
