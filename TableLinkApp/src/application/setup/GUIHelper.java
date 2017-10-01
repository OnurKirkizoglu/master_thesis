package application.setup;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Property;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import at.jku.sea.cloud.rest.client.RestCollectionArtifact;
import init.Constants;
import init.setup.Link;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.Comparator;
import java.util.List;

import org.controlsfx.control.CheckComboBox;
// TODO Whole class interacts with design space.
/**
 * Provides some basic GUI controls and callbacks, used throughout the project
 */
public class GUIHelper {

	private GUIHelper() {
	}

	/**
	 * A callback for {@link ListView#cellFactoryProperty} which handles the
	 * correct naming of {@link Package} in {@link ListView}s and showing a
	 * tooltip, containing all alive properties of the hovered {@link Package}.
	 */
	public static final Callback<ListView<Package>, ListCell<Package>> listPackageCallBack = new Callback<ListView<Package>, ListCell<Package>>() {
		@Override
		public ListCell<Package> call(ListView<Package> param) {
			return new ListCell<Package>() {
				@Override
				protected void updateItem(Package item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null) {
						StringBuilder sb = new StringBuilder();
						setText((String) item.getPropertyValueOrNull(DataStorage.PROPERTY_NAME));

						for (Property p : item.getAliveProperties()) {
							sb.append(
									p.getName() + ":\t" + (p.getValue() == null ? "" : p.getValue().toString()) + "\n");
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

	/**
	 * A callback for {@link ListView#cellFactoryProperty} which handles the
	 * correct naming of {@link Artifact}s in {@link ListView}s and showing a
	 * Tooltip, containing all alive properties of the hovered {@link Artifact}.
	 * <br>
	 * <br>
	 * <b>Important note!</b> <br>
	 * This callback is intended to be used for link artifacts only, as it
	 * creates a Tooltip for certain properties which represent a link</b>
	 */
	public static final Callback<ListView<Artifact>, ListCell<Artifact>> listLinkCallBack = new Callback<ListView<Artifact>, ListCell<Artifact>>() {
		@Override
		public ListCell<Artifact> call(ListView<Artifact> param) {
			return new ListCell<Artifact>() {
				@Override
				protected void updateItem(Artifact item, boolean empty) {
					super.updateItem(item, empty);
					if (item != null) {
						StringBuilder sb = new StringBuilder();
						setText((String) item.getPropertyValueOrNull(MMMTypeProperties.NAME));

						sb.append("Type:\t" + item.getType().getPropertyValue(MMMTypeProperties.NAME) + "\n");
						for (Property p : item.getAliveProperties()) {
							if (p.getName().equals(Constants.LINK_SOURCE)) {
								sb.append("Source:\t"
										+ ((Artifact) p.getValue()).getPropertyValue(MMMTypeProperties.NAME).toString()
										+ "\n");
							} else if (p.getName().equals(Constants.LINK_TARGET)) {
								Object value = p.getValue();
								if (value instanceof RestCollectionArtifact) {
									((RestCollectionArtifact) value).getElements().stream().map(a -> (Artifact) a)
											.forEach(a -> sb.append("Target:\t"
													+ a.getPropertyValue(MMMTypeProperties.NAME).toString() + "\n"));
								} else {
									sb.append("Target:\t" + ((Artifact) p.getValue())
											.getPropertyValue(MMMTypeProperties.NAME).toString() + "\n");
								}
							} else if (p.getName().equals(Constants.LINK_DESCRIPTION)) {
								sb.append("Description:\t" + ((Artifact) p.getValue())
										.getPropertyValue(Constants.DESC_DESCRIPTION).toString() + "\n");
							}
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

	/**
	 * This {@link StringConverter} creates the displayed text for
	 * {@link Package}s inside the {@link ListView}. To make packages more
	 * distinguishable for the user, additionally to the package name, its 3
	 * parents will be prepended to it.
	 */
	public static final StringConverter<Package> packageStringConverter = new StringConverter<Package>() {

		@Override
		public String toString(Package object) {
			int parentLevel = 0;
			StringBuilder pathName = new StringBuilder("");
			while (parentLevel <= 3 && object != null) {
				pathName.insert(0, (String) object.getPropertyValue(DataStorage.PROPERTY_NAME));
				object = object.getPackage();
				if (object != null && parentLevel < 3) {
					pathName.insert(0, "\\");
				}
				parentLevel++;
			}
			return pathName.toString();
		}

		@Override
		public Package fromString(String string) {
			return null;
		}
	};

	public static final StringConverter<Artifact> artifactLinkComplexTypeStringConverter = new StringConverter<Artifact>() {

		@Override
		public String toString(Artifact object) {
			return (String) object.getPropertyValue(MMMTypeProperties.NAME);
		}

		@Override
		public Artifact fromString(String string) {
			return null;
		}
	};


	public static final StringConverter<Link> linkComplexTypeStringConverter = new StringConverter<Link>() {

		@Override
		public String toString(Link object) {
			return object.getName();
		}

		@Override
		public Link fromString(String string) {
			return null;
		}
	};
	
	public static ObservableList<Artifact> createSortedComboBoxArtifactList(List<Artifact> elements) {

		ObservableList<Artifact> sortedList = FXCollections.observableArrayList(elements);
		sortedList.sort(new Comparator<Artifact>() {
			@Override
			public int compare(Artifact a, Artifact b) {
				String n1 = (String) a.getPropertyValueOrNull(MMMTypeProperties.NAME);
				String n2 = (String) b.getPropertyValueOrNull(MMMTypeProperties.NAME);

				return n1.compareTo(n2);
			}
		});

		return sortedList;
	}

	public static ObservableList<Link> createSortedComboBoxLinkList(List<Link> elements, Link multipleLinks) {

		ObservableList<Link> sortedList = FXCollections.observableArrayList(elements);
		sortedList.add(multipleLinks);
		sortedList.sort(new Comparator<Link>() {
			@Override
			public int compare(Link a, Link b) {
				if (a.isMultipleLink()) {
					return -1;
				} else if (b.isMultipleLink()) {
					return 1;
				} else {
					return a.getName().compareTo(b.getName());
				}
			}
		});

		return sortedList;
	}

	/**
	 * Creates a {@link ComboBox} used to display {@link Artifact}s by their
	 * property name.
	 *
	 * @param elements
	 *            the elements to be displayed in the comboBox
	 * @return the newly created comboBox
	 */
	public static ComboBox<Artifact> createComboBox(List<Artifact> elements) {
		ObservableList<Artifact> options = FXCollections.observableArrayList(elements);

		ComboBox<Artifact> comboBox = new ComboBox<>(options);
		comboBox.setEditable(false);

		comboBox.setCellFactory(new Callback<ListView<Artifact>, ListCell<Artifact>>() {
			@SuppressWarnings("unchecked")
			@Override
			public ListCell<Artifact> call(ListView<Artifact> p) {
				@SuppressWarnings("rawtypes")
				ListCell cell = new ListCell<Artifact>() {
					@Override
					protected void updateItem(Artifact item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText("");
						} else {
							setText(item.getPropertyValueOrNull(MMMTypeProperties.NAME) + "");
						}
					}
				};
				return cell;
			}
		});

		comboBox.setButtonCell(new ListCell<Artifact>() {
			@Override
			protected void updateItem(Artifact item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText("");
				} else {
					setText(item.getPropertyValueOrNull(MMMTypeProperties.NAME) + "");
				}
			}
		});
		return comboBox;
	}

	public static ComboBox<Link> createLinkComboBox(List<Link> elements) {
		ObservableList<Link> options = FXCollections.observableArrayList(elements);

		ComboBox<Link> comboBox = new ComboBox<>(options);
		comboBox.setEditable(false);

		comboBox.setCellFactory(new Callback<ListView<Link>, ListCell<Link>>() {
			@SuppressWarnings("unchecked")
			@Override
			public ListCell<Link> call(ListView<Link> p) {
				@SuppressWarnings("rawtypes")
				ListCell cell = new ListCell<Link>() {
					@Override
					protected void updateItem(Link item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setText("");
						} else {
							setText(item.getName());
						}
					}
				};
				return cell;
			}
		});

		comboBox.setButtonCell(new ListCell<Link>() {
			@Override
			protected void updateItem(Link item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText("");
				} else {
					setText(item.getName());
				}
			}
		});
		return comboBox;
	}

	/**
	 * Creates a {@link ComboBox} used to display {@link Artifact}s by their
	 * property name.
	 *
	 * @param elements
	 *            the elements to be displayed in the comboBox
	 * @return the newly created comboBox
	 */
	public static CheckComboBox<Artifact> createCheckComboBox(List<Artifact> elements) {
		ObservableList<Artifact> options = FXCollections.observableArrayList(elements);

		CheckComboBox<Artifact> checkComboBox = new CheckComboBox<>(options);
		checkComboBox.setConverter(new StringConverter<Artifact>() {

			@Override
			public String toString(Artifact object) {
				// TODO Auto-generated method stub
				return (object.getPropertyValueOrNull(MMMTypeProperties.NAME).toString());
			}

			@Override
			public Artifact fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		return checkComboBox;

	}

}