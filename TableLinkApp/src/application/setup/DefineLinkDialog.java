package application.setup;

import application.MMMDataModel;
import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import init.Constants;
import init.setup.Link;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A dialog, used to define new types of links.<br>
 * Each link definition has the following properties:
 * <li>
 *     <ul>Name</ul>
 *     <ul>Source</ul>
 *     <ul>Target</ul>
 *     <ul>Description</ul>
 * </li>
 *
 */
public class DefineLinkDialog extends Dialog<Void> {

	private static final Color COLOR_WHITE = Color.web("#000000");
	private static final Color COLOR_RED = Color.web("#db0404");

	private MMMDataModel model;

	private Button linkCreationButton;
	private Button removeTargetSelectionButton;
	private Button removeSourceSelectionButton;

	private TextField linkSourceText;
	private TextField linkTargetText;
	private TextField linkNameText;
	private TextArea linkDescriptionText;

	private ListView<Link> definedLinkListView;

	private Artifact selectedSource;
	private Artifact selectedTarget;
	private Link selectedDefinedLink;

	private Label labelLinkSource;
	private Label labelLinkTarget;
	private Label labelLinkName;
	
	private ContextMenu contextMenu;
	private MenuItem removeMenuItem;

	private FilterablePane targetPane;
	private FilterablePane sourcePane;

	public DefineLinkDialog(MMMDataModel model) {
		super();
		this.model = model;
		model.addListener(myModelListener);

		buildGUI();
	}

	private void setActionListeners() {
		this.getDialogPane().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

		removeSourceSelectionButton.setOnAction(event -> {
            selectedSource = null;
            sourcePane.clearSelection();
            linkSourceText.setText("");
        });

		removeTargetSelectionButton.setOnAction(event -> {
            selectedTarget = null;
            targetPane.clearSelection();
            linkTargetText.setText("");
        });

		linkCreationButton.setOnAction(event -> {

            labelLinkSource.setTextFill(selectedSource == null ? COLOR_RED : COLOR_WHITE);
			labelLinkTarget.setTextFill(selectedTarget == null ? COLOR_RED : COLOR_WHITE);
			labelLinkName.setTextFill(linkNameText.getText().trim().isEmpty() ? COLOR_RED : COLOR_WHITE);

			if (selectedSource != null && selectedTarget != null && linkNameText.getText().trim().length() > 0) {
				if(!model.addDefinedLink(linkNameText.getText(), selectedSource, selectedTarget, "" /*linkDescriptionText.getText()*/)){
					// link already exists
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Link already exists");
					alert.setContentText(String.format("The link with the given properties already exists:\nname:\t%s\nsource:\t%s\ntarget:\t%s", linkNameText.getText(), linkSourceText.getText(), linkTargetText.getText()));
					alert.showAndWait();
				} else {
					resetAll();
				}
			} else {
				labelLinkSource.setTextFill(COLOR_RED);
				labelLinkTarget.setTextFill(COLOR_RED);
				labelLinkName.setTextFill(COLOR_RED);
			}
		});
		removeMenuItem.setOnAction(a -> model.removeDefinedLink(selectedDefinedLink));
	}

	private void resetAll() {
		labelLinkSource.setTextFill(COLOR_WHITE);
		labelLinkTarget.setTextFill(COLOR_WHITE);
		labelLinkName.setTextFill(COLOR_WHITE);
		linkNameText.setText("");
		linkDescriptionText.setText("");
		linkSourceText.clear();
		linkTargetText.clear();
		sourcePane.clearSelection();
		targetPane.clearSelection();
	}

