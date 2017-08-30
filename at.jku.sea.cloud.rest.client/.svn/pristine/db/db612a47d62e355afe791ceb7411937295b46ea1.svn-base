package at.jku.sea.cloud.rest.client;

import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Version;
import at.jku.sea.cloud.rest.client.handler.VersionHandler;
import at.jku.sea.cloud.utils.StringUtils;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class RestVersion implements Version {

  private long version;

	private VersionHandler handler;

	protected RestVersion(long version) {
		this.version = version;
		handler = VersionHandler.getInstance();
	}

	@Override
	public long getVersionNumber() {
		return version;
	}

	@Override
	public Owner getOwner() {
		return handler.getOwner(version);
	}

	@Override
	public Tool getTool() {
		return handler.getTool(version);
	}

	@Override
	public String getIdentifier() {
		return handler.getIdentifier(version);
	}

	@Override
	public String toString() {
		return StringUtils.printf("%s[version=%s]", this.getClass()
				.getSimpleName(), version);
	}
	
	@Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (version ^ (version >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RestVersion other = (RestVersion) obj;
    if (version != other.version)
      return false;
    return true;
  }
}
