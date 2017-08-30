package at.jku.sea.cloud.stream.predicate;

import at.jku.sea.cloud.stream.AbstractStream;
import at.jku.sea.cloud.stream.Context;

public class PredicateStreamImpl<T> implements PredicateStream<T> {

  private final AbstractStream<T> stream;

  public PredicateStreamImpl(AbstractStream<T> stream) {
    this.stream = stream;
  }

  @Override
  public Predicate<T> anyMatch(final String context, final Predicate<T> p) {
    return new AbstractPredicate<T>() {

      @Override
      public boolean test(Context c) {
        return stream.anyMatch(context, p, c);
      }
    };
  }

  @Override
  public Predicate<T> allMatch(final String context, final Predicate<T> p) {
    return new AbstractPredicate<T>() {

      @Override
      public boolean test(Context c) {
        return stream.allMatch(context, p, c);
      }
    };
  }

  @Override
  public Predicate<T> noneMatch(final String context, final Predicate<T> p) {
    return new AbstractPredicate<T>() {

      @Override
      public boolean test(Context c) {
        return stream.noneMatch(context, p, c);
      }
    };
  }
}
