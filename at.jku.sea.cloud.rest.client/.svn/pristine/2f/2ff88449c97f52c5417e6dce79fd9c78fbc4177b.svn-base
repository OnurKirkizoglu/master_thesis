package at.jku.sea.cloud.rest.client.handler.listeners;

import java.rmi.RemoteException;

import at.jku.sea.cloud.listeners.WorkspaceListener;
import at.jku.sea.cloud.listeners.events.Change;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactAliveSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCommited;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactContainerSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactRollBack;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactTypeSet;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElement;
import at.jku.sea.cloud.listeners.events.artifact.CollectionAddedElements;
import at.jku.sea.cloud.listeners.events.artifact.CollectionRemovedElement;
import at.jku.sea.cloud.listeners.events.artifact.MapCleared;
import at.jku.sea.cloud.listeners.events.artifact.MapElementRemoved;
import at.jku.sea.cloud.listeners.events.artifact.MapPut;
import at.jku.sea.cloud.listeners.events.property.PropertyAliveSet;
import at.jku.sea.cloud.listeners.events.property.PropertyCommited;
import at.jku.sea.cloud.listeners.events.property.PropertyMapSet;
import at.jku.sea.cloud.listeners.events.property.PropertyRollBack;
import at.jku.sea.cloud.listeners.events.property.PropertyValueSet;
import at.jku.sea.cloud.listeners.events.workspace.VersionCommited;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceClosed;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceParentSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRollBack;
import at.jku.sea.cloud.rest.client.handler.AbstractHandler;
import at.jku.sea.cloud.rest.pojo.listener.PojoChange;

public class RestPollWorkspaceListener extends AbstractHandler implements Runnable {

  public static final long POLL_INTERVAL = 1000;

  private WorkspaceListener listener;
  private boolean stop;
  private long interval;
  private long lid;

  public RestPollWorkspaceListener(long lid, WorkspaceListener listener) {
    this.lid = lid;
    this.listener = listener;
    stop = false;
    interval = POLL_INTERVAL;
  }

  public RestPollWorkspaceListener(long lid, WorkspaceListener listener, long interval) {
    this(lid, listener);
    this.interval = interval;
  }

  public long getListenerId() {
    return lid;
  }

  public long getInterval() {
    return interval;
  }

  public boolean isStopped() {
    return stop;
  }

  public void stopPolling() {
    stop = true;
  }

  @Override
  public void run() {
    String url = String.format(WORKSPACE_LISTENERPOLL_ADDRESS, lid);
    while (!stop) {
      Change[] changes = restFactory.createRestArray(template.getForEntity(url, PojoChange[].class).getBody());
      for (Change event : changes) {
        try {
          if (event instanceof ArtifactCreated) {
            listener.artifactCreated((ArtifactCreated) event);
          } else if (event instanceof ArtifactAliveSet) {
            listener.artifactAliveSet((ArtifactAliveSet) event);
          } else if (event instanceof ArtifactTypeSet) {
            listener.artifactTypeSet((ArtifactTypeSet) event);
          } else if (event instanceof ArtifactContainerSet) {
            listener.artifactContainerSet((ArtifactContainerSet) event);
          } else if (event instanceof PropertyAliveSet) {
            listener.propertyAliveSet((PropertyAliveSet) event);
          } else if (event instanceof PropertyValueSet) {
            listener.propertyValueSet((PropertyValueSet) event);
          } else if (event instanceof CollectionAddedElements) {
            listener.collectionAddedElements((CollectionAddedElements) event);
          } else if (event instanceof CollectionAddedElement) {
            listener.collectionAddedElement((CollectionAddedElement) event);
          } else if (event instanceof CollectionRemovedElement) {
            listener.collectionRemovedElement((CollectionRemovedElement) event);
          } else if (event instanceof MapCleared) {
            listener.mapCleared((MapCleared) event);
          } else if (event instanceof MapElementRemoved) {
            listener.mapElementRemoved((MapElementRemoved) event);
          } else if (event instanceof MapPut) {
            listener.putMap((MapPut) event);
          } else if (event instanceof ArtifactCommited) {
            listener.artifactCommited((ArtifactCommited) event);
          } else if (event instanceof PropertyCommited) {
            listener.propertyCommited((PropertyCommited) event);
          } else if (event instanceof PropertyMapSet) {
            listener.propertyMapSet((PropertyMapSet) event);
          } else if (event instanceof VersionCommited) {
            listener.versionCommited((VersionCommited) event);
          } else if (event instanceof ArtifactRollBack) {
            listener.artifactRollback((ArtifactRollBack) event);
          } else if (event instanceof PropertyRollBack) {
            listener.propertyRollback((PropertyRollBack) event);
          } else if (event instanceof WorkspaceRollBack) {
            listener.workspaceRollback((WorkspaceRollBack) event);
          } else if (event instanceof WorkspaceRebased) {
            listener.workspaceRebased((WorkspaceRebased) event);
          } else if (event instanceof WorkspaceClosed) {
            listener.workspaceClosed((WorkspaceClosed) event);
          } else if (event instanceof WorkspaceParentSet) {
            listener.workspaceParentSet((WorkspaceParentSet) event);
          }
        } catch (RemoteException re) {
          re.printStackTrace();
        }
      }

      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
