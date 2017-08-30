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
 * WorkspaceListener.java created on Sep 18, 2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud.listeners;

import java.rmi.Remote;
import java.rmi.RemoteException;

import at.jku.sea.cloud.listeners.events.artifact.ArtifactAliveSet;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCommited;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactCreated;
import at.jku.sea.cloud.listeners.events.artifact.ArtifactContainerSet;
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

/**
 * @author alexander noehrer
 */
public interface WorkspaceListener extends Remote {
  public void artifactCreated(final ArtifactCreated event) throws RemoteException;

  public void artifactAliveSet(final ArtifactAliveSet event) throws RemoteException;

  public void artifactTypeSet(final ArtifactTypeSet event) throws RemoteException;

  public void artifactContainerSet(final ArtifactContainerSet event) throws RemoteException;

  public void propertyAliveSet(final PropertyAliveSet event) throws RemoteException;

  public void propertyValueSet(final PropertyValueSet event) throws RemoteException;

  public void propertyMapSet(final PropertyMapSet event) throws RemoteException;

  public void collectionAddedElement(final CollectionAddedElement event) throws RemoteException;
  
  public void collectionAddedElements(final CollectionAddedElements event) throws RemoteException;

  public void collectionRemovedElement(final CollectionRemovedElement event) throws RemoteException;
  
  public void putMap(final MapPut event) throws RemoteException;
  
  public void mapElementRemoved(final MapElementRemoved event) throws RemoteException;
  
  public void mapCleared(final MapCleared event) throws RemoteException;

  public void artifactCommited(final ArtifactCommited event) throws RemoteException;

  public void propertyCommited(final PropertyCommited event) throws RemoteException;

  public void versionCommited(final VersionCommited event) throws RemoteException;

  public void artifactRollback(final ArtifactRollBack event) throws RemoteException;

  public void propertyRollback(final PropertyRollBack event) throws RemoteException;

  public void workspaceRollback(final WorkspaceRollBack event) throws RemoteException;

  public void workspaceRebased(final WorkspaceRebased event) throws RemoteException;

  public void workspaceClosed(final WorkspaceClosed event) throws RemoteException;

  public void workspaceParentSet(final WorkspaceParentSet event) throws RemoteException;
}
