package at.jku.sea.cloud.rest.server.factory;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.navigator.TerminalManeuverable;
import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.navigator.PojoAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoArtifactNavigator;
import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;
import at.jku.sea.cloud.rest.pojo.navigator.PojoOwnerAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoPropertyArtifactAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoPropertyCharacterAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoPropertyNumberAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoPropertyStringAccess;
import at.jku.sea.cloud.rest.pojo.navigator.PojoStreamNavigator;
import at.jku.sea.cloud.rest.pojo.navigator.PojoToolAccess;
import at.jku.sea.cloud.stream.Contexts;

public class NavigatorFactory {

  private static NavigatorFactory INSTANCE;

  private NavigatorFactory() {
  }

  public static NavigatorFactory getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new NavigatorFactory();
    }
    return INSTANCE;
  }

  @SuppressWarnings("rawtypes")
  public TerminalOperation map(Cloud cloud, PojoNavigator navigator) {
    TerminalManeuverable pipeline = null;
    TerminalOperation last = null;
    if (navigator instanceof PojoArtifactNavigator) {
      PojoArtifact pa = ((PojoArtifactNavigator) navigator).getStartElement();
      Artifact a = cloud.getArtifact(pa.getVersion(), pa.getId());
      pipeline = cloud.queryFactory().navigatorProvider().from(a);
    } else if (navigator instanceof PojoStreamNavigator) {
      String context = ((PojoStreamNavigator) navigator).getContext();
      pipeline = cloud.queryFactory().navigatorProvider().from(Contexts.of(context));
    }
    PojoAccess[] access = navigator.getAccess();
    for (PojoAccess a : access) {
      if (a instanceof PojoOwnerAccess) {
        last = pipeline = pipeline.toOwner();
      } else if (a instanceof PojoToolAccess) {
        last = pipeline = pipeline.toTool();
      } else if (a instanceof PojoPropertyArtifactAccess) {
        last = pipeline = pipeline.to(((PojoPropertyArtifactAccess) a).getProperty());
      } else if (a instanceof PojoPropertyNumberAccess) {
        last = pipeline.toNumber(((PojoPropertyNumberAccess) a).getProperty());
      } else if (a instanceof PojoPropertyStringAccess) {
        last = pipeline.toString(((PojoPropertyStringAccess) a).getProperty());
      } else if (a instanceof PojoPropertyCharacterAccess) {
        last = pipeline.toCharacter(((PojoPropertyCharacterAccess) a).getProperty());
      }
    }
    return last == null ? pipeline : last;
  }
}
