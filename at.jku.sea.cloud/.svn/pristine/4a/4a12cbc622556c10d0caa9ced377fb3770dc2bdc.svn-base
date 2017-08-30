/*
 * (C) Johannes Kepler University Linz, Austria, 2005-2013
 * Institute for Systems Engineering and Automation (SEA)
 * 
 * The software may only be used for academic purposes (teaching, scientific 
 * research). Any redistribution or commercialization of the software program
 * and documentation (or any part thereof) requires prior written permission of
 * the JKU. Redistributions of source code must retain the above copyright 
 * notice, this list of conditions and the following disclaimer.
 * 
 * This software program and documentation are copyrighted by Johannes Kepler 
 * University Linz, Austria (the JKU). The software program and documentation 
 * are supplied AS IS, without any accompanying services from the JKU. The JKU 
 * does not warrant that the operation of the program will be uninterrupted or 
 * error-free. The end-user understands that the program was developed for 
 * research purposes and is advised not to rely exclusively on the program for 
 * any reason.
 * 
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, 
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, 
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE
 * AUTHOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THE AUTHOR 
 * SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE AUTHOR HAS
 * NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, 
 * OR MODIFICATIONS. 
 */

/*
 * DemoSQLDataStorage.java created on 22.10.2013
 * 
 * (c) alexander noehrer
 */
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
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import at.jku.sea.cloud.mmm.MMMTypeUtils;
import at.jku.sea.cloud.mmm.MMMTypesFactory;

/**
 * @author alexander noehrer
 */
public class DemoSQLDataStorage extends SQLDataStorage implements DataStorage {
  public static final String QUANTIFIED_LINK = "QuantifiedLink";
  public static final String QUANTIFIED_DATA = "QuantifiedData";
  public static final String GTE_LINK = "GTELink";
  public static final String DATA_LINK = "EqualityLink";
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
    // attribute.setPackage(ws, featurePckg);

