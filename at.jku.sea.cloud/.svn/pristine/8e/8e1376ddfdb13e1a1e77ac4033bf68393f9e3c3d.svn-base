package at.jku.sea.cloud.stream;

import java.util.List;
import java.util.Objects;

import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.stream.predicate.Predicate;
import at.jku.sea.cloud.stream.predicate.PredicateStream;
import at.jku.sea.cloud.stream.predicate.PredicateStreamImpl;
import at.jku.sea.cloud.stream.provider.Provider;
import at.jku.sea.cloud.stream.sink.ChainedSink;
import at.jku.sea.cloud.stream.sink.CountSink;
import at.jku.sea.cloud.stream.sink.ExistSink;
import at.jku.sea.cloud.stream.sink.FindSink;
import at.jku.sea.cloud.stream.sink.ForallSink;
import at.jku.sea.cloud.stream.sink.ListSink;
import at.jku.sea.cloud.stream.sink.NavigatorSink;
import at.jku.sea.cloud.stream.sink.NoneSink;
import at.jku.sea.cloud.stream.sink.PredicateSink;

public abstract class AbstractStream<T> implements Stream<T> {
  protected AbstractStream<?> src;
  protected AbstractStream<?> upStream;
  protected ChainedSink<T> curSink;
  
  protected boolean isLinked = false;
  
  protected AbstractStream() {
  }
  
  protected AbstractStream(AbstractStream<?> src, AbstractStream<?> upStream, ChainedSink<T> curSink) {
    this.src = src;
    this.upStream = upStream;
    if (upStream.isLinked) {
      throw new IllegalArgumentException("Stream already linked.");
    } else {
      upStream.isLinked = true;
    }
    this.curSink = curSink;
  }
  
  @Override
  public <U> Stream<U> map(String context, TerminalOperation<U> navigator) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(navigator);
    
    NavigatorSink<T, U> navSink = new NavigatorSink<>(context, navigator);
    curSink.setDownStreamSink(navSink);
    ChainedSink<U> next = new ChainedSink<>();
    navSink.setDownStreamSink(next);
    return new IntermediateStream<U>(this.src, this, next);
  }
  
  @Override
  public T find(String context, Predicate<T> p) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(p);
    
    FindSink<T> findSink = new FindSink<>(context, p);
    curSink.setDownStreamSink(findSink);
    forEach();
    return findSink.get();
  }
  
  @Override
  public boolean allMatch(String context, Predicate<T> p) {
    return allMatch(context, p, Contexts.empty());
  }
  
  public boolean allMatch(String context, Predicate<T> p, Context c) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(p);
    Objects.requireNonNull(c);
    
    ForallSink<T> forallSink = new ForallSink<>(context, p);
    curSink.setDownStreamSink(forallSink);
    forEach(c);
    return forallSink.get();
  }
  
  @Override
  public boolean anyMatch(String context, Predicate<T> p) {
    return anyMatch(context, p, Contexts.empty());
  }
  
  public boolean anyMatch(String context, Predicate<T> p, Context c) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(p);
    Objects.requireNonNull(c);
    
    ExistSink<T> existSink = new ExistSink<>(context, p);
    curSink.setDownStreamSink(existSink);
    forEach(c);
    return existSink.get();
  }
  
  @Override
  public boolean noneMatch(String context, Predicate<T> p) {
    return noneMatch(context, p, Contexts.empty());
  }
  
  public boolean noneMatch(String context, Predicate<T> p, Context c) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(p);
    Objects.requireNonNull(c);
    
    NoneSink<T> noneSink = new NoneSink<>(context, p);
    curSink.setDownStreamSink(noneSink);
    forEach(c);
    return noneSink.get();
  }
  
  @Override
  public Stream<T> filter(String context, Predicate<T> p) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(p);
    
    ChainedSink<T> next = new PredicateSink<>(context, p);
    curSink.setDownStreamSink(next);
    return new IntermediateStream<>(this.src, this, next);
  }
  
  @Override
  public List<T> toList() {
    ListSink<T> listSink = new ListSink<>();
    curSink.setDownStreamSink(listSink);
    forEach();
    return listSink.get();
  }
  
  @Override
  public long count() {
    CountSink<T> countSink = new CountSink<>();
    curSink.setDownStreamSink(countSink);
    forEach();
    return countSink.get();
  }
  
  @Override
  public PredicateStream<T> asPredicate() {
    return new PredicateStreamImpl<>(this);
  }
  
  protected void forEach() {
    forEach(Contexts.empty());
  }
  
  protected abstract void forEach(Context c);
  
  static class HeadStream<T> extends AbstractStream<T> {
    private Provider<T> provider;
    
    public HeadStream(Provider<T> provider) {
      Objects.requireNonNull(provider);
      
      this.provider = provider;
      curSink = new ChainedSink<T>();
      src = this;
    }
    
    @Override
    protected void forEach(Context c) {
      for (T t : provider.get()) {
        curSink.apply(c, t);
        if (curSink.isCancelled()) {
          break;
        }
      }
    }
  }
  
  static class IntermediateStream<T> extends AbstractStream<T> {
    public IntermediateStream(AbstractStream<?> src, AbstractStream<?> upStream, ChainedSink<T> next) {
      super(src, upStream, next);
    }
    
    @Override
    protected void forEach(Context c) {
      src.forEach(c);
    }
  }
}