package matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import application.MMMDataModel;
import application.setup.LinkListDialog;
import application.setup.ModelListener;
import application.setup.Triple;
import at.jku.sea.cloud.Artifact;
import init.setup.Link;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class MatrixView extends StackPane implements ModelListener {
	private MMMDataModel model;
	private WebView webView;
	private WebEngine webEngine;
	private MatrixElement[][] matrix;
	private int col_size;
	private int row_size;
	private Link matrixLink;

	public MatrixView(MMMDataModel model) {
		this.model = model;

		webView = new WebView();
		String viewerDir = System.getProperty("user.dir") + "/src/matrix/index.html";
		viewerDir = ("file:///" + viewerDir.substring(3));
		webEngine = webView.getEngine();
		webEngine.load(viewerDir);

		getChildren().add(webView);
	}

	public void update(Map<Artifact, Set<Artifact>> selectedSources, Map<Artifact, Set<Artifact>> selectedTargets,
			Link selectedLink) {

		updateMatrix(selectedSources, selectedTargets, selectedLink);

		webEngine.executeScript("removeMatrix()");
		JSObject window = (JSObject) webEngine.executeScript("window");
		window.setMember("matrix", this);
		window.setMember("java", new JavaOutput());
		webEngine.executeScript("refreshMatrix()");
	}

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
					if (currentLink.isMultipleLink()) { // multiple links possible
						matrix[row][col] = new MatrixElement(
								(model.getDefinedLinks(matrix[row][0].getHeaderComplexType(),
										matrix[0][col].getHeaderComplexType())),
								MatrixElementType.MULTIPLE_LINK);
					} else {
						matrix[row][col] = new MatrixElement(model.getLinkInstance((matrix[row][0]).getHeaderInstance(),
								(matrix[0][col]).getHeaderInstance(), currentLink.getComplexType()));
					}
				}
			}
		}

	}

	public void addLink(int row, int col) {
		// source: row/0 target: 0/col
		matrix[row][col] = new MatrixElement(row, col, true, null);
	}

	public void deleteLink(int row, int col) {
		matrix[row][col] = new MatrixElement(row, col, false, matrix[row][col].getLinkInstance());
	}

	public String getStringElement(int row, int col) {
		return (matrix[row][col]).toString();
	}

	public int getRowSize() {
		return matrix.length;
	}

	public int getColSize() {
		return matrix[0].length;
	}

	public void saveLinks(){
		if(matrixLink != null && matrixLink.isMultipleLink()){
			saveMultipleLinks();
		}else{
			saveSingleLinks();
		}
	}
	public void saveSingleLinks() {
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

	public void saveMultipleLinks() {
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

	public void showMultipleLinks(int row, int col) {
		if (!matrix[row][col].isToBeProcessed()) {
			List<Link> linkInstances = model.getLinkInstances(matrix[row][0].getHeaderInstance(),
					matrix[0][col].getHeaderInstance(), matrix[row][col].getDefinedLinks());
			matrix[row][col].setListOfLinkInstances(linkInstances);
		}

		matrix[row][col].setToBeProcessed(true);
		new LinkListDialog(matrix[row][col]).showAndWait();

	}

	public boolean isMultipleMatrix() {
		return matrixLink.isMultipleLink();
	}

	public class JavaOutput {

		public void println(String output) {
			System.out.println(output);
		}
	}

	@Override
	public void dataPackagesChanged() {

	}

	@Override
	public void dataContentChanged() {

	}

	@Override
	public void linkPackageChanged() {

	}

	@Override
	public void linkContentChanged() {

	}
}