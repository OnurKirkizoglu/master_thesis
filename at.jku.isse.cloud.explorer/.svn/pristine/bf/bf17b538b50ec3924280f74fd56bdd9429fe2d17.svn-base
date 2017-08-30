/**
 * Created by sebastian on 09.01.16.
 */

/**
 * Global variables
 */
var GO = go.GraphObject.make;  // for conciseness in defining templates
var myDiagram;

/**
 * @type {{bluegrad, greengrad, redgrad, yellowgrad, lightgrad}}
 */
var goColours = {
    // define several shared Brushes
    bluegrad : GO(go.Brush, "Linear", { 0: "rgb(150, 150, 250)", 0.5: "rgb(86, 86, 186)", 1: "rgb(86, 86, 186)" }),
    greengrad : GO(go.Brush, "Linear", { 0: "rgb(158, 209, 159)", 1: "rgb(67, 101, 56)" }),
    redgrad : GO(go.Brush, "Linear", { 0: "rgb(206, 106, 100)", 1: "rgb(180, 56, 50)" }),
    yellowgrad : GO(go.Brush, "Linear", { 0: "rgb(254, 221, 50)", 1: "rgb(254, 182, 50)" }),
    lightgrad : GO(go.Brush, "Linear", { 1: "#E6E6FA", 0: "#FFFAF0" })
    // TODO: register further colours
};

/**
 * Init function provided required from and provided by the GOJS library
 * for more information, see the GOJS doc http://gojs.net/latest/intro/index.html
 */
var goInit = function () {
    myDiagram =
        GO(go.Diagram, "goDiagram",  // must name or refer to the DIV HTML element
            {
                initialContentAlignment: go.Spot.Center,
                allowDelete: false,
                allowCopy: false,
                layout: GO(go.ForceDirectedLayout),
                "undoManager.isEnabled": true
            });

    // the template for each attribute in a node's array of item data
    var itemTempl =
        GO(go.Panel, "Horizontal",
            GO(go.Shape,
                { desiredSize: new go.Size(10, 10) },
                new go.Binding("figure", "figure"),
                new go.Binding("fill", "color")),
            GO(go.TextBlock,
                { stroke: "#333333",
                    font: "bold 14px sans-serif" },
                new go.Binding("text", "name"))
        );

    // define the Node template, representing an entity
    myDiagram.nodeTemplate =
        GO(go.Node, "Auto",  // the whole node panel
            { selectionAdorned: true,
                resizable: true,
                layoutConditions: go.Part.LayoutStandard & ~go.Part.LayoutNodeSized,
                fromSpot: go.Spot.AllSides,
                toSpot: go.Spot.AllSides,
                isShadowed: true,
                shadowColor: "#C5C1AA" },
            new go.Binding("location", "location").makeTwoWay(),
            // define the node's outer shape, which will surround the Table
            GO(go.Shape, "Rectangle",
                { fill: goColours.lightgrad, stroke: "#756875", strokeWidth: 3 }),
            GO(go.Panel, "Table",
                { margin: 8, stretch: go.GraphObject.Fill },
                GO(go.RowColumnDefinition, { row: 0, sizing: go.RowColumnDefinition.None }),
                // the table header
                GO(go.TextBlock,
                    {
                        row: 0, alignment: go.Spot.Center,
                        margin: new go.Margin(0, 14, 0, 2),  // leave room for Button
                        font: "bold 16px sans-serif"
                    },
                    new go.Binding("text", "key")),
                // the collapse/expand button
                GO("PanelExpanderButton", "LIST",  // the name of the element whose visibility this button toggles
                    { row: 0, alignment: go.Spot.TopRight }),
                // the list of Panels, each showing an attribute
                GO(go.Panel, "Vertical",
                    {
                        name: "LIST",
                        row: 1,
                        padding: 3,
                        alignment: go.Spot.TopLeft,
                        defaultAlignment: go.Spot.Left,
                        stretch: go.GraphObject.Horizontal,
                        itemTemplate: itemTempl
                    },
                    new go.Binding("itemArray", "items"))
            )  // end Table Panel
        );  // end Node

    // define the Link template, representing a relationship
    myDiagram.linkTemplate =
        GO(go.Link,  // the whole link panel
            {
                selectionAdorned: true,
                layerName: "Foreground",
                reshapable: true,
                routing: go.Link.AvoidsNodes,
                corner: 5,
                curve: go.Link.JumpOver
            },
            GO(go.Shape,  // the link shape
                { stroke: "#303B45", strokeWidth: 2.5 }),
            GO(go.TextBlock,  // the "from" label
                {
                    textAlign: "center",
                    font: "bold 14px sans-serif",
                    stroke: "#1967B3",
                    segmentIndex: 0,
                    segmentOffset: new go.Point(NaN, NaN),
                    segmentOrientation: go.Link.OrientUpright
                },
                new go.Binding("text", "text")),
            GO(go.TextBlock,  // the "to" label
                {
                    textAlign: "center",
                    font: "bold 14px sans-serif",
                    stroke: "#1967B3",
                    segmentIndex: -1,
                    segmentOffset: new go.Point(NaN, NaN),
                    segmentOrientation: go.Link.OrientUpright
                },
                new go.Binding("text", "toText"))
        );
};

