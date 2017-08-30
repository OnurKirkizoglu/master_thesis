package at.jku.sea.cloud.mmm;

public class MMMTypeProperties {

  public static final String PREFIX = "mmm::";

  /**
   * MetaModel
   */
  public static final String METAMODEL = PREFIX + "MetaModel";

  /*** NamedElement ***/
  public static final String NAME = PREFIX + "Name";

  public static final String TYPEDELEMENT_NAME = PREFIX + "TypedElement";
  public static final String IS_MANY = PREFIX + "IsMany";
  public static final String IS_ORDERED = PREFIX + "IsOrdered";
  public static final String IS_UNIQUE = PREFIX + "IsUnique";
  public static final String ELEMENT_TYPE = PREFIX + "ElementType";

  /*** Type ***/
  public static final String TYPE_NAME = PREFIX + "Type";
  public static final String ALLSUPER_TYPES = PREFIX + "AllSuperTypes";

  /*** DataType ***/
  public static final String DATATYPE_NAME = PREFIX + "DataType";

  /*** TypeParameter ***/
  public static final String TYPEPARAMETER_NAME = PREFIX + "TypeParameter";
  public static final String TYPEPARAMETER_GENERICBOUNDTYPE = PREFIX + "GenericBoundType";

  /*** ComplexType ***/
  public static final String COMPLEXTYPE_NAME = PREFIX + "ComplexType";
  public static final String COMPLEXTYPE_ALLFEATURES = PREFIX + "AllFeatures";
  public static final String COMPLEXTYPE_ALLOPERATIONS = PREFIX + "AllOperations";
  public static final String IS_INTERFACE = PREFIX + "IsInterface";
  public static final String IS_ABSTRACT = PREFIX + "IsAbstract";

  public static final String COMPLEXTYPE_GENERICTYPES = PREFIX + "GenericTypes";
  public static final String COMPLEXTYPE_BOUNDTYPES = PREFIX + "BoundTypes";

  /*** Enum ***/
  public static final String ENUM_NAME = PREFIX + "Enum";
  public static final String ENUM_LITERALS = PREFIX + "Literals";

  /*** EnumLiteral ***/
  public static final String ENUMLIT_NAME = PREFIX + "EnumLiteral";
  public static final String ENUMLIT_LITERAL = PREFIX + "Literal";
  public static final String ENUMLIT_VALUE = PREFIX + "Value";
  public static final String ENUMLIT_ENUM = PREFIX + "Enum";

  /*** PrimitiveTypes ***/
  public static final String IS_INT = PREFIX + "IsInt";
  public static final String IS_BOOLEAN = PREFIX + "IsBoolean";
  public static final String IS_STRING = PREFIX + "IsString";
  public static final String IS_REAL = PREFIX + "IsReal";

  /*** Feature ***/
  public static final String FEATURE_NAME = PREFIX + "Feature";

  /*** Parameter ***/
  public static final String PARAMETER_NAME = PREFIX + "Parameter";

  /*** Operation ***/
  public static final String OPERATION_NAME = PREFIX + "Operation";
  public static final String OPERATION_PARAMETERS = PREFIX + "Parameters";

  /*** Constraint ***/
  public static final String CONSTRAINT_NAME = PREFIX + "Constraint";
  public static final String CONSTRAINT_RULE = PREFIX + "ConstraintRule";
  public static final String CONSTRAINT_CONTEXT = PREFIX + "ConstraintContext";
  public static final String CONSTRAINT_DESCRIPTION = PREFIX + "ConstraintDescription";
  public static final String CONSTRAINT_ENABLED = PREFIX + "Enabled";
  public static final String CONSTRAINT_REPAIRABLE = PREFIX + "Repairable";
  public static final String CONSTRAINT_KEEPVALIDATION = PREFIX + "KeepValidation";

