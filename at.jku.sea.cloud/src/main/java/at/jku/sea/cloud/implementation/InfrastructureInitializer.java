package at.jku.sea.cloud.implementation;

import static at.jku.sea.cloud.DataStorage.*;

import java.util.HashMap;
import java.util.Map;

import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.implementation.DefaultMapArtifact.DefaultEntry;
import at.jku.sea.cloud.mmm.MMMTypeProperties;

class InfrastructureInitializer {
  private static String ADMIN_NAME = "Admin";
  private static String ADMIN_LOGIN = "admin";
  private static String ADMIN_PASSWORD = "adminpassword";
  
  private static String DEMO_NAME = "Demo User";
  private static String DEMO_LOGIN = "demo";
  private static String DEMO_PASSWORD = "demopassword";
  
  private AbstractDataStorage datastorage;

  InfrastructureInitializer(AbstractDataStorage datastorage) {
    this.datastorage = datastorage;
  }

  public void init() {
    this.initArtifactInfrastructure();
    this.initModelAnalyzer();
    this.initMappingArtifact();
    this.initTraceLink();
    this.initEPlanTypes();
    this.initProETypes();
    this.initTestProject();
    this.initJavaMetamodel();
    this.initUsers();
    this.initDemoProject();
  }

  /**
   * Add admin user and demo user
   * @author jMayer
   */
  protected void initUsers() {
    this.datastorage.createUser(ADMIN_NAME, ADMIN_LOGIN, ADMIN_PASSWORD, ADMIN);
    this.datastorage.createUser(DEMO_NAME, DEMO_LOGIN, DEMO_PASSWORD, DEMO);
  }
  
  protected void initDemoProject() {
    this.datastorage.addElement(FIRST_VERSION, TEST_PROJECT_ID, ADMIN, ROOT_TOOL_ID, OWNER_PACKAGE_ID);
    this.datastorage.addElement(FIRST_VERSION, TEST_PROJECT_ID, ADMIN, ROOT_TOOL_ID, TOOL_PACKAGE_ID);
  }
  
  /***
   * This method initializes the root artifact types (the root artifact itself, tool, owner, metamodel, etc). <br>
   * 
   * Basically it just stores the artifacts types in the database and gives them a name. <br>
   */
  protected void initArtifactInfrastructure() {
    // infrastructure
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, DESIGNSPACE_META_MODEL, META_MODEL_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, DESIGNSPACE_META_MODEL, PROPERTY_COLLECTION_NEXT_IDX, 0l, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, DESIGNSPACE_META_MODEL, PROPERTY_COLLECTION_REFERENCES, true, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, DESIGNSPACE_META_MODEL, PROPERTY_NAME, "DesignSpaceMetaModel", false, true);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, ROOT_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, ROOT_TYPE_ID, PROPERTY_NAME, "Root-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, ROOT_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, COLLECTION_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, COLLECTION_TYPE_ID, PROPERTY_NAME, "Collection-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, COLLECTION_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, PACKAGE_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, PACKAGE_TYPE_ID, PROPERTY_NAME, "Package-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, PACKAGE_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, PROJECT_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, PROJECT_TYPE_ID, PROPERTY_NAME, "Project-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, PROJECT_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, OWNER_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, OWNER_TYPE_ID, PROPERTY_NAME, "Owner-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, OWNER_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TOOL_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TOOL_TYPE_ID, PROPERTY_NAME, "Tool-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, TOOL_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, META_MODEL_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, META_MODEL_TYPE_ID, PROPERTY_NAME, "MetaModel-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, META_MODEL_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MAP_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MAP_TYPE_ID, PROPERTY_NAME, "Map-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, MAP_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, CONTAINER_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, CONTAINER_TYPE_ID, PROPERTY_NAME, "Container-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, CONTAINER_TYPE_ID);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, RESOURCE_TYPE_ID, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, RESOURCE_TYPE_ID, PROPERTY_NAME, "Resource-Type");
    this.datastorage.addElementToCollection(FIRST_VERSION, DESIGNSPACE_META_MODEL, ADMIN, ROOT_TOOL_ID, RESOURCE_TYPE_ID);

