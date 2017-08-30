package at.jku.sea.cloud.stream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.jku.sea.cloud.stream.Context.Path;

public class Contexts {
  
  private static final Context EMPTY = new ContextImpl();
  
  private Contexts() {
  }
  
  public static Context empty() {
    return EMPTY;
  }
  
  public static Context.Path of(String name) {
    Objects.requireNonNull(name);
    return new PathImpl(name);
  }
  
  private static class PathImpl implements Path {
    
    private final String name;
    
    public PathImpl(String name) {
      this.name = name;
    }
    
    @Override
    public String get() {
      return name;
    }
    
  }
  
  private static class ContextImpl implements Context {
    private final Map<String, Object> contexts;
    
    private ContextImpl() {
      this.contexts = Collections.emptyMap();
    }
    
    private ContextImpl(Map<String, Object> contexts) {
      this.contexts = Collections.unmodifiableMap(contexts);
    }
    
    @Override
    public <T> T get(String name) {
      if (!contexts.containsKey(name)) {
        throw new IllegalArgumentException("Unknown context label: " + name);
      }
      @SuppressWarnings("unchecked")
      T result = (T) contexts.get(name);
      return result;
    }
    
    @Override
    public Context put(String name, Object o) {
      Objects.requireNonNull(name);
      if (contexts.containsKey(name)) {
        throw new IllegalArgumentException("Context already bound to this name: " + name);
      }
      Map<String, Object> newContexts = new HashMap<>(contexts);
      newContexts.put(name, o);
      return new ContextImpl(newContexts);
    }
  }
}
