package at.jku.sea.cloud.rest.client.handler.util;

import java.util.List;

import at.jku.sea.cloud.rest.client.stream.RestStream;
import at.jku.sea.cloud.rest.client.stream.actions.StreamAction;
import at.jku.sea.cloud.rest.client.stream.predicate.implementation.RestPredicate;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.stream.PojoMatchStream;
import at.jku.sea.cloud.rest.pojo.stream.PojoStream;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoStreamAction;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;

public class Stream2PojoFactory {

  public static <T> PojoStream map(PojoFactory pojoFactory, RestStream<T> stream) {
    PojoProvider pojoProvider = stream.getProvider().map(pojoFactory);
    PojoStreamAction[] pojoActions = map(stream.getActions(), pojoFactory);
    return new PojoStream(pojoProvider, pojoActions);
  }

  public static <T> PojoMatchStream map(PojoFactory pojoFactory, RestStream<T> stream, String context,
      RestPredicate<T> predicate) {
    PojoProvider pojoProvider = stream.getProvider().map(pojoFactory);
    PojoStreamAction[] pojoActions = map(stream.getActions(), pojoFactory);
    PojoPredicate pojoPredicate = predicate.map(pojoFactory);
    return new PojoMatchStream(pojoProvider, pojoActions, context, pojoPredicate);
  }

  private static PojoStreamAction[] map(List<StreamAction> actions, PojoFactory pojoFactory) {
    PojoStreamAction[] pojoActions = new PojoStreamAction[actions.size()];
    for (int i = 0; i < pojoActions.length; i++) {
      pojoActions[i] = actions.get(i).map(pojoFactory);
    }
    return pojoActions;
  }
}
