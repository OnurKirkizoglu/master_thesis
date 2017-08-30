package application.setup;

import java.util.HashMap;
import java.util.List;

import org.controlsfx.control.CheckListView;

import init.setup.Link;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import matrix.MatrixElement;

public class LinkListDialog extends Dialog<List<? extends Link>> {
	private LinkFilterablePane linkPane;
	@SuppressWarnings("unused")
	private MatrixElement mElement;
	HashMap<Link, Triple> links;

	public LinkListDialog MultipleLinkListDialog(MatrixElement elem) {
		return new LinkListDialog(elem);
	}

	public LinkListDialog() {
	}

	public LinkListDialog(MatrixElement elem) {
		this.links = elem.getLinksToBeProcessed();
		createContent("Possible links");
	}

	@SuppressWarnings("unchecked")
	private void createContent(String title) {
		this.setTitle(title);

		this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		GridPane pane = new GridPane();

		linkPane = new LinkFilterablePane("Select Links");
		ListView<Link> displayedList = linkPane.getDisplayedList();

		CheckListView<Link> view = (CheckListView<Link>) displayedList;
		view.setCellFactory(
				p -> new CheckBoxListCell<>(view::getItemBooleanProperty, GUIHelper.linkComplexTypeStringConverter));

		linkPane.setListItems(links.keySet());
		checkAvailableLinks(displayedList);

		pane.add(linkPane, 0, 0);

		this.getDialogPane().setContent(pane);

		this.setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				CheckListView<Link> list = (CheckListView<Link>) displayedList;
				links.keySet().forEach(defLink -> {
					Triple triple = links.get(defLink);
					if (list.getCheckModel().getCheckedItems().contains(defLink)) {
						triple.setToCreate(true);
					} else {
						triple.setToCreate(false);
					}
				});
			}
			return null;
		});
	}

	private void checkAvailableLinks(ListView<Link> displayedList) {
		if (displayedList instanceof CheckListView) {
			CheckListView<Link> list = (CheckListView<Link>) displayedList;
			links.keySet().forEach(link -> {
				Triple triple = links.get(link);
				if (triple.isToCreate()) {
					list.getCheckModel().check(link);
				}
			});
		}
	}
}
