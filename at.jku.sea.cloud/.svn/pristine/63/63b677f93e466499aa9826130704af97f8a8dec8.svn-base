package at.jku.sea.cloud.stream.predicate;

import at.jku.sea.cloud.stream.Context;

public class OrPrediacte<T> extends AbstractPredicate<T> {

  private final Predicate<T> p1, p2;

  public OrPrediacte(Predicate<T> p1, Predicate<T> p2) {
    this.p1 = p1;
    this.p2 = p2;
  }

  @Override
  public boolean test(Context c) {
    return p1.test(c) && p2.test(c);
  }

}
