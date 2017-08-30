package at.jku.sea.cloud.mmm;

import java.io.File;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.Workspace;

public class MMMTypeUtils {
  private static final Collection<Long> ROOT_ARTIFACTS = MMMTypes.getRootArtifacts();
  private static final String DATA_LOG4J_PROPERTIES = "." + File.separator + "data" + File.separator + "log4j.properties";
  private static Logger logger = LoggerFactory.getLogger(MMMTypeUtils.class);

  public static boolean isOfType(final Artifact type, final Artifact artifact) {
    final Artifact rootType = artifact.getType();// storage.getArtifactType(version, artifact);
    final Collection<Artifact> superTypes = (Collection<Artifact>) ((CollectionArtifact) rootType.getPropertyValue(MMMTypeProperties.ALLSUPER_TYPES)).getElements();
    return superTypes.contains(type) || (rootType.equals(type));
  }

  /*** generics ***/
  public static Collection<Artifact> getGenericTypes(final Artifact complexType) {
    logger.debug("generic_types(type={})", new Object[] { complexType });
    @SuppressWarnings("unchecked")
    final Collection<Artifact> generic_types = (Collection<Artifact>) ((CollectionArtifact) complexType.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_GENERICTYPES)).getElements();
    logger.debug("generic_types(type={}) => {}", new Object[] { complexType, generic_types });
    return generic_types;
  }

  public static Artifact getTypeParameter(Artifact complexType, String name) {
    Collection<Artifact> generics = getGenericTypes(complexType);
    for (final Artifact generic : generics) {
      if (((String) generic.getPropertyValue(DataStorage.PROPERTY_NAME)).compareTo(name) == 0) {
        return generic;
      }
    }
    return null;
  }

  public static Artifact getBinding(final Workspace ws, final Artifact complexType, final Artifact typeParameter) {
    @SuppressWarnings("unchecked")
    final MapArtifact bindings = (MapArtifact) complexType.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES);
    return (Artifact) bindings.get(typeParameter);
  }

  public static boolean bindTypeParameter(final Cloud cloud, final Workspace ws, final Artifact complexType, final Artifact typeParameter, final Artifact toBind) {
    if (isBindingConform(cloud, typeParameter, toBind)) {
      @SuppressWarnings("unchecked")
      final MapArtifact boundTypes = (MapArtifact) complexType.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES);
      if (!boundTypes.containsKey(typeParameter)) {
        boundTypes.put(ws, typeParameter, toBind);
//        complexType.setPropertyValue(ws, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, boundTypes);
        return true;
      }
    }
    return false;
  }

  private static boolean isBindingConform(final Cloud cloud, final Artifact typeParameter, final Artifact toBind) {
    final boolean isTypeParameter = MMMTypeUtils.isOfType(cloud.getArtifact(typeParameter.getVersionNumber(), DataStorage.TYPE_PARAMETER), typeParameter);
    final boolean isToBindType = MMMTypeUtils.isOfType(cloud.getArtifact(typeParameter.getVersionNumber(), DataStorage.TYPE), toBind);
    final Artifact boundType = (Artifact) typeParameter.getPropertyValue(MMMTypeProperties.TYPEPARAMETER_GENERICBOUNDTYPE);
    @SuppressWarnings("unchecked")
    final Collection<Artifact> allSuperTypes = ((Collection<Artifact>) ((CollectionArtifact) toBind.getPropertyValue(MMMTypeProperties.ALLSUPER_TYPES)).getElements());
    final boolean isToBindSubTypeOfBoundType = boundType != null ? allSuperTypes.contains(boundType) : true;
    final boolean isBoundTypeRootArtifact = boundType != null ? boundType.getId() == DataStorage.ROOT_TYPE_ID : false;
    return isTypeParameter && (isToBindSubTypeOfBoundType || isBoundTypeRootArtifact) && isToBindType;
  }
}
