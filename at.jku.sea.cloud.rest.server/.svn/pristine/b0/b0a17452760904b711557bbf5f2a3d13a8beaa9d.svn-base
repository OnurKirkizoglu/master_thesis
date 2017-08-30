package at.jku.sea.cloud.rest.server.factory;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Container;
import at.jku.sea.cloud.Container.Filter;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Resource;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.navigator.TerminalOperation;
import at.jku.sea.cloud.rest.pojo.PojoArtifact;
import at.jku.sea.cloud.rest.pojo.PojoCollectionArtifact;
import at.jku.sea.cloud.rest.pojo.PojoContainer;
import at.jku.sea.cloud.rest.pojo.PojoMapArtifact;
import at.jku.sea.cloud.rest.pojo.PojoMetaModel;
import at.jku.sea.cloud.rest.pojo.PojoObject;
import at.jku.sea.cloud.rest.pojo.PojoOwner;
import at.jku.sea.cloud.rest.pojo.PojoOwnerToolStringLong;
import at.jku.sea.cloud.rest.pojo.PojoProject;
import at.jku.sea.cloud.rest.pojo.PojoResource;
import at.jku.sea.cloud.rest.pojo.PojoTool;
import at.jku.sea.cloud.rest.pojo.navigator.PojoNavigator;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicate;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateAllMatch;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateAnd;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateAnyMatch;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateContainsArtifact;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateContainsKey;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateContainsValue;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateEq;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateEqContext;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateExistsElement;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasId;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasOwner;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasProperty;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasPropertyValue;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasTool;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasType;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateHasVersion;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateInContainer;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateInv;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateIsAlive;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateIsPropertyAlive;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateNoneMatch;
import at.jku.sea.cloud.rest.pojo.predicate.PojoPredicateOr;
import at.jku.sea.cloud.rest.pojo.stream.PojoStream;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoFilterAction;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoMapAction;
import at.jku.sea.cloud.rest.pojo.stream.action.PojoStreamAction;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoCollectionArtifactOnlyArtifactProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoCollectionArtifactProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoCollectionProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoContainerProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoContainerWithFilterProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoMapArtifactKeyProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoMapArtifactValueProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoMetaModelProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoOwnerProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProjectPackageProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProjectProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoResourceProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoToolProvider;
import at.jku.sea.cloud.rest.pojo.stream.provider.PojoWorkspaceWithFilterProvider;
import at.jku.sea.cloud.stream.Contexts;
import at.jku.sea.cloud.stream.Stream;
import at.jku.sea.cloud.stream.predicate.Predicate;

public class StreamFactory {
  private static StreamFactory INSTANCE;

  private StreamFactory() {
  }

