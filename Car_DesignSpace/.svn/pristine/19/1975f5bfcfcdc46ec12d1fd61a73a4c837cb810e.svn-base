package carpackage.setup;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Workspace;
import init.MMMHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Philipp Grassauer on 24.02.2017.
 */
public class Requirement {

    private final Workspace ws;
    private final Artifact intType;
    private final Artifact stringType;
    private final Artifact artifactType;
    private Package requirementPackage;

    public Requirement(MMMHelper helper) {
        ws = helper.getWorkspace();
        intType = helper.getIntType();
        stringType = helper.getStringType();
        artifactType = helper.getArtifactType();
        
        init();
    }

    private void init() {
        // 1. create necessary packages / package hierarchy
        requirementPackage = ws.createPackage("RequirementPackage");


        Artifact reqComplexType = createRequirementComplexType();

        createRequirementInstance(reqComplexType, 1, "Functional", "Implementation of a Stop-Start-System. ", "Engine");
        createRequirementInstance(reqComplexType, 2, "Constraint", "Number of seats in car: 2 <= x <= 7.", "Seat");
        createRequirementInstance(reqComplexType, 3, "Functional", "Massage-& Heating function.", "Seat-Functionality");
        createRequirementInstance(reqComplexType, 4, "Constraint", "Tank capacity must be x > 50 l", "Tank");
        createRequirementInstance(reqComplexType, 5, "Constraint", "Car weight is max. 2000.", "Weight");
        createRequirementInstance(reqComplexType, 6, "Functional", "Possible to set up car with navigation system", "Navigation System");
        createRequirementInstance(reqComplexType, 7, "Functional", "Automatic detection of possible free lot + automatic parking.", "Parking System");
        createRequirementInstance(reqComplexType, 8, "Functional", "Requires: REQUIREMENT NUMBER 7 and REQUIREMENT NUMBER 6, auto-pilot via Navigation System", "Automatic self-driving");

        createFullRequirementInstances(reqComplexType);
    }

    private Artifact createRequirementComplexType() {
        Map<String, Artifact> features = new HashMap<>();

        features.put("Requirement Number", intType);
        features.put("Product Use Case Number", intType);
        features.put("Requirement Type", stringType);
        features.put("Description", stringType);
        features.put("Rationale", stringType);
        features.put("Fit Criterion", stringType);
        features.put("Other Related PUCs", stringType);
        features.put("Originator", artifactType);
        features.put("Customer Satisfaction", intType);
        features.put("Customer Dissatisfaction", intType);
        features.put("Priority", intType);
        features.put("Conflicts", artifactType);
        features.put("Supporting Materials", stringType);
        features.put("Other Interested Stakeholders", artifactType);
        features.put("History", stringType);
        features.put("Related BUC", stringType);

        Artifact complexType = MMMHelper.createComplexType(ws, requirementPackage, "RequirementType", features);

        return complexType;
    }

    private Artifact createRequirementInstance(Artifact complexType, int reqNumber, String reqType, String reqDescription, String reqHistory){
        Map<String, Object> instanceValues = new HashMap<>();
        instanceValues.put("Requirement Number", reqNumber);
        instanceValues.put("Requirement Type", reqType);
        instanceValues.put("Description", reqDescription);
        instanceValues.put("History", reqHistory);

        return MMMHelper.createComplexTypeInstance(ws, requirementPackage, reqHistory, complexType, instanceValues);
    }

    private void createFullRequirementInstances(Artifact complexType) {
        Map<String, Object> instanceValues = new HashMap<>();

        //FIRST
        instanceValues.put("Requirement Number", 10);
        instanceValues.put("Product Use Case Number", 521);
        instanceValues.put("Requirement Type", "Constraint");
        instanceValues.put("Description",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        instanceValues.put("Rationale",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        instanceValues.put("Fit Criterion",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        instanceValues.put("Other Related PUCs",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        instanceValues.put("Originator", null);
        instanceValues.put("Customer Satisfaction", 8);
        instanceValues.put("Customer Dissatisfaction", 1);
        instanceValues.put("Priority", 5);
        instanceValues.put("Conflicts", null);
        instanceValues.put("Supporting Materials",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        instanceValues.put("Other Interested Stakeholders", null);
        instanceValues.put("History",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
        instanceValues.put("Related BUC",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");

        MMMHelper.createComplexTypeInstance(ws, requirementPackage, "FullRequirement1", complexType, instanceValues);

        //SECOND
        instanceValues.put("Requirement Number", 11);
        instanceValues.put("Product Use Case Number", 385);
        instanceValues.put("Requirement Type", "Functional");
        instanceValues.put("Originator", null);
        instanceValues.put("Conflicts", null);
        instanceValues.put("Other Interested Stakeholders", null);

        MMMHelper.createComplexTypeInstance(ws, requirementPackage, "FullRequirement2", complexType, instanceValues);

        //THIRD
        instanceValues.put("Requirement Number", 12);
        instanceValues.put("Product Use Case Number", 935);
        MMMHelper.createComplexTypeInstance(ws, requirementPackage, "FullRequirement3", complexType, instanceValues);
    }
}


