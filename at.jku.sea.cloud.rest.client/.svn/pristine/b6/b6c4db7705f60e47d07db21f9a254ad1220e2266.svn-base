package at.jku.sea.cloud.rest.client.stream.provider.implementation;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.rest.client.stream.provider.RestProvider;
import at.jku.sea.cloud.rest.pojo.PojoOwnerToolStringLong;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoWorkspaceWithFilterProvider;

public class WorkspaceWithFilterProvider extends RestProvider<Artifact> {

  private final Workspace ws;
  private final Artifact[] filters;

  public WorkspaceWithFilterProvider(Workspace ws, Artifact[] filters) {
    this.ws = ws;
    this.filters = filters;
  }

  public Workspace getWs() {
    return ws;
  }

  public Artifact[] getFilters() {
    return filters;
  }

  @Override
  public PojoProvider map(PojoFactory pojoFactory) {
    PojoOwnerToolStringLong pojoOwnerToolString = new PojoOwnerToolStringLong(pojoFactory.createPojo(ws.getOwner()),
        pojoFactory.createPojo(ws.getTool()), ws.getIdentifier(), null);
    return new PojoWorkspaceWithFilterProvider(pojoOwnerToolString,
        filters == null ? null : pojoFactory.createPojoArray(filters));
  }
}
