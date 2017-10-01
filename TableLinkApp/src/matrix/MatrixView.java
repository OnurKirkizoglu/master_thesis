package matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import application.MMMDataModel;
import application.setup.LinkListDialog;
import application.setup.Triple;
import at.jku.sea.cloud.Artifact;
import init.setup.Link;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * 
 * This view represents matrix.
 * 
 */
public class MatrixView extends StackPane {
	private MMMDataModel model;

	// Visualization of matrix
	private WebView webView;
	private WebEngine webEngine;

	// Matrix consists of MatrixElements
	private MatrixElement[][] matrix;

	// Maximum row/col size of matrix
	private int row_size;
	private int col_size;

	// Selected link
	private Link matrixLink;

	/**
	 * Constructor to created matrix view by a certain model.
	 * 
	 * @param model
	 *            Model to be represent.
	 */
	public MatrixView(MMMDataModel model) {
		this.model = model;

		webView = new WebView();
		// loading of matrix view
		String viewerDir = System.getProperty("user.dir") + "/src/matrix/matrix.html";
		viewerDir = ("file:///" + viewerDir.substring(3));
		webEngine = webView.getEngine();
		webEngine.load(viewerDir);

		getChildren().add(webView);
	}

	/**
	 * Updates matrix view by given parameters.
	 * 
	 * @param selectedSources
	 *            Selected sources.
	 * @param selectedTargets
	 *            Selected targets
	 * @param selectedLink
	 *            Selected link.
	 */
	public void update(Map<Artifact, Set<Artifact>> selectedSources, Map<Artifact, Set<Artifact>> selectedTargets,
			Link selectedLink) {

		// refreshing of matrix by given new data
		updateMatrix(selectedSources, selectedTargets, selectedLink);

		// visualize/load new matrix into screen
		webEngine.executeScript("removeMatrix()");
		JSObject window = (JSObject) webEngine.executeScript("window");
		window.setMember("matrix", this);
		webEngine.executeScript("refreshMatrix()");
	}

	/**
	 * Creation of link for selected row/col.
	 * 
	 * @param row
	 *            Row of selected element.
	 * @param col
	 *            Col of selected element.
	 */
	public void addLink(int row, int col) {
		// Replace matrix element by new element with checked link flag.
		matrix[row][col] = new MatrixElement(row, col, true, null);
	}

	public void deleteLink(int row, int col) {
		// Replace matrix element by new element with unchecked link flag and
		// existing link (if available).
		matrix[row][col] = new MatrixElement(row, col, false, matrix[row][col].getLinkInstance());
	}

	/**
	 * Saving of Links created/deleted by matrix.
	 */
	public void saveLinks() {
		if (matrixLink != null && matrixLink.isMultipleLink()) {
			saveMultipleLinks();
		} else {
			saveSingleLinks();
		}
	}

	/**
	 * Only for non-specific links. Shows via dialog the available defined links
	 * between source and target.<br>
	 * Checks defined link, if an instance already exists.
	 * 
	 * @param row
	 *            Row of the selected element.
	 * @param col
	 *            Column of the selected element.
	 */
	public void showMultipleLinks(int row, int col) {
		if (!matrix[row][col].isToBeProcessed()) {
			List<Link> linkInstances = model.getLinkInstances(matrix[row][0].getHeaderInstance(),
					matrix[0][col].getHeaderInstance(), matrix[row][col].getDefinedLinks());
			matrix[row][col].setListOfLinkInstances(linkInstances);
		}

		matrix[row][col].setToBeProcessed(true);
		new LinkListDialog(matrix[row][col]).showAndWait();
	}

	public String getStringElement(int row, int col) {
		return (matrix[row][col]).toString();
	}

	public boolean isMultipleMatrix() {
		return matrixLink.isMultipleLink();
	}

	public int getRowSize() {
		return matrix.length;
	}

	public int getColSize() {
		return matrix[0].length;
	}

