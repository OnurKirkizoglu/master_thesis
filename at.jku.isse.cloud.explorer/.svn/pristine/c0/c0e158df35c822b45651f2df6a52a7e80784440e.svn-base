package application;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.Tool;
import at.jku.sea.cloud.Workspace;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController implements Initializable {

	@FXML
	WebView webview;
	
	@FXML
	CheckBox showArtifactNames;

	@FXML
	Text artifactDescription;

	@FXML
	TextField searchField;

	@FXML
	private TreeView<ArtifactTreeItem> projectExplorer;

	@FXML
	private TableView<ArtifactInspectorEntry> artifactInspector;

	@FXML
	private TableColumn<ArtifactInspectorEntry, String> propertyNameColumn;

	@FXML
	private TableColumn<ArtifactInspectorEntry, String> propertyValueColumn;

	private TreeItem<ArtifactTreeItem> rootItem;
	private TreeItem<ArtifactTreeItem> uncategorizedPackages;
	private TreeItem<ArtifactTreeItem> uncategorizedArtifacts;
	private TreeItem<ArtifactTreeItem> rootCopy;
	private Cloud cloud;

	private ArrayList<Long> categorizedArtifactIDs;
	private MainApp mainApp;
	
	public MainViewController() {
		categorizedArtifactIDs = new ArrayList<Long>();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		propertyNameColumn.setCellValueFactory(new PropertyValueFactory<>("propertyName"));
		propertyValueColumn.setCellValueFactory(new PropertyValueFactory<>("propertyValue"));
		MenuItem visualizeMenu = new MenuItem("Visualize");
		MenuItem addPackage = new MenuItem("Create Package");
		
		//Context menu event handler
		EventHandler<ActionEvent> e = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(event.getSource().equals(visualizeMenu)){
					if (projectExplorer.getSelectionModel().getSelectedItem() != null) {
						ArtifactTreeItem selectedArtifact = ((TreeItem<ArtifactTreeItem>) (projectExplorer.getSelectionModel()
								.getSelectedItem())).getValue();
						if (selectedArtifact.artifact != null) {
							String id = "" + selectedArtifact.artifact.getId();
							String version = "" + selectedArtifact.artifact.getVersionNumber();
							webview.getEngine().executeScript("jfxTest("+id+","+version+")");
							System.out.println("Visualizing Artifact");													
						}
					}
				}
				else if(event.getSource().equals(addPackage))
				{
					System.out.println("Creating Package");
					createPackage(projectExplorer.getSelectionModel().getSelectedItem().getValue());
				}
			}

		};
		
		//Adding the event handler to the context menu items
		addPackage.setOnAction(e);
		visualizeMenu.setOnAction(e);
		
		//Creating the context menu, adding its items
		ContextMenu ctxMenu = new ContextMenu();
		ctxMenu.getItems().add(visualizeMenu);
		ctxMenu.getItems().add(addPackage);
		projectExplorer.setContextMenu(ctxMenu);
		initializeWebView();
	}

	private void initializeWebView() {
		String viewerDir = System.getProperty("user.dir")+"/src/Viewer-App/index.html";
		viewerDir = ("file:///"+viewerDir.substring(3));
		WebEngine webEngine = webview.getEngine();
		webEngine.load(viewerDir);
	}

	public void setMainApp(MainApp mA) {
		mainApp = mA;
	}

	public void setCloud(Cloud c) {
		cloud = c;
	}

	@FXML
	public void refreshTreeView() {
		System.out.println("Refreshing TreeView!");
		initMainView();
	}

	@FXML
	public void showSettings() {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setContentText("This feature isn't yet implemented");
		a.show();
	}

	@FXML
	public void filterTreeView() {
		System.out.println("Filtering TreeView!");
		String searchString = searchField.getText();

		rootCopy.getChildren().clear();
		copyTreeView(rootItem, rootCopy);
//		sortTreeView();
		projectExplorer.setRoot(rootCopy);
		deepExpand(rootCopy);
		
		
		
		
		if (!searchString.equals("")) {
			findPredicate(rootCopy, searchString);
		}
	}

	@FXML
	public void toggleArtifactNames() {
		boolean showNames = showArtifactNames.isSelected();
		projectExplorer.setRoot(null);
		renameChildren(rootCopy, showNames);
		projectExplorer.setRoot(rootCopy);
	}

	@FXML
	public void onTreeViewSelection() {
		if (projectExplorer.getSelectionModel().getSelectedItem() != null) {
			ArtifactTreeItem selectedArtifact = ((TreeItem<ArtifactTreeItem>) (projectExplorer.getSelectionModel()
					.getSelectedItem())).getValue();

			if (selectedArtifact.artifact != null) {
				artifactDescription.setText("Identifier: \t" + selectedArtifact.artifact.toString() + "\n"
						+ "Type: \t\t" + selectedArtifact.artifact.getType() + "\n" + "Package:  \t"
						+ selectedArtifact.artifact.getPackage()

				);
				for (ArtifactInspectorEntry e : selectedArtifact.getProperties()) {

					System.out.println(e.getPropertyName() + ": " + e.getPropertyValue());
				}
				artifactInspector.getItems().clear();
				artifactInspector.getItems().setAll(selectedArtifact.getProperties());

			} else {
				artifactDescription.setText("Artifact Description");
				artifactInspector.getItems().clear();
			}
		}
	}

	public void renameChildren(TreeItem<ArtifactTreeItem> parent, boolean showNames) {
		parent.getValue().toggleDisplayName(showNames);

		for (TreeItem<ArtifactTreeItem> t : parent.getChildren()) {
			renameChildren(t, showNames);
		}
	}

	
	
	void initMainView()
	{
		boolean showNames = showArtifactNames.isSelected();
		TreeViewFactory factory = new TreeViewFactory();
		rootItem = factory.getTreeView(cloud, showNames).getRoot();
		
		ArtifactTreeItem rootArtCopy = new ArtifactTreeItem(null, "Design Space");
		rootCopy = new TreeItem<ArtifactTreeItem>(rootArtCopy);
		copyTreeView(rootItem, rootCopy);
		projectExplorer.setRoot(rootCopy);
		deepExpand(rootCopy);
	}
	
	@Deprecated
	public void initMainView1() {
		boolean showNames = showArtifactNames.isSelected();
		Collection<Artifact> allArtifacts = cloud.getArtifacts(cloud.getHeadVersionNumber());

		ArtifactTreeItem rootArtItem = new ArtifactTreeItem(null, "Design Space");
		rootItem = new TreeItem<ArtifactTreeItem>(rootArtItem);
		rootItem.setExpanded(true);

		for (Artifact a : allArtifacts) {
			ArtifactTreeItem artTreeItem = new ArtifactTreeItem(a, showNames);
			TreeItem<ArtifactTreeItem> t = new TreeItem<ArtifactTreeItem>(artTreeItem);
			rootItem.getChildren().add(t);
		}
		
		ArtifactTreeItem rootArtCopy = new ArtifactTreeItem(null, "Design Space");
		rootCopy = new TreeItem<ArtifactTreeItem>(rootArtCopy);
		copyTreeView(rootItem, rootCopy);
		sortTreeView();
		projectExplorer.setRoot(rootCopy);
		deepExpand(rootCopy);
	}

	@Deprecated
	private void sortTreeView() {
		ArrayList<TreeItem<ArtifactTreeItem>> allTreeItems = getAllTreeItems();
		
		
		for(TreeItem<ArtifactTreeItem> t : allTreeItems)
		{
			long artifactPackageID = t.getValue().parentID;
			if(artifactPackageID != -1)
			{
				if(t.getValue().artifact.getId() != artifactPackageID)
				{
					TreeItem<ArtifactTreeItem> tParent = findParent(artifactPackageID, allTreeItems);
					if(tParent != null){
						linkToParent(t, tParent);						
					}else{
						System.out.println("WARNING: Package with ID "+ artifactPackageID + "not found! Linking "+t+" to root TreeItem!");
						rootCopy.getChildren().add(t);
					}
					
				}
			}else{
				rootCopy.getChildren().add(t);
			}
		}
	}
	
	@Deprecated
	private TreeItem<ArtifactTreeItem> findParent(long artifactPackageID, ArrayList<TreeItem<ArtifactTreeItem>> allTreeItems) {
		for(TreeItem<ArtifactTreeItem> t : allTreeItems)
		{
			if(t.getValue().artifact.getId() == artifactPackageID)
			{
				return t;
			}
		}
		return null;
	}

	@Deprecated
	private void linkToParent(TreeItem<ArtifactTreeItem> child, TreeItem<ArtifactTreeItem> parent)
	{
		
		rootCopy.getChildren().remove(child);

		parent.getChildren().add(child);
		
	}
	
	@Deprecated
	private ArrayList<TreeItem<ArtifactTreeItem>> getAllTreeItems() {
		Object[] treeItemArray = rootCopy.getChildren().toArray();
		rootCopy.getChildren().clear();
		ArrayList<TreeItem<ArtifactTreeItem>> allTreeItems = new ArrayList<TreeItem<ArtifactTreeItem>>();
		
		for(Object o : treeItemArray)
		{
			@SuppressWarnings("unchecked")
			TreeItem<ArtifactTreeItem> artTreeItem = (TreeItem<ArtifactTreeItem>)o;
			allTreeItems.add(artTreeItem);
		}
		
		return allTreeItems;
	}

	

	public void deepExpand(TreeItem<ArtifactTreeItem> parent) {
		parent.setExpanded(true);
		for (TreeItem<ArtifactTreeItem> t : parent.getChildren()) {
			deepExpand(t);
		}
	}

	public void copyTreeView(TreeItem<ArtifactTreeItem> parent, TreeItem<ArtifactTreeItem> parentCopy) {
		for (TreeItem<ArtifactTreeItem> t : parent.getChildren()) {

			TreeItem<ArtifactTreeItem> tCopy = new TreeItem<ArtifactTreeItem>(t.getValue());

			if (tCopy.getValue().artifact instanceof Project) {
				tCopy.setGraphic(
						new ImageView(new Image(getClass().getResourceAsStream("blue-folder-horizontal-icon.png"))));
			} else if (tCopy.getValue().artifact instanceof Package) {
				tCopy.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("package-icon.png"))));
			} else {
				if (tCopy.getValue().artifact instanceof CollectionArtifact) {
					tCopy.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("bricks-icon.png"))));
				} else {
					tCopy.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("brick-icon.png"))));
				}
			}

			if (tCopy.getValue().artifact == null && tCopy != rootCopy) {
				tCopy.setGraphic(
						new ImageView(new Image(getClass().getResourceAsStream("blue-folder-horizontal-icon.png"))));
			}
			parentCopy.getChildren().add(tCopy);
			copyTreeView(t, tCopy);

		}
	}

	public boolean findPredicate(TreeItem<ArtifactTreeItem> t, String predicate) {
		boolean found = false;
		ArrayList<TreeItem<ArtifactTreeItem>> markedForRemoval = new ArrayList<TreeItem<ArtifactTreeItem>>();
		if (t.getValue().predicateList.contains(predicate)) {
			found = true;
		}

		for (TreeItem<ArtifactTreeItem> child : t.getChildren()) {
			if (findPredicate(child, predicate)) {
				found = true;
			} else {
				markedForRemoval.add(child);
			}
		}
		for (TreeItem<ArtifactTreeItem> r : markedForRemoval) {
			t.getChildren().remove(r);
		}
		return found;
	}
	
	private void createPackage(ArtifactTreeItem artifactTreeItem)
	{
		Artifact parentPackage = artifactTreeItem.artifact;
		
		if(!(parentPackage instanceof Project || parentPackage instanceof Package))
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot Create Package");
			alert.setContentText("Package cannot created under an Artifact node!! Please select a Project or a Package!");
			alert.setHeaderText(null);
			alert.showAndWait();
		}
		else
		{
			Long parentId = parentPackage.getId();
			Long versionNum = parentPackage.getVersionNumber();
			//Here, we need the workspace?? possibly.
			Workspace ws = cloud.getWorkspace(-1L);
			//FIXME: somehow the workspace -1 must be replace with actual variable.
			
			
			try{
				//Create new window here.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("InsertPackageView.fxml"));

				BorderPane newPane = loader.load();
				InsertPackageController controller = loader.getController();
				controller.setMainApp(mainApp);
				
				Scene insertScene = new Scene(newPane);


				Stage insertStage = new Stage();
				insertStage.setScene(insertScene);
				
				insertStage.initModality(Modality.APPLICATION_MODAL);
				insertStage.showAndWait();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			
			//createPackage or Create artifact.
			//Create Properties
			
			Package newPackage = ws.createPackage();
			
			System.out.println(mainApp.getPackageName());
			
			newPackage.createProperty(ws, "name");
			newPackage.setPropertyValue(ws, "name", mainApp.getPackageName()); //The "something" will be taken from the create window that I am going to create.
			newPackage.setPackage(ws, (Package) parentPackage);
			((Package)parentPackage).addArtifact(ws, newPackage);
			
			ws.commitAll("");
			
			System.out.println("Created");
			
		}
	}
}
