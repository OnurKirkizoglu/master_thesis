package application.setup;
import at.jku.sea.cloud.Artifact;
import init.setup.Link;

public class Triple {
	private Link link;
	private boolean available;

	/* needed for save process
	 if toCreate is true: link have to be create if available is false, else do nothing
	 if toCreate is false: link have to be delete if available is true, else do nothing */
	private boolean toCreate;
	
	public Triple(Link link, boolean available, boolean toCreate){
		this.link = link;
		this.available = available;
		this.toCreate = toCreate;
	}
	public boolean isToCreate() {
		return toCreate;
	}
	public void setToCreate(boolean toCreate) {
		this.toCreate = toCreate;
	}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	
}
