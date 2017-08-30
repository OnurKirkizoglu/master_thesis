package application.setup;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import at.jku.sea.cloud.rest.client.RestPackage;
import init.setup.Link;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.IndexedCheckModel;

import java.util.*;

/**
 * This class represents a pane consisting of a listView and a filter field. The
 * displayed list can either be a normal listView or a CheckListView for
 * multiple selections. Furthermore it automatically handles which type of
 * listView needs to be displayed.
 */
public class LinkFilterablePane extends GridPane {

	private static final Font FONT = new Font("Arial", 15);

	private FilteredList<Link> filteredData;
	private ObservableList list = FXCollections.observableArrayList();
	private CheckListView<Link> checkListView;
	private TextField filterField;

	// used to store checked items
	private List<Link> checkedItems = new ArrayList<>();

	/**
	 * Constructor to create the panes content and sets the title
	 *
	 * @param title
	 *            the label to be displayed at the very top of the pane
	 */
	public LinkFilterablePane(String title) {
		createContent(title);
	}

	private void createContent(String title) {
		GridPane.setHgrow(this, Priority.ALWAYS);
		Button button = createButton();
		filterField = createTextField();
		GridPane filterPane = new GridPane();
		filterPane.add(filterField, 0, 0);
		filterPane.add(button, 1, 0);

		filteredData = new FilteredList(list, null);
		checkListView = createCheckListView(filteredData);

		Label label = new Label(title);
		label.setPadding(new Insets(7, 0, 2, 0));
		label.setFont(FONT);
		this.add(label, 0, 0);
		this.add(filterPane, 0, 1);
		this.add(checkListView, 0, 2);
	}

	private TextField createTextField() {
		TextField textField = new TextField();
		GridPane.setHgrow(textField, Priority.ALWAYS);
		textField.setPromptText("Artifact Filter");
		textField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				e.consume();
				filterList(textField.getText());
			}
		});

		return textField;
	}

	private Button createButton() {
		Button b = new Button("Filter");
		b.setOnAction(e -> filterList(filterField.getText()));

		return b;
	}

	private ListView<Artifact> createListView(FilteredList list) {
		ListView<Artifact> listView = new ListView<>(list);
		GridPane.setHgrow(listView, Priority.ALWAYS);
		listView.setMinHeight(480);

		final Callback<ListView<Artifact>, ListCell<Artifact>> listCallBack = new Callback<ListView<Artifact>, ListCell<Artifact>>() {
			@Override
			public ListCell<Artifact> call(ListView<Artifact> param) {
				return new ListCell<Artifact>() {
					@Override
					protected void updateItem(Artifact item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							StringBuilder sb = new StringBuilder();
							setText((String) item.getPropertyValueOrNull(MMMTypeProperties.NAME));

							for (Property p : item.getAliveProperties()) {
								sb.append(p.getName() + ":\t" + (p.getValue() == null ? "" : p.getValue().toString())
										+ "\n");
							}
							Tooltip tp = new Tooltip(sb.toString());
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
		};

		listView.setCellFactory(listCallBack);

		return listView;
	}

	private CheckListView<Link> createCheckListView(FilteredList list) {
		CheckListView<Link> checkListView = new CheckListView<>(list);
		GridPane.setHgrow(checkListView, Priority.ALWAYS);
		checkListView.setMinHeight(480);

		checkListView.setCellFactory(p -> new CheckBoxListCell<Link>(checkListView::getItemBooleanProperty) {
			@Override
			public void updateItem(Link item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getName());

					// Tooltip tp = new Tooltip(sb.toString());
					// tp.setMaxWidth(400);
					// tp.setWrapText(true);
					// setTooltip(tp);
				}
				if (empty || item == null) {
					setGraphic(null);
					setText(null);
				}
			}
		});
		
		checkListView.getCheckModel().getCheckedItems().addListener((ListChangeListener<? super Link>) c -> {
			c.next();
			if (c.wasAdded()) {
				checkedItems.add(c.getAddedSubList().get(0));
			}
			if (c.wasRemoved()) {
				checkedItems.remove(c.getRemoved().get(0));
			}
		});

		return checkListView;
	}

	private void filterList(String text) {
		if (text == null || text.length() == 0) {
			filteredData.setPredicate(null);
		} else {
			filteredData.setPredicate(s -> {
				return s.getName() != null && s.getName().toLowerCase().contains(text.toLowerCase());
			});
		}
		checkItems();
	}

	private void checkItems() {
		IndexedCheckModel<Link> checkModel = checkListView.getCheckModel();
		checkedItems.forEach(checkModel::check);
	}

	/**
	 * @return the currently displayed listView
	 */
	public CheckListView getDisplayedList() {
		return checkListView;
	}

	/**
	 * Replaces the elements of the list
	 *
	 * @param items
	 *            the items to be added to the list
	 */
	public void setListItems(Collection<? extends Link> items) {
		if (items.isEmpty())
			return;
		list.clear();

		// set boolean to indicate which type of elements are displayed
		list.addAll(items);
		list.sort((o1, o2) -> {
			return ((Link) o1).getName().compareTo(((Link) o2).getName());
		});
		// in case a filter is still set!
		if (!filterField.getText().isEmpty()) {
			filterList(filterField.getText());
		}

		getDisplayedList().refresh();
	}

	/**
	 * @return a List of selected Items
	 */
	public List<? extends Link> getSelectedItems() {
		return Collections.unmodifiableList(checkedItems);
	}

	/**
	 * Clears the list
	 */
	public void resetList() {
		list.clear();
		getDisplayedList().refresh();
	}

	/**
	 * Deselects all items from the displayed list
	 */
	public void clearSelection() {
		checkListView.getCheckModel().clearChecks();
	}

}