	/**
	 * Creates new matrix by given parameters.
	 * 
	 * @param source
	 *            Selected sources.
	 * @param target
	 *            Selected targets.
	 * @param currentLink
	 *            Selected link.
	 */
	private void updateMatrix(Map<Artifact, Set<Artifact>> source, Map<Artifact, Set<Artifact>> target,
			Link currentLink) {
		matrixLink = currentLink;

		// prepare header: complex/instance
		List<MatrixElement> sourceList = new ArrayList<>();
		source.entrySet().stream().forEach(map -> {
			map.getValue().stream().forEach(instance -> {
				sourceList.add(new MatrixElement(map.getKey(), instance));
			});
		});

		List<MatrixElement> targetList = new ArrayList<>();
		target.entrySet().stream().forEach(map -> {
			map.getValue().stream().forEach(instance -> {
				targetList.add(new MatrixElement(map.getKey(), instance));
			});
		});

		row_size = sourceList.size() + 1;
		col_size = targetList.size() + 1;

		matrix = new MatrixElement[row_size][col_size];
		MatrixElement temp = new MatrixElement();

		for (int row = 0; row < row_size; row++) {
			for (int col = 0; col < col_size; col++) {
				if (row == 0 && col == 0) { // start
					matrix[0][0] = new MatrixElement();
				} else if (row >= 1 && col == 0) { // y-axis
					temp = sourceList.get(row - 1);
					matrix[row][col] = new MatrixElement(temp.getHeaderComplexType(), temp.getHeaderInstance());
				} else if (row == 0 && col >= 1) { // x-axis
					temp = targetList.get(col - 1);
					matrix[row][col] = new MatrixElement(temp.getHeaderComplexType(), temp.getHeaderInstance());
				} else { // data
					if (currentLink.isMultipleLink()) {
						// load only defined links
						matrix[row][col] = new MatrixElement(
								(model.getDefinedLinks(matrix[row][0].getHeaderComplexType(),
										matrix[0][col].getHeaderComplexType())),
								MatrixElementType.MULTIPLE_LINK);
					} else {
						// load instance
						matrix[row][col] = new MatrixElement(model.getLinkInstance((matrix[row][0]).getHeaderInstance(),
								(matrix[0][col]).getHeaderInstance(), currentLink.getComplexType()));
					}
				}
			}
		}
	}

	/**
	 * Saving process of a <b>specific</b> link.<br>
	 * Only those elements with <b>processed flag = true</b> will be processed.
	 *
	 */
	private void saveSingleLinks() {
		MatrixElement mElement;
		for (int row = 1; row < row_size; row++) {
			for (int col = 1; col < col_size; col++) {
				mElement = matrix[row][col];
				if (mElement.isToBeProcessed()) {
					if (mElement.isLinkExists()) {
						matrix[row][col] = new MatrixElement(
								model.addLinkInstance(null, matrixLink.isMultipleLink() ? null : matrixLink,
										(matrix[row][0]).getHeaderInstance(), (matrix[0][col]).getHeaderInstance()));
					} else {
						model.removeLinkInstance(null, matrix[row][0].getHeaderInstance(),
								matrix[0][col].getHeaderInstance(), matrix[row][col].getLinkInstance());
						matrix[row][col] = new MatrixElement(null);
					}
				}
			}
		}
	}

	/**
	 * Saving process of a <b>non specific</b> link. <br>Only those elements with
	 * <b>processed flag = true</b> will be processed.
	 */
	private void saveMultipleLinks() {
		MatrixElement mElement;
		for (int row = 1; row < row_size; row++) {
			for (int col = 1; col < col_size; col++) {
				mElement = matrix[row][col];
				if (mElement.isToBeProcessed()) {
					HashMap<Link, Triple> linksToBeProcessed = mElement.getLinksToBeProcessed();
					for (Link defLink : linksToBeProcessed.keySet()) {
						Triple triple = linksToBeProcessed.get(defLink);
						if (triple.isAvailable() && !triple.isToCreate()) {
							// delete link and set null instance
							model.removeLinkInstance(triple.getLink());
							triple.setLink(null);
							triple.setAvailable(false);
						} else if (!triple.isAvailable() && triple.isToCreate()) {
							// create link and store it
							triple.setLink(model.addLinkInstance(null, defLink, (matrix[row][0]).getHeaderInstance(),
									(matrix[0][col]).getHeaderInstance()));
							triple.setAvailable(true);
						}
					}
				}
			}
		}
	}
}