	private void buildGUI() {
		this.setTitle("Define Links");
		GridPane mainPane = new GridPane();
		GridPane linkDefinitionPane = new GridPane();
		mainPane.setPadding(new Insets(10, 10, 0, 10));
		linkDefinitionPane.setPadding(new Insets(10, 10, 0, 10));

		Set<Artifact> items = model.getDataMap().keySet();

		// target
		targetPane = new FilterablePane("Target");
		targetPane.setPadding(new Insets(25, 10, 0, 20));
		targetPane.setListItems(items);
		targetPane.getDisplayedList().setOnMousePressed(l -> {
			selectedTarget = targetPane.getSelectedItems().get(0);
			setText(linkTargetText, selectedTarget);
		});
		// source
		sourcePane = new FilterablePane("Source");
		sourcePane.setPadding(new Insets(25, 20, 0, 10));
		sourcePane.setListItems(items);
		sourcePane.getDisplayedList().setOnMousePressed(l -> {
			selectedSource = sourcePane.getSelectedItems().get(0);
			setText(linkSourceText, selectedSource);
		});

		// context menu on defined links
		contextMenu = new ContextMenu();
		removeMenuItem = new MenuItem("Remove");
		contextMenu.getItems().add(removeMenuItem);

		// link
		Label labelDefineNewLink = new Label("Define New Link");
		labelDefineNewLink.setPadding(new Insets(0, 0, 2, 0));
		labelDefineNewLink.setFont(Font.font("Arial", FontWeight.BOLD, 15));

		GridPane linkSourcePane = new GridPane();
		GridPane.setHgrow(linkSourcePane, Priority.ALWAYS);
		labelLinkSource = new Label("Selected Source*:");
		labelLinkSource.setPadding(new Insets(7, 0, 5, 0));
		int textSize = 12;
		labelLinkSource.setFont(new Font("Arial", textSize));
		int labelWidth = 120;
		labelLinkSource.setPrefWidth(labelWidth);
		linkSourceText = new TextField();
		linkSourceText.setStyle("-fx-background-color: lightgrey;\n" + "-fx-border-color: black;\n");
		linkSourceText.setEditable(false);
		removeSourceSelectionButton = new Button("X");
		GridPane.setHgrow(linkSourceText, Priority.ALWAYS);
		linkSourcePane.add(labelLinkSource, 0, 0);
		linkSourcePane.add(linkSourceText, 1, 0);
		linkSourcePane.add(removeSourceSelectionButton, 2, 0);

		GridPane linkTargetPane = new GridPane();
		GridPane.setHgrow(linkTargetPane, Priority.ALWAYS);
		labelLinkTarget = new Label("Selected Target*:");
		labelLinkTarget.setPadding(new Insets(7, 0, 2, 0));
		labelLinkTarget.setFont(new Font("Arial", textSize));
		labelLinkTarget.setPrefWidth(labelWidth);
		linkTargetText = new TextField();
		linkTargetText.setStyle("-fx-background-color: lightgrey;\n" + "-fx-border-color: black;\n");
		linkTargetText.setEditable(false);
		GridPane.setHgrow(linkTargetText, Priority.ALWAYS);
		removeTargetSelectionButton = new Button("X");
		linkTargetPane.add(labelLinkTarget, 0, 0);
		linkTargetPane.add(linkTargetText, 1, 0);
		linkTargetPane.add(removeTargetSelectionButton, 2, 0);

		labelLinkName = new Label("Name*");
		labelLinkName.setPadding(new Insets(7, 0, 2, 0));
		labelLinkName.setFont(new Font("Arial", textSize));
		linkNameText = new TextField();
		GridPane.setHgrow(linkNameText, Priority.ALWAYS);

		Label labelLinkDescription = new Label("Description");
		labelLinkDescription.setPadding(new Insets(7, 0, 2, 0));
		labelLinkDescription.setFont(new Font("Arial", textSize));
		linkDescriptionText = new TextArea();
		linkDescriptionText.setPrefRowCount(8);
		linkDescriptionText.setWrapText(true);
		GridPane.setHgrow(linkDescriptionText, Priority.ALWAYS);
		linkDescriptionText.setPrefHeight(20);

		linkCreationButton = new Button("Create Link");
		linkCreationButton.setPrefWidth(200);
		GridPane.setHgrow(linkCreationButton, Priority.ALWAYS);
		GridPane.setHalignment(linkCreationButton, HPos.CENTER);

		definedLinkListView = new ListView<>();
		definedLinkListView.setCellFactory(new Callback<ListView<Link>, ListCell<Link>>() {
			@Override
			public ListCell<Link> call(ListView<Link> param) {
				return new ListCell<Link>() {
					@Override
					protected void updateItem(Link item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							String src = (String) (item.getSource())
									.getPropertyValue(MMMTypeProperties.NAME);
							String tar = (String) (item.getTarget())
									.getPropertyValue(MMMTypeProperties.NAME);
							String desc = (String) (item.getDescription())
									.getPropertyValue(Constants.DESC_DESCRIPTION);
							setText(item.getName() + ": " + src + " -> "
									+ tar);
							Tooltip tp = new Tooltip("Source: \t" + src + "\n" + "Target: \t" + tar + "\n"
									+ "Description: \t" + desc);
							tp.setMaxWidth(400);
							tp.setWrapText(true);
							setTooltip(tp);
						}
						if (empty || item == null) {
							setGraphic(null);
							setText(null);
						}
					}
				};
			}
		});
		definedLinkListView.setOnMousePressed(e -> selectedDefinedLink = definedLinkListView.getSelectionModel().getSelectedItem());
		definedLinkListView.setOnContextMenuRequested(e -> {
			contextMenu.hide();
			if (selectedDefinedLink != null) {
				contextMenu.show(definedLinkListView, e.getScreenX(), e.getScreenY());
			}
		});
		Label labelDefinedLinks = new Label("Defined Links");
		labelDefinedLinks.setFont(new Font("Arial", 15));
		labelDefinedLinks.setPadding(new Insets(10, 0, 2, 0));

