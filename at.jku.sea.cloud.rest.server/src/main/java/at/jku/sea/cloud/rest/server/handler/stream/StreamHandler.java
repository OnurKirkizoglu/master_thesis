package at.jku.sea.cloud.rest.server.handler.stream;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.stream.PojoMatchStream;
import at.jku.sea.cloud.rest.pojo.stream.PojoStream;
import at.jku.sea.cloud.rest.server.factory.DesignSpaceFactory;
import at.jku.sea.cloud.rest.server.factory.NavigatorFactory;
import at.jku.sea.cloud.rest.server.factory.StreamFactory;
import at.jku.sea.cloud.stream.Stream;
import at.jku.sea.cloud.stream.predicate.Predicate;

public class StreamHandler {
  
  @Autowired
  protected Cloud cloud;
  @Autowired
  protected PojoFactory pojoFactory;
  @Autowired
  protected NavigatorFactory navigatorFactory;
  @Autowired
  protected StreamFactory streamFactory;
  @Autowired
  protected DesignSpaceFactory dsFactory;
  
  public PojoObject find(PojoMatchStream pojoStream)
      throws PropertyDoesNotExistException, NoSuchElementException, IllegalArgumentException {
    @SuppressWarnings("rawtypes")
    Stream stream = streamFactory.fromPojoStream(cloud, navigatorFactory, dsFactory, pojoStream);
    
    String context = pojoStream.getContext();
    PojoPredicate pojoPredicate = pojoStream.getPredicate();
    
    @SuppressWarnings("rawtypes")
    Predicate p = streamFactory.fromPojoPredicate(cloud, navigatorFactory, dsFactory, pojoPredicate);
    
    @SuppressWarnings("unchecked")
    Object o = stream.find(context, p);
    
    return pojoFactory.createPojo(o);
  }
  
  public Boolean anyMatch(PojoMatchStream pojoStream) throws PropertyDoesNotExistException, IllegalArgumentException {
    @SuppressWarnings("rawtypes")
    Stream stream = streamFactory.fromPojoStream(cloud, navigatorFactory, dsFactory, pojoStream);
    
    String context = pojoStream.getContext();
    PojoPredicate pojoPredicate = pojoStream.getPredicate();
    
    @SuppressWarnings("rawtypes")
    Predicate p = streamFactory.fromPojoPredicate(cloud, navigatorFactory, dsFactory, pojoPredicate);
    
    @SuppressWarnings("unchecked")
    boolean anyMatch = stream.anyMatch(context, p);
    
    return anyMatch;
  }
  
  public Boolean allMatch(PojoMatchStream pojoStream) throws PropertyDoesNotExistException, IllegalArgumentException {
    @SuppressWarnings("rawtypes")
    Stream stream = streamFactory.fromPojoStream(cloud, navigatorFactory, dsFactory, pojoStream);
    
    String context = pojoStream.getContext();
    PojoPredicate pojoPredicate = pojoStream.getPredicate();
    
    @SuppressWarnings("rawtypes")
    Predicate p = streamFactory.fromPojoPredicate(cloud, navigatorFactory, dsFactory, pojoPredicate);
    
    @SuppressWarnings("unchecked")
    boolean allMatch = stream.allMatch(context, p);
    
    return allMatch;
  }
  
  public Boolean noneMatch(PojoMatchStream pojoStream) throws PropertyDoesNotExistException, IllegalArgumentException {
    @SuppressWarnings("rawtypes")
    Stream stream = streamFactory.fromPojoStream(cloud, navigatorFactory, dsFactory, pojoStream);
    
    String context = pojoStream.getContext();
    PojoPredicate pojoPredicate = pojoStream.getPredicate();
    
    @SuppressWarnings("rawtypes")
    Predicate p = streamFactory.fromPojoPredicate(cloud, navigatorFactory, dsFactory, pojoPredicate);
    
    @SuppressWarnings("unchecked")
    boolean noneMatch = stream.noneMatch(context, p);
    
    return noneMatch;
  }
  
  public PojoObject[] toList(PojoStream pojoStream) throws PropertyDoesNotExistException, IllegalArgumentException {
    
    @SuppressWarnings("rawtypes")
    Stream stream = streamFactory.fromPojoStream(cloud, navigatorFactory, dsFactory, pojoStream);
    
    @SuppressWarnings("unchecked")
    List<Object> list = stream.toList();
    
    PojoObject[] objects = pojoFactory.createPojoArray(list.toArray());
    
    return objects;
  }
  
  public Long count(PojoStream pojoStream) throws PropertyDoesNotExistException, IllegalArgumentException {
    @SuppressWarnings("rawtypes")
    Stream stream = streamFactory.fromPojoStream(cloud, navigatorFactory, dsFactory, pojoStream);
    
    Long count = stream.count();
    
    return count;
  }
  
}
