package at.jku.sea.cloud;

import java.util.Collection;
import java.util.Set;

import at.jku.sea.cloud.exceptions.PropertyDeadException;
import at.jku.sea.cloud.exceptions.TypeNotSupportedException;

/**
 * An version controlled object that maps keys to values. A map cannot contain duplicate keys; each key can map to at
 * most one value.
 *
 * Keys and values can be any of the following types: {@link Number}, {@link Boolean}, {@link Character}, {@link String}
 * , {@link Artifact}
 */
public interface MapArtifact extends Artifact {

  /**
   * Returns the size of the map.
   *
   * @return the size of the map.
   */
  long size();

  /**
   * Returns <tt>true</tt> if this map contains a mapping for the specified key. More formally, returns <tt>true</tt> if
   * and only if this map contains a mapping for a key <tt>k</tt> such that
   * <tt>(key==null ? k==null : key.equals(k))</tt>. (There can be at most one such mapping.)
   *
   * @param key
   *          key whose presence in this map is to be tested
   * @return <tt>true</tt> if this map contains a mapping for the specified key
   */
  boolean containsKey(Object key);

  /**
   * Returns <tt>true</tt> if this map maps one or more keys to the specified value. More formally, returns
   * <tt>true</tt> if and only if this map contains at least one mapping to a value <tt>v</tt> such that
   * <tt>(value==null ? v==null : value.equals(v))</tt>. This operation will probably require time linear in the map
   * size for most implementations of the <tt>Map</tt> interface.
   *
   * @param value
   *          value whose presence in this map is to be tested
   * @return <tt>true</tt> if this map maps one or more keys to the specified value
   */
  boolean containsValue(Object value);

  /**
   * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the
   * key.
   *
   * <p>
   * More formally, if this map contains a mapping from a key {@code k} to a value {@code v} such that
   * {@code (key==null ? k==null :
   * key.equals(k))}, then this method returns {@code v}; otherwise it returns {@code null}. (There can be at most one
   * such mapping.)
   *
   * @param key
   *          the key whose associated value is to be returned
   * @return the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key
   */
  Object get(Object key);

  /**
   * Associates the specified value with the specified key in this map. If the map previously contained a mapping for
   * the key, the old value is replaced by the specified value. (A map <tt>m</tt> is said to contain a mapping for a key
   * <tt>k</tt> if and only if {@link #containsKey(Object) m.containsKey(k)} would return <tt>true</tt>.)
   *
   * @param workspace
   *          workspace which stores the change
   * @param key
   *          key with which the specified value is to be associated
   * @param value
   *          value to be associated with the specified key
   * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for <tt>key</tt>.
   * @throws TypeNotSupportedException
   *           if key or value is not in the specified bounds
   * @throws PropertyDeadException
   *           if value property got deleted
   */
  Object put(Workspace workspace, Object key, Object value) throws TypeNotSupportedException, PropertyDeadException;

  /**
   * Removes the mapping for a key from this map if it is present. More formally, if this map contains a mapping from
   * key <tt>k</tt> to value <tt>v</tt> such that <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is
   * removed. (The map can contain at most one such mapping.)
   *
   * <p>
   * Returns the value to which this map previously associated the key, or <tt>null</tt> if the map contained no mapping
   * for the key.
   *
   * <p>
   * The map will not contain a mapping for the specified key once the call returns.
   *
   * @param key
   *          key whose mapping is to be removed from the map
   * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for <tt>key</tt>.
   */
  Object remove(Workspace workspace, Object key) throws TypeNotSupportedException;

  /**
   * Removes all of the mappings from this map. The map will be empty after this call returns.
   *
   * @param workspace
   *          workspace which stores the changes
   */
  void clear(Workspace workspace);

  /**
   * Returns a {@link Set} view of the keys contained in this map.
   *
   * @return a set view of the keys contained in this map
   */
  Set<Object> keySet();

  /**
   * Returns a {@link Collection} view of the values contained in this map.
   *
   * @return a collection view of the values contained in this map
   */
  Collection<Object> values();

  /**
   *
   * Returns a {@link Set} view of the mappings contained in this map.
   *
   * @return a set view of the mappings contained in this map
   */
  Set<Entry> entrySet();

  interface Entry extends Artifact {
    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key corresponding to this entry
     */
    Object getKey();

    /**
     * Returns the value corresponding to this entry.
     *
     * @return the value corresponding to this entry
     */
    Object getValue();

    /**
     * Replaces the value corresponding to this entry with the specified value. (Writes through to the map.) The
     * behavior of this call is undefined if the mapping has already been removed from the map
     *
     * @param workspace
     *          workspace stores the changes
     * @param value
     *          new value to be stored in this entry
     * @return old value corresponding to the entry
     * @throws TypeNotSupportedException
     *           if key or value is not in the specified bounds
     * @throws PropertyDeadException
     *           if value property got deleted
     */
    Object setValue(Workspace workspace, Object value) throws TypeNotSupportedException, PropertyDeadException;

    /**
     * Compares the specified object with this entry for equality. Returns <tt>true</tt> if the given object is also a
     * map entry and the two entries represent the same mapping. More formally, two entries <tt>e1</tt> and <tt>e2</tt>
     * represent the same mapping if
     *
     * <pre>
     * (e1.getKey() == null ? e2.getKey() == null : e1.getKey().equals(e2.getKey())) &amp;&amp; (e1.getValue() == null ? e2.getValue() == null : e1.getValue().equals(e2.getValue()))
     * </pre>
     *
     * This ensures that the <tt>equals</tt> method works properly across different implementations of the
     * <tt>Map.Entry</tt> interface.
     *
     * @param o
     *          object to be compared for equality with this map entry
     * @return <tt>true</tt> if the specified object is equal to this map entry
     */
    @Override
    boolean equals(Object o);

    /**
     * Returns the hash code value for this map entry. The hash code of a map entry <tt>e</tt> is defined to be:
     *
     * <pre>
     * (e.getKey() == null ? 0 : e.getKey().hashCode()) &circ; (e.getValue() == null ? 0 : e.getValue().hashCode())
     * </pre>
     *
     * This ensures that <tt>e1.equals(e2)</tt> implies that <tt>e1.hashCode()==e2.hashCode()</tt> for any two Entries
     * <tt>e1</tt> and <tt>e2</tt>, as required by the general contract of <tt>Object.hashCode</tt>.
     *
     * @return the hash code value for this map entry
     * @see Object#hashCode()
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    @Override
    int hashCode();
  }
}
