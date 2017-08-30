package at.jku.sea.cloud.javaobjectstorage.test.data;

public class ComplexObject {
  public ComplexObject(final int a, final int b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.a;
    result = (prime * result) + this.b;
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
    final ComplexObject other = (ComplexObject) obj;
    return (this.a == other.a) && (this.b == other.b);
  }

  private final int a;
  private final int b;
}
