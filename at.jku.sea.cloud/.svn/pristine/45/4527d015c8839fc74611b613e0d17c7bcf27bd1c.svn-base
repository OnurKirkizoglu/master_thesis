package at.jku.sea.cloud.javaobjectstorage.test.data;

import java.util.Arrays;

public class Node {
  public Node(final int value, final Node parent, final Node... children) {
    this.value = value;
    this.parent = parent;
    this.children = children;
  }

  public void setParent(final Node parent) {
    this.parent = parent;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(this.children);
    // result = prime * result + ((parent == null) ? 0 : parent.hashCode());
    result = (prime * result) + this.value;
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Node other = (Node) obj;
    if (!Arrays.equals(this.children, other.children)) {
      return false;
    }
    // if (parent == null) {
    // if (other.parent != null) return false;
    // } else if (!parent.equals(other.parent)) return false;
    if (this.value != other.value) {
      return false;
    }

    return true;
  }

  private final int value;
  @SuppressWarnings("unused")
  private Node parent;
  private final Node[] children;
}
