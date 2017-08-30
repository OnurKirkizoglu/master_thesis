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
 * Tool.java created on 22.07.2013
 * 
 * (c) alexander noehrer
 */
package at.jku.sea.cloud;

import java.util.Collection;

/**
 * Represents a tool (e.g., Excel, IBM RSA, ProE) that is integrated with the DesignSpace. Changes to {@link Artifact}s
 * or {@link Property}s are performed with a specific tool. The same {@link Artifact}s or {@link Property}s can in
 * different {@link Version}s be edited from different tools.
 * 
 * Tools should define a string property "name" and if appropriate a property "toolVersion" describing the version of the tool,
 * like for instance Java 1.6, 1.7 or 8.
 * 
 * @author alexander noehrer
 * @author mriedl
 */
public interface Tool extends Artifact {

  /**
   * Returns the collection of {@link Artifact}s that where at some point in the version history edited from the tool.
   * 
   * @return returns the collection of {@link Artifact}s edited from the tool. Each {@link Artifact} is instantiated in
   *         the current {@link Version} of the tool.
   */
  public Collection<Artifact> getArtifacts();
  
  /**
   * Returns the name of the tool.
   * @return 
   */
  public String getName();
  

  /**
   * Version of the tool, e.g. Java 1.6, 1.7, 8
   * @return
   */
  public String getToolVersion();
}
