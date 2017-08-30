package application.setup;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Property;
import init.Constants;
import init.MMMHelper;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.controlsfx.control.CheckListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A dialog used to display a list and select {@link Artifact}s from it.
 */
public class ListDialog extends Dialog<List<? extends Artifact>> {

    private final List<Package> previouslySelected = new ArrayList<>();
    private final List<Package> list = new ArrayList<>();
    private MMMHelper helper;
    private FilterablePane packagePane;

    /**
     * Creates a ListDialog which displays all data packages.<br>
     * Multiple selections are allowed and managed via a {@link CheckListView}.
     *
     * @param allPackages all packages, which should be displayed in the list
     * @param selectedPackages the previously selected packages, which currently being used by the LinkingTool.
     * @return the new ListDialog
     */
    public static ListDialog DataPackageListDialog(Collection<Package> allPackages, Collection<Package> selectedPackages) {
        return new ListDialog(allPackages, selectedPackages);
    }

    /**
     * Creates a ListDialog which displays all link packages.<br>
     * Allows the user to add further link packages to the design space.<br>
     * Only single selections are supported.
     *
     * @param helper the {@link MMMHelper} used to create additional link packages
     * @param allPackages all packages, which should be displayed in the list
     * @param linkPackage the currently used link package
     * @return
     */
    public static ListDialog LinkPackageListDialog(MMMHelper helper, Collection<Package> allPackages, Package... linkPackage) {
        return new ListDialog(helper, allPackages, linkPackage);
    }

    private ListDialog() {}

    private ListDialog(Collection<Package> allPackages, Collection<Package> selectedPackages) {
        list.addAll(allPackages);
        previouslySelected.addAll(selectedPackages);
        createContent("Select Data Packages", true);
    }

    private ListDialog(MMMHelper helper, Collection<Package> allPackages, Package... linkPackage) {
        this.helper = helper;
        list.addAll(allPackages);
        previouslySelected.addAll(Arrays.asList(linkPackage));
        GridPane pane = createContent("Select Link Package", false);
        addPackageCreation(pane);
    }

    // Adds a pane with textField and button to enable the creation of link packages.
    // This should only be called if the second constructor is used.
    private void addPackageCreation(GridPane pane) {
        GridPane gridPane = new GridPane();

        TextField field = new TextField();
        GridPane.setHgrow(field, Priority.ALWAYS);
        field.setPromptText("New Package Name");
        gridPane.add(field, 0,0);

        Button b = new Button("+");
        b.setOnAction(e -> {
            if(field.getText().trim().isEmpty()) return;
            // create package and set property
            Package pkg = helper.getWorkspace().createPackage(field.getText());
            Property isLinkPackage = pkg.createProperty(helper.getWorkspace(), Constants.PACKAGE_LINK_PROPERTY);
            isLinkPackage.setValue(helper.getWorkspace(), true);
            helper.commit();

            // add to listview
            list.add(pkg);
            packagePane.setListItems(list);

            //finally clear the textfield
            field.clear();
        });
        gridPane.add(b,1,0);

        pane.add(gridPane,0,1);
    }
    
    private GridPane createContent(String title, boolean multipleSelection) {
        this.setTitle(title);

        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane pane = new GridPane();

        packagePane = new FilterablePane("Select Packages");
        packagePane.setView(multipleSelection);
        ListView displayedList = packagePane.getDisplayedList();
        if(multipleSelection && displayedList instanceof CheckListView) {
            CheckListView view = (CheckListView) displayedList;
            view.setCellFactory(p -> new CheckBoxListCell<>(view::getItemBooleanProperty, GUIHelper.packageStringConverter));
        } else {
            displayedList.setCellFactory(GUIHelper.listPackageCallBack);
        }
        packagePane.setListItems(list);
        checkSelectedPackages(displayedList);

        pane.add(packagePane, 0,0);

        this.getDialogPane().setContent(pane);

        this.setResultConverter(buttonType -> {
            if(buttonType == ButtonType.OK) {
                return packagePane.getSelectedItems();
            }
            return null;
        });

        return pane;
    }

    private void checkSelectedPackages(ListView displayedList) {
        if(displayedList instanceof CheckListView) {
            CheckListView list = (CheckListView) displayedList;
            for(Package p : previouslySelected) {
                list.getCheckModel().check(p);
            }
        } else {
            displayedList.getSelectionModel().select(previouslySelected.get(0));
        }

    }

}