/**
 * Helper function for consistent naming of artifacts
 * Note: This function can be modified BUT the names of the artifacts have to be absolutely unique
 * as they are used within the links for graphical joining.
 *
 * @param id
 * @param version
 * @returns {string}
 */
var createArtifactName = function(id, version) {
    return id + "_v:" + version;
};


/**
 * Draw received artifacts and their links / update current graphic respectively
 *
 * @param jsonResponse
 */
var drawArtifacts = function(jsonResponse) {
    var rawNodes = jsonResponse.artifacts;
    //window.alert(JSON.stringify(jsonResponse));
    var nodeArray = [];
    //document.getElementById("TestOutput").innerHTML = JSON.stringify(jsonResponse);
    
    
    // transform raw Artifacts into suitable Nodes to be displayed
    rawNodes.forEach(function(element) {
        var nodeProps = [];
        element.properties.forEach(function(elemprop) {
            nodeProps.push(
                {
                    name: (elemprop.name + " : " + elemprop.value),
                    iskey: false,
                    figure: (elemprop.name === "age") ? "TriangleUp" : "Decision",
                    // [Cube1, Decision, MagneticData, TriangleUp] find more on http://gojs.net

                    color: (elemprop.info === "blue") ? goColours.bluegrad : goColours.yellowgrad
                    // TODO: change figure / colour based on 'elemprop.info'
                }
            )
        });
        // sort the Properties alphabetically
        nodeProps.sort(function(a, b) { return (a.name > b.name) ? 1 : -1; });

        // check whether the Artifact in the given version already exists in the list
        var curName = createArtifactName(element.id, element.version);
        var alreadyIn = false;
        nodeArray.forEach(function(node) {
           if(node.name === curName)
               alreadyIn = true;
        });

        if(!alreadyIn) {
            nodeArray.push(
                {
                    key: curName,
                    items: nodeProps
                }
            );
        }
    });


    var rawLinks = jsonResponse.links;
    var linkArray = [];

    // transform raw Links into suitable Links to be displayed
    rawLinks.forEach(function(element) {
       linkArray.push(
           {
               from: createArtifactName(element.fromID, element.fromVersion),
               to: createArtifactName(element.toID, element.toVersion),
               text: (element.fromInfo !== "") ? element.fromInfo : "1",
               toText: "*"
               // TODO: change 'text' and 'toText' based on 'element.fromInfo' and 'element.toInfo'
           }
       );
    });


    myDiagram.model = new go.GraphLinksModel(nodeArray, linkArray);
};