  public static StreamFactory getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new StreamFactory();
    }
    return INSTANCE;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Stream fromPojoStream(Cloud c, NavigatorFactory navigatorFactory, DesignSpaceFactory dsFactory,
      PojoStream pojoStream) {
    Stream stream = fromPojoProvider(c, pojoStream.getProvider(), dsFactory);
    for (PojoStreamAction action : pojoStream.getActions()) {
      if (action instanceof PojoFilterAction) {
        PojoPredicate pojoPredicate = ((PojoFilterAction) action).getPredicate();
        String context = action.getContext();

        Predicate p = fromPojoPredicate(c, navigatorFactory, dsFactory, pojoPredicate);

        stream = stream.filter(context, p);

      } else if (action instanceof PojoMapAction) {
        PojoNavigator pojoNavigator = ((PojoMapAction) action).getNavigator();
        String context = action.getContext();

        TerminalOperation navigator = navigatorFactory.map(c, pojoNavigator);

        stream = stream.map(context, navigator);
      }
    }
    return stream;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Predicate fromPojoPredicate(Cloud c, NavigatorFactory navigatorFactory, DesignSpaceFactory dsFactory,
      PojoPredicate predicate) {
    if (predicate instanceof PojoPredicateAllMatch) {
      PojoStream pojoStream = ((PojoPredicateAllMatch) predicate).getStream();
      String context = ((PojoPredicateAllMatch) predicate).getContext();
      PojoPredicate pojoPredicate = ((PojoPredicateAllMatch) predicate).getPredicate();

      Stream stream = fromPojoStream(c, navigatorFactory, dsFactory, pojoStream);
      Predicate p = fromPojoPredicate(c, navigatorFactory, dsFactory, pojoPredicate);

      return stream.asPredicate().allMatch(context, p);

    } else if (predicate instanceof PojoPredicateAnyMatch) {
      PojoStream pojoStream = ((PojoPredicateAnyMatch) predicate).getStream();
      String context = ((PojoPredicateAnyMatch) predicate).getContext();
      PojoPredicate pojoPredicate = ((PojoPredicateAnyMatch) predicate).getPredicate();

      Stream stream = fromPojoStream(c, navigatorFactory, dsFactory, pojoStream);
      Predicate p = fromPojoPredicate(c, navigatorFactory, dsFactory, pojoPredicate);

      return stream.asPredicate().anyMatch(context, p);

    } else if (predicate instanceof PojoPredicateContainsArtifact) {
      PojoArtifact pojoArtifact = ((PojoPredicateContainsArtifact) predicate).getArtifact();
      String context = ((PojoPredicateContainsArtifact) predicate).getContext();
      Artifact artifact = c.getArtifact(pojoArtifact.getVersion(), pojoArtifact.getId());

      return c.queryFactory().predicateProvider().containsArtifact(Contexts.of(context), artifact);

    } else if (predicate instanceof PojoPredicateContainsKey) {
      PojoObject pojoObject = ((PojoPredicateContainsKey) predicate).getObject();
      String context = ((PojoPredicateContainsKey) predicate).getContext();
      Object object = dsFactory.createDS(pojoObject);

      return c.queryFactory().predicateProvider().containsKey(Contexts.of(context), object);

    } else if (predicate instanceof PojoPredicateContainsValue) {
      PojoObject pojoObject = ((PojoPredicateContainsValue) predicate).getObject();
      String context = ((PojoPredicateContainsValue) predicate).getContext();
      Object object = dsFactory.createDS(pojoObject);

      return c.queryFactory().predicateProvider().containsValue(Contexts.of(context), object);

    } else if (predicate instanceof PojoPredicateEq) {
      PojoObject pojoObject = ((PojoPredicateEq) predicate).getObject();
      String context = ((PojoPredicateEq) predicate).getContext();
      Object object = dsFactory.createDS(pojoObject);

      return c.queryFactory().predicateProvider().eq(Contexts.of(context), object);

    } else if (predicate instanceof PojoPredicateEqContext) {
      String context1 = ((PojoPredicateEqContext) predicate).getContext1();
      String context2 = ((PojoPredicateEqContext) predicate).getContext2();

      return c.queryFactory().predicateProvider().eq(Contexts.of(context1), Contexts.of(context2));

    } else if (predicate instanceof PojoPredicateExistsElement) {
      PojoObject pojoObject = ((PojoPredicateExistsElement) predicate).getObject();
      String context = ((PojoPredicateExistsElement) predicate).getContext();
      Object object = dsFactory.createDS(pojoObject);

      return c.queryFactory().predicateProvider().existsElement(Contexts.of(context), object);

    } else if (predicate instanceof PojoPredicateHasId) {
      Long id = ((PojoPredicateHasId) predicate).getId();
      String context = ((PojoPredicateHasId) predicate).getContext();

      return c.queryFactory().predicateProvider().hasId(Contexts.of(context), id);

    } else if (predicate instanceof PojoPredicateHasOwner) {
      PojoOwner pojoOwner = ((PojoPredicateHasOwner) predicate).getOwner();
      String context = ((PojoPredicateHasOwner) predicate).getContext();
      Owner owner = c.getOwner(pojoOwner.getId());

      return c.queryFactory().predicateProvider().hasOwner(Contexts.of(context), owner);

    } else if (predicate instanceof PojoPredicateHasProperty) {
      String property = ((PojoPredicateHasProperty) predicate).getProperty();
      String context = ((PojoPredicateHasProperty) predicate).getContext();

      return c.queryFactory().predicateProvider().hasProperty(Contexts.of(context), property);

    } else if (predicate instanceof PojoPredicateHasPropertyValue) {
      String name = ((PojoPredicateHasPropertyValue) predicate).getName();
      String context = ((PojoPredicateHasPropertyValue) predicate).getContext();
      Object value = dsFactory.createDS(((PojoPredicateHasPropertyValue) predicate).getValue());

      return c.queryFactory().predicateProvider().hasPropertyValue(Contexts.of(context), name, value);

    } else if (predicate instanceof PojoPredicateHasTool) {
      PojoTool pojoTool = ((PojoPredicateHasTool) predicate).getTool();
      String context = ((PojoPredicateHasTool) predicate).getContext();
      Tool tool = c.getTool(pojoTool.getId());

      return c.queryFactory().predicateProvider().hasTool(Contexts.of(context), tool);

    } else if (predicate instanceof PojoPredicateHasType) {
      PojoArtifact pojoArtifact = ((PojoPredicateHasType) predicate).getType();
      String context = ((PojoPredicateHasType) predicate).getContext();
      Artifact type = c.getArtifact(pojoArtifact.getVersion(), pojoArtifact.getId());

      return c.queryFactory().predicateProvider().hasType(Contexts.of(context), type);

    } else if (predicate instanceof PojoPredicateHasVersion) {
      Long version = ((PojoPredicateHasVersion) predicate).getVersion();
      String context = ((PojoPredicateHasVersion) predicate).getContext();

      return c.queryFactory().predicateProvider().hasVersion(Contexts.of(context), version);

    } else if (predicate instanceof PojoPredicateInContainer) {
      PojoContainer pojoContainer = ((PojoPredicateInContainer) predicate).getContainer();
      String context = ((PojoPredicateInContainer) predicate).getContext();
      Container container = (Container) c.getArtifact(pojoContainer.getVersion(), pojoContainer.getId());

      return c.queryFactory().predicateProvider().inContainer(Contexts.of(context), container);

    } else if (predicate instanceof PojoPredicateInv) {
      PojoPredicate pojoPredicate = ((PojoPredicateInv) predicate).getPredicate();
      Predicate p = fromPojoPredicate(c, navigatorFactory, dsFactory, pojoPredicate);

      return c.queryFactory().predicateProvider().inv(p);

    } else if (predicate instanceof PojoPredicateIsAlive) {
      String context = ((PojoPredicateIsAlive) predicate).getContext();

      return c.queryFactory().predicateProvider().isAlive(Contexts.of(context));

    } else if (predicate instanceof PojoPredicateIsPropertyAlive) {
      String property = ((PojoPredicateIsPropertyAlive) predicate).getProperty();
      String context = ((PojoPredicateIsPropertyAlive) predicate).getContext();

      return c.queryFactory().predicateProvider().isPropertyAlive(Contexts.of(context), property);

    } else if (predicate instanceof PojoPredicateNoneMatch) {
      PojoStream pojoStream = ((PojoPredicateNoneMatch) predicate).getStream();
      String context = ((PojoPredicateNoneMatch) predicate).getContext();
      PojoPredicate pojoPredicate = ((PojoPredicateNoneMatch) predicate).getPredicate();

      Stream stream = fromPojoStream(c, navigatorFactory, dsFactory, pojoStream);
      Predicate p = fromPojoPredicate(c, navigatorFactory, dsFactory, pojoPredicate);
      return stream.asPredicate().noneMatch(context, p);
    } else if (predicate instanceof PojoPredicateOr) {
      Predicate p1 = fromPojoPredicate(c, navigatorFactory, dsFactory, ((PojoPredicateOr) predicate).getPredicate1());
      Predicate p2 = fromPojoPredicate(c, navigatorFactory, dsFactory, ((PojoPredicateOr) predicate).getPredicate2());
      return p1.or(p2);
    } else if (predicate instanceof PojoPredicateAnd) {
      Predicate p1 = fromPojoPredicate(c, navigatorFactory, dsFactory, ((PojoPredicateAnd) predicate).getPredicate1());
      Predicate p2 = fromPojoPredicate(c, navigatorFactory, dsFactory, ((PojoPredicateAnd) predicate).getPredicate2());
      return p1.and(p2);
    }
    return null;

  }

  @SuppressWarnings({ "rawtypes" })
  public Stream fromPojoProvider(Cloud c, PojoProvider provider, DesignSpaceFactory dsFactory) {
    if (provider instanceof PojoCollectionArtifactOnlyArtifactProvider) {
      PojoCollectionArtifact pca = ((PojoCollectionArtifactOnlyArtifactProvider) provider).getCollectionArtifact();
      CollectionArtifact ca = c.getCollectionArtifact(pca.getVersion(), pca.getId());
      return c.queryFactory().streamProvider().onlyArtifacts(ca);
    } else if (provider instanceof PojoCollectionArtifactProvider) {
      PojoCollectionArtifact pca = ((PojoCollectionArtifactProvider) provider).getCollectionArtifact();
      CollectionArtifact ca = c.getCollectionArtifact(pca.getVersion(), pca.getId());
      return c.queryFactory().streamProvider().onlyArtifacts(ca);
    } else if (provider instanceof PojoCollectionProvider) {
      PojoObject[] pojoObjects = ((PojoCollectionProvider) provider).getObjects();
      Object[] objects = new Object[pojoObjects.length];
      for (int i = 0; i < objects.length; i++) {
        objects[i] = dsFactory.createDS(pojoObjects[i]);
      }
      return c.queryFactory().streamProvider().of(objects);
    } else if (provider instanceof PojoContainerProvider) {
      PojoContainer pojoContainer = ((PojoContainerProvider) provider).getContainer();
      Container container = (Container) c.getArtifact(pojoContainer.getVersion(), pojoContainer.getId());
      return c.queryFactory().streamProvider().of(container);
    } else if (provider instanceof PojoContainerWithFilterProvider) {
      PojoContainer pojoContainer = ((PojoContainerWithFilterProvider) provider).getContainer();
      PojoArtifact[] pojoFilter = ((PojoContainerWithFilterProvider) provider).getFilter();

      Container container = (Container) c.getArtifact(pojoContainer.getVersion(), pojoContainer.getId());
      Filter filter = dsFactory.createFilter(pojoFilter);
      return c.queryFactory().streamProvider().of(container, filter);
    } else if (provider instanceof PojoMapArtifactKeyProvider) {
      PojoMapArtifact pojoMapArtifact = ((PojoMapArtifactKeyProvider) provider).getMapArtifact();
      MapArtifact mapArtifact = c.getMapArtifact(pojoMapArtifact.getVersion(), pojoMapArtifact.getId());
      return c.queryFactory().streamProvider().keys(mapArtifact);
    } else if (provider instanceof PojoMapArtifactValueProvider) {
      PojoMapArtifact pojoMapArtifact = ((PojoMapArtifactKeyProvider) provider).getMapArtifact();
      MapArtifact mapArtifact = c.getMapArtifact(pojoMapArtifact.getVersion(), pojoMapArtifact.getId());
      return c.queryFactory().streamProvider().values(mapArtifact);
    } else if (provider instanceof PojoMetaModelProvider) {
      PojoMetaModel pojoMetaModel = ((PojoMetaModelProvider) provider).getMetaModel();
      MetaModel metaModel = c.getMetaModel(pojoMetaModel.getVersion(), pojoMetaModel.getVersion());
      return c.queryFactory().streamProvider().of(metaModel);
    } else if (provider instanceof PojoOwnerProvider) {
      PojoOwner pojoOwner = ((PojoOwnerProvider) provider).getOwner();
      Owner owner = c.getOwner(pojoOwner.getId());
      return c.queryFactory().streamProvider().of(owner);
    } else if (provider instanceof PojoProjectPackageProvider) {
      PojoProject pojoProject = ((PojoProjectPackageProvider) provider).getProject();
      Project project = c.getProject(pojoProject.getVersion(), pojoProject.getId());
      return c.queryFactory().streamProvider().packages(project);
    } else if (provider instanceof PojoProjectProvider) {
      PojoProject pojoProject = ((PojoProjectProvider) provider).getProject();
      Project project = c.getProject(pojoProject.getVersion(), pojoProject.getId());
      return c.queryFactory().streamProvider().of(project);
    } else if (provider instanceof PojoToolProvider) {
      PojoTool pojoTool = ((PojoToolProvider) provider).getTool();
      Tool tool = c.getTool(pojoTool.getId());
      return c.queryFactory().streamProvider().of(tool);
    } else if (provider instanceof PojoResourceProvider) {
      PojoResource pojoResource = ((PojoResourceProvider) provider).getResource();
      Resource resource = c.getResource(pojoResource.getVersion(), pojoResource.getId());
      return c.queryFactory().streamProvider().of(resource);
    } else if (provider instanceof PojoWorkspaceWithFilterProvider) {
      PojoOwnerToolStringLong pojoWorkspace = ((PojoWorkspaceWithFilterProvider) provider).getWorkspace();
      PojoArtifact[] pojoArtifacts = ((PojoWorkspaceWithFilterProvider) provider).getFilter();
      Workspace workspace = c.getWorkspace(
          c.getOwner(pojoWorkspace.getOwner().getId()),
          c.getTool(pojoWorkspace.getTool().getId()), pojoWorkspace.getString());
      Artifact[] artifacts = null;
      if (pojoArtifacts != null) {
        artifacts = new Artifact[pojoArtifacts.length];
        for (int i = 0; i < artifacts.length; i++) {
          artifacts[i] = c.getArtifact(pojoArtifacts[i].getVersion(), pojoArtifacts[i].getId());
        }
      }
      return c.queryFactory().streamProvider().of(workspace, artifacts);
    }
    return null;
  }
}
