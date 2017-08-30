package carpackage.setup;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.utils.ArtifactUtils;
import init.Constants;
import init.MMMHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MetaModel {

	private Workspace ws;
	private Artifact intType;
	private Artifact booleanType;
	private Artifact stringType;
	private Artifact artifactType;
	private Artifact voidType;
	
	private Artifact umlClassComplexType;
	private Artifact umlAttributeComplexType;
	private Artifact umlOperationComplexType;
	private Artifact umlRelationComplexType;
	private Package metaModelDefinitionPackage;
	private Package metaModelInstancePackage;

	MetaModel(MMMHelper helper) {
		ws = helper.getWorkspace();

		intType = helper.getIntType();
		booleanType = helper.getBooleanType();
		stringType = helper.getStringType();
		artifactType = helper.getArtifactType();
		voidType = helper.getVoidType();

		init();
	}

	private void init() {
		// 1. create necessary packages / package hierarchy
		Package metaModelPackage = ws.createPackage("MetaModelPackage");
		metaModelDefinitionPackage = ws.createPackage(metaModelPackage, "MetaModelDefinitions");
		metaModelInstancePackage = ws.createPackage(metaModelPackage, "MetaModelInstances");

		// uml complex types
		umlClassComplexType = createUmlClassComplexType();
		umlAttributeComplexType = createUmlAttributeComplexType();
		umlOperationComplexType = createUmlOperationComplexType();
		umlRelationComplexType = createUmlRelationComplexType();

		// create different instances of uml complex types
		// see example on dropbox example2.docx
		// operations
		Artifact fastStart = createInstanceUmlOperation("fastStart", voidType, null);
		Artifact fastStop = createInstanceUmlOperation("fastStop", voidType, null);
		Artifact isFull = createInstanceUmlOperation("isFull", booleanType, null);
		Artifact isEmpty = createInstanceUmlOperation("isEmpty", booleanType, null);

		// attributes
		Artifact cylinder = createInstanceUmlAttributes("cylinder", intType);
		Artifact isRunning = createInstanceUmlAttributes("isRunning", booleanType);
		Artifact capacity = createInstanceUmlAttributes("capacity", intType);
		Artifact fillLevel = createInstanceUmlAttributes("fillLevel", intType);

		// classes
		Artifact tank = createInstanceUmlClass("Tank",
				ArtifactUtils.createCollectionArtifact(ws, Arrays.asList(capacity, fillLevel), true, metaModelInstancePackage),
				ArtifactUtils.createCollectionArtifact(ws, Arrays.asList(isFull, isEmpty), true, metaModelInstancePackage));
		Artifact car = createInstanceUmlClass("Car",
				ArtifactUtils.createCollectionArtifact(ws, Arrays.asList(cylinder, isRunning), true, metaModelInstancePackage),
				ArtifactUtils.createCollectionArtifact(ws, Arrays.asList(fastStart, fastStop), true, metaModelInstancePackage));
		
		// relations
		createInstanceUmlRelation("Komposition", car, tank, "1", "1");

	}

	private Artifact createInstanceUmlRelation(String name, Artifact src, Artifact tar, String srcCardinality,
			String tarCardinality) {
		Map<String, Object> instanceValues = new HashMap<>();

		instanceValues.put(Constants.METAMODEL_LINK_SOURCE, src);
		instanceValues.put(Constants.METAMODEL_LINK_TARGET, tar);
		instanceValues.put(Constants.METAMODEL_LINK_SOURCE_CARDINALITY, srcCardinality);
		instanceValues.put(Constants.METAMODEL_LINK_TARGET_CARDINALITY, tarCardinality);
		return MMMHelper.createComplexTypeInstance(ws, metaModelInstancePackage, name, umlRelationComplexType, instanceValues);
	}

	private Artifact createInstanceUmlClass(String name, CollectionArtifact attributes, CollectionArtifact operations) {
		Map<String, Object> instanceValues = new HashMap<>();

		instanceValues.put("Attributes", attributes);
		instanceValues.put("Operations", operations);
		return MMMHelper.createComplexTypeInstance(ws, metaModelInstancePackage, name, umlClassComplexType, instanceValues);
	}

	private Artifact createInstanceUmlAttributes(String name, Artifact type) {
		Map<String, Object> instanceValues = new HashMap<>();

		instanceValues.put("Type", type);
		return MMMHelper.createComplexTypeInstance(ws, metaModelInstancePackage, name, umlAttributeComplexType, instanceValues);

	}

	private Artifact createInstanceUmlOperation(String name, Artifact returnType, CollectionArtifact parameterTypes) {
		Map<String, Object> instanceValues = new HashMap<>();

		instanceValues.put("Return Type", returnType);
		instanceValues.put("Parameter Type", parameterTypes);
		return MMMHelper.createComplexTypeInstance(ws, metaModelInstancePackage, name, umlOperationComplexType, instanceValues);

	}

	private Artifact createUmlRelationComplexType() {
		Map<String, Artifact> features = new HashMap<>();
		features.put(Constants.METAMODEL_LINK_SOURCE, artifactType);
		features.put(Constants.METAMODEL_LINK_TARGET, artifactType);
		features.put(Constants.METAMODEL_LINK_SOURCE_CARDINALITY, stringType);
		features.put(Constants.METAMODEL_LINK_TARGET_CARDINALITY, stringType);
		return MMMHelper.createComplexType(ws, metaModelDefinitionPackage, "UmlRelation", features);
	}

	private Artifact createUmlOperationComplexType() {
		Map<String, Artifact> features = new HashMap<>();
		features.put("Return Type", artifactType);
		features.put("Parameter Type", artifactType); // CollectionArtifact?
		return MMMHelper.createComplexType(ws, metaModelDefinitionPackage, "UmlOperation", features);
	}

	private Artifact createUmlAttributeComplexType() {
		Map<String, Artifact> features = new HashMap<>();
		features.put("Type", artifactType);
		return MMMHelper.createComplexType(ws, metaModelDefinitionPackage, "UmlAttribute", features);
	}

	private Artifact createUmlClassComplexType() {
		Map<String, Artifact> features = new HashMap<>();
		features.put("Attributes", artifactType);
		features.put("Operations", artifactType);
		return MMMHelper.createComplexType(ws, metaModelDefinitionPackage, "UmlClass", features);
	}
}
