package at.jku.sea.cloud.implementation.sql;

import java.util.Collection;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.PropagationType;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.exceptions.WorkspaceExpiredException;
import at.jku.sea.cloud.implementation.DefaultCloud;
import at.jku.sea.cloud.mmm.MMMTypeUtils;
import at.jku.sea.cloud.mmm.MMMTypesFactory;

public class EPlanDemoSQLDataStorage extends SQLDataStorage implements DataStorage {

  public static final String EPLAN_CODE_LINK = "EPlanCodeLink";
  public static final String SOURCECODE_LINK = "SourceLink";
  private static final long serialVersionUID = 1L;
  private static long dimID;
  private static long dataID;

  private static CollectionArtifact createCollectionArtifact(final Collection<Artifact> collection, final Workspace ws) {
    final CollectionArtifact aCollection = ws.createCollection(true);
    for (final Artifact element : collection) {
      aCollection.addElement(ws, element);
    }
    return aCollection;
  }

  private static void createLinkMetaModel(final Cloud cloud, final Workspace ws) {
    final Package linkPackage = ws.getPackage(TRACE_LINK_PACKAGE_ID);

    final MetaModel linkMetaModel = ws.getMetaModel(LINK_META_MODEL_ID);
    linkMetaModel.setPackage(ws, linkPackage);

    final MetaModel dataMetaModel = ws.createMetaModel();
    dataMetaModel.setPackage(ws, linkPackage);
    dataMetaModel.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "DataMetaModel");

    final Package realTypePckg = ws.createPackage();
    realTypePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Real");
    final Artifact realType = realType(ws, realTypePckg);
    realType.setPackage(ws, realTypePckg);

