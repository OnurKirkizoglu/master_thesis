/*
 * (C) Johannes Kepler University Linz, Austria, 2005-2013
 * Institute for Systems Engineering and Automation (SEA)
 *
 * The software may only be used for academic purposes (teaching, scientific
 * research). Any redistribution or commercialization of the software program
 * and documentation (or any part thereof) requires prior written permission of
 * the JKU. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * This software program and documentation are copyrighted by Johannes Kepler
 * University Linz, Austria (the JKU). The software program and documentation
 * are supplied AS IS, without any accompanying services from the JKU. The JKU
 * does not warrant that the operation of the program will be uninterrupted or
 * error-free. The end-user understands that the program was developed for
 * research purposes and is advised not to rely exclusively on the program for
 * any reason.
 *
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE
 * AUTHOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THE AUTHOR
 * SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE AUTHOR HAS
 * NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 */

/*
 * CloudAdapter.java created on Sep 18, 2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.listeners;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceAdded;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceClosed;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceParentSet;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRebased;
import at.jku.sea.cloud.listeners.events.workspace.WorkspaceRollBack;

/**
 * @author alexander noehrer
 */
public class CloudAdapter implements CloudListener {
  private static Logger logger = LoggerFactory.getLogger(CloudAdapter.class);

  private final boolean log;

  public CloudAdapter() {
    this(false);
  }

  public CloudAdapter(final boolean log) {
    super();
    this.log = log;
  }

  @Override
  public void artifactCreated(final ArtifactCreated event) throws RemoteException {
    if (this.log) {
      logger.debug("artifactCreated(artifact={})", event.artifact);
    }
  }

  @Override
  public void artifactAliveSet(final ArtifactAliveSet event) throws RemoteException {
    if (this.log) {
      logger.debug("artifactAliveSet(artifact={}, alive={})", event.artifact, event.alive);
    }
  }

  @Override
  public void artifactTypeSet(final ArtifactTypeSet event) throws RemoteException {
    if (this.log) {
      logger.debug("artifactTypeSet(artifact={}, type={})", event.artifact, event.artifactType);
    }
  }

  @Override
  public void artifactContainerSet(final ArtifactContainerSet event) throws RemoteException {
    if (this.log) {
      logger.debug("artifactPackageSet(artifact={}, pkg={})", event.artifact, event.container);
    }
  }

  @Override
  public void propertyAliveSet(final PropertyAliveSet event) throws RemoteException {
    if (this.log) {
      logger.debug("propertyAliveSet(property={}, alive={})", event.property, event.alive);
    }
  }

  @Override
  public void propertyValueSet(final PropertyValueSet event) throws RemoteException {
    if (this.log) {
      logger.debug("propertyValueSet(property={}, oldValue={}, newValue={}, wasReference={})", new Object[] { event.property, event.oldValue, event.value, event.wasReference });
    }
  }

  @Override
  public void propertyMapSet(final PropertyMapSet event) throws RemoteException {
    if (this.log) {
      logger.debug("propertyMapSet(changes={})", new Object[] { event.changes });
    }
  }

  @Override
  public void collectionAddedElement(final CollectionAddedElement event) throws RemoteException {
    if (this.log) {
      logger.debug("collectionAddedElement(collArtifact={}, element={})", new Object[] { event.artifact, event.value });
    }
  }

  @Override
  public void collectionAddedElements(CollectionAddedElements event) throws RemoteException {
    if (this.log) {
      logger.debug("collectionAddedElement(collArtifacts={}, element={})", new Object[] { event.artifact, event.values });
    }
  }

  @Override
  public void collectionRemovedElement(final CollectionRemovedElement event) throws RemoteException {
    if (this.log) {
      logger.debug("collectionRemovedElement(collArtifact={}, element={})", new Object[] { event.artifact, event.value });
    }
  }

  @Override
  public void mapCleared(MapCleared event) throws RemoteException {
    if (this.log) {
      logger.debug("mapCleared(mapArtifact={})", new Object[] { event.artifact });
    }
  }

  @Override
  public void mapElementRemoved(MapElementRemoved event) throws RemoteException {
    if (this.log) {
      logger.debug("mapElementRemoved(mapArtifact={}, key={}, value={})", new Object[] { event.artifact, event.key, event.value });
    }
  }

  @Override
  public void putMap(MapPut event) throws RemoteException {
    if (this.log) {
      logger.debug("putMap(mapArtifact={}, key={}, oldValue={}, newValue={}, isAdded={})", new Object[] { event.artifact, event.key, event.oldValue, event.newValue, event.isAdded });
    }
  }

  @Override
  public void artifactCommited(final ArtifactCommited event) throws RemoteException {
    if (this.log) {
      logger.debug("artifactCommited(artifact={}, version={})", event.artifact, event.version);
    }
  }

  @Override
  public void propertyCommited(final PropertyCommited event) throws RemoteException {
    if (this.log) {
      logger.debug("propertyCommited(property={}, version={})", event.property, event.version);
    }
  }

  @Override
  public void versionCommited(final VersionCommited event) throws RemoteException {
    if (this.log) {
      logger.debug("versionCommited(workspace={}, version={})", event.workspace, event.version);
    }
  }

  @Override
  public void artifactRollback(final ArtifactRollBack event) throws RemoteException {
    if (this.log) {
      logger.debug("artifactRollback(artifact={})", event.artifact);
    }
  }

  @Override
  public void propertyRollback(final PropertyRollBack event) throws RemoteException {
    if (this.log) {
      logger.debug("propertyRollback(property={})", event.property);
    }
  }

  @Override
  public void workspaceRollback(final WorkspaceRollBack event) throws RemoteException {
    if (this.log) {
      logger.debug("workspaceRollback(workspace={})", event.workspace);
    }
  }

  @Override
  public void workspaceClosed(final WorkspaceClosed event) throws RemoteException {
    if (this.log) {
      logger.debug("workspaceClosed(workspace={})", event.workspace);
    }
  }

  @Override
  public void workspaceAdded(final WorkspaceAdded event) throws RemoteException {
    if (this.log) {
      logger.debug("workspaceAdded(workspace={})", event.workspace);
    }
  }

  @Override
  public void workspaceRebased(final WorkspaceRebased event) throws RemoteException {
    if (this.log) {
      logger.debug("workspaceRebased(workspace={}, publicVersion={})", event.workspace, event.oldBaseVersion);
    }
  }

  @Override
  public void workspaceParentSet(WorkspaceParentSet event) throws RemoteException {
    if (this.log) {
      logger.debug("workspaceParentSet(parent={}, workspace={})", event.parent, event.workspace);
    }
  }

}