    /** QuantifiedData **/
    final Package qDataPackage = ws.createPackage();
    qDataPackage.setPackage(ws, linkPackage);
    qDataPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, QUANTIFIED_DATA);

    final Artifact qData = MMMTypesFactory.createComplexType(ws, qDataPackage, QUANTIFIED_DATA,/* is_interface */
        false, /* is_abstract */
        false);
    dataMetaModel.addArtifact(ws, qData);
    MMMTypesFactory.addSuperTypeToComplexType(ws, qData, data);
    final Package qfeaturePckg = ws.createPackage();
    qfeaturePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    qfeaturePckg.setPackage(ws, qDataPackage);

    // final Artifact attribute = MMMTypesFactory.createFeature(ws, "name", data, stringType, false, false, false);

    final Artifact attribute1 = MMMTypesFactory.createFeature(ws, "value", data, realType, false, false, false);
    final Artifact unit = MMMTypesFactory.createFeature(ws, "unit", qData, stringType, false, false, false);

    MMMTypesFactory.addFeatureToComplexType(ws, qData, attribute1);

    attribute1.setPackage(ws, featurePckg);
    unit.setPackage(ws, qfeaturePckg);

    /** Dimension **/
    final Artifact dimension = ws.getArtifact(PROE_DIMENSION);
    dataMetaModel.addArtifact(ws, dimension);
    MMMTypesFactory.addSuperTypeToComplexType(ws, dimension, qData);

    MMMTypesFactory.addFeatureToComplexType(ws, dimension, attribute1);
    MMMTypesFactory.addFeatureToComplexType(ws, dimension, unit);

    /** Parameter **/
    final Artifact parameter = ws.getArtifact(PROE_PARAMETER);
    dataMetaModel.addArtifact(ws, parameter);
    MMMTypesFactory.addSuperTypeToComplexType(ws, parameter, qData);

    MMMTypesFactory.addFeatureToComplexType(ws, parameter, attribute1);
    MMMTypesFactory.addFeatureToComplexType(ws, parameter, unit);

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
    final Package dataLinkTypePackage = ws.createPackage();
    dataLinkTypePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, DATA_LINK);
    dataLinkTypePackage.setPackage(ws, linkPackage);
    final Artifact dataLinkType = MMMTypesFactory.createComplexType(ws, dataLinkTypePackage, DATA_LINK, false, false);

    MMMTypesFactory.addSuperTypeToComplexType(ws, dataLinkType, linkType);

    MMMTypesFactory.addFeatureToComplexType(ws, dataLinkType, sourceFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, dataLinkType, tParameter, data);
    MMMTypesFactory.addFeatureToComplexType(ws, dataLinkType, targetFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, dataLinkType, kParameter, data);

    /** GTELinkType **/
    final Package gteLinkTypePackage = ws.createPackage();
    gteLinkTypePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, GTE_LINK);
    gteLinkTypePackage.setPackage(ws, linkPackage);
    final Artifact gteLinkType = MMMTypesFactory.createComplexType(ws, gteLinkTypePackage, GTE_LINK, false, false);

    MMMTypesFactory.addSuperTypeToComplexType(ws, gteLinkType, linkType);

    MMMTypesFactory.addFeatureToComplexType(ws, gteLinkType, sourceFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, gteLinkType, tParameter, data);
    MMMTypesFactory.addFeatureToComplexType(ws, gteLinkType, targetFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, gteLinkType, kParameter, data);

    /** QuantifiedLinkType **/
    final Package qLinkTypePackage = ws.createPackage();
    qLinkTypePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, QUANTIFIED_LINK);
    qLinkTypePackage.setPackage(ws, linkPackage);
    final Artifact quantifiedLinkType = MMMTypesFactory.createComplexType(ws, qLinkTypePackage, QUANTIFIED_LINK, false, false);

    MMMTypesFactory.addSuperTypeToComplexType(ws, quantifiedLinkType, linkType);

    MMMTypesFactory.addFeatureToComplexType(ws, quantifiedLinkType, sourceFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, quantifiedLinkType, tParameter, qData);
    MMMTypesFactory.addFeatureToComplexType(ws, quantifiedLinkType, targetFeature);
    MMMTypeUtils.bindTypeParameter(cloud, ws, quantifiedLinkType, kParameter, qData);

    // linkMetaModel.addArtifact(ws, linkType);
    linkMetaModel.addArtifact(ws, dataLinkType);
    linkMetaModel.addArtifact(ws, quantifiedLinkType);
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

  // TODO make unit test for these scenarios
  private static void createProblem(final Workspace ws, final Workspace childWorkspace) {
    final Artifact artifact109 = ws.getArtifact(109);
    artifact109.setPackage(ws, null);
    final Artifact artifact108 = ws.getArtifact(108);
    artifact108.setPackage(ws, ws.getPackage(SEA_PACKAGE_ID));
    artifact108.setPropertyValue(ws, "test", "x");
    ws.commitAll("");
    artifact108.setPackage(ws, null);
    artifact108.setPropertyValue(ws, "test", "y");
    ws.commitAll("");
    artifact108.setPackage(ws, ws.getPackage(SEA_PACKAGE_ID));
    artifact108.setPackage(childWorkspace, ws.getPackage(JKU_PACKAGE_ID));
    artifact108.setPropertyValue(ws, "test", "z");
    artifact108.setPropertyValue(childWorkspace, "test", artifact109);

    Collection<Artifact> artifacts = childWorkspace.getArtifacts(ws.getPackage(NO_CONTAINER_ID));
    final Artifact artifact108childWS = childWorkspace.getArtifact(108);
    if (artifacts.contains(artifact108childWS)) {
      System.err.println("FAILURE1!");
      System.exit(-1);
    }
    artifacts = childWorkspace.getArtifactsWithProperty("test", null);
    if (!artifacts.contains(artifact108childWS)) {
      System.err.println("FAILURE2!");
      System.exit(-1);
    }
    artifacts = childWorkspace.getArtifactsWithProperty("test", "z");
    if (artifacts.contains(artifact108childWS)) {
      System.err.println("FAILURE3!");
      System.exit(-1);
    }
    artifacts = childWorkspace.getArtifactsWithProperty("test", artifact109);
    if (!artifacts.contains(artifact108childWS)) {
      System.err.println("FAILURE4!");
      System.exit(-1);
    }
    artifacts = ws.getArtifactsWithProperty("test", "z");
    if (!artifacts.contains(ws.getArtifact(108))) {
      System.err.println("FAILURE5!");
      System.exit(-1);
    }
  }

  private static DataStorage initDemo() {
    final DataStorage storage = new DemoSQLDataStorage();

    if (storage.getHeadVersionNumber() == 1) {
      final Cloud cloud = new DefaultCloud(storage);
      final Owner demo = cloud.getOwner(DEMO);
      final Tool tool = cloud.getTool(EDITOR_TOOL_ID);
      final Workspace ws = cloud.createWorkspace(demo, tool, "testWS");

      // final Tool rsa = cloud.getTool(RSA_TOOL_ID);
      // final Workspace rsaws = cloud.getWorkspace(demo, rsa, "demo-init");
      createLinkMetaModel(cloud, ws);
      // createUMLProblem(rsaws);

      // final Tool eclipse = cloud.getTool(ECLIPSE_TOOL_ID);
      // final Workspace eclipsews = cloud.getWorkspace(demo, eclipse, "demo-init");
      // createInstances(ws);

      final Package pckg = ws.createPackage();
      pckg.setPropertyValue(ws, PROPERTY_NAME, "Links");
      // createACCM(ws);

      // createEclipseProblem(eclipsews);

      ws.commitAll("");
      // rsaws.commitAll();
      // eclipsews.commitAll();
      // ws.close();
      // final Workspace childWorkspace = cloud.getWorkspace(anoehrer, tool, "base-1", ws.getVersionNumber());
      // createProblem(ws, childWorkspace);
    }
    return storage;
  }

  private static void createEclipseProblem(final Workspace ws) {
    final Package pckg = ws.createPackage();
    pckg.setPropertyValue(ws, PROPERTY_NAME, "RobotImplementation");
    final Artifact robotClass = ws.createArtifact();
    robotClass.setPropertyValue(ws, PROPERTY_NAME, "Robot");
    robotClass.setPackage(ws, pckg);

    final Artifact valueSpec = ws.createArtifact();
    valueSpec.setPackage(ws, pckg);
    valueSpec.setPropertyValue(ws, "name", "IntegerValueSpec");
    valueSpec.setPropertyValue(ws, "value", 2.0);

    final Artifact maxDistance = ws.createArtifact();
    maxDistance.setPackage(ws, pckg);
    maxDistance.setPropertyValue(ws, "name", "maxDistance");
    maxDistance.setPropertyValue(ws, "value", valueSpec);
    maxDistance.setPropertyValue(ws, "isStatic", true);
    maxDistance.setPropertyValue(ws, "visibility", "public");

    final Artifact grab = ws.createArtifact();
    grab.setPackage(ws, pckg);
    grab.setPropertyValue(ws, PROPERTY_NAME, "grab");
    grab.setPropertyValue(ws, "isStatic", false);
    grab.setPropertyValue(ws, "visibility", "public");

    final Artifact ifExpr = ws.createArtifact();
    ifExpr.setPackage(ws, pckg);
    ifExpr.setPropertyValue(ws, "name", "ifExpr");

    final Artifact d2 = ws.createArtifact();
    d2.setPackage(ws, pckg);
    d2.setPropertyValue(ws, "name", "d2");
    d2.setPropertyValue(ws, "type", "double");

    final Artifact h2 = ws.createArtifact();
    h2.setPackage(ws, pckg);
    h2.setPropertyValue(ws, "name", "h2");
    h2.setPropertyValue(ws, "type", "double");

    final Artifact c2 = ws.createArtifact();
    c2.setPackage(ws, pckg);
    c2.setPropertyValue(ws, "name", "c2");
    c2.setPropertyValue(ws, "type", "double");

    final Artifact l2 = ws.createArtifact();
    l2.setPackage(ws, pckg);
    l2.setPropertyValue(ws, "name", "l2");
    l2.setPropertyValue(ws, "type", "double");

    final Artifact phi1 = ws.createArtifact();
    phi1.setPackage(ws, pckg);
    phi1.setPropertyValue(ws, "name", "phi1");
    phi1.setPropertyValue(ws, "type", "double");

    final Artifact phi2 = ws.createArtifact();
    phi2.setPackage(ws, pckg);
    phi2.setPropertyValue(ws, "name", "phi2");
    phi2.setPropertyValue(ws, "type", "double");

    final Artifact phi3 = ws.createArtifact();
    phi3.setPackage(ws, pckg);
    phi3.setPropertyValue(ws, "name", "phi3");
    phi3.setPropertyValue(ws, "type", "double");

    final Artifact rotate = ws.createArtifact();
    rotate.setPackage(ws, pckg);
    rotate.setPropertyValue(ws, PROPERTY_NAME, "rotate");
    rotate.setPropertyValue(ws, "isStatic", false);
    rotate.setPropertyValue(ws, "visibility", "public");

    final Artifact mthdCall = ws.createArtifact();
    mthdCall.setPackage(ws, pckg);
    mthdCall.setPropertyValue(ws, PROPERTY_NAME, "rotate");
    mthdCall.setPropertyValue(ws, "parameters", "[phi1, phi2, phi3]");

    final CollectionArtifact grabStatements = ws.createCollection(true);
    grabStatements.setPropertyValue(ws, PROPERTY_NAME, "statements");
    grabStatements.addElement(ws, ifExpr);
    grabStatements.addElement(ws, d2);
    grabStatements.addElement(ws, h2);
    grabStatements.addElement(ws, c2);
    grabStatements.addElement(ws, l2);
    grabStatements.addElement(ws, phi1);
    grabStatements.addElement(ws, phi2);
    grabStatements.addElement(ws, phi3);
    grabStatements.addElement(ws, mthdCall);

    grabStatements.setPackage(ws, pckg);

    final Artifact registerTimeOut = ws.createArtifact();
    registerTimeOut.setPackage(ws, pckg);
    registerTimeOut.setPropertyValue(ws, PROPERTY_NAME, "registerTimeOut");
    registerTimeOut.setPropertyValue(ws, "isStatic", false);
    registerTimeOut.setPropertyValue(ws, "visibility", "public");

    final CollectionArtifact members = ws.createCollection(true);
    members.setPackage(ws, pckg);
    members.setPropertyValue(ws, PROPERTY_NAME, "members");
    members.addElement(ws, maxDistance);
    members.addElement(ws, grab);
    members.addElement(ws, rotate);
    members.addElement(ws, registerTimeOut);
    robotClass.setPropertyValue(ws, "members", members);
  }

  private static void createUMLProblem(final Workspace ws) {
    final Package robotPckg = ws.createPackage();
    robotPckg.setPropertyValue(ws, PROPERTY_NAME, "RobotMM");
    final MetaModel robotMM = ws.createMetaModel();
    robotMM.setPropertyValue(ws, PROPERTY_NAME, "RobotMM");
    robotMM.setPackage(ws, robotPckg);

    /** Technology **/
    final Package technologyPackage = ws.createPackage();
    technologyPackage.setPackage(ws, robotPckg);
    technologyPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Technology");

    final CollectionArtifact literals = ws.createCollection(true);
    final Artifact technology = MMMTypesFactory.createEnumType(ws, "Technology", literals);
    robotMM.addArtifact(ws, technology);
    technology.setPackage(ws, technologyPackage);
    final Package technologyFeaturePckg = ws.createPackage();
    technologyFeaturePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "literals");
    technologyFeaturePckg.setPackage(ws, technologyPackage);
    literals.setPackage(ws, technologyFeaturePckg);

    /** electromechanic **/
    final Artifact electromechanic = MMMTypesFactory.createEnumLiteral(ws, "electromechanic", "electromechanic", technology);
    final Artifact hydraulic = MMMTypesFactory.createEnumLiteral(ws, "hydraulic", "hydraulic", technology);

    electromechanic.setPackage(ws, technologyFeaturePckg);
    hydraulic.setPackage(ws, technologyFeaturePckg);
    literals.addElement(ws, electromechanic);
    literals.addElement(ws, hydraulic);

    /** RobotSegment **/
    final Package RobotSegmentPackage = ws.createPackage();
    RobotSegmentPackage.setPackage(ws, robotPckg);
    RobotSegmentPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "RobotSegment");

    final Artifact robotSegment = MMMTypesFactory.createComplexType(ws, RobotSegmentPackage, /* name */"RobotSegment",/* is_interface */
        false, /* is_abstract */
        true);
    robotMM.addArtifact(ws, robotSegment);

    final Package realTypePckg = ws.createPackage();
    realTypePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Real");
    final Artifact realType = realType(ws, realTypePckg);
    realType.setPackage(ws, realTypePckg);

    realTypePckg.setPackage(ws, robotPckg);

    /** Joint **/
    final Package actuatorPackage = ws.createPackage();
    actuatorPackage.setPackage(ws, robotPckg);
    actuatorPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Joint");

    final Artifact actuator = MMMTypesFactory.createComplexType(ws, actuatorPackage, /* name */"Joint",/* is_interface */
        false, /* is_abstract */
        false);
    robotMM.addArtifact(ws, actuator);
    MMMTypesFactory.addSuperTypeToComplexType(ws, actuator, robotSegment);

    final Package actuatorFeaturePckg = ws.createPackage();
    actuatorFeaturePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    actuatorFeaturePckg.setPackage(ws, actuatorPackage);

    final Artifact techFeature = MMMTypesFactory.createFeature(ws, "technology", actuator, technology, false, false, false);
    techFeature.setPackage(ws, actuatorFeaturePckg);
    final Artifact minPhiFeature = MMMTypesFactory.createFeature(ws, "minPhi", actuator, realType, false, false, false);
    minPhiFeature.setPackage(ws, actuatorFeaturePckg);
    final Artifact maxPhiFeature = MMMTypesFactory.createFeature(ws, "maxPhi", actuator, realType, false, false, false);
    maxPhiFeature.setPackage(ws, actuatorFeaturePckg);

    /** Arm **/
    final Package RobotArmPackage = ws.createPackage();
    RobotArmPackage.setPackage(ws, robotPckg);
    RobotArmPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Arm");

    final Artifact robotArm = MMMTypesFactory.createComplexType(ws, RobotArmPackage, /* name */"Arm",/* is_interface */
        false, /* is_abstract */
        false);
    robotMM.addArtifact(ws, robotArm);
    MMMTypesFactory.addSuperTypeToComplexType(ws, robotArm, robotSegment);

    final Package robotArmFeaturePckg = ws.createPackage();
    robotArmFeaturePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    robotArmFeaturePckg.setPackage(ws, RobotArmPackage);
    final Artifact armlength = MMMTypesFactory.createFeature(ws, "length", actuator, realType, false, false, false);
    armlength.setPackage(ws, robotArmFeaturePckg);

    /** Elbow **/
    final Package ElbowPackage = ws.createPackage();
    ElbowPackage.setPackage(ws, robotPckg);
    ElbowPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Elbow");

    final Artifact elbow = MMMTypesFactory.createComplexType(ws, ElbowPackage, /* name */"Elbow",/* is_interface */
        false, /* is_abstract */
        false);
    robotMM.addArtifact(ws, elbow);
    MMMTypesFactory.addSuperTypeToComplexType(ws, elbow, actuator);

    MMMTypesFactory.addFeatureToComplexType(ws, elbow, techFeature);
    MMMTypesFactory.addFeatureToComplexType(ws, elbow, minPhiFeature);
    MMMTypesFactory.addFeatureToComplexType(ws, elbow, maxPhiFeature);

    final Package elbowFeaturePckg = ws.createPackage();
    elbowFeaturePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    elbowFeaturePckg.setPackage(ws, ElbowPackage);

    final Artifact lowerArmFeature = MMMTypesFactory.createFeature(ws, "lowerArm", elbow, robotArm, false, false, false);
    lowerArmFeature.setPackage(ws, elbowFeaturePckg);
    final Artifact upperArmFeature = MMMTypesFactory.createFeature(ws, "upperArm", elbow, robotArm, false, false, false);
    upperArmFeature.setPackage(ws, elbowFeaturePckg);

    /** Waist **/
    final Package basePackage = ws.createPackage();
    basePackage.setPackage(ws, robotPckg);
    basePackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Waist");

    final Artifact base = MMMTypesFactory.createComplexType(ws, basePackage, /* name */"Waist",/* is_interface */
        false, /* is_abstract */
        false);
    robotMM.addArtifact(ws, base);
    MMMTypesFactory.addSuperTypeToComplexType(ws, base, robotSegment);

    /** GripperSlot ***/
    final Package gripperSlotPackage = ws.createPackage();
    gripperSlotPackage.setPackage(ws, robotPckg);
    gripperSlotPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "GripperSlot");

    final Artifact gripperSlot = MMMTypesFactory.createComplexType(ws, gripperSlotPackage, /* name */"GripperSlot",/* is_interface */
        false, /* is_abstract */
        false);
    robotMM.addArtifact(ws, base);
    MMMTypesFactory.addSuperTypeToComplexType(ws, gripperSlot, robotSegment);

    final Package gripperSlotFeaturePckg = ws.createPackage();
    gripperSlotFeaturePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    gripperSlotFeaturePckg.setPackage(ws, gripperSlotPackage);
    final Artifact gripperSlotLength = MMMTypesFactory.createFeature(ws, "length", gripperSlot, realType, false, false, false);
    gripperSlotLength.setPackage(ws, gripperSlotFeaturePckg);

    /** Wrist **/
    final Package WristPackage = ws.createPackage();
    WristPackage.setPackage(ws, robotPckg);
    WristPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Wrist");

    final Artifact wrist = MMMTypesFactory.createComplexType(ws, WristPackage, /* name */"Wrist",/* is_interface */
        false, /* is_abstract */
        false);
    robotMM.addArtifact(ws, wrist);
    MMMTypesFactory.addSuperTypeToComplexType(ws, wrist, actuator);

    MMMTypesFactory.addFeatureToComplexType(ws, wrist, techFeature);

    MMMTypesFactory.addFeatureToComplexType(ws, wrist, minPhiFeature);
    MMMTypesFactory.addFeatureToComplexType(ws, wrist, maxPhiFeature);

    final Package wristFeaturePckg = ws.createPackage();
    wristFeaturePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    wristFeaturePckg.setPackage(ws, WristPackage);

    final Artifact wristArmFeature = MMMTypesFactory.createFeature(ws, "arm", wrist, robotArm, false, false, false);
    wristArmFeature.setPackage(ws, wristFeaturePckg);
    final Artifact gripperSlotFeature = MMMTypesFactory.createFeature(ws, "gripperSlot", wrist, gripperSlot, false, false, false);
    gripperSlotFeature.setPackage(ws, wristFeaturePckg);

    /** RobotShoulder **/
    final Package RobotShoulderPackage = ws.createPackage();
    RobotShoulderPackage.setPackage(ws, robotPckg);
    RobotShoulderPackage.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "Shoulder");

    final Artifact robotShoulder = MMMTypesFactory.createComplexType(ws, RobotShoulderPackage, /* name */"Shoulder",/* is_interface */
        false, /* is_abstract */
        false);
    robotMM.addArtifact(ws, robotShoulder);
    MMMTypesFactory.addSuperTypeToComplexType(ws, robotShoulder, actuator);

    final Package robotShoulderFeaturePckg = ws.createPackage();
    robotShoulderFeaturePckg.setPropertyValue(ws, DataStorage.PROPERTY_NAME, "features");
    robotShoulderFeaturePckg.setPackage(ws, RobotShoulderPackage);

    MMMTypesFactory.addFeatureToComplexType(ws, robotShoulder, techFeature);

    MMMTypesFactory.addFeatureToComplexType(ws, robotShoulder, minPhiFeature);
    MMMTypesFactory.addFeatureToComplexType(ws, robotShoulder, maxPhiFeature);

    final Artifact waistFeature = MMMTypesFactory.createFeature(ws, "waist", robotShoulder, base, false, false, false);
    waistFeature.setPackage(ws, robotShoulderFeaturePckg);
    final Artifact shoulderArmFeature = MMMTypesFactory.createFeature(ws, "arm", robotShoulder, robotArm, false, false, false);
    shoulderArmFeature.setPackage(ws, robotShoulderFeaturePckg);

    /** Instances **/
    final Package robotInstPckg = ws.createPackage();
    robotInstPckg.setPropertyValue(ws, PROPERTY_NAME, "RobotInstance");

    final Artifact waist = ws.createArtifact(base);
    waist.setPropertyValue(ws, PROPERTY_NAME, "waist");
    waist.setPackage(ws, robotInstPckg);

    final Artifact lowerArm = ws.createArtifact(robotArm);
    lowerArm.setPropertyValue(ws, PROPERTY_NAME, "lowerArm");
    lowerArm.setPackage(ws, robotInstPckg);

    final Artifact upperArm = ws.createArtifact(robotArm);
    upperArm.setPropertyValue(ws, PROPERTY_NAME, "upperArm");
    upperArm.setPackage(ws, robotInstPckg);

    final Artifact gripper = ws.createArtifact(gripperSlot);
    gripper.setPropertyValue(ws, PROPERTY_NAME, "gripperSlot");
    gripper.setPackage(ws, robotInstPckg);

    final Artifact shoulderInstance = ws.createArtifact(robotShoulder);
    shoulderInstance.setPropertyValue(ws, MMMTypeProperties.NAME, "shoulder");
    shoulderInstance.setPackage(ws, robotInstPckg);
    shoulderInstance.setPropertyValue(ws, "technology", "electromechanic");
    shoulderInstance.setPropertyValue(ws, "waist", waist);
    shoulderInstance.setPropertyValue(ws, "arm", lowerArm);
    shoulderInstance.setPropertyValue(ws, "minPhi", 0.2);
    shoulderInstance.setPropertyValue(ws, "maxPhi", 1.4);

    final Artifact lower2UpperArm = ws.createArtifact(elbow);
    lower2UpperArm.setPropertyValue(ws, MMMTypeProperties.NAME, "elbow");
    lower2UpperArm.setPackage(ws, robotInstPckg);
    lower2UpperArm.setPropertyValue(ws, "technology", "electromechanic");
    lower2UpperArm.setPropertyValue(ws, "lowerArm", lowerArm);
    lower2UpperArm.setPropertyValue(ws, "upperArm", upperArm);
    lower2UpperArm.setPropertyValue(ws, "minPhi", 0.4);
    lower2UpperArm.setPropertyValue(ws, "maxPhi", 2.8);

    final Artifact upperArm2Wrist = ws.createArtifact(wrist);
    upperArm2Wrist.setPropertyValue(ws, MMMTypeProperties.NAME, "wrist");
    upperArm2Wrist.setPackage(ws, robotInstPckg);
    upperArm2Wrist.setPropertyValue(ws, "technology", "electromechanic");
    upperArm2Wrist.setPropertyValue(ws, "arm", upperArm);
    upperArm2Wrist.setPropertyValue(ws, "gripperSlot", gripper);
    upperArm2Wrist.setPropertyValue(ws, "minPhi", -1);
    upperArm2Wrist.setPropertyValue(ws, "maxPhi", -1);

  }

  private static void createInstances(final Workspace ws) {
    final Artifact data1 = ws.createArtifact(ws.getArtifact(dataID));
    data1.setPropertyValue(ws, "value", 40.0);

    final Artifact data2 = ws.createArtifact(ws.getArtifact(dataID));
    data2.setPropertyValue(ws, "value", 40.0);

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
