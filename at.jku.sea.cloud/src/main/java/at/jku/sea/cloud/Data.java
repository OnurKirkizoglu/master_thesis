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
 * Data.java created on Sep 18, 2013
 *
 * (c) alexander noehrer
 */
package at.jku.sea.cloud;

import java.io.Serializable;

/**
 * Base class for both {@link Artifact} and {@link Property}
 *
 * @author alexander noehrer
 */
public interface Data extends Serializable {

  /**
   * Returns the id of the data (for both {@link Artifact} and {@link Property} the id of the {@link Artifact})
   *
   * @return the id of the data
   */
  public long getId();

  /**
   * Returns the version number for which the data was obtained.
   *
   * @return the obtained version number.
   */
  public long getVersionNumber();

  /**
   * Returns the {@link Owner} of the data (i.e., the <tt>last</tt> owner editing either the {@link Artifact} or
   * {@link Property})
   *
   * @return the <tt>last</tt> {@link Owner} of the data.
   */
  public Owner getOwner();

  /**
   * Returns the {@link Tool} of the data (i.e., the <tt>last</tt> owner editing either the {@link Artifact} or
   * {@link Property})
   *
   * @return the <tt>last</tt> {@link Tool} of the data.
   */
  public Tool getTool();

}
