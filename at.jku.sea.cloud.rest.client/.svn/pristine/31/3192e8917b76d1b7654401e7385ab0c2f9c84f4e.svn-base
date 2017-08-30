package at.jku.sea.cloud.rest.client;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;
import at.jku.sea.cloud.rest.client.handler.MapArtifactHandler;
import at.jku.sea.cloud.rest.client.handler.MapEntryHandler;

public class RestMapArtifact extends RestArtifact implements MapArtifact {
	private static final long serialVersionUID = 1L;

	private MapArtifactHandler handler;

	protected RestMapArtifact(long id, long version) {
		super(id, version);
		handler = MapArtifactHandler.getInstance();
	}

	@Override
	public long size() {
		return handler.size(id, version);
	}

	@Override
	public boolean containsKey(Object key) {
		return handler.containsKey(id, version, key);
	}

	@Override
	public boolean containsValue(Object value) {
		return handler.constainsValue(id, version, value);
	}

	@Override
	public Object get(Object key) {
		return handler.get(id, version, key);
	}

	@Override
	public Object put(Workspace workspace, Object key, Object value)
			throws TypeNotSupportedException, PropertyDeadException {
		return handler.put(workspace.getId(), id, version, key, value);
	}

	@Override
	public Object remove(Workspace workspace, Object key)
			throws TypeNotSupportedException {
		return handler.remove(workspace.getId(), id, version, key);
	}

	@Override
	public void clear(Workspace workspace) {
		handler.clear(workspace.getId(), id, version);
	}

	@Override
	public Set<Object> keySet() {
		return handler.keySet(id, version);
	}

	@Override
	public Collection<Object> values() {
		return handler.values(id, version);
	}

	@Override
	public Set<Entry> entrySet() {
		return handler.entrySet(id, version);
	}

	public static class RestEntry extends RestArtifact implements
			MapArtifact.Entry {

		private static final long serialVersionUID = 1L;

		private final long entryId;
		private final long entryVersion;
		private final long mapId;
		private final long mapVersion;
		private final MapEntryHandler entryHandler;

		protected RestEntry(long mapId, long mapVersion, long entryId,
				long entryVersion) {
			super(entryId, entryVersion);
			this.mapId = mapId;
			this.mapVersion = mapVersion;
			this.entryId = entryId;
			this.entryVersion = entryVersion;
			entryHandler = MapEntryHandler.getInstance();
		}

		@Override
		public Object getKey() {
			return entryHandler
					.getKey(mapId, mapVersion, entryId, entryVersion);
		}

		@Override
		public Object getValue() {
			return entryHandler.getValue(mapId, mapVersion, entryId,
					entryVersion);
		}

		@Override
		public Object setValue(Workspace workspace, Object value)
				throws TypeNotSupportedException, PropertyDeadException {
			return entryHandler.setValue(workspace.getId(), mapId, mapVersion,
					entryId, entryVersion, value);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof RestEntry)) {
				return false;
			}
			RestEntry other = (RestEntry) obj;

			return entryId == other.entryId
					&& entryVersion == other.entryVersion;
		}

		@Override
		public int hashCode() {
			return Objects.hash(entryVersion, entryId);
		}
		
		@Override
		public String toString() {
			return "[" + this.getKey() + ": " + this.getValue() + "]";
		}
	}
}
