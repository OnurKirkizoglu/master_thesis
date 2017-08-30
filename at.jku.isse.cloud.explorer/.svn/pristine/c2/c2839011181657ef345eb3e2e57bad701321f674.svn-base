/**
 * Created by sebastian on 09.01.16.
 */

var app = angular.module('myApp', [])
    .directive('goDiagram', function() {    // for details, see http://gojs.net/latest/samples/angular.html
        return {
            restrict: 'E',
            template: '',  // just an empty DIV element
            replace: true,
            scope: { model: '=goModel' },
            link: function(scope, element, attrs) {
                var $ = go.GraphObject.make;
                var diagram =  // create a Diagram for the given HTML DIV element
                    $(go.Diagram, element[0],
                        {
                            nodeTemplate: $(go.Node, "Auto",
                                { locationSpot: go.Spot.Center },
                                new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                                $(go.Shape, "RoundedRectangle", new go.Binding("fill", "color"),
                                    {
                                        portId: "", cursor: "pointer",
                                        fromLinkable: true, toLinkable: true,
                                        fromLinkableSelfNode: true, toLinkableSelfNode: true,
                                        fromLinkableDuplicates: true, toLinkableDuplicates: true
                                    }),
                                $(go.TextBlock, { margin: 3, editable: true },
                                    new go.Binding("text", "name").makeTwoWay())
                            ),
                            linkTemplate: $(go.Link,
                                { relinkableFrom: true, relinkableTo: true },
                                $(go.Shape),
                                $(go.Shape, { toArrow: "OpenTriangle" })
                            ),
                            initialContentAlignment: go.Spot.Center,
                            "undoManager.isEnabled": true
                        });

                // whenever a GoJS transaction has finished modifying the model, update all Angular bindings
                function updateAngular(e) {
                    if (e.isTransactionFinished) scope.$apply();
                }

                // notice when the value of "model" changes: update the Diagram.model
                scope.$watch("model", function(newmodel) {
                    var oldmodel = diagram.model;
                    if (oldmodel !== newmodel) {
                        if (oldmodel) oldmodel.removeChangedListener(updateAngular);
                        newmodel.addChangedListener(updateAngular);
                        diagram.model = newmodel;
                    }
                });

                scope.$watch("model.selectedNodeData.name", function(newname) {
                    // disable recursive updates
                    diagram.model.removeChangedListener(updateAngular);
                    // change the name
                    diagram.startTransaction("change name");
                    // the data property has already been modified, so setDataProperty would have no effect
                    var node = diagram.findNodeForData(diagram.model.selectedNodeData);
                    if (node !== null) node.updateTargetBindings("name");
                    diagram.commitTransaction("change name");
                    // re-enable normal updates
                    diagram.model.addChangedListener(updateAngular);
                });

                // update the model when the selection changes
                diagram.addDiagramListener("ChangedSelection", function(e) {
                    var selnode = diagram.selection.first();
                    diagram.model.selectedNodeData = (selnode instanceof go.Node ? selnode.data : null);
                    scope.$apply();
                });
            }
        };
    })
    .factory("$myRESTAPI", ["$q", "$http", function($q, $http) {    // provide a simple REST service
        return {
            getJSON : function(targetURL) {
                var deferred = $q.defer();
                $http({
                    method: "get",
                    url: targetURL
                }).success(function (data) {
                    deferred.resolve(data);
                }).error(function (data, status) {
                    deferred.reject("Problem authenticating");
                });
                return deferred.promise;
            }
        }
    }])
    .controller('myCtrl', function($scope, $http, $myRESTAPI) {     // for Java-Only-programmers: this is the "main method"

        /*
         * BEGIN Business logic
         */
        $scope.searchLevel = 3;     // TODO: define standard max depth for the graph search
        $scope.maxArtifacts = 7;    // TODO: define standard max number of artifacts to be transmitted
        $scope.baseURL = "http://localhost:8080/designspace/";

        // build URL to get all Artifacts from a selected tool
        var URL_ARTIFACTS_FROM_TOOL = function(id) {
            return $scope.baseURL + "tools/id=" + id + "/artifacts";
        };

        // build URL to get Artifact and its Properties / sub-Artifacts
        var URL_ARTIFACT_DEEP = function(id, version, level, maxArtifacts) {
            return $scope.baseURL + "viewer/artifact/id=" + id + "&v=" + version + "&l=" + level + "&a=" + maxArtifacts;
        };

        /*
         * END Business Logic
         */


        // Do not change the following
        $scope.showControl = true;
        $scope.toolSelected = 0;
        $scope.toolValid = true;
        $scope.counter = 0;
        $scope.artifactSelected = "Select artifact..";
        $scope.artifacts = [];
        $scope.searchLevelReached = false;
        $scope.maxArtifactsReached = false;


        // Load all Artifacts in the currently selected Tool
        $scope.onToolChanged = function() {
            if($scope.toolSelected !== "" && 0 < $scope.toolSelected) {
                $myRESTAPI.getJSON(URL_ARTIFACTS_FROM_TOOL($scope.toolSelected)).then(function(result) {
                    console.log("Loaded artifacts from tool no " + $scope.toolSelected);
                    $scope.artifacts = result;
                    $scope.toolValid = true;
                }, function(error) {
                    $scope.artifacts = [];
                    $scope.toolValid = false;
                    console.log("Error: Could not load list of artifacts from tool no " + $scope.toolSelected);
                });
            }
        };

        // Load Artifact and its sub-Artifacts with Properties
        $scope.onArtifactSelectChanged = function() {
            if($scope.artifactSelected !== "") {
                var params = $scope.artifactSelected.split(";");
   
                $myRESTAPI.getJSON(URL_ARTIFACT_DEEP(params[0], params[1], $scope.searchLevel, $scope.maxArtifacts)).then(function(result) {
                    console.log("Successfully loaded artifact");
                    
                    $scope.searchLevelReached = (result.reachedEnd === "true");
                    $scope.maxArtifactsReached = (result.reachedMaxArtifacts === "true");

                    drawArtifacts(result);
                }, function(error) {
                    console.log("Error: artifact could not be loaded");
                });
            }
        };

        // Toggle the boolean flag for showing / hiding the control
        $scope.toggleControl = function() {
            $scope.showControl = !$scope.showControl;
        };

        // init GOJS library
        goInit();

              
});