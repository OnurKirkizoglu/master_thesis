package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import application.setup.DefineLinkDialog;
import application.setup.GUIHelper;
import application.setup.ListDialog;
import application.setup.LoginDialog;
import application.setup.ModelListener;
import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.User;
import at.jku.sea.cloud.exceptions.CredentialsException;
import at.jku.sea.cloud.rest.client.RestCloud;
import graph.Control;
import graph.GraphView;
import init.Constants;
import init.MMMHelper;
import init.setup.Link;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import matrix.MatrixView;

/**
 * 
 * @author Onur Kirkizoglu
 * 
 * Main Class. Start of Application
 */
public class LinkApplication extends Application implements ModelListener {
	private MMMDataModel model;
	private MMMHelper helper;
	private MatrixView matrixView;
	private GraphView graphView;

	private CheckComboBox<Artifact> sourceCheckComboBox;
	private CheckComboBox<Artifact> targetCheckComboBox;
	private ComboBox<Link> linkComboBox;

	private DefineLinkDialog defineLinkDialog;
	private MenuItem defineLinkMenuItem;
	private CheckMenuItem matrixItem;

	private BorderPane root;
	private GridPane sourcePane = new GridPane();
	private GridPane targetPane = new GridPane();
	private ToolBar toolBar;
	private ToolBar graphHandlingBar;
	private Label sourceLabel;
	private Label targetLabel;
	private VBox bars;

	private final int BARS_HEIGHT = 200;
	
	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		Cloud cloud = RestCloud.getInstance();
		User user = null;

		List<String> args = getParameters().getRaw();
		if(args.isEmpty() || !args.contains("USER:")) { // no args or no user given, show login dialog
			Optional<User> optional = new LoginDialog(cloud).showAndWait();
			if (!optional.isPresent()) return;
			user = optional.get();
		}
		helper = new MMMHelper(user);
		model = new MMMDataModel(helper);
		model.addListener(this);

		defineLinkDialog = new DefineLinkDialog(model);
		matrixView = new MatrixView(model);
		graphView = new GraphView(model);

		
		root = new BorderPane();
		bars = new VBox();
		MenuBar menuBar = createMenuBar();
		toolBar = createToolBar();
		graphHandlingBar = createGrahpHandlingBar();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		toolBar.prefWidthProperty().bind(primaryStage.widthProperty());
		bars.getChildren().addAll(menuBar, toolBar);
		bars.setMaxHeight(BARS_HEIGHT);
		
		ToolBar saveBar = new ToolBar();
		HBox spacer = new HBox();
		spacer.setHgrow(spacer, Priority.ALWAYS);
		saveBar.getItems().add(spacer);
		Button saveButton = new Button("SAVE LINKS");
		saveButton.setOnAction(event -> {
			if(matrixItem.isSelected()){
				matrixView.saveLinks();
			}else{
				graphView.saveLinks();
			}
		});

		saveButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		saveButton.setTextFill(Color.GREEN);
		saveButton.setAlignment(Pos.CENTER);
		saveBar.getItems().add(saveButton);

		// TODO to REMOVE - initial load!!!!
		// TODO: Interaction with design space
		List<Package> collect = helper.getWorkspace().getPackages().stream()
				.filter(p -> "CodePackage".equals(((String) p.getPropertyValue(DataStorage.PROPERTY_NAME))))
				.collect(Collectors.toList());
		model.setDataPackages(collect);

		
		root.setTop(bars);
		setView(matrixView); // center
		root.setBottom(saveBar);
		root.requestFocus();

