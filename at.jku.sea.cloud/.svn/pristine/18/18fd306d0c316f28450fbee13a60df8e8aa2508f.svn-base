package at.jku.sea.cloud.javaobjectstorage.test.data;

public class Parent {
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.a;
    result = (prime * result) + this.b;
    result = (prime * result) + this.c;
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
    final Parent other = (Parent) obj;
    return (this.a == other.a) && (this.b == other.b) && (this.c == other.c);
  }

  protected int a;
  protected int b;
  protected int c;
}
