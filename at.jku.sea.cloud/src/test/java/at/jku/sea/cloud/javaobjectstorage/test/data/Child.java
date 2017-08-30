package at.jku.sea.cloud.javaobjectstorage.test.data;

public class Child extends Parent {
  public Child(final float a, final String b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = (prime * result) + Float.floatToIntBits(this.a);
    result = (prime * result) + ((this.b == null) ? 0 : this.b.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Child other = (Child) obj;
    if (Float.floatToIntBits(this.a) != Float.floatToIntBits(other.a)) {
      return false;
    }
    if (this.b == null) {
      if (other.b != null) {
        return false;
      }
    } else if (!this.b.equals(other.b)) {
      return false;
    }
    return true;
  }

  private final float a;
  private final String b;
}
