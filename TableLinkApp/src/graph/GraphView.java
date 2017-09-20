package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import application.MMMDataModel;
import application.setup.LinkListDialog;
import application.setup.Triple;
import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import init.setup.Link;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GraphView extends Canvas {

	private MMMDataModel model;
	private double dragX = 0;
	private double dragY = 0;

	private ArtifactShape selected;

	private Link graphLink;
	private List<ArtifactShape> sourceList = new ArrayList<ArtifactShape>();
	private List<ArtifactShape> targetList = new ArrayList<ArtifactShape>();
	private List<LinkShape> linkList = new ArrayList<LinkShape>();

	private final int RECT_TEXT_CHAR_WIDTH = 7;
	private final int RECT_HEIGHT = 51;
	private Control control;
	private ImageCursor cursorDeleteLink;

	public GraphView(MMMDataModel model) {
		this.model = model;

		this.setOnMouseReleased(onMouseReleasedEventHandler);
		this.setOnMouseClicked(onMouseClickedEventHandler);
		this.setOnMousePressed(onMousePressedEventHandler);
		this.setOnMouseDragged(onMouseDraggedEventHandler);

		setGraphController(Control.MOVE);
		cursorDeleteLink = new ImageCursor(new Image("file:res/delete.png"));
	}

	public void setGraphController(Control controller) {
		this.control = controller;
		if (control == Control.CREATE_LINK) {
			this.setCursor(Cursor.DEFAULT);
		} else if (control == Control.DELETE_LINK) {
			this.setCursor(cursorDeleteLink);
		} else {
			this.setCursor(Cursor.MOVE);
		}
	}

	public void repaint() {
		repaint(sourceList, targetList, linkList, graphLink);
	}

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			if (control == Control.MOVE) {
				selected = null;
				repaint();
			}
		}
	};

	EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			double sceneX = t.getX();
			double sceneY = t.getY();
			if (control == Control.CREATE_LINK || control == Control.DELETE_LINK) {
				for (ArtifactShape rect : sourceList) {
					if (isMouseInShape(sceneX, sceneY, rect)) {
						selected = rect;
						repaint();
						return;
					}
				}
				;
				for (ArtifactShape target : targetList) {
					if (selected != null && isMouseInShape(sceneX, sceneY, target)) {
						if (graphLink.isMultipleLink()) {
							showMultipleLinks(selected, target);
						} else {
							if (control == Control.CREATE_LINK) {
								addSingleLink(selected, target);
							} else { // delete link
								deleteSingleLink(selected, target);
							}
						}
						selected = null;
						repaint();
						return;
					}
				}
			}
		}
	};

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			if (control == Control.MOVE) {
				double sceneX = t.getX();
				double sceneY = t.getY();
				sourceList.stream().forEach(rect -> {
					if (isMouseInShape(sceneX, sceneY, rect)) {
						selected = rect;
						dragX = sceneX - rect.getX();
						dragY = sceneY - rect.getY();
						repaint();
						return;
					}
				});
				if (selected == null) {
					targetList.stream().forEach(rect -> {
						if (isMouseInShape(sceneX, sceneY, rect)) {
							selected = rect;
							dragX = sceneX - rect.getX();
							dragY = sceneY - rect.getY();
							repaint();
							return;
						}
					});
				}
			}
		}
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			if (control == Control.MOVE) {
				double sceneX = t.getX();
				double sceneY = t.getY();
				if (selected != null) {
					double newX = sceneX - dragX;
					double newY = sceneY - dragY;
					selected.setX(newX);
					selected.setY(newY);
					repaint();
				}
			}
		}
	};

	public void update(Map<Artifact, Set<Artifact>> selectedSources, Map<Artifact, Set<Artifact>> selectedTargets,
			Link selectedLink) {
		this.graphLink = selectedLink;
		sourceList.clear();
		targetList.clear();
		linkList.clear();

		double sourcePosX = 10;
		double sourcePosY = 10;
		double targetPosX = 700;
		double targetPosY = 10;
		ArtifactShape sourceShape = null;
		ArtifactShape targetShape = null;

		for (Entry<Artifact, Set<Artifact>> sourceComplexType : selectedSources.entrySet()) {
			for (Artifact sourceInstance : sourceComplexType.getValue()) {
				sourceShape = addArtifactToShapeList(sourceList, "S: ", sourceComplexType, sourceInstance, sourcePosX,
						sourcePosY);
				sourcePosX += sourceShape.getWidth() + 10;
				if (sourcePosX > 600) {
					sourcePosX = 10;
					sourcePosY += RECT_HEIGHT + 10;
				}
			}
		}
		for (Entry<Artifact, Set<Artifact>> targetComplexType : selectedTargets.entrySet()) {
			for (Artifact targetInstance : targetComplexType.getValue()) {
				targetShape = addArtifactToShapeList(targetList, "T: ", targetComplexType, targetInstance, targetPosX,
						targetPosY);
				targetPosX += targetShape.getWidth() + 10;
				if (targetPosX > 1000) {
					targetPosX = 710;
					targetPosY += RECT_HEIGHT + 10;
				}
			}
		}

		// searching for available defined Links
		for (ArtifactShape source : sourceList) {
			for (ArtifactShape target : targetList) {
				if (graphLink.isMultipleLink()) {
					linkList.add(new LinkShape(source, target,
							model.getDefinedLinks(source.getComplexType(), target.getComplexType())));
				} else {
					Link linkInstance = model.getLinkInstance(source.getInstance(), target.getInstance(),
							graphLink.getComplexType());
					if (linkInstance != null) {
						linkList.add(new LinkShape(source, target, graphLink.getComplexType(),
								linkInstance.getComplexType()));
					}
				}
			}
		}
		repaint(sourceList, targetList, linkList, graphLink);
	}

	private void showMultipleLinks(ArtifactShape sourceShape, ArtifactShape targetShape) {
		LinkShape searchedLink = new LinkShape();
		for (LinkShape link : linkList) {
			if (link.getSource().equals(sourceShape)
					&& link.getTarget().equals(targetShape)) {
				List<Link> linkInstances = model.getLinkInstances(sourceShape.getInstance(), targetShape.getInstance(),
						link.getListOfDefinedLinks());
				link.setListOfInstanceLinks(linkInstances);
				searchedLink = link;
			}
		}
		;
		searchedLink.setToBeProcessed(true);
		new LinkListDialog(searchedLink).showAndWait();
	}

	private void addSingleLink(ArtifactShape source, ArtifactShape target) {
		LinkShape existingLinkShape = null;
		for (LinkShape linkShape : linkList) {
			if (linkShape.getSource().equals(source) && linkShape.getTarget().equals(target)
					&& linkShape.getComplexType().equals(graphLink.getComplexType())) {
				existingLinkShape = linkShape;
				break;
			}
		}

		if (existingLinkShape == null) {
			existingLinkShape = new LinkShape(source, target, graphLink.getComplexType(), null);
			existingLinkShape.setToBeProcessed(true);
			existingLinkShape.setLinkShouldBeCreated(true);
			existingLinkShape.setLinkShouldBeDeleted(false);
			linkList.add(existingLinkShape);
		} else {
			existingLinkShape.setToBeProcessed(false);
			existingLinkShape.setLinkShouldBeCreated(false);
			existingLinkShape.setLinkShouldBeDeleted(false);
		}
	}

	private void deleteSingleLink(ArtifactShape source, ArtifactShape target) {
		LinkShape existingLinkShape = null;
		for (LinkShape linkShape : linkList) {
			if (linkShape.getSource().equals(source) && linkShape.getTarget().equals(target)
					&& linkShape.getComplexType().equals(graphLink.getComplexType())) {
				existingLinkShape = linkShape;
				break;
			}
		}

		if (existingLinkShape != null) {
			existingLinkShape.setToBeProcessed(true);
			existingLinkShape.setLinkShouldBeDeleted(true);
			existingLinkShape.setLinkShouldBeCreated(false);
		}
	}

	private void repaint(List<ArtifactShape> sourceList, List<ArtifactShape> targetList, List<LinkShape> linkList,
			Link graphLink) {
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.clearRect(0, 0, 4000, 4000);

		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, 4000, 4000);

		linkList.stream().forEach(link -> {
			if (!link.isLinkShouldBeDeleted()) {
				if (graphLink.isMultipleLink()) {
					if (link.getListOfDefinedLinks().size() > 0) {
						paintLink(gc, link.getSource(), link.getTarget(),
								"Def. Links:" + link.getListOfDefinedLinks().size());
					}
				} else {
					paintLink(gc, link.getSource(), link.getTarget(), graphLink.getName());
				}
			}
		});

		sourceList.stream().forEach(elem -> paintArtifact(gc, elem, Color.RED, selected == elem));
		targetList.stream().forEach(elem -> paintArtifact(gc, elem, Color.GREY, selected == elem));
	}

	private void paintLink(GraphicsContext gc, ArtifactShape source, ArtifactShape target, String text) {
		gc.setStroke(Color.BLACK);
		double x1 = source.getX();
		double y1 = source.getY();
		double x2 = target.getX();
		double y2 = target.getY();

		paintLinkText(gc, x1, y1, x2, y2, text);
		gc.strokeLine(x1 + source.getWidth() / 2, y1 + source.getHeight() / 2, x2 + target.getWidth() / 2,
				y2 + target.getHeight() / 2);
	}

	private void paintLinkText(GraphicsContext gc, double x1, double y1, double x2, double y2, String text) {
		double x = x1 < x2 ? ((x2 - x1) / 2) + x1 : ((x1 - x2) / 2) + x2;
		double y = y1 < y2 ? ((y2 - y1) / 2) + y1 : ((y1 - y2) / 2) + y2;
		gc.strokeText(text, x, y);
	}

	private void paintArtifact(GraphicsContext gc, ArtifactShape elem, Color fill, boolean isSelected) {
		if (isSelected) {
			gc.setStroke(Color.BLUE);
		} else {
			gc.setStroke(Color.WHITE);
		}
		gc.setFill(fill);
		gc.fillRect(elem.getX(), elem.getY(), elem.getWidth(), elem.getHeight());
		gc.strokeText(elem.getName(), elem.getX() + RECT_TEXT_CHAR_WIDTH, elem.getY() + RECT_HEIGHT / 2 + 5,
				elem.getWidth() - RECT_TEXT_CHAR_WIDTH * 2);
	}

	private ArtifactShape addArtifactToShapeList(List<ArtifactShape> list, String prefix,
			Entry<Artifact, Set<Artifact>> targetComplexType, Artifact targetInstance, double posX, double posY) {
		String name = prefix + (String) targetComplexType.getKey().getPropertyValueOrNull(MMMTypeProperties.NAME) + "/"
				+ (String) targetInstance.getPropertyValueOrNull(MMMTypeProperties.NAME);
		double width = name.length() * RECT_TEXT_CHAR_WIDTH;
		ArtifactShape shape = new ArtifactShape(posX, posY, width, RECT_HEIGHT, name, targetComplexType.getKey(),
				targetInstance);
		list.add(shape);
		return shape;
	}

	private boolean isMouseInShape(double sceneX, double sceneY, ArtifactShape rect) {
		return (rect.getX() < sceneX && sceneX < rect.getX() + rect.getWidth() && rect.getY() < sceneY
				&& sceneY < rect.getY() + rect.getHeight());
	}

	public void saveLinks() {
		if (graphLink.isMultipleLink()) {
			saveMultipleLinks();
		} else {
			saveSingleLinks();
		}
	}

	private void saveSingleLinks() {
		List<LinkShape> toBeDeleted = new ArrayList<>();
		for (LinkShape linkShape : linkList) {
			if (linkShape.isToBeProcessed()) {
				if (linkShape.isLinkShouldBeCreated()) {
					Link newLinkInstance = model.addLinkInstance(null, graphLink, linkShape.getSource().getInstance(),
							linkShape.getTarget().getInstance());
					linkShape.setLinkInstance(newLinkInstance.getComplexType());
				} else if (linkShape.isLinkShouldBeDeleted()) {
					model.removeLinkInstance(null, linkShape.getSource().getInstance(),
							linkShape.getTarget().getInstance(), linkShape.getLinkInstance());
					toBeDeleted.add(linkShape);
				}
				linkShape.setDefaultParameter();
			}
		}
		linkList.removeAll(toBeDeleted);
		repaint();
	}

	private void saveMultipleLinks() {
		linkList.stream().forEach(link -> {
			if (link.isToBeProcessed()) {
				HashMap<Link, Triple> linksToBeProcessed = link.getLinksToBeProcessed();
				for (Link defLink : linksToBeProcessed.keySet()) {
					Triple triple = linksToBeProcessed.get(defLink);
					if (triple.isAvailable() && !triple.isToCreate()) {
						// delete link and set null instance
						model.removeLinkInstance(triple.getLink());
						triple.setLink(null);
						triple.setAvailable(false);
					} else if (!triple.isAvailable() && triple.isToCreate()) {
						// create link and store it
						triple.setLink(model.addLinkInstance(null, defLink, link.getSource().getInstance(),
								link.getTarget().getInstance()));
						triple.setAvailable(true);
					}
				}
			}
		});
	}

}