		// set size of the application to the corresponding window resolution
		primaryStage.setTitle("Linking Tool by Onur Kirkizoglu");
		primaryStage.setScene(new Scene(root));
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void refreshGraph() {
		Map<Artifact, Set<Artifact>> sourceData;
		Map<Artifact, Set<Artifact>> targetData;
		Link selectedLink = linkComboBox.getSelectionModel().getSelectedItem();
		
		// if link is specified, only certain source/target is possible
		if (!selectedLink.isMultipleLink()) {
			sourceData = model.getDataMap().entrySet().stream().filter(map -> {
				return selectedLink.getSource().equals(map.getKey());
			}).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			targetData = model.getDataMap().entrySet().stream().filter(map -> {
				return selectedLink.getTarget().equals(map.getKey());
			}).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		} else {
			sourceData = model.getDataMap().entrySet().stream().filter(map -> {
				return sourceCheckComboBox.getCheckModel().getCheckedItems().contains(map.getKey());
			}).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			targetData = model.getDataMap().entrySet().stream()
					.filter(map -> targetCheckComboBox.getCheckModel().getCheckedItems().contains(map.getKey()))
					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		}
		
		graphView.update(sourceData, targetData, selectedLink);
	}
	
	private void refreshMatrix() {
		Map<Artifact, Set<Artifact>> sourceData;
		Map<Artifact, Set<Artifact>> targetData;
		Link selectedLink = linkComboBox.getSelectionModel().getSelectedItem();
		
		// if link is specified, only certain source/target is possible
		if (!selectedLink.isMultipleLink()) {
			sourceData = model.getDataMap().entrySet().stream().filter(map -> {
				return selectedLink.getSource().equals(map.getKey());
			}).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			targetData = model.getDataMap().entrySet().stream().filter(map -> {
				return selectedLink.getTarget().equals(map.getKey());
			}).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		} else {
			sourceData = model.getDataMap().entrySet().stream().filter(map -> {
				return sourceCheckComboBox.getCheckModel().getCheckedItems().contains(map.getKey());
			}).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			targetData = model.getDataMap().entrySet().stream()
					.filter(map -> targetCheckComboBox.getCheckModel().getCheckedItems().contains(map.getKey()))
					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		}
		matrixView.update(sourceData, targetData, selectedLink);
	}

	/**
	 * Updating of control bar.
	 */
	private void refreshSelections() {
		ObservableList<Artifact> checkedSourceItems = sourceCheckComboBox.getCheckModel().getCheckedItems();
		ObservableList<Artifact> checkedTargettems = targetCheckComboBox.getCheckModel().getCheckedItems();
		Link selectedItem = linkComboBox.getSelectionModel().getSelectedItem();

		sourceCheckComboBox.getItems().clear();
		targetCheckComboBox.getItems().clear();
		linkComboBox.getItems().clear();

		ObservableList<Artifact> sortedList = GUIHelper
				.createSortedComboBoxArtifactList(new ArrayList<Artifact>(model.getDataMap().keySet()));

		sourceCheckComboBox.getItems().addAll(sortedList);
		checkedSourceItems.stream().forEach(checkedItem -> {
			sourceCheckComboBox.getCheckModel().check(checkedItem);
		});

		targetCheckComboBox.getItems().addAll(sortedList);
		checkedTargettems.stream().forEach(checkedItem -> {
			sourceCheckComboBox.getCheckModel().check(checkedItem);
		});

		linkComboBox.getItems().addAll(GUIHelper.createSortedComboBoxLinkList(model.getDefinedLinks(),
				new Link(Constants.COMBOBOX_ALL_SELECTION)));
		
		if(linkComboBox.getItems().contains(selectedItem)){
			linkComboBox.getSelectionModel().select(selectedItem);
		}else{
			linkComboBox.getSelectionModel().select(0);
		}
		root.requestFocus();
	}

	@Override
	public void dataContentChanged() {
	}

	@Override
	public void dataPackagesChanged() {
		refreshSelections();
	}

	@Override
	public void linkPackageChanged() {
		refreshSelections();
		defineLinkMenuItem.setDisable(false);
	}

	@Override
	public void linkContentChanged() {
		refreshSelections();
	}

	@SuppressWarnings("unchecked")
	private MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();

		Menu applicationMenu = new Menu("Application");