    final Package stringTypePckg = ws.createPackage();
    stringTypePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "String");
    final Artifact stringType = stringType(ws, stringTypePckg);
    stringType.setPackage(ws, stringTypePckg);

    final Package intTypePckg = ws.createPackage();
    intTypePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Integer");
    final Artifact intType = intType(ws, intTypePckg);
    intType.setPackage(ws, intTypePckg);

    final Package boolTypePckg = ws.createPackage();
    boolTypePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Boolean");
    final Artifact boolType = boolType(ws, boolTypePckg);
    boolType.setPackage(ws, boolTypePckg);

    final Package dataTypePackage = ws.createPackage();
    dataTypePackage.setPackage(ws, linkPackage);
    dataTypePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "DataTypes");
    realType.getPackage().setPackage(ws, dataTypePackage);
    stringType.getPackage().setPackage(ws, dataTypePackage);
    intType.getPackage().setPackage(ws, dataTypePackage);
    boolType.getPackage().setPackage(ws, dataTypePackage);
    dataMetaModel.addArtifact(ws, realType);
    dataMetaModel.addArtifact(ws, intType);
    dataMetaModel.addArtifact(ws, boolType);
    dataMetaModel.addArtifact(ws, stringType);

    /** Data **/
    final Package dataPackage = ws.createPackage();
    dataPackage.setPackage(ws, linkPackage);
    dataPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Data");

    final Artifact data = MMMTypesFactory.createComplexType(ws, dataPackage, /* name */"Data",/* is_interface */
        false, /* is_abstract */
        false);
    dataID = data.getId();
    dataMetaModel.addArtifact(ws, data);

    final Package featurePckg = ws.createPackage();
    featurePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    featurePckg.setPackage(ws, dataPackage);

    final Artifact attribute1 = MMMTypesFactory.createFeature(ws, "escapedValue", data, stringType, false, false, false);

    MMMTypesFactory.addFeatureToComplexType(ws, data, attribute1);

    attribute1.setPackage(ws, featurePckg);

    // /** Dimension **/
    // final Artifact dimension = ws.getArtifact(PROE_DIMENSION);
    // dataMetaModel.addArtifact(ws, dimension);
    // MMMTypesFactory.addSuperTypeToComplexType(ws, dimension, qData);
    //
    // MMMTypesFactory.addFeatureToComplexType(ws, dimension, attribute1);
    // MMMTypesFactory.addFeatureToComplexType(ws, dimension, unit);
    //
    // /** Parameter **/
    // final Artifact parameter = ws.getArtifact(PROE_PARAMETER);
    // dataMetaModel.addArtifact(ws, parameter);
    // MMMTypesFactory.addSuperTypeToComplexType(ws, parameter, qData);
    //
    // MMMTypesFactory.addFeatureToComplexType(ws, parameter, attribute1);
    // MMMTypesFactory.addFeatureToComplexType(ws, parameter, unit);

    /** LinkType **/

    // final Package linkTypePackage = ws.createPackage();
    // linkTypePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "TraceLink");
    // linkTypePackage.setPackage(ws, linkPackage);
    // linkType = MMMTypesFactory.createComplexType(ws, linkTypePackage, linkTypeName, false, false);
    //
    // final Package linkTypeFeaturePackage = ws.createPackage();
    // linkTypeFeaturePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    // linkTypeFeaturePackage.setPackage(ws, linkTypePackage);
    final Artifact linkType = ws.getArtifact(DataStorage.TRACE_LINK);

    final Artifact tParameter = MMMTypesFactory.createTypeParameter(ws, "T", null, linkType);
    tParameter.setPackage(ws, linkType.getPackage());

    final Artifact kParameter = MMMTypesFactory.createTypeParameter(ws, "K", null, linkType);
    kParameter.setPackage(ws, linkType.getPackage());

    final Artifact sourceFeature = MMMTypesFactory.createFeature(ws, "source", linkType, tParameter, false, false, true);
    sourceFeature.setPackage(ws, ws.getPackage(DataStorage.TRACE_LINK_TYPE_FEATURE_PACKAGE_ID));
    MMMTypesFactory.addFeatureToComplexType(ws, linkType, sourceFeature);

    final Artifact targetFeature = MMMTypesFactory.createFeature(ws, "target", linkType, kParameter, false, false, true);
    targetFeature.setPackage(ws, ws.getPackage(DataStorage.TRACE_LINK_TYPE_FEATURE_PACKAGE_ID));
    MMMTypesFactory.addFeatureToComplexType(ws, linkType, targetFeature);

    /** DataLinkType **/
    final Package sourceLinkTypePackage = ws.createPackage();
    sourceLinkTypePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, SOURCECODE_LINK);
    sourceLinkTypePackage.setPackage(ws, linkPackage);
    final Artifact sourceLinkType = MMMTypesFactory.createComplexType(ws, sourceLinkTypePackage, SOURCECODE_LINK, false, false);

    MMMTypesFactory.addSuperTypeToComplexType(ws, sourceLinkType, linkType);

    MMMTypesFactory.addFeatureToComplexType(ws, sourceLinkType, sourceFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, sourceLinkType, tParameter, data);
    MMMTypesFactory.addFeatureToComplexType(ws, sourceLinkType, targetFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, sourceLinkType, kParameter, data);

    /** GTELinkType **/
    final Package eplanCodeLinkTypePackage = ws.createPackage();
    eplanCodeLinkTypePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, EPLAN_CODE_LINK);
    eplanCodeLinkTypePackage.setPackage(ws, linkPackage);
    final Artifact eplanCodeLinkType = MMMTypesFactory.createComplexType(ws, eplanCodeLinkTypePackage, EPLAN_CODE_LINK, false, false);

    MMMTypesFactory.addSuperTypeToComplexType(ws, eplanCodeLinkType, linkType);

    MMMTypesFactory.addFeatureToComplexType(ws, eplanCodeLinkType, sourceFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, eplanCodeLinkType, tParameter, ws.getArtifact(DataStorage.EPLAN_CONNECTION_TYPE));
    MMMTypesFactory.addFeatureToComplexType(ws, eplanCodeLinkType, targetFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, eplanCodeLinkType, kParameter, sourceLinkType);

    // linkMetaModel.addArtifact(ws, linkType);
    linkMetaModel.addArtifact(ws, sourceLinkType);
    linkMetaModel.addArtifact(ws, eplanCodeLinkType);
    // targetFeature.delete(ws);

  }

  private static Artifact stringType(final Workspace ws, final Package pckg) {
    return MMMTypesFactory.createStringDataType(ws, pckg, "String");
  }

  private static Artifact realType(final Workspace ws, final Package pckg) {
    return MMMTypesFactory.createRealDataType(ws, pckg, "double");
  }

  private static Artifact intType(final Workspace ws, final Package pckg) {
    return MMMTypesFactory.createIntDataType(ws, pckg, "int");
  }

  private static Artifact boolType(final Workspace ws, final Package pckg) {
    return MMMTypesFactory.createBooleanDataType(ws, pckg, "boolean");
  }

  private static DataStorage initDemo() {
    final DataStorage storage = new DemoSQLDataStorage();

    if (storage.getHeadVersionNumber() == 1) {
      final Cloud cloud = new DefaultCloud(storage);
      final Owner demo = cloud.getOwner(DEMO);
      final Tool tool = cloud.getTool(EDITOR_TOOL_ID);
      final Workspace ws = cloud.createWorkspace(demo, tool, "demo-init");
      createLinkMetaModel(cloud, ws);

      final Package pckg = ws.createPackage();
      pckg.setPropertyValue(ws, PROPERTY_NAME, "Links");
      ws.commitAll("");
    }
    return storage;
  }

  /*
   * (non-Javadoc)
   *
   * @see at.jku.sea.cloud.implementation.sql.SQLDataStorage#init()
   */
  // @Override
  // protected void init() {
  // this.truncateAll();
  // super.init();
  // }

  @Override
  public Long getWorkspaceVersionNumber(final long owner, final long tool, final String identifier) {
    try {
      super.getWorkspaceRepresentation(-1L);
    } catch (final WorkspaceExpiredException e) {
      super.storePrivateWorkspace(-1L, ADMIN, EDITOR_TOOL_ID, "demo", super.getHeadVersionNumber(), null, PropagationType.triggered, PropagationType.triggered);
    }
    return -1L;
  }

  public static void main(final String[] args) throws Exception {
    bindDataStorageInstance(initDemo());
  }

}