		linkDefinitionPane.add(labelDefineNewLink, 0, 0);
		linkDefinitionPane.add(linkSourcePane, 0, 1);
		linkDefinitionPane.add(linkTargetPane, 0, 2);
		linkDefinitionPane.add(labelLinkName, 0, 3);
		linkDefinitionPane.add(linkNameText, 0, 4);
		linkDefinitionPane.add(labelLinkDescription, 0, 5);
		linkDefinitionPane.add(linkDescriptionText, 0, 6);
		linkDefinitionPane.add(linkCreationButton, 0, 8);
		linkDefinitionPane.add(labelDefinedLinks, 0, 9);
		linkDefinitionPane.add(definedLinkListView, 0, 10);

		// main
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(30);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(40);
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(30);
		mainPane.getColumnConstraints().addAll(col1, col2, col3);

		mainPane.add(sourcePane, 0, 0);
		mainPane.add(linkDefinitionPane, 1, 0);
		mainPane.add(targetPane, 2,0);
		setActionListeners();

		this.getDialogPane().setContent(mainPane);
		this.getDialogPane().getButtonTypes().add(ButtonType.OK);
		this.getDialogPane().getScene().getWindow().sizeToScene();
		this.getDialogPane().setPrefHeight(490);
		this.getDialogPane().setPrefWidth(800);

        this.setOnShown(e -> {
            this.setHeight(640);
            this.setWidth(806);
        });
    }
	

	private void setText(TextField textField, Artifact artifact) {
		if(artifact == null) return;
		textField.setText((String) artifact.getPropertyValueOrNull(MMMTypeProperties.NAME));
	}

	private void updateDefinedLinkListView() {
		Set<Artifact> items = model.getDataMap().keySet();
		Set<Link> set = model.getDefinedLinks().stream()
				.filter(link -> items.contains(link.getSource()) && items.contains(link.getTarget()))
				.collect(Collectors.toSet());
		definedLinkListView.setItems(FXCollections.observableArrayList(set));
		definedLinkListView.refresh();
	}

	/**
	 * Removes the listener from the model
	 */
	public void dispose() {
		model.removeListener(myModelListener);
	}

	private final ModelListener myModelListener = new ModelListener() {
		@Override
		public void dataPackagesChanged() {
			Set<Artifact> items = model.getDataMap().keySet();
			sourcePane.setListItems(items);
			targetPane.setListItems(items);
			updateDefinedLinkListView();
		}

		@Override
		public void dataContentChanged() {
			Set<Artifact> items = model.getDataMap().keySet();
			sourcePane.setListItems(items);
			targetPane.setListItems(items);
			updateDefinedLinkListView();
		}

		@Override
		public void linkPackageChanged() {
			updateDefinedLinkListView();
		}

		@Override
		public void linkContentChanged() {
			updateDefinedLinkListView();
		}
	};
}
