package carpackage.setup;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Workspace;
import at.jku.sea.cloud.utils.ArtifactUtils;
import init.MMMHelper;

import java.util.*;

/**
 * Created by Philipp Grassauer on 24.02.2017.
 */
class Code {

    private final Workspace ws;
    private final Artifact artifactType;
    private final Artifact intType;
    private final Artifact booleanType;
    private final Artifact voidType;
    private Artifact methodComplexType;
    private Artifact fieldComplexType;
    private Package codeDefinitionPackage;
    private Package codeInstancePackage;
    private Artifact classComplexType;

    Code(MMMHelper helper) {
        ws = helper.getWorkspace();

        artifactType = helper.getArtifactType();
        intType = helper.getIntType();
        booleanType = helper.getBooleanType();
        voidType = helper.getVoidType();
        init();
    }

    private void init() {
        // 1. create necessary packages / package hierarchy
        Package codePackage = ws.createPackage("CodePackage");
        codeDefinitionPackage = ws.createPackage(codePackage, "CodeDefinitions");
        codeInstancePackage = ws.createPackage(codePackage, "CodeInstances");

        // 2. create complex types
        methodComplexType = createMethodComplexType();
        fieldComplexType = createFieldComplexType();
        classComplexType = createClassComplexType();

        // 3. Engine
        List<Artifact> fields = new ArrayList<>();
        List<Artifact> methods = new ArrayList<>();
        // 3.1 fields
        fields.add(createFieldInstance("cylinder", intType));
        fields.add(createFieldInstance("isRunning", booleanType));
        fields.add(createFieldInstance("startStopSystem", booleanType));
        // 3.2 methods
        methods.add(createMethodInstance("fastStart", booleanType, null));
        methods.add(createMethodInstance("fastStop", booleanType, null));
        methods.add(createMethodInstance("getCylinder", intType, null));
        methods.add(createMethodInstance("hasStartStopSystem", booleanType, null));
        // 3.3 class
        createClassInstance("Engine", ArtifactUtils.createCollectionArtifact(ws, fields, true, codePackage), ArtifactUtils.createCollectionArtifact(ws, methods, true, codePackage));

        // 4. Seat
        fields.clear();
        methods.clear();
        // 4.1 fields
        fields.add(createFieldInstance("isMassaging", booleanType));
        fields.add(createFieldInstance("isHeating", booleanType));
        // 4.2 methods
        methods.add(createMethodInstance("setMassaging", voidType, ArtifactUtils.createCollectionArtifact(ws, Collections.singletonList(booleanType), true, codePackage)));
        methods.add(createMethodInstance("Seat", artifactType, ArtifactUtils.createCollectionArtifact(ws, Arrays.asList(booleanType, booleanType), true, codePackage)));
        methods.add(createMethodInstance("isMassaging", booleanType, null));
        methods.add(createMethodInstance("isHeating", booleanType, null));
        methods.add(createMethodInstance("setHeating", voidType, ArtifactUtils.createCollectionArtifact(ws, Collections.singletonList(booleanType), true, codePackage)));
        // 4.3 class
        createClassInstance("Seat", ArtifactUtils.createCollectionArtifact(ws, fields, true, codePackage), ArtifactUtils.createCollectionArtifact(ws, methods, true, codePackage));
    }

    private Artifact createClassComplexType() {
        Map<String, Artifact> features = new HashMap<>();
        features.put("Fields", artifactType);
        features.put("Methods", artifactType);
        return MMMHelper.createComplexType(ws, codeDefinitionPackage, "CodeClass", features);
    }

    private Artifact createClassInstance(String name, CollectionArtifact fields, CollectionArtifact methods) {
        Map<String, Object> instanceValues = new HashMap<>();

        instanceValues.put("Fields", fields);
        instanceValues.put("Methods", methods);
        return MMMHelper.createComplexTypeInstance(ws, codeInstancePackage, name, classComplexType, instanceValues);
    }

    private Artifact createMethodComplexType() {
        Map<String, Artifact> features = new HashMap<>();
        features.put("Return Type", artifactType);
        features.put("Parameters", artifactType);
        return MMMHelper.createComplexType(ws, codeDefinitionPackage, "CodeMethod", features);
    }

    private Artifact createMethodInstance(String name, Artifact returnType, CollectionArtifact params) {
        Map<String, Object> instanceValues = new HashMap<>();

        instanceValues.put("Return Type", returnType);
        instanceValues.put("Parameters", params);
        return MMMHelper.createComplexTypeInstance(ws, codeInstancePackage, name, methodComplexType, instanceValues);
    }

    private Artifact createFieldComplexType() {
        Map<String, Artifact> features = new HashMap<>();

        features.put("Type", artifactType);
        return MMMHelper.createComplexType(ws, codeDefinitionPackage, "CodeField", features);
    }

    private Artifact createFieldInstance(String name, Artifact type) {
        Map<String, Object> instanceValues = new HashMap<>();

        instanceValues.put("Type", type);
        return MMMHelper.createComplexTypeInstance(ws, codeInstancePackage, name, fieldComplexType, instanceValues);
    }
}
