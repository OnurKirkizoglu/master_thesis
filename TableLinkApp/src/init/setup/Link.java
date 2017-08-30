package init.setup;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.mmm.MMMTypeProperties;

public class Link implements Comparable<Object> {

    private String linkName;
    private Artifact source;
    private Artifact target;
    private Artifact description;
    private Artifact complexType;
    private Artifact instanceOfComplexType;
    private boolean isMultipleLink = false;
    
    public Link(String linkName, Artifact source, Artifact target, Artifact description, Artifact complexType) {
        this.linkName = linkName;
        this.source = source;
        this.target = target;
        this.description = description;
        this.complexType = complexType;
    }

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
		return "ComplexType of Instance" + (String)instanceOfComplexType.getType().getPropertyValueOrNull(MMMTypeProperties.NAME);
	}
    
}
