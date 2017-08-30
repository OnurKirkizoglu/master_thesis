package at.jku.sea.cloud.stream.predicate;

import java.util.Objects;

public abstract class AbstractPredicate<T> implements Predicate<T> {

  @Override
  public Predicate<T> and(Predicate<T> p) {
    Objects.requireNonNull(p);
    return new AndPrediacte<>(this, p);
  }

  @Override
  public Predicate<T> or(Predicate<T> p) {
    Objects.requireNonNull(p);
    return new OrPrediacte<>(this, p);
  }

}
