package at.jku.sea.cloud.mmm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MapArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.utils.ArtifactUtils;

public class MMMTypesFactory {

  private MMMTypesFactory() {
    // prevent initialization
  }

  private static Artifact getType(final Workspace ws, final Long type) {
    return ws.getArtifact(type);
  }

  /*** generic specifics ***/
  public static Artifact createTypeParameter(final Workspace ws, final String name, final Artifact boundType, final Artifact complexType) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.TYPEPARAMETER_GENERICBOUNDTYPE, boundType);
    final Artifact typeParameter = ws.createArtifact(getType(ws, DataStorage.TYPE_PARAMETER), null, null, null, properties);
    final CollectionArtifact genericTypes = (CollectionArtifact) complexType.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_GENERICTYPES);
    genericTypes.addElement(ws, typeParameter);
    return typeParameter;
  }

  /*** instance retrieval and creation ***/
  public static Artifact createComplexTypeInstance(final Workspace ws, final String name, final Artifact type) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name); // mmm::Name val: "Tank"
    final Artifact complexType = ws.createArtifact(type, null, null, null, properties);
    return complexType;
  }

  public static void createFeatureInstance(final Workspace ws, final Object value, final Artifact type, final Artifact owner) {
    final String name = (String) type.getPropertyValue(MMMTypeProperties.NAME);
    owner.setPropertyValue(ws, name, value);
  }

  /*** creation of types ***/
  public static Artifact createIntDataType(final Workspace ws, final Package pckg, final String name) {
    return MMMTypesFactory.createDataType(ws, pckg, name, false, true, false, false);
  }

  public static Artifact createBooleanDataType(final Workspace ws, final Package pckg, final String name) {
    return MMMTypesFactory.createDataType(ws, pckg, name, true, false, false, false);
  }

  public static Artifact createStringDataType(final Workspace ws, final Package pckg, final String name) {
    return MMMTypesFactory.createDataType(ws, pckg, name, false, false, false, true);
  }

  public static Artifact createRealDataType(final Workspace ws, final Package pckg, final String name) {
    return MMMTypesFactory.createDataType(ws, pckg, name, false, false, true, false);
  }

  public static Artifact createDataType(final Workspace ws, final String name, final boolean isBool, final boolean isInt, final boolean isReal, final boolean isString) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    final CollectionArtifact superTypes = ArtifactUtils.createCollectionArtifact(ws, Arrays.asList(new Artifact[] { getType(ws, DataStorage.DATA_TYPE) }), true, null);
    properties.put(MMMTypeProperties.ALLSUPER_TYPES, superTypes);
    properties.put(MMMTypeProperties.IS_INT, isInt);
    properties.put(MMMTypeProperties.IS_REAL, isReal);
    properties.put(MMMTypeProperties.IS_BOOLEAN, isBool);
    properties.put(MMMTypeProperties.IS_STRING, isString);
    final Artifact dataType = ws.createArtifact(getType(ws, DataStorage.DATA_TYPE), null, null, null, properties);
    return dataType;
  }

  public static Artifact createDataType(final Workspace ws, final Package pckg, final String name, final boolean isBool, final boolean isInt, final boolean isReal, final boolean isString) {
    final Package superTypePckg = ws.createPackage(pckg);
    superTypePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "superTypes");
    final CollectionArtifact superTypes = ArtifactUtils.createCollectionArtifact(ws, Arrays.asList(new Artifact[] { getType(ws, DataStorage.DATA_TYPE) }), true, superTypePckg);
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ALLSUPER_TYPES, superTypes);
    properties.put(MMMTypeProperties.IS_INT, isInt);
    properties.put(MMMTypeProperties.IS_REAL, isReal);
    properties.put(MMMTypeProperties.IS_BOOLEAN, isBool);
    properties.put(MMMTypeProperties.IS_STRING, isString);
    final Artifact dataType = ws.createArtifact(getType(ws, DataStorage.DATA_TYPE), pckg, null, null, properties);
    return dataType;
  }

  public static Artifact createComplexType(final Workspace ws, final String name, final CollectionArtifact allSuperTypes, final CollectionArtifact features, final CollectionArtifact operations,
      final MapArtifact boundTypes, final boolean isInterface, final boolean isAbstract) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ALLSUPER_TYPES, allSuperTypes);
    properties.put(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, features);
    properties.put(MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS, operations);
    properties.put(MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), true, null));
    properties.put(MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, boundTypes);
    properties.put(MMMTypeProperties.IS_INTERFACE, isInterface);
    properties.put(MMMTypeProperties.IS_ABSTRACT, isAbstract);
    final Artifact complexType = ws.createArtifact(getType(ws, DataStorage.COMPLEX_TYPE), null, null, null, properties);
    return complexType;
  }

  public static Artifact createComplexType(final Workspace ws, final String name, final boolean isInterface, final boolean isAbstract) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    Map<String, Object> gProperties = new HashMap<>();
    gProperties.put(DataStorage.PROPERTY_NAME, "genericTypes");
    final CollectionArtifact genericTypes = ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), gProperties, true, null);
    properties.put(MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, genericTypes);
    final MapArtifact map = ws.createMap();
    map.setPropertyValue(ws, MMMTypeProperties.NAME, "boundTypes");
    properties.put(MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, map);

    Map<String, Object> allSuperTypesProps = new HashMap<>();
    allSuperTypesProps.put(DataStorage.PROPERTY_NAME, "allSuperTypes");
    final CollectionArtifact allSuperTypes = ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), allSuperTypesProps, true, null);
    Map<String, Object> featureProps = new HashMap<>();
    featureProps.put(DataStorage.PROPERTY_NAME, "features");
    final CollectionArtifact features = ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), featureProps, true, null);
    Map<String, Object> operationProps = new HashMap<>();
    operationProps.put(DataStorage.PROPERTY_NAME, "operations");
    final CollectionArtifact operations = ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), operationProps, true, null);
    properties.put(MMMTypeProperties.ALLSUPER_TYPES, allSuperTypes);
    properties.put(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, features);
    properties.put(MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS, operations);

    properties.put(MMMTypeProperties.IS_INTERFACE, isInterface);
    properties.put(MMMTypeProperties.IS_ABSTRACT, isAbstract);
    final Artifact complexType = ws.createArtifact(getType(ws, DataStorage.COMPLEX_TYPE), null, null, null, properties);
    return complexType;
  }

  public static Artifact createComplexType(final Workspace ws, final Package pckg, final String name, final boolean isInterface, final boolean isAbstract) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    Map<String, Object> genericTypeProps = new HashMap<>();
    genericTypeProps.put(DataStorage.PROPERTY_NAME, "genericTypes");
    final CollectionArtifact genericTypes = ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), genericTypeProps, true, pckg);
    properties.put(MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, genericTypes);
    final MapArtifact map = ws.createMap(pckg);
    map.setPropertyValue(ws, MMMTypeProperties.NAME, "boundTypes");
    properties.put(MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, map);

    Map<String, Object> allSuperTypesProps = new HashMap<>();
    allSuperTypesProps.put(DataStorage.PROPERTY_NAME, "allSuperTypes");
    final CollectionArtifact allSuperTypes = ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), allSuperTypesProps, true, pckg);
    Map<String, Object> featureProps = new HashMap<>();
    featureProps.put(DataStorage.PROPERTY_NAME, "features");
    final CollectionArtifact features = ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), featureProps, true, pckg);
    Map<String, Object> operationProps = new HashMap<>();
    operationProps.put(DataStorage.PROPERTY_NAME, "operations");
    final CollectionArtifact operations = ArtifactUtils.createCollectionArtifact(ws, Collections.<Artifact> emptyList(), operationProps, true, pckg);
    properties.put(MMMTypeProperties.ALLSUPER_TYPES, allSuperTypes);
    properties.put(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, features);
    properties.put(MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS, operations);

    properties.put(MMMTypeProperties.IS_INTERFACE, isInterface);
    properties.put(MMMTypeProperties.IS_ABSTRACT, isAbstract);
    final Artifact complexType = ws.createArtifact(getType(ws, DataStorage.COMPLEX_TYPE), pckg, null, null, properties);
    return complexType;
  }

  public static void addSuperTypeToComplexType(final Workspace ws, final Artifact complexType, final Artifact superType) {
    final CollectionArtifact superTypes = (CollectionArtifact) complexType.getPropertyValue(MMMTypeProperties.ALLSUPER_TYPES);
    superTypes.addElement(ws, superType);
  }

  public static void removeSuperTypeFromComplexType(final Workspace ws, final Artifact complexType, final Artifact superType) {
    final CollectionArtifact superTypes = (CollectionArtifact) complexType.getPropertyValue(MMMTypeProperties.ALLSUPER_TYPES);
    superTypes.removeElement(ws, superType);
  }

  public static void addFeatureToComplexType(final Workspace ws, final Artifact complexType, final Artifact feature) {
    final CollectionArtifact features = (CollectionArtifact) complexType.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES);
    features.addElement(ws, feature);
  }

  public static void removeFeatureFromComplexType(final Workspace ws, final Artifact complexType, final Artifact feature) {
    final CollectionArtifact features = (CollectionArtifact) complexType.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES);
    features.removeElement(ws, feature);
  }

  public static Artifact createEnumType(final Workspace ws, final String name, final CollectionArtifact literals) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ENUM_LITERALS, literals);
    final Artifact enumm = ws.createArtifact(getType(ws, DataStorage.ENUMM), null, null, null, properties);
    return enumm;
  }

  public static Artifact createEnumType(final Workspace ws, final String name, final CollectionArtifact literals, final Package pckg) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ENUM_LITERALS, literals);
    final Artifact enumm = ws.createArtifact(getType(ws, DataStorage.ENUMM), pckg, null, null, properties);
    return enumm;
  }

  public static Artifact createEnumLiteral(final Workspace ws, final String name, final String literal, final Artifact enum_) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ENUMLIT_LITERAL, literal);
    properties.put(MMMTypeProperties.ENUMLIT_ENUM, enum_);
    final Artifact literalArtifact = ws.createArtifact(getType(ws, DataStorage.ENUMM_LITERAL), null, null, null, properties);
    return literalArtifact;
  }

  public static Artifact createEnumLiteral(final Workspace ws, final String name, final String literal, final Artifact enum_, final Package pckg) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ENUMLIT_LITERAL, literal);
    properties.put(MMMTypeProperties.ENUMLIT_ENUM, enum_);
    final Artifact literalArtifact = ws.createArtifact(getType(ws, DataStorage.ENUMM_LITERAL), pckg, null, null, properties);
    return literalArtifact;
  }

  public static Artifact createTypedElement(final Workspace ws, final String name, final Artifact dataType, final boolean isMany, final boolean isOrdered, final boolean isUnique) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ELEMENT_TYPE, dataType);
    properties.put(MMMTypeProperties.IS_MANY, isMany);
    properties.put(MMMTypeProperties.IS_ORDERED, isOrdered);
    properties.put(MMMTypeProperties.IS_UNIQUE, isUnique);
    final Artifact typedElement = ws.createArtifact(getType(ws, DataStorage.TYPED_ELEMENT), null, null, null, properties);
    return typedElement;
  }

  public static Artifact createFeature(final Workspace ws, final String name, final Artifact elementType, final boolean is_many, final boolean isOrdered, final boolean isUnique) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ELEMENT_TYPE, elementType);
    properties.put(MMMTypeProperties.IS_MANY, is_many);
    properties.put(MMMTypeProperties.IS_ORDERED, isOrdered);
    properties.put(MMMTypeProperties.IS_UNIQUE, isUnique);
    final Artifact feature = ws.createArtifact(getType(ws, DataStorage.FEATURE), null, null, null, properties);
    return feature;
  }

  public static Artifact createFeature(final Workspace ws, final String name, final Artifact owner, final Artifact elementType, final boolean is_many, final boolean isOrdered, final boolean isUnique) {
    final Artifact feature = createFeature(ws, name, elementType, is_many, isOrdered, isUnique);
    final CollectionArtifact features = (CollectionArtifact) owner.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES);
    features.addElement(ws, feature);

    return feature;
  }

  public static Artifact createOperation(final Workspace ws, final String name, final Artifact returnType, final CollectionArtifact parameters, final boolean isMany, final boolean isOrdered,
      final boolean isUnique) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.OPERATION_PARAMETERS, parameters);
    properties.put(MMMTypeProperties.ELEMENT_TYPE, returnType);
    properties.put(MMMTypeProperties.IS_MANY, isMany);
    properties.put(MMMTypeProperties.IS_ORDERED, isOrdered);
    properties.put(MMMTypeProperties.IS_UNIQUE, isUnique);
    final Artifact operation = ws.createArtifact(getType(ws, DataStorage.OPERATION), null, null, null, properties);
    return operation;
  }

  public static void addOperationToComplexType(final Workspace ws, final Artifact complexType, final Artifact operation) {
    final CollectionArtifact features = (CollectionArtifact) complexType.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS);
    features.addElement(ws, operation);
  }

  public static void removeOperationFromComplexType(final Workspace ws, final Artifact complexType, final Artifact operation) {
    final CollectionArtifact features = (CollectionArtifact) complexType.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS);
    features.removeElement(ws, operation);
  }

  public static Artifact createOperation(final Workspace ws, final String name, final Artifact owner, final Artifact returnType, final CollectionArtifact parameters, final boolean isMany,
      final boolean isOrdered, final boolean isUnique) {
    final Artifact operation = createOperation(ws, name, returnType, parameters, isMany, isOrdered, isUnique);
    final CollectionArtifact operations = (CollectionArtifact) owner.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS);
    operations.addElement(ws, operation);
    return operation;
  }

  public static Artifact createParameter(final Workspace ws, final String name, final Artifact elementType, final boolean isMany, final boolean isOrdered, final boolean isUnique) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(MMMTypeProperties.NAME, name);
    properties.put(MMMTypeProperties.ELEMENT_TYPE, elementType);
    properties.put(MMMTypeProperties.IS_MANY, isMany);
    properties.put(MMMTypeProperties.IS_ORDERED, isOrdered);
    properties.put(MMMTypeProperties.IS_UNIQUE, isUnique);
    final Artifact parameter = ws.createArtifact(getType(ws, DataStorage.PARAMETER), null, null, null, properties);
    return parameter;
  }

  public static void addParameterToOperation(final Workspace ws, final Artifact operation, final Artifact parameter) {
    final CollectionArtifact features = (CollectionArtifact) operation.getPropertyValue(MMMTypeProperties.OPERATION_PARAMETERS);
    features.addElement(ws, parameter);
  }

  public static void removeParameterFromOperation(final Workspace ws, final Artifact operation, final Artifact parameter) {
    final CollectionArtifact features = (CollectionArtifact) operation.getPropertyValue(MMMTypeProperties.OPERATION_PARAMETERS);
    features.removeElement(ws, parameter);
  }

  public static Artifact createConstraint(final Workspace ws, final String name, final Artifact context, final String rule, final boolean enabled, final boolean repairable) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(DataStorage.PROPERTY_NAME, name);
    properties.put(MMMTypeProperties.CONSTRAINT_CONTEXT, context);
    properties.put(MMMTypeProperties.CONSTRAINT_RULE, rule);
    properties.put(MMMTypeProperties.CONSTRAINT_ENABLED, enabled);
    properties.put(MMMTypeProperties.CONSTRAINT_REPAIRABLE, repairable);
    final Artifact constraint = ws.createArtifact(getType(ws, DataStorage.CONSTRAINT), null, null, null, properties);
    return constraint;
  }

  public static Artifact createQuery(final Workspace ws, final String name, final Artifact type, final Artifact context, final String rule, final CollectionArtifact params) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(DataStorage.PROPERTY_NAME, name);
    properties.put(MMMTypeProperties.QUERY_CONTEXT, context);
    properties.put(MMMTypeProperties.QUERY_RETURN_TYPE, type);
    properties.put(MMMTypeProperties.QUERY_DEFINITION, rule);
    properties.put(MMMTypeProperties.QUERY_PARAMS, params);
    final Artifact query = ws.createArtifact(getType(ws, DataStorage.QUERY), null, null, null, properties);
    return query;
  }

  public static Artifact createInstance(final Workspace ws, final String name, final Artifact contextElement, final Artifact rule, final boolean result) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(DataStorage.PROPERTY_NAME, name);
    properties.put(MMMTypeProperties.INSTANCE_CONTEXT_ELEMENT, contextElement);
    properties.put(MMMTypeProperties.INSTANCE_RULE, rule);
    properties.put(MMMTypeProperties.INSTANCE_RESULT, result);
    final Artifact instance = ws.createArtifact(getType(ws, DataStorage.INSTANCE), null, null, null, properties);
    return instance;
  }

  public static Artifact createMappingArtifact(final Workspace ws, final MetaModel mm, final String name, final Artifact complexType, final Artifact dataType, final Artifact operationType,
      final Artifact parameterType, final Artifact attributeType, final Artifact referenceType, final Artifact enummType, final Artifact enummLitType, final Artifact typeParamType,
      final Artifact stringType, final Artifact intType, final Artifact booleanType, final Artifact doubleType, final String allSuperTypes, final String allFeatures, final String allOperations,
      final String parameters, final String unique, final String ordered, final String many, final String isAbstract, final String isInterface, final String typeProperty) {
    final Artifact mapping = ws.createArtifact(getType(ws, DataStorage.MMM_MAPPING));
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_NAME, name);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_METAMODEL, mm);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_COMPLEX_TYPE, complexType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_DATA_TYPE, dataType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_OPERATION_TYPE, operationType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_PARAMETER_TYPE, parameterType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ATTRIBUTE_TYPE, attributeType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_REFERENCE_TYPE, referenceType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ENUM_TYPE, enummType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ENUMLITERAL_TYPE, enummLitType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_TYPEPARAMETER_TYPE, typeParamType);

    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_STRING_TYPE, stringType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_BOOLEAN_TYPE, booleanType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_INTEGER_TYPE, intType);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_DOUBLE_TYPE, doubleType);

    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ALLSUPERTYPES_PROPERTY, allSuperTypes);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ALLFEATURES_PROPERTY, allFeatures);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ALLOPERATIONS_PROPERTY, allOperations);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_PARAMETERS_PROPERTY, parameters);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ORDERED_PROPERTY, ordered);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_UNIQUE_PROPERTY, unique);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_MANY_PROPERTY, many);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ISABSTRACT_PROPERTY, isAbstract);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_ISINTERFACE_PROPERTY, isInterface);
    mapping.setPropertyValue(ws, MMMTypeProperties.MAPPING_TYPE_PROPERTY, typeProperty);

    return mapping;
  }
}
