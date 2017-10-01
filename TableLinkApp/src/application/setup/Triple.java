package application.setup;
import init.setup.Link;

public class Triple {
	private Link link;
	// available and toCreate are needed for saving process.
	
	// True if link is available, else false
	private boolean available;
	// Depends on field available:
	// - If available is true: If toCreate is true: do nothing, else delete link.
	// - If available is false:If toCreate is true: link have to be create, else do nothing
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