		MenuItem loadDataMenuItem = new MenuItem("Load Data");
		loadDataMenuItem.setDisable(false);
		// TODO: Interaction with design space
		loadDataMenuItem.setOnAction(actionEvent -> {
			Optional<List<? extends Artifact>> optional = ListDialog
					.DataPackageListDialog(helper.getWorkspace().getPackages(), model.getDataPackages()).showAndWait();
			optional.ifPresent(artifacts -> model.setDataPackages((List<Package>) artifacts));
		});

		MenuItem configureDataMenuItem = new MenuItem("Load Link Package");
		// TODO: Interaction with design space
		configureDataMenuItem.setOnAction(actionEvent -> {
			List<Package> linkPackages = helper.getWorkspace().getPackages().stream()
					.filter(p -> Boolean.TRUE.equals(p.getPropertyValueOrNull(Constants.PACKAGE_LINK_PROPERTY)))
					.collect(Collectors.toList());
			Optional<List<? extends Artifact>> optional = ListDialog
					.LinkPackageListDialog(helper, linkPackages, model.getLinkPackage()).showAndWait();
			optional.ifPresent(artifacts -> model.setLinkPackage((Package) artifacts.get(0)));
			refreshView();
		});

		defineLinkMenuItem = new MenuItem("Define Link");
		defineLinkMenuItem.setDisable(true);
		defineLinkMenuItem.setOnAction(actionEvent -> {
			defineLinkDialog.showAndWait();
		});

		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setOnAction(actionEvent -> Platform.exit());

		applicationMenu.getItems().addAll(loadDataMenuItem, configureDataMenuItem, new SeparatorMenuItem(),
				defineLinkMenuItem, exitMenuItem);

		Menu viewMenu = new Menu("View");
		matrixItem = new CheckMenuItem("Matrix");
		CheckMenuItem graphItem = new CheckMenuItem("Graph");

		graphItem.setOnAction(actionEvent -> {
			matrixItem.setSelected(false);
			
			BorderPane wrapperPane = new BorderPane();
	        // Put canvas in the center of the window
	        wrapperPane.setCenter(graphView);
	        // Bind the width/height property to the wrapper Pane
	        graphView.widthProperty().bind(wrapperPane.widthProperty());
	        graphView.heightProperty().bind(wrapperPane.heightProperty());
	        // redraw when resized
	        graphView.widthProperty().addListener(event -> graphView.repaint());
	        graphView.heightProperty().addListener(event -> graphView.repaint());
//	        graphHandlingBar.setVisible(true);
	        bars.getChildren().add(graphHandlingBar);
	        refreshGraph();
			setView(wrapperPane);
		});
		matrixItem.setOnAction(actionEvent -> {
			graphItem.setSelected(false);
//			graphHandlingBar.setVisible(false);
			bars.getChildren().remove(graphHandlingBar);
			refreshMatrix();
			setView(matrixView);
		});

		// initial matrix view is enabled
		matrixItem.setSelected(true);

		viewMenu.getItems().addAll(matrixItem, graphItem);
		menuBar.getMenus().addAll(applicationMenu, viewMenu);
		return menuBar;
	}