    // packages
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, AT_PACKAGE_ID, PACKAGE_TYPE_ID, null, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, AT_PACKAGE_ID, PROPERTY_NAME, "at");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, AT_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, JKU_PACKAGE_ID, PACKAGE_TYPE_ID, AT_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, JKU_PACKAGE_ID, PROPERTY_NAME, "jku");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, JKU_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, SEA_PACKAGE_ID, PACKAGE_TYPE_ID, JKU_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, SEA_PACKAGE_ID, PROPERTY_NAME, "sea");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, SEA_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, DESIGNSPACE_PACKAGE_ID, PACKAGE_TYPE_ID, SEA_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, DESIGNSPACE_PACKAGE_ID, PROPERTY_NAME, "designspace");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, DESIGNSPACE_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.designspace");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MMM_PACKAGE_ID, PACKAGE_TYPE_ID, SEA_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MMM_PACKAGE_ID, PROPERTY_NAME, "mmm");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MMM_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.mmm");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, NO_CONTAINER_ID, PACKAGE_TYPE_ID, null, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, NO_CONTAINER_ID, PROPERTY_NAME, "(default package)");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, NO_CONTAINER_ID, PROPERTY_FULL_QUALIFIED_NAME, "(default package)");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MAPPING_PACKAGE_ID, PACKAGE_TYPE_ID, SEA_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MAPPING_PACKAGE_ID, PROPERTY_NAME, "mapping");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MAPPING_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.mapping");

    // owner package
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, OWNER_PACKAGE_ID, PACKAGE_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, OWNER_PACKAGE_ID, PROPERTY_NAME, "owner");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, OWNER_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.designspace.owner");
    
    // admin owner
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, ADMIN, OWNER_TYPE_ID, OWNER_PACKAGE_ID, true);

    // demo owner
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, DEMO, OWNER_TYPE_ID, OWNER_PACKAGE_ID, true);

    // tools
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TOOL_PACKAGE_ID, PACKAGE_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TOOL_PACKAGE_ID, PROPERTY_NAME, "tools");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TOOL_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.designspace.tools");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, ROOT_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, ROOT_TOOL_ID, PROPERTY_NAME, "DataStorage");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, RSA_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, RSA_TOOL_ID, PROPERTY_NAME, "RSA");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, PROE_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, PROE_TOOL_ID, PROPERTY_NAME, "ProEngineer");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, ECLIPSE_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, ECLIPSE_TOOL_ID, PROPERTY_NAME, "Eclipse Application");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EXCEL_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EXCEL_TOOL_ID, PROPERTY_NAME, "Excel");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MA_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, MA_TOOL_ID, PROPERTY_NAME, "ModelAnalyzer");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, JUNIT_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, JUNIT_TOOL_ID, PROPERTY_NAME, "JUnit");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EDITOR_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EDITOR_TOOL_ID, PROPERTY_NAME, "Editor");

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_TOOL_ID, TOOL_TYPE_ID, TOOL_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_TOOL_ID, PROPERTY_NAME, "EPlan");

  }

  private void initTestProject() {
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TEST_PROJECT_ID, PROJECT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TEST_PROJECT_ID, PROPERTY_COLLECTION_NEXT_IDX, 0l, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TEST_PROJECT_ID, PROPERTY_COLLECTION_REFERENCES, true, false, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TEST_PROJECT_ID, PROPERTY_NAME, "TEST_PROJECT");
  }

  /***
   * This method initializes an artifact graph (the root metamodel) that is used to model subsequent (meta-)models that are understood e.g., by the Model/Analyzer. <br>
   * The metamodel is reflective, in the sense that it is modeled by itself (e.g. the ComplexType Type is ComplexType), which enables consistency checking of the root metamodel
   * itself.<br>
   * Effective, it initializes a slimed down version of the ecore metamodel. <br>
   * http://www.kermeta.org/docs/org.kermeta.ecore.documentation/build/html.chunked/Ecore-MDK/Ecore-MDK_figures/EcoreMainView.png <br>
   * 
   * Correspondences: <br>
   * EClassifier = Type supertype of types (either stuff like datatype (e.g. int) or classes (e.g. a car)) <br>
   * EDataType = DataType (e.g., int, string) <br>
   * EClass = ComplexType (e.g., a car "class") <br>
   * 
   * ETypedElement = TypedElement (supertype for Feature, Operation, Parameter, everything that has a type) <br>
   * EStructuralFeature = Feature (e.g. models a reference or attribute)<br>
   * 
   * 
   */
  protected void initModelAnalyzer() {
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_META_MODEL, META_MODEL_TYPE_ID, MMM_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_META_MODEL, PROPERTY_COLLECTION_NEXT_IDX, 0l, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_META_MODEL, PROPERTY_COLLECTION_REFERENCES, true, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_META_MODEL, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_META_MODEL, PROPERTY_NAME, "MetaMetaModel", false, true);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, MODELANALYZER_PACKAGE_ID, PACKAGE_TYPE_ID, null, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MODELANALYZER_PACKAGE_ID, PROPERTY_NAME, "ModelAnalyzer");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MODELANALYZER_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "ModelAnalyzer");

    /*** Type ***/
    final long typePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, typePckg, PROPERTY_NAME, MMMTypeProperties.TYPE_NAME);
    final long typeFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, typePckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, COMPLEX_TYPE, typePckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, TYPE);

    /*** DataType ***/
    final long dataTypePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataTypePckg, PROPERTY_NAME, MMMTypeProperties.DATATYPE_NAME);
    final long dataTypeFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, dataTypePckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataTypeFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, COMPLEX_TYPE, dataTypePckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, DATA_TYPE);

    /*** ComplexType ***/
    final long complexTypePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, complexTypePckg, PROPERTY_NAME, MMMTypeProperties.COMPLEXTYPE_NAME);
    final long complexTypeFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, complexTypePckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, complexTypeFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, COMPLEX_TYPE, complexTypePckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, COMPLEX_TYPE);

    /*** Enum ***/
    final long enumTypePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumTypePckg, PROPERTY_NAME, MMMTypeProperties.ENUM_NAME);
    final long enumTypeFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, enumTypePckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumTypeFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, COMPLEX_TYPE, enumTypePckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, ENUMM);

    /*** EnumLiteral ***/
    final long enumLiteralPckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumLiteralPckg, PROPERTY_NAME, MMMTypeProperties.ENUMLIT_NAME);
    final long enumLiteralFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, enumLiteralPckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumLiteralFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, COMPLEX_TYPE, enumLiteralPckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, ENUMM_LITERAL);

    /*** TypedElement ***/
    final long typedElementPckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, typedElementPckg, PROPERTY_NAME, MMMTypeProperties.TYPEDELEMENT_NAME);
    final long typedElementFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, typedElementPckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, typedElementFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, COMPLEX_TYPE, typedElementPckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, TYPED_ELEMENT);

    /*** Feature ***/
    final long featurePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, featurePckg, PROPERTY_NAME, MMMTypeProperties.FEATURE_NAME);
    final long featureFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, featurePckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, featureFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, COMPLEX_TYPE, featurePckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, FEATURE);

    /*** Parameter ***/
    final long parameterPckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, parameterPckg, PROPERTY_NAME, MMMTypeProperties.PARAMETER_NAME);
    final long parameterFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, parameterPckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, parameterFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, COMPLEX_TYPE, parameterPckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, PARAMETER);

    /*** Operation ***/
    final long operationPckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, operationPckg, PROPERTY_NAME, MMMTypeProperties.OPERATION_NAME);
    final long operationFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, operationPckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, operationFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, COMPLEX_TYPE, operationPckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, OPERATION);

    /*** TypeParameter ***/
    final long typeParameterPckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeParameterPckg, PROPERTY_NAME, MMMTypeProperties.TYPEPARAMETER_NAME);
    final long typeParameterFeaturePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, typeParameterPckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeParameterFeaturePckg, PROPERTY_NAME, "features");
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, COMPLEX_TYPE, typeParameterPckg, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, TYPE_PARAMETER);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, CONSTRAINT, CONSTRAINT, MMM_PACKAGE_ID, true);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, QUERY, QUERY, MMM_PACKAGE_ID, true);

    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, INSTANCE, INSTANCE, MMM_PACKAGE_ID, true);

    /*** Basic DataTypes ***/
    final long basicDataTypePckg = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, PACKAGE_TYPE_ID, MMM_PACKAGE_ID);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, basicDataTypePckg, PROPERTY_NAME, "mmm::DataTypePckg");
    final long intType = this.createDataType("mmm::IntType", basicDataTypePckg, true, false, false, false);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, intType);
    final long realType = this.createDataType("mmm::RealType", basicDataTypePckg, false, true, false, false);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, realType);
    final long boolType = this.createDataType("mmm::boolType", basicDataTypePckg, false, false, true, false);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, boolType);
    final long stringType = this.createDataType("mmm::StringType", basicDataTypePckg, false, false, false, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, MMM_META_MODEL, ADMIN, MA_TOOL_ID, stringType);

    /*** Features ***/

    final long name = this.createFeature(MMMTypeProperties.NAME, typeFeaturePckg, stringType, false, false, false);
    final long isMany = this.createFeature(MMMTypeProperties.IS_MANY, typedElementFeaturePckg, boolType, false, false, false);
    final long isOrdered = this.createFeature(MMMTypeProperties.IS_ORDERED, typedElementFeaturePckg, boolType, false, false, false);
    final long isUnique = this.createFeature(MMMTypeProperties.IS_UNIQUE, typedElementFeaturePckg, boolType, false, false, false);
    final long elementType = this.createFeature(MMMTypeProperties.ELEMENT_TYPE, typedElementFeaturePckg, DataStorage.TYPE, false, false, false);
    final long genericBoundType = this.createFeature(MMMTypeProperties.TYPEPARAMETER_GENERICBOUNDTYPE, typeParameterFeaturePckg, DataStorage.TYPE, false, false, false);
    final long allFeatures = this.createFeature(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, complexTypeFeaturePckg, DataStorage.FEATURE, true, false, false);
    final long allOperations = this.createFeature(MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS, complexTypeFeaturePckg, DataStorage.OPERATION, true, false, false);
    final long allSuperTypes = this.createFeature(MMMTypeProperties.ALLSUPER_TYPES, typeFeaturePckg, COMPLEX_TYPE, true, false, false);
    final long isInterface = this.createFeature(MMMTypeProperties.IS_INTERFACE, complexTypeFeaturePckg, boolType, false, false, false);
    final long isAbstract = this.createFeature(MMMTypeProperties.IS_ABSTRACT, complexTypeFeaturePckg, boolType, false, false, false);

    final long genericTypes = this.createFeature(MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, complexTypeFeaturePckg, stringType, false, false, false);
    final long boundTypes = this.createFeature(MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, complexTypeFeaturePckg, TYPE, false, false, false);

    final long enumLiterals = this.createFeature(MMMTypeProperties.ENUM_LITERALS, enumTypeFeaturePckg, stringType, false, false, false);

    final long enumLiteralLiteral = this.createFeature(MMMTypeProperties.ENUMLIT_LITERAL, enumLiteralFeaturePckg, stringType, false, false, false);
    final long enumLiteralValue = this.createFeature(MMMTypeProperties.ENUMLIT_VALUE, enumLiteralFeaturePckg, stringType, false, false, false);
    final long enumLiteralEnum = this.createFeature(MMMTypeProperties.ENUMLIT_ENUM, enumLiteralFeaturePckg, stringType, false, false, false);

    final long isInt = this.createFeature(MMMTypeProperties.IS_INT, dataTypeFeaturePckg, boolType, false, false, false);
    final long isBoolean = this.createFeature(MMMTypeProperties.IS_BOOLEAN, dataTypeFeaturePckg, boolType, false, false, false);
    final long isReal = this.createFeature(MMMTypeProperties.IS_REAL, dataTypeFeaturePckg, boolType, false, false, false);
    final long isString = this.createFeature(MMMTypeProperties.IS_STRING, dataTypeFeaturePckg, boolType, false, false, false);

    final long parameters = this.createFeature(MMMTypeProperties.OPERATION_PARAMETERS, operationFeaturePckg, stringType, false, false, false);

    /*** Type ***/
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.NAME, MMMTypeProperties.TYPE_NAME);
    final long typeAllSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, typeAllSuperTypes, ADMIN, MA_TOOL_ID, TYPE);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.ALLSUPER_TYPES, typeAllSuperTypes);
    final long typeAllFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, typeAllFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, typeAllFeatures, ADMIN, MA_TOOL_ID, allSuperTypes);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, typeAllFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE, MMMTypeProperties.IS_ABSTRACT, true);

    /*** DataType ***/
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.NAME, MMMTypeProperties.DATATYPE_NAME);
    final long dataTypeSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataTypeFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, dataTypeSuperTypes, ADMIN, MA_TOOL_ID, TYPE);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.ALLSUPER_TYPES, dataTypeSuperTypes);
    final long dataTypeAllFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataTypeFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, dataTypeAllFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, dataTypeAllFeatures, ADMIN, MA_TOOL_ID, isInt);
    this.datastorage.addElementToCollection(FIRST_VERSION, dataTypeAllFeatures, ADMIN, MA_TOOL_ID, isReal);
    this.datastorage.addElementToCollection(FIRST_VERSION, dataTypeAllFeatures, ADMIN, MA_TOOL_ID, isBoolean);
    this.datastorage.addElementToCollection(FIRST_VERSION, dataTypeAllFeatures, ADMIN, MA_TOOL_ID, isString);
    this.datastorage.addElementToCollection(FIRST_VERSION, dataTypeAllFeatures, ADMIN, MA_TOOL_ID, allSuperTypes);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, typeAllFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataTypeFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataTypeFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataTypeFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, MMMTypeProperties.IS_ABSTRACT, true);

    /*** TypeParameter ***/
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.NAME, MMMTypeProperties.TYPEPARAMETER_NAME);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.TYPEPARAMETER_GENERICBOUNDTYPE, DataStorage.COMPLEX_TYPE);
    final long typeParameterSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeParameterFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, typeParameterSuperTypes, ADMIN, MA_TOOL_ID, TYPE_PARAMETER);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.ALLSUPER_TYPES, typeParameterSuperTypes);
    final long typeParameterFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeParameterFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, typeParameterFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, typeParameterFeatures, ADMIN, MA_TOOL_ID, genericBoundType);
    this.datastorage.addElementToCollection(FIRST_VERSION, typeParameterFeatures, ADMIN, MA_TOOL_ID, allSuperTypes);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, typeParameterFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeParameterFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeParameterFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, typeParameterFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPE_PARAMETER, MMMTypeProperties.IS_ABSTRACT, true);

    /*** ComplexType ***/

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.NAME, MMMTypeProperties.COMPLEXTYPE_NAME);
    final long complexTypeSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, complexTypeFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeSuperTypes, ADMIN, MA_TOOL_ID, TYPE);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.ALLSUPER_TYPES, complexTypeSuperTypes);

    final long complexTypeFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, complexTypeFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeFeatures, ADMIN, MA_TOOL_ID, allFeatures);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeFeatures, ADMIN, MA_TOOL_ID, allOperations);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeFeatures, ADMIN, MA_TOOL_ID, allSuperTypes);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeFeatures, ADMIN, MA_TOOL_ID, genericTypes);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeFeatures, ADMIN, MA_TOOL_ID, boundTypes);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeFeatures, ADMIN, MA_TOOL_ID, isInterface);
    this.datastorage.addElementToCollection(FIRST_VERSION, complexTypeFeatures, ADMIN, MA_TOOL_ID, isAbstract);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, complexTypeFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, complexTypeFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, complexTypeFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, complexTypeFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, COMPLEX_TYPE, MMMTypeProperties.IS_ABSTRACT, true);

    /*** Enum ***/

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.NAME, MMMTypeProperties.ENUM_NAME);
    final long enumSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumTypeFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, enumSuperTypes, ADMIN, MA_TOOL_ID, DATA_TYPE);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.ALLSUPER_TYPES, enumSuperTypes);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.ENUM_LITERALS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumTypeFeaturePckg, true));

    final long enumFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumTypeFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, enumFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, enumFeatures, ADMIN, MA_TOOL_ID, enumLiterals);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, enumFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumTypeFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumTypeFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumTypeFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM, MMMTypeProperties.IS_ABSTRACT, true);

    /*** EnumLiteral ***/

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.NAME, MMMTypeProperties.ENUMLIT_NAME);
    final long enumLiteralSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumLiteralFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, enumLiteralSuperTypes, ADMIN, MA_TOOL_ID, ENUMM_LITERAL);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.ALLSUPER_TYPES, enumLiteralSuperTypes);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.ENUMLIT_LITERAL, null);

    final long enumLiteralFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumLiteralFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, enumLiteralFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, enumLiteralFeatures, ADMIN, MA_TOOL_ID, enumLiteralEnum);
    this.datastorage.addElementToCollection(FIRST_VERSION, enumLiteralFeatures, ADMIN, MA_TOOL_ID, enumLiteralLiteral);
    this.datastorage.addElementToCollection(FIRST_VERSION, enumLiteralFeatures, ADMIN, MA_TOOL_ID, enumLiteralValue);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, enumLiteralFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumLiteralFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumLiteralFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, enumLiteralFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, ENUMM_LITERAL, MMMTypeProperties.IS_ABSTRACT, true);

    /*** TypedElement ***/
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.NAME, MMMTypeProperties.TYPEDELEMENT_NAME);
    final long typeElementsuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typedElementFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, typeElementsuperTypes, ADMIN, MA_TOOL_ID, TYPED_ELEMENT);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.ALLSUPER_TYPES, typeElementsuperTypes);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.ELEMENT_TYPE, COMPLEX_TYPE);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.IS_ORDERED, false);

    final long typedElementFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_PACKAGE_ID, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, typedElementFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, typedElementFeatures, ADMIN, MA_TOOL_ID, elementType);
    this.datastorage.addElementToCollection(FIRST_VERSION, typedElementFeatures, ADMIN, MA_TOOL_ID, isMany);
    this.datastorage.addElementToCollection(FIRST_VERSION, typedElementFeatures, ADMIN, MA_TOOL_ID, isOrdered);
    this.datastorage.addElementToCollection(FIRST_VERSION, typedElementFeatures, ADMIN, MA_TOOL_ID, isUnique);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, typedElementFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typedElementFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, typedElementFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, typedElementFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TYPED_ELEMENT, MMMTypeProperties.IS_ABSTRACT, true);

    /*** Feature ***/

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.NAME, MMMTypeProperties.FEATURE_NAME);
    final long featureSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, featureFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, featureSuperTypes, ADMIN, MA_TOOL_ID, TYPED_ELEMENT);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.ALLSUPER_TYPES, featureSuperTypes);

    final long featureFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, featureFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, featureFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, featureFeatures, ADMIN, MA_TOOL_ID, elementType);
    this.datastorage.addElementToCollection(FIRST_VERSION, featureFeatures, ADMIN, MA_TOOL_ID, isMany);
    this.datastorage.addElementToCollection(FIRST_VERSION, featureFeatures, ADMIN, MA_TOOL_ID, isOrdered);
    this.datastorage.addElementToCollection(FIRST_VERSION, featureFeatures, ADMIN, MA_TOOL_ID, isUnique);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, featureFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, featureFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, featureFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, featureFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, MMMTypeProperties.IS_ABSTRACT, true);

    /*** Parameter ***/

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.NAME, MMMTypeProperties.PARAMETER_NAME);
    final long parameterSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, parameterFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, parameterSuperTypes, ADMIN, MA_TOOL_ID, TYPED_ELEMENT);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.ALLSUPER_TYPES, parameterSuperTypes);

    final long parameterFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, parameterFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, parameterFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, parameterFeatures, ADMIN, MA_TOOL_ID, elementType);
    this.datastorage.addElementToCollection(FIRST_VERSION, parameterFeatures, ADMIN, MA_TOOL_ID, isMany);
    this.datastorage.addElementToCollection(FIRST_VERSION, parameterFeatures, ADMIN, MA_TOOL_ID, isOrdered);
    this.datastorage.addElementToCollection(FIRST_VERSION, parameterFeatures, ADMIN, MA_TOOL_ID, isUnique);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, parameterFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, parameterFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, parameterFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, parameterFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, PARAMETER, MMMTypeProperties.IS_ABSTRACT, true);

    /*** Operation ***/
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.NAME, MMMTypeProperties.OPERATION_NAME);
    final long operationSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, operationFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, operationSuperTypes, ADMIN, MA_TOOL_ID, TYPED_ELEMENT);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.ALLSUPER_TYPES, operationSuperTypes);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.OPERATION_PARAMETERS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, operationFeaturePckg, true));

    final long operationFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, operationFeaturePckg, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, operationFeatures, ADMIN, MA_TOOL_ID, name);
    this.datastorage.addElementToCollection(FIRST_VERSION, operationFeatures, ADMIN, MA_TOOL_ID, parameters);
    this.datastorage.addElementToCollection(FIRST_VERSION, operationFeatures, ADMIN, MA_TOOL_ID, elementType);
    this.datastorage.addElementToCollection(FIRST_VERSION, operationFeatures, ADMIN, MA_TOOL_ID, isMany);
    this.datastorage.addElementToCollection(FIRST_VERSION, operationFeatures, ADMIN, MA_TOOL_ID, isOrdered);
    this.datastorage.addElementToCollection(FIRST_VERSION, operationFeatures, ADMIN, MA_TOOL_ID, isUnique);

    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, operationFeatures);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.COMPLEXTYPE_ALLOPERATIONS,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, operationFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES,
        this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, operationFeaturePckg, true));
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES,
        this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, operationFeaturePckg));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.IS_INTERFACE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OPERATION, MMMTypeProperties.IS_ABSTRACT, true);

    /*** ModelAnalyzer ***/
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, CONSTRAINT, MMMTypeProperties.NAME, MMMTypeProperties.CONSTRAINT_NAME);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, QUERY, MMMTypeProperties.NAME, MMMTypeProperties.QUERY_NAME);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, INSTANCE, MMMTypeProperties.NAME, MMMTypeProperties.INSTANCE_NAME);

  }

  private long createFeature(final String name, final Long pckg, final Long elementType, final boolean isMany, final boolean isOrdered, final boolean isUnique) {
    final long feature = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, pckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, feature, MMMTypeProperties.NAME, name);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, feature, MMMTypeProperties.ELEMENT_TYPE, elementType);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, feature, MMMTypeProperties.IS_MANY, isMany);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, feature, MMMTypeProperties.IS_ORDERED, isOrdered);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, feature, MMMTypeProperties.IS_UNIQUE, isUnique);
    return feature;
  }

  private long createDataType(final String name, final Long pckg, final boolean isInt, final boolean isReal, final boolean isBool, final boolean isString) {
    final long dataType = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, pckg);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataType, MMMTypeProperties.NAME, name);
    this.datastorage.setArtifactContainer(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataType, pckg);
    this.datastorage
        .setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataType, MMMTypeProperties.ALLSUPER_TYPES, this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, pckg, true));
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataType, MMMTypeProperties.IS_INT, isInt);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataType, MMMTypeProperties.IS_REAL, isReal);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataType, MMMTypeProperties.IS_BOOLEAN, isBool);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, dataType, MMMTypeProperties.IS_STRING, isString);
    return dataType;
  }

  private void initMappingArtifact() {
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, ROOT_TYPE_ID, MAPPING_PACKAGE_ID, true);
    this.datastorage.setArtifactType(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMM_MAPPING);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.NAME, MMMTypeProperties.MAPPING_NAME);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_METAMODEL, null);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_COMPLEX_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_DATA_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_OPERATION_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_PARAMETER_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ATTRIBUTE_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_REFERENCE_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ENUM_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ENUMLITERAL_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_TYPEPARAMETER_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_STRING_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_BOOLEAN_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_INTEGER_TYPE, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_DOUBLE_TYPE, null);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ALLSUPERTYPES_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ALLFEATURES_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ALLOPERATIONS_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_PARAMETERS_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ORDERED_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_UNIQUE_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_MANY_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ISABSTRACT_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_ISINTERFACE_PROPERTY, null);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, MMM_MAPPING, MMMTypeProperties.MAPPING_TYPE_PROPERTY, null);
  }

  protected void initTraceLink() {
    /** LinkPackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_PACKAGE_ID, PACKAGE_TYPE_ID, SEA_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_PACKAGE_ID, PROPERTY_NAME, "link");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.link");

    /** LinkTypePackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_TYPE_PACKAGE_ID, PACKAGE_TYPE_ID, TRACE_LINK_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_TYPE_PACKAGE_ID, PROPERTY_NAME, "TraceLink");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_TYPE_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.link.tracelink");

    /** LinkTypeFeaturePackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_TYPE_FEATURE_PACKAGE_ID, PACKAGE_TYPE_ID, TRACE_LINK_TYPE_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_TYPE_FEATURE_PACKAGE_ID, PROPERTY_NAME, "features");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, TRACE_LINK_TYPE_FEATURE_PACKAGE_ID, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.link.tracelink.features");

    /** LinkMetaModel **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, LINK_META_MODEL_ID, META_MODEL_TYPE_ID, TRACE_LINK_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, LINK_META_MODEL_ID, PROPERTY_COLLECTION_NEXT_IDX, 0l, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, LINK_META_MODEL_ID, PROPERTY_COLLECTION_REFERENCES, true, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, LINK_META_MODEL_ID, PROPERTY_NAME, "LinkMetaModel", false, true);

    /** LinkType **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, ROOT_TYPE_ID, TRACE_LINK_TYPE_PACKAGE_ID, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, LINK_META_MODEL_ID, ADMIN, MA_TOOL_ID, TRACE_LINK);

    /** allSuperTypes **/
    final Long allSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK_TYPE_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, allSuperTypes, PROPERTY_NAME, "allSuperTypes", false, true);

    /** allFeatures **/
    final Long allFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK_TYPE_FEATURE_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, allFeatures, PROPERTY_NAME, "features", false, true);

    /** genericTypes **/
    final Long genericTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK_TYPE_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, genericTypes, PROPERTY_NAME, "genericTypes", false, true);

    final Long tParameter = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, DataStorage.TYPE_PARAMETER, TRACE_LINK_TYPE_FEATURE_PACKAGE_ID);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, tParameter, PROPERTY_NAME, "T", false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, tParameter, MMMTypeProperties.TYPEPARAMETER_GENERICBOUNDTYPE, ROOT_TYPE_ID, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, genericTypes, ADMIN, MA_TOOL_ID, tParameter);

    final Long kParameter = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, DataStorage.TYPE_PARAMETER, TRACE_LINK_TYPE_FEATURE_PACKAGE_ID);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, kParameter, PROPERTY_NAME, "K", false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, kParameter, MMMTypeProperties.TYPEPARAMETER_GENERICBOUNDTYPE, ROOT_TYPE_ID, true, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, genericTypes, ADMIN, MA_TOOL_ID, kParameter);

    // final Artifact sourceFeature = MMMTypesFactory.createFeature(ws, "source", linkType, tParameter, false, false,
    // true);
    // sourceFeature.setPackage(ws, ws.getPackage(DataStorage.TRACE_LINK_TYPE_FEATURE_PACKAGE_ID));
    // MMMTypesFactory.addFeatureToComplexType(ws, linkType, sourceFeature);

    final long sourceFeature = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, TRACE_LINK_TYPE_FEATURE_PACKAGE_ID);
    this.datastorage.addElementToCollection(FIRST_VERSION, allFeatures, ADMIN, MA_TOOL_ID, sourceFeature);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, sourceFeature, MMMTypeProperties.NAME, "source");
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, sourceFeature, MMMTypeProperties.ELEMENT_TYPE, tParameter);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, sourceFeature, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, sourceFeature, MMMTypeProperties.IS_ORDERED, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, sourceFeature, MMMTypeProperties.IS_UNIQUE, false);

    // final Artifact targetFeature = MMMTypesFactory.createFeature(ws, "target", linkType, kParameter, false, false,
    // true);
    // targetFeature.setPackage(ws, ws.getPackage(DataStorage.TRACE_LINK_TYPE_FEATURE_PACKAGE_ID));
    // MMMTypesFactory.addFeatureToComplexType(ws, linkType, targetFeature);

    final long targetFeature = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, TRACE_LINK_TYPE_FEATURE_PACKAGE_ID);
    this.datastorage.addElementToCollection(FIRST_VERSION, allFeatures, ADMIN, MA_TOOL_ID, targetFeature);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, targetFeature, MMMTypeProperties.NAME, "target");
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, targetFeature, MMMTypeProperties.ELEMENT_TYPE, kParameter);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, targetFeature, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, targetFeature, MMMTypeProperties.IS_ORDERED, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, targetFeature, MMMTypeProperties.IS_UNIQUE, false);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, MMMTypeProperties.NAME, "TraceLink");
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, MMMTypeProperties.ALLSUPER_TYPES, allSuperTypes, true, true);
    this.datastorage.setArtifactType(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, COMPLEX_TYPE);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, allFeatures, true, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, genericTypes, true, true);
    final long map = this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK_TYPE_PACKAGE_ID);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, map);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, MMMTypeProperties.IS_INTERFACE, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, TRACE_LINK, MMMTypeProperties.IS_ABSTRACT, false);

    final long mapCollection = this.datastorage.getPropertyReference(FIRST_VERSION, map, PROPERTY_MAP_COLLECTION);
    final Map<String, Long> properties = new HashMap<String, Long>();
    properties.put(DefaultEntry.KEY, tParameter);
    properties.put(DefaultEntry.VALUE, ROOT_TYPE_ID);
    long bindtParameter = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, ROOT_TYPE_ID, null, null, null, null, properties);
    properties.clear();
    properties.put(DefaultEntry.KEY, kParameter);
    properties.put(DefaultEntry.VALUE, ROOT_TYPE_ID);
    long bindkParameter = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, ROOT_TYPE_ID, null, null, null, null, properties);
    this.datastorage.addElementToCollection(FIRST_VERSION, mapCollection, ADMIN, MA_TOOL_ID, bindtParameter);
    this.datastorage.addElementToCollection(FIRST_VERSION, mapCollection, ADMIN, MA_TOOL_ID, bindkParameter);
  }

  protected void initEPlanTypes() {
    /** EPlanPackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_PCKG, PACKAGE_TYPE_ID, SEA_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_PCKG, PROPERTY_NAME, "eplan");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_PCKG, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.eplan");

    /** Function Package **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_FUNCTION_TYPE_PCKG, PACKAGE_TYPE_ID, EPLAN_PCKG, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_FUNCTION_TYPE_PCKG, PROPERTY_NAME, "FunctionType");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_FUNCTION_TYPE_PCKG, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.eplan.functiontype");

    /** FunctionType Feature Package **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_FUNCTION_TYPE_FEATURE_PCKG, PACKAGE_TYPE_ID, EPLAN_FUNCTION_TYPE_PCKG, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_FUNCTION_TYPE_FEATURE_PCKG, PROPERTY_NAME, "features");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_FUNCTION_TYPE_FEATURE_PCKG, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.eplan.functiontype.features");

    /** Connection Package **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_CONNECTION_TYPE_PCKG, PACKAGE_TYPE_ID, EPLAN_PCKG, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_CONNECTION_TYPE_PCKG, PROPERTY_NAME, "ConnectionType");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_CONNECTION_TYPE_PCKG, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.eplan.connectiontype");

    /** ConnectionType Feature Package **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_CONNECTION_TYPE_FEATURE_PCKG, PACKAGE_TYPE_ID, EPLAN_CONNECTION_TYPE_PCKG, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_CONNECTION_TYPE_FEATURE_PCKG, PROPERTY_NAME, "features");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, ROOT_TOOL_ID, EPLAN_CONNECTION_TYPE_FEATURE_PCKG, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.eplan.connectiontype.features");

    /** LinkMetaModel **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_METAMODEL, META_MODEL_TYPE_ID, EPLAN_PCKG, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_METAMODEL, PROPERTY_COLLECTION_NEXT_IDX, 0l, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_METAMODEL, PROPERTY_COLLECTION_REFERENCES, true, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_METAMODEL, PROPERTY_NAME, "EPlanMetaModel", false, true);

    /** FunctionType **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, ROOT_TYPE_ID, EPLAN_FUNCTION_TYPE_PCKG, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, EPLAN_METAMODEL, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE);

    /** allSuperTypes **/
    final Long functionTypeAllSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE_PCKG, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, functionTypeAllSuperTypes, PROPERTY_NAME, "allSuperTypes", false, true);

    /** allFeatures **/
    final Long functionTypeAllFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE_FEATURE_PCKG, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, functionTypeAllFeatures, PROPERTY_NAME, "features", false, true);

    /** genericTypes **/
    final Long functionTypeGenericTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE_PCKG, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, functionTypeGenericTypes, PROPERTY_NAME, "genericTypes", false, true);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, MMMTypeProperties.NAME, "FunctionType");
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, MMMTypeProperties.ALLSUPER_TYPES, functionTypeAllSuperTypes, true, true);
    this.datastorage.setArtifactType(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, COMPLEX_TYPE);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, functionTypeAllFeatures, true, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, functionTypeGenericTypes, true, true);
    long map = this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE_PCKG);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, map);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, MMMTypeProperties.IS_INTERFACE, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_FUNCTION_TYPE, MMMTypeProperties.IS_ABSTRACT, false);

    /** ConnectionType **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, ROOT_TYPE_ID, EPLAN_CONNECTION_TYPE_PCKG, true);
    this.datastorage.addElementToCollection(FIRST_VERSION, EPLAN_METAMODEL, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE);

    /** allSuperTypes **/
    final Long connectionTypeAllSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE_PCKG, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, connectionTypeAllSuperTypes, PROPERTY_NAME, "allSuperTypes", false, true);

    /** allFeatures **/
    final Long connectionTypeAllFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE_FEATURE_PCKG, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, connectionTypeAllFeatures, PROPERTY_NAME, "features", false, true);

    /** genericTypes **/
    final Long connectionTypeGenericTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE_PCKG, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, connectionTypeGenericTypes, PROPERTY_NAME, "genericTypes", false, true);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, MMMTypeProperties.NAME, "ConnectionType");
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, MMMTypeProperties.ALLSUPER_TYPES, connectionTypeAllSuperTypes, true, true);
    this.datastorage.setArtifactType(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, COMPLEX_TYPE);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, connectionTypeAllFeatures, true, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, connectionTypeGenericTypes, true, true);
    map = this.datastorage.createMap(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE_PCKG);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, map);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, MMMTypeProperties.IS_INTERFACE, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, EPLAN_CONNECTION_TYPE, MMMTypeProperties.IS_ABSTRACT, false);

    /** string type **/
    final long stringType = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, DATA_TYPE, EPLAN_PCKG);
    this.datastorage.addElementToCollection(FIRST_VERSION, EPLAN_METAMODEL, ADMIN, MA_TOOL_ID, stringType);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, stringType, MMMTypeProperties.NAME, "String");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, stringType, MMMTypeProperties.IS_INT, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, stringType, MMMTypeProperties.IS_REAL, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, stringType, MMMTypeProperties.IS_BOOLEAN, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, stringType, MMMTypeProperties.IS_STRING, true);

    /** Function DT **/
    final long DT = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, EPLAN_FUNCTION_TYPE_FEATURE_PCKG);
    this.datastorage.addElementToCollection(FIRST_VERSION, functionTypeAllFeatures, ADMIN, MA_TOOL_ID, DT);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, DT, MMMTypeProperties.NAME, "DT");
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, DT, MMMTypeProperties.ELEMENT_TYPE, stringType);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, DT, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, DT, MMMTypeProperties.IS_ORDERED, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, DT, MMMTypeProperties.IS_UNIQUE, false);

    /** Function PLC Address **/
    final long PLCADDRESS = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, EPLAN_CONNECTION_TYPE_FEATURE_PCKG);
    this.datastorage.setArtifactContainer(FIRST_VERSION, ADMIN, MA_TOOL_ID, PLCADDRESS, EPLAN_FUNCTION_TYPE_FEATURE_PCKG);
    this.datastorage.addElementToCollection(FIRST_VERSION, functionTypeAllFeatures, ADMIN, MA_TOOL_ID, PLCADDRESS);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, PLCADDRESS, MMMTypeProperties.NAME, "PLC Address");
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, PLCADDRESS, MMMTypeProperties.ELEMENT_TYPE, stringType);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, PLCADDRESS, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, PLCADDRESS, MMMTypeProperties.IS_ORDERED, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, PLCADDRESS, MMMTypeProperties.IS_UNIQUE, false);

    /** Connection OUT Address **/
    final long OUT = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, EPLAN_CONNECTION_TYPE_FEATURE_PCKG);
    this.datastorage.addElementToCollection(FIRST_VERSION, connectionTypeAllFeatures, ADMIN, MA_TOOL_ID, OUT);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT, MMMTypeProperties.NAME, "Out");
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT, MMMTypeProperties.ELEMENT_TYPE, EPLAN_FUNCTION_TYPE);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT, MMMTypeProperties.IS_ORDERED, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT, MMMTypeProperties.IS_UNIQUE, false);

    /** Connection OUT_PIN Address **/
    final long OUT_PIN = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, EPLAN_CONNECTION_TYPE_FEATURE_PCKG);
    this.datastorage.addElementToCollection(FIRST_VERSION, connectionTypeAllFeatures, ADMIN, MA_TOOL_ID, OUT_PIN);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT_PIN, MMMTypeProperties.NAME, "OutPin");
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT_PIN, MMMTypeProperties.ELEMENT_TYPE, stringType);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT_PIN, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT_PIN, MMMTypeProperties.IS_ORDERED, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, OUT_PIN, MMMTypeProperties.IS_UNIQUE, false);

    /** Connection IN Address **/
    final long IN = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, EPLAN_CONNECTION_TYPE_FEATURE_PCKG);
    this.datastorage.addElementToCollection(FIRST_VERSION, connectionTypeAllFeatures, ADMIN, MA_TOOL_ID, IN);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN, MMMTypeProperties.NAME, "In");
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN, MMMTypeProperties.ELEMENT_TYPE, EPLAN_FUNCTION_TYPE);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN, MMMTypeProperties.IS_ORDERED, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN, MMMTypeProperties.IS_UNIQUE, false);

    /** Connection IN_PIN Address **/
    final long IN_PIN = this.datastorage.createArtifact(FIRST_VERSION, ADMIN, MA_TOOL_ID, FEATURE, EPLAN_CONNECTION_TYPE_FEATURE_PCKG);
    this.datastorage.addElementToCollection(FIRST_VERSION, connectionTypeAllFeatures, ADMIN, MA_TOOL_ID, IN_PIN);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN_PIN, MMMTypeProperties.NAME, "InPin");
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN_PIN, MMMTypeProperties.ELEMENT_TYPE, stringType);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN_PIN, MMMTypeProperties.IS_MANY, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN_PIN, MMMTypeProperties.IS_ORDERED, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, MA_TOOL_ID, IN_PIN, MMMTypeProperties.IS_UNIQUE, false);
  }

  protected void initProETypes() {
    /** ProEPackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE, PACKAGE_TYPE_ID, SEA_PACKAGE_ID, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE, PROPERTY_NAME, "proe");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.proe");

    /** DimensionPackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION, PACKAGE_TYPE_ID, PROE_PACKAGE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION, PROPERTY_NAME, "Dimension");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.link.dimension");

    /** DimensionFeaturePackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION_FEATURE, PACKAGE_TYPE_ID, PROE_PACKAGE_DIMENSION, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION_FEATURE, PROPERTY_NAME, "features");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION_FEATURE, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.link.dimension.features");

    /** ParameterPackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER, PACKAGE_TYPE_ID, PROE_PACKAGE, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER, PROPERTY_NAME, "Parameter");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.proe.parameter");

    /** DimensionFeaturePackage **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER_FEATURE, PACKAGE_TYPE_ID, PROE_PACKAGE_PARAMETER, true);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER_FEATURE, PROPERTY_NAME, "features");
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER_FEATURE, PROPERTY_FULL_QUALIFIED_NAME, "at.jku.sea.link.parameter.features");

    /** Dimension **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, ROOT_TYPE_ID, PROE_PACKAGE_DIMENSION, true);

    /** allSuperTypes **/
    final Long dimAllSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, dimAllSuperTypes, PROPERTY_NAME, "allSuperTypes", false, true);

    /** allFeatures **/
    final Long dimAllFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION_FEATURE, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, dimAllFeatures, PROPERTY_NAME, "features", false, true);

    /** genericTypes **/
    final Long dimGenericTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, dimGenericTypes, PROPERTY_NAME, "genericTypes", false, true);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, MMMTypeProperties.NAME, "Dimension");
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, MMMTypeProperties.ALLSUPER_TYPES, dimAllSuperTypes, true, true);
    this.datastorage.setArtifactType(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, COMPLEX_TYPE);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, dimAllFeatures, true, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, dimGenericTypes, true, true);
    long map = this.datastorage.createMap(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_DIMENSION);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, map);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, MMMTypeProperties.IS_INTERFACE, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_DIMENSION, MMMTypeProperties.IS_ABSTRACT, false);

    /** Parameter **/
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, ROOT_TYPE_ID, PROE_PACKAGE_PARAMETER, true);

    /** allSuperTypes **/
    final Long paramAllSuperTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, paramAllSuperTypes, PROPERTY_NAME, "allSuperTypes", false, true);

    /** allFeatures **/
    final Long paramAllFeatures = this.datastorage.createCollection(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER_FEATURE, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, paramAllFeatures, PROPERTY_NAME, "features", false, true);

    /** genericTypes **/
    final Long paramGenericTypes = this.datastorage.createCollection(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, paramGenericTypes, PROPERTY_NAME, "genericTypes", false, true);

    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, MMMTypeProperties.NAME, "Parameter");
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, MMMTypeProperties.ALLSUPER_TYPES, paramAllSuperTypes, true, true);
    this.datastorage.setArtifactType(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, COMPLEX_TYPE);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, MMMTypeProperties.COMPLEXTYPE_ALLFEATURES, paramAllFeatures, true, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, MMMTypeProperties.COMPLEXTYPE_GENERICTYPES, paramGenericTypes, true, true);
    map = this.datastorage.createMap(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PACKAGE_PARAMETER);
    this.datastorage.setPropertyReference(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, MMMTypeProperties.COMPLEXTYPE_BOUNDTYPES, map);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, MMMTypeProperties.IS_INTERFACE, false);
    this.datastorage.setPropertyValue(FIRST_VERSION, ADMIN, PROE_TOOL_ID, PROE_PARAMETER, MMMTypeProperties.IS_ABSTRACT, false);
  }

  protected void initJavaMetamodel() {
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ECLIPSE_TOOL_ID, JAVA_METAMODEL, META_MODEL_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ECLIPSE_TOOL_ID, JAVA_METAMODEL, MMMTypeProperties.METAMODEL, MMM_META_MODEL, true, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ECLIPSE_TOOL_ID, JAVA_METAMODEL, PROPERTY_COLLECTION_NEXT_IDX, 0l, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ECLIPSE_TOOL_ID, JAVA_METAMODEL, PROPERTY_COLLECTION_REFERENCES, true, false, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ECLIPSE_TOOL_ID, JAVA_METAMODEL, PROPERTY_NAME, "JavaMetaModel", false, true);
    this.datastorage.storeArtifact(FIRST_VERSION, ADMIN, ECLIPSE_TOOL_ID, JAVA_OBJECT_TYPE, ROOT_TYPE_ID, DESIGNSPACE_PACKAGE_ID, true);
    this.datastorage.storeProperty(FIRST_VERSION, ADMIN, ECLIPSE_TOOL_ID, JAVA_OBJECT_TYPE, PROPERTY_NAME, "JavaObject", false, true);
  }

}
