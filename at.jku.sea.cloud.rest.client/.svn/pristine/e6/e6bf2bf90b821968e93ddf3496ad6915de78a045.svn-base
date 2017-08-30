package at.jku.sea.cloud.rest.client.handler.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.client.HttpClientErrorException;

import at.jku.sea.cloud.exceptions.PropertyDoesNotExistException;
import at.jku.sea.cloud.rest.client.handler.AbstractHandler;
import at.jku.sea.cloud.rest.client.handler.util.Stream2PojoFactory;
import at.jku.sea.cloud.rest.client.stream.RestStream;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.RestPredicate;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.stream.PojoMatchStream;
import at.jku.sea.cloud.rest.pojo.stream.PojoStream;

public class StreamHandler extends AbstractHandler {
  
  public <T> T find(RestStream<T> restStream, String context, RestPredicate<T> p) throws NoSuchElementException {
    PojoMatchStream pojoStream = Stream2PojoFactory.map(pojoFactory, restStream, context, p);
    PojoObject response = null;
    try {
      response = template.postForEntity(STREAM_ADDRESS + "/find", pojoStream, PojoObject.class).getBody();
    } catch (HttpClientErrorException e) {
      if (NoSuchElementException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new NoSuchElementException();
      } else if (PropertyDoesNotExistException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new PropertyDoesNotExistException();
      } else if (IllegalArgumentException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new IllegalArgumentException("Context not found");
      }
      throw e;
    }
    @SuppressWarnings("unchecked")
    T result = (T) restFactory.createRest(response);
    return result;
  }
  
  public <T> boolean anyMatch(RestStream<T> restStream, String context, RestPredicate<T> p) {
    PojoMatchStream pojoStream = Stream2PojoFactory.map(pojoFactory, restStream, context, p);
    Boolean response = null;
    try {
      response = template.postForEntity(STREAM_ADDRESS + "/anymatch", pojoStream, Boolean.class).getBody();
    } catch (HttpClientErrorException e) {
      if (PropertyDoesNotExistException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new PropertyDoesNotExistException();
      } else if (IllegalArgumentException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new IllegalArgumentException("Context not found");
      }
      throw e;
    }
    return response;
  }
  
  public <T> boolean allMatch(RestStream<T> restStream, String context, RestPredicate<T> p) {
    PojoMatchStream pojoStream = Stream2PojoFactory.map(pojoFactory, restStream, context, p);
    Boolean response = null;
    try {
      response = template.postForEntity(STREAM_ADDRESS + "/allmatch", pojoStream, Boolean.class).getBody();
    } catch (HttpClientErrorException e) {
      if (PropertyDoesNotExistException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new PropertyDoesNotExistException();
      } else if (IllegalArgumentException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new IllegalArgumentException("Context not found");
      }
      throw e;
    }
    return response;
  }
  
  public <T> boolean noneMatch(RestStream<T> restStream, String context, RestPredicate<T> p) {
    PojoMatchStream pojoStream = Stream2PojoFactory.map(pojoFactory, restStream, context, p);
    Boolean response = null;
    try {
      response = template.postForEntity(STREAM_ADDRESS + "/nonematch", pojoStream, Boolean.class).getBody();
    } catch (HttpClientErrorException e) {
      if (PropertyDoesNotExistException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new PropertyDoesNotExistException();
      } else if (IllegalArgumentException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new IllegalArgumentException("Context not found");
      }
      throw e;
    }
    return response;
  }
  
  public <T> List<T> toList(RestStream<T> restStream) {
    PojoStream pojoStream = Stream2PojoFactory.map(pojoFactory, restStream);
    PojoObject[] response = null;
    try {
      response = template.postForEntity(STREAM_ADDRESS + "/tolist", pojoStream, PojoObject[].class).getBody();
    } catch (HttpClientErrorException e) {
      if (PropertyDoesNotExistException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new PropertyDoesNotExistException();
      } else if (IllegalArgumentException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new IllegalArgumentException("Context not found");
      }
      throw e;
    }
    @SuppressWarnings("unchecked")
    List<T> result = new ArrayList<T>(Arrays.asList((T[]) restFactory.createRestArray(response)));
    return result;
    
  }
  
  public <T> long count(RestStream<T> restStream) {
    PojoStream pojoStream = Stream2PojoFactory.map(pojoFactory, restStream);
    Long response = null;
    try {
      response = template.postForEntity(STREAM_ADDRESS + "/count", pojoStream, Long.class).getBody();
    } catch (HttpClientErrorException e) {
      if (PropertyDoesNotExistException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new PropertyDoesNotExistException();
      } else if (IllegalArgumentException.class.getSimpleName().equals(e.getResponseBodyAsString())) {
        throw new IllegalArgumentException("Context not found");
      }
      throw e;
    }
    return response;
  }
  
}
