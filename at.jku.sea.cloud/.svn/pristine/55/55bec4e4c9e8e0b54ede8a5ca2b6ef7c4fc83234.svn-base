package at.jku.sea.cloud;

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
 * Version.java created on Sep 17, 2013
 * 
 * (c) alexander noehrer
 */

/**
 * Base class for both public and private versions (i.e., {@link PublicVersion} and {@link Workspace}, respectively).
 * 
 * @author alexander noehrer
 */
public interface Version {
  /**
   * Returns the version number. <0 if the version is private. >0 if the version is public.
   * 
   * @return the version number.
   */
  public long getVersionNumber();

  /**
   * Returns the {@link Owner}, which created the version.
   * 
   * @return the owner, which created the version.
   */
  public Owner getOwner();

  /**
   * Returns the {@link Tool}, which created the version.
   * 
   * @return the tool, which created the version.
   */
  public Tool getTool();

  /**
   * Returns the specified identifier of the version.
   * 
   * @return the specified identifier of the version.
   */
  public String getIdentifier();
}
