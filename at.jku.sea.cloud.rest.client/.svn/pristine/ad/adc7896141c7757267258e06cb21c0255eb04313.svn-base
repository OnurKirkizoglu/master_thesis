package at.jku.sea.cloud.rest.client.stream.actions;

import at.jku.sea.cloud.rest.client.stream.predicate.implementation.RestPredicate;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoFilterAction;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoStreamAction;

public class FilterAction<T> extends StreamAction {

  private final RestPredicate<T> p;

  public FilterAction(String context, RestPredicate<T> p) {
    super(context);
    this.p = p;
  }

  public RestPredicate<T> getPredicate() {
    return p;
  }

  @Override
  public PojoStreamAction map(PojoFactory pojoFactory) {
    return new PojoFilterAction(context, p.map(pojoFactory));
  }

}
