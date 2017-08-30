package application;

import java.util.ArrayList;
import java.util.Collection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.rest.client.RestCloud;
import at.jku.sea.cloud.Package;

public class TreeViewFactory {

	
	public TreeView<ArtifactTreeItem> getTreeView(Cloud c, boolean showNames) {
		
		ArtifactTreeItem rootArtItem = new ArtifactTreeItem(null, "Design Space");
		TreeItem<ArtifactTreeItem> rootItem = new TreeItem<ArtifactTreeItem>(rootArtItem);
		rootItem.setExpanded(true);
		
		Collection<Artifact> artifacts = c.getArtifacts(c.getHeadVersionNumber());
		
		ArrayList<Artifact> rootArtifacts = new ArrayList<Artifact>();
		
		for(Artifact a : artifacts)
		{
			if(a.getPackage()==null)
			{
				rootArtifacts.add(a);
			}
		}
		
		for(Artifact a : rootArtifacts)
		{
			ArrayList<Long> idChain = new ArrayList<Long>();
			iterateSubtree(a, rootItem, idChain, showNames);
		}
		TreeView<ArtifactTreeItem> tree = new TreeView<ArtifactTreeItem> (rootItem);   
		
		return tree;
	}	
	
	void iterateSubtree(Artifact parent, TreeItem<ArtifactTreeItem> parentTreeItem, ArrayList<Long> idChain, boolean showNames)
	{
		if(idChain.contains(parent.getId()))
		{
			return;
		}
		idChain.add(parent.getId());
		
		
		ArtifactTreeItem newArtifactTreeItem = new ArtifactTreeItem(parent, showNames);
		TreeItem<ArtifactTreeItem> newTreeItem = new TreeItem<ArtifactTreeItem>(newArtifactTreeItem);
		newTreeItem.setExpanded(true);
		
		parentTreeItem.getChildren().add(newTreeItem);
		setTreeItemGraphics(parent, newTreeItem);
		
		if(parent instanceof Project)
		{
			Collection<Package> packages = ((Project) parent).getPackages();
			for(Package p : packages)
			{
				iterateSubtree(p, newTreeItem, idChain, showNames);
			}
		}else if(parent instanceof Package)
		{
			Collection<Package> packages = ((Package) parent).getPackages();
			for(Package p : packages)
			{
				iterateSubtree(p, newTreeItem, idChain, showNames);
			}
			
			addPackageArtifacts(newTreeItem, ((Package) parent), showNames);
		}
	}

	private void addPackageArtifacts(TreeItem<ArtifactTreeItem> parentTreeItem, Package pkg, boolean showNames) {
		for(Artifact a : pkg.getArtifacts())
		{
			if(!(a instanceof Package))
			{
				ArtifactTreeItem artifactTreeItem = new ArtifactTreeItem(a, showNames);
				TreeItem<ArtifactTreeItem> treeItem = new TreeItem<ArtifactTreeItem>(artifactTreeItem);
				parentTreeItem.getChildren().add(treeItem);
				setTreeItemGraphics(a, treeItem);
				
			}
		}		
	}

	private void setTreeItemGraphics(Artifact a, TreeItem<ArtifactTreeItem> newTreeItem)
	{
		if(a instanceof Package)
		{
			newTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("package-icon.png"))));		
		}else{			
			if(a instanceof Project)
			{
				newTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("blue-folder-horizontal-icon.png"))));					
			}else {
				if(a instanceof CollectionArtifact)
				{
					newTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("bricks-icon.png"))));	
				}else{
					newTreeItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("brick-icon.png"))));					
				}
			}
		}
		
		
	}
	
	
	

}