  /*** Query ***/
  public static final String QUERY_NAME = PREFIX + "Query";
  public static final String QUERY_DEFINITION = PREFIX + "Definition";
  public static final String QUERY_CONTEXT = PREFIX + "QueryContext";
  public static final String QUERY_RETURN_TYPE = PREFIX + "ReturnType";
  public static final String QUERY_PARAMS = PREFIX + "FormalParams";
  public static final String QUERY_DESCRIPTION = PREFIX + "QueryDescription";

  /*** Instance ***/
  public static final String INSTANCE_NAME = PREFIX + "Instance";
  public static final String INSTANCE_RULE = PREFIX + "InstanceRule";
  public static final String INSTANCE_CONTEXT_ELEMENT = PREFIX + "InstanceContext";
  public static final String INSTANCE_RESULT = PREFIX + "ValidationResult";
  public static final String INSTANCE_SCOPE = PREFIX + "Scope";
  
  /*** Scope ***/
  public static final String SCOPE_INSTANCE = PREFIX + "ScopeInstance";
  public static final String SCOPE_MODELELEMENT = PREFIX + "ScopeCollection";

  /*** Mapping ***/
  /** Types **/
  public static final String MAPPING_NAME = PREFIX + "Mapping";
  public static final String MAPPING_COMPLEX_TYPE = MAPPING_NAME + "::" + "ComplexType";
  public static final String MAPPING_DATA_TYPE = MAPPING_NAME + "::" + "DataType";
  public static final String MAPPING_OPERATION_TYPE = MAPPING_NAME + "::" + "OperationType";
  public static final String MAPPING_PARAMETER_TYPE = MAPPING_NAME + "::" + "ParameterType";
  public static final String MAPPING_ATTRIBUTE_TYPE = MAPPING_NAME + "::" + "AttributType";
  public static final String MAPPING_REFERENCE_TYPE = MAPPING_NAME + "::" + "ReferenceType";
  public static final String MAPPING_ENUM_TYPE = MAPPING_NAME + "::" + "EnumType";
  public static final String MAPPING_ENUMLITERAL_TYPE = MAPPING_NAME + "::" + "EnumLiteralType";
  public static final String MAPPING_TYPEPARAMETER_TYPE = MAPPING_NAME + "::" + "TypeParameterType";

  public static final String MAPPING_STRING_TYPE = MAPPING_NAME + "::" + "StringType";
  public static final String MAPPING_BOOLEAN_TYPE = MAPPING_NAME + "::" + "BooleanType";
  public static final String MAPPING_INTEGER_TYPE = MAPPING_NAME + "::" + "IntegerType";
  public static final String MAPPING_DOUBLE_TYPE = MAPPING_NAME + "::" + "DoubleType";

  /** Properties **/
  public static final String MAPPING_ALLSUPERTYPES_PROPERTY = MAPPING_NAME + "::" + "allSuperTypesProperty";
  public static final String MAPPING_ALLFEATURES_PROPERTY = MAPPING_NAME + "::" + "allStructuralFeaturesProperty";
  public static final String MAPPING_ALLOPERATIONS_PROPERTY = MAPPING_NAME + "::" + "OperationsProperty";
  public static final String MAPPING_PARAMETERS_PROPERTY = MAPPING_NAME + "::" + "ParameterProperty";
  public static final String MAPPING_ORDERED_PROPERTY = MAPPING_NAME + "::" + "OrderedProperty";
  public static final String MAPPING_UNIQUE_PROPERTY = MAPPING_NAME + "::" + "UniqueProperty";
  public static final String MAPPING_MANY_PROPERTY = MAPPING_NAME + "::" + "ManyProperty";
  public static final String MAPPING_ISABSTRACT_PROPERTY = MAPPING_NAME + "::" + "IsAbstractProperty";
  public static final String MAPPING_ISINTERFACE_PROPERTY = MAPPING_NAME + "::" + "IsInterfaceProperty";
  public static final String MAPPING_TYPE_PROPERTY = MAPPING_NAME + "::" + "TypeProperty";
  public static final String MAPPING_METAMODEL = MAPPING_NAME + "::" + "MetaModel";

}