/**
 * 
 * @return A special Toolbar to select sources/targets and/or links of given view.
 * 
 */
	private ToolBar createToolBar() {
		GridPane linkPane = new GridPane();
		GridPane applySelectionPane = new GridPane();
		ToolBar t = new ToolBar();
		sourceLabel = new Label("SELECTED SOURCES:\t");
		sourceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		sourceLabel.setPadding(new Insets(0, 0, 0, 10));
		sourcePane.add(sourceLabel, 0, 0);
		// create the data to show in the CheckComboBox
		sourceCheckComboBox = GUIHelper.createCheckComboBox(new ArrayList<>(model.getDataMap().keySet()));
		// Create the CheckComboBox with the data
		sourcePane.add(sourceCheckComboBox, 1, 0);
		sourceCheckComboBox.setMinWidth(150);

		targetLabel = new Label("SELECTED TARGETS:\t");
		targetLabel.setPadding(new Insets(0, 0, 0, 10));
		targetLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		targetPane.add(targetLabel, 0, 0);
		// create the data to show in the CheckComboBox
		targetCheckComboBox = GUIHelper.createCheckComboBox(new ArrayList<>(model.getDataMap().keySet()));
		// Create the CheckComboBox with the data
		targetPane.add(targetCheckComboBox, 1, 0);
		targetCheckComboBox.setMinWidth(150);

		Label linkLabel = new Label("SELECTED LINK:\t");
		linkLabel.setPadding(new Insets(0, 0, 0, 10));
		linkLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		linkPane.add(linkLabel, 0, 0);
		linkComboBox = GUIHelper.createLinkComboBox(model.getDefinedLinks());

		linkComboBox.setMinWidth(150);
		linkPane.add(linkComboBox, 1, 0);

		Button applySelection = new Button("APPLY SELECTION");
		applySelection.setOnAction(event -> {
			refreshView();
		});

		applySelectionPane.setPadding(new Insets(0, 20, 0, 20));
		applySelectionPane.add(applySelection, 0, 0);
		applySelection.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		applySelection.setTextFill(Color.GREEN);
		applySelection.setPrefWidth(150);

		t.getItems().addAll(linkPane, sourcePane, targetPane, applySelectionPane);

		linkComboBox.valueProperty().addListener(new ChangeListener<Link>() {

			@Override
			public void changed(ObservableValue<? extends Link> observable, Link oldValue, Link newValue) {
				if (newValue != null) {
					if (newValue.isMultipleLink()) {
						toolBar.getItems().remove(sourcePane);
						toolBar.getItems().remove(targetPane);
						toolBar.getItems().add(1, sourcePane);
						toolBar.getItems().add(2, targetPane);
					} else {
						toolBar.getItems().remove(sourcePane);
						toolBar.getItems().remove(targetPane);
					}
				}
			}
		});
		return t;
	}
/**
 * 
 * @return A special Toolbar for handling different graph operations:<p>
 * 
 *  - Moving of elements <br>
 *  - Creation of Link between elements<br>
 *  - Deletion of Link between elements<p>
 *  Only visible, if graphView is visible.
 */
	private ToolBar createGrahpHandlingBar() {
		ToolBar t = new ToolBar();

		// move elems
		Button moveElement = new Button("MOVE ELEMENT");
		moveElement.setOnAction(event -> {
			graphView.setGraphController(Control.MOVE);
			root.requestFocus();
		});
		moveElement.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		moveElement.setTextFill(Color.BLACK);
		moveElement.setPrefWidth(150);
		
		// creation of links between elems
		Button createLink = new Button("CREATE LINK");
		createLink.setOnAction(event -> {
			graphView.setGraphController(Control.CREATE_LINK);
			root.requestFocus();
		});
		createLink.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		createLink.setTextFill(Color.BLUE);
		createLink.setPrefWidth(150);
		
		// deletion links between elems
		Button deleteLink = new Button("DELETE LINK");
		deleteLink.setOnAction(event -> {
			graphView.setGraphController(Control.DELETE_LINK);
			root.requestFocus();
		});	
		deleteLink.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		deleteLink.setTextFill(Color.RED);
		deleteLink.setPrefWidth(150);
		
		t.getItems().addAll(moveElement, createLink, deleteLink);
		return t;
	}

	/**
	 * Updated selected view.
	 */
	private void refreshView() {
		if (matrixItem.isSelected()) {
			refreshMatrix();
		} else {
			refreshGraph();
		}
		root.requestFocus();
	}

	private void setView(Pane view) {
		view.setMinSize(4000, 4000);
		view.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		ScrollPane sp = new ScrollPane(view);
		sp.setPrefSize(300, 300);
		sp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		sp.setFitToWidth(true);
		sp.setFitToHeight(true);
		root.requestFocus();
		root.setCenter(sp);
	}
}
