package at.jku.sea.cloud.stream.provider;

public interface Provider<T> {
  Iterable<T> get();
}