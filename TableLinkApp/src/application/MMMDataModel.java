package application;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.mmm.MMMTypeProperties;
import at.jku.sea.cloud.mmm.MMMTypeUtils;
import at.jku.sea.cloud.mmm.MMMTypesFactory;
import at.jku.sea.cloud.rest.client.RestArtifact;
import init.Constants;
import init.MMMHelper;
import init.setup.Link;

import java.util.*;
import java.util.stream.Collectors;

import application.setup.ModelListener;

public class MMMDataModel {
	private final MMMHelper helper;

	private final Artifact linkType;
	private Package linkPackage;
	private final List<Link> linkInstances = new ArrayList<>();
	private final List<Link> definedLinks = new ArrayList<>();

	// key = setComplexType, value = set of instances of corresponding
	// setComplexType
	private final Map<Artifact, Set<Artifact>> dataMap = new HashMap<>();

	// list of views
	private final List<ModelListener> listeners = new ArrayList<>();

	private final Set<Package> dataPackages = new HashSet<>();

	public MMMDataModel(MMMHelper helper, String... packageIDs) {
		this.helper = helper;
		linkType = helper.getLinkType();

		refreshDefinedLinks();
		refreshLinkInstances();
	}

	private synchronized boolean refreshDefinedLinks() {
		if (linkPackage == null)
			return false;

		Set<Link> toCompare = new TreeSet<>(definedLinks);
		definedLinks.clear();

		linkPackage.getArtifacts().stream().filter(link -> hasSuperTypeLinkType(link)).forEach(linkType -> {
			String linkName = (String) linkType.getPropertyValue(MMMTypeProperties.NAME);
			Artifact sourceComplexType = null;
			Artifact targetComplexType = null;
			Artifact descriptionInstance = null;
			Collection<?> elements = ((CollectionArtifact) linkType
					.getPropertyValue(MMMTypeProperties.COMPLEXTYPE_ALLFEATURES)).getElements();
			for (Object a : elements) {
				RestArtifact feature = (RestArtifact) a;
				if (feature.getPropertyValue(MMMTypeProperties.NAME).equals(Constants.LINK_SOURCE)) {
					sourceComplexType = (Artifact) (feature.getPropertyValue(MMMTypeProperties.ELEMENT_TYPE));
				}
				if (feature.getPropertyValue(MMMTypeProperties.NAME).equals(Constants.LINK_TARGET)) {
					targetComplexType = (Artifact) (feature.getPropertyValue(MMMTypeProperties.ELEMENT_TYPE));
				}
				if (feature.getPropertyValue(MMMTypeProperties.NAME).equals(Constants.LINK_DESCRIPTION)) {
					descriptionInstance = (Artifact) feature.getPropertyValueOrNull(MMMTypeProperties.ELEMENT_TYPE);
				}
			}
			definedLinks.add(new Link(linkName, sourceComplexType, targetComplexType, descriptionInstance, linkType));
		});

		return !toCompare.equals(definedLinks);
	}

	private synchronized boolean refreshLinkInstances() {
		if (linkPackage == null)
			return false;

		// prior to update, store size for later comparison
		int oldSize = linkInstances.size();
		linkInstances.clear();

		linkPackage.getArtifacts().stream().forEach(linkInstance -> {
			if (linkInstance.getType().hasProperty(MMMTypeProperties.ALLSUPER_TYPES)) {
				if (MMMTypeUtils.isOfType(helper.getLinkType(), linkInstance)) {
					Artifact sourceInstance = (Artifact) linkInstance.getPropertyValue(Constants.LINK_SOURCE);
					Artifact targetInstance = (Artifact) linkInstance.getPropertyValue(Constants.LINK_TARGET);
					// TODO: switch linkInstance to LinkInstance variable instead of complexType() OTHER CONSTRUCTOR
					linkInstances.add(new Link(null, sourceInstance, targetInstance, null, linkInstance));
				}
			}
		});

		return oldSize != linkInstances.size();
	}

	private void refreshDataMap() {
		dataMap.clear();

		// 1. get all possible artifacts
		List<Artifact> allArtifacts = new ArrayList<>();
		for (Package p : dataPackages) {
			allArtifacts.addAll(p.getArtifacts()); // add artifacts from "root"
													// package
			allArtifacts.addAll(getAllSubArtifacts(p)); // add all artifacts
														// from the subpackages
		}

		// 2. create map of complexTypes with its instances
		allArtifacts.stream().filter(a -> a.getType().equals(helper.getArtifactType())).forEach(complexType -> {
			Set<Artifact> set = allArtifacts.stream().filter(instance -> instance.getType().equals(complexType))
					.collect(Collectors.toSet());
			dataMap.put(complexType, set);

		});
	}

	private Collection<Artifact> getAllSubArtifacts(Package p) {
		if (p != null && p.getPackage() != null && p.getPackages().isEmpty()) {
			return p.getArtifacts();
		}

		Collection<Artifact> subArtifacts = new ArrayList<>();
		for (Package subPackage : p.getPackages()) {
			subArtifacts.addAll(getAllSubArtifacts(subPackage));
		}

		return subArtifacts;
	}

	/**
	 * @return a set defined links
	 */
	public synchronized List<Link> getDefinedLinks() {
		return definedLinks;
	}

	/**
	 * @return the source data map
	 */
	public Map<Artifact, Set<Artifact>> getDataMap() {
		return Collections.unmodifiableMap(dataMap);
	}

	@SuppressWarnings("unchecked")
	private boolean hasSuperTypeLinkType(Artifact link) {
		if (link.hasProperty(MMMTypeProperties.ALLSUPER_TYPES)) {
			final Collection<Artifact> superTypes = (Collection<Artifact>) ((CollectionArtifact) link
					.getPropertyValue(MMMTypeProperties.ALLSUPER_TYPES)).getElements();
			return superTypes.contains(linkType);
		}
		return false;
	}

	/**
	 * * Creates a new complex type link and adds it to the model's cached list
	 * of link definitions
	 *
	 * @param linkName
	 * @param source
	 * @param target
	 * @param description
	 * @return
	 */
	public synchronized boolean addDefinedLink(String linkName, Artifact source, Artifact target, String description) {
		Link newLink = new Link(linkName, source, target);

		if (!definedLinks.contains(newLink)) {
			Map<String, Artifact> features = new HashMap<>();
			// description instance
			Map<String, Object> instanceValues = new HashMap<>();
			instanceValues.put(Constants.DESC_DESCRIPTION, description);
			Artifact descriptionInstance = MMMHelper.createComplexTypeInstance(helper.getWorkspace(), linkPackage,
					"DescriptionInstance", helper.getDescriptionType(), instanceValues);

			features.put(Constants.LINK_DESCRIPTION, descriptionInstance);
			features.put(Constants.LINK_SOURCE, source);
			Artifact linkComplexType = MMMHelper.createComplexType(helper.getWorkspace(), linkPackage, linkName,
					features);
			// set isMany property of target
			Artifact targetFeature = MMMTypesFactory.createFeature(helper.getWorkspace(), Constants.LINK_TARGET, target,
					false, false, false);
			MMMTypesFactory.addFeatureToComplexType(helper.getWorkspace(), linkComplexType, targetFeature);

			MMMTypesFactory.addSuperTypeToComplexType(helper.getWorkspace(), linkComplexType, linkType);

			newLink.setDescription(descriptionInstance);
			newLink.setComplexType(linkComplexType);
			definedLinks.add(newLink);

			helper.commit();

			fireLinkContentChanged();
			return true;
		}
		return false;
	}

	/**
	 * Creates a new link instances in the design space and adds this instance
	 * to the model's cached list of link instances
	 *
	 * @param linkName
	 *            the name of the link instance
	 * @param link
	 *            the super type of the link instance
	 * @param sourceInstance
	 *            the source artifact
	 * @param sourceTarget
	 *            the target artifact
	 * @return the created link instance
	 */
	public synchronized Link addLinkInstance(String linkName, Link link, Artifact sourceInstance,
			Artifact sourceTarget) {
		Artifact createdLinkInstance = helper.createLinkInstance(linkName, link, sourceInstance, sourceTarget,
				linkPackage);
		helper.commit();

		// update cached list
		Link l = new Link(linkName, sourceInstance, sourceTarget, null, createdLinkInstance);
		linkInstances.add(l);
		fireLinkContentChanged();

		return l;
	}

	/**
	 * @param linkInstance
	 */
	public synchronized void removeLinkInstance(String name, Artifact source, Artifact target, Artifact linkInstance) {
		linkInstances.remove(new Link(name, source, target, null, linkInstance));
		linkInstance.delete(helper.getWorkspace());
		helper.commit();
		fireLinkContentChanged();
	}

	public synchronized void removeLinkInstance(Link linkInstance) {
		linkInstances.remove(linkInstance);
		// TODO switch link.getInstance() instead of link.getComplexType()
		linkInstance.getComplexType().delete(helper.getWorkspace());
		helper.commit();
		fireLinkContentChanged();
	}
	/**
	 * Overwrites the previously used data packages and refreshes the dataMap
	 *
	 * @param packages
	 *            the data packages to set, used for the dataMap refresh
	 */
	public synchronized void setDataPackages(List<Package> packages) {
		dataPackages.clear();
		dataPackages.addAll(packages);
		refreshDataMap();
		fireDataPackagesChanged();
	}

	/**
	 * @return the selected data packages
	 */
	public Set<Package> getDataPackages() {
		return Collections.unmodifiableSet(dataPackages);
	}

	/**
	 * Sets the link package and refreshes the defined links and link instances
	 *
	 * @param linkPackage
	 *            the link package to set
	 */
	public synchronized void setLinkPackage(Package linkPackage) {
		if (linkPackage == null || linkPackage.equals(this.linkPackage))
			return;
		this.linkPackage = linkPackage;

		refreshDefinedLinks();
		refreshLinkInstances();
		fireLinkPackageChanged();
	}

	/**
	 * Removes the link from the model's list and sets the alive flag of the
	 * corresponding design space complex type to false which is equivalent to
	 * deleting it.
	 *
	 * @param link
	 *            the link which should be removed/deleted
	 */
	public synchronized void removeDefinedLink(Link link) {
		definedLinks.remove(link);
		link.getComplexType().delete(helper.getWorkspace());
		helper.commit();
		fireLinkContentChanged();
	}

	/**
	 * @return a list of link instances based on given <b>source</b> and
	 *         <b>target</b> instance and defined link.
	 */	
	public Link getLinkInstance(Artifact source, Artifact target, Artifact link) {
		return linkInstances.stream().filter(curInstance -> {
			// curInstance.getComplexType = Artifact link itself!!!
			// TODO switch curInstance.getInstance() instead of curInstance.getComplexType()
			if ((link == null || curInstance.getComplexType().getType().equals(link))
					&& curInstance.getSource().equals(source) && curInstance.getTarget().equals(target)) {
				return true;
			} else {
				return false;
			}
		}).findFirst().orElse(null);
	}

	/**
	 * @return a list of defined links based on given <b>source</b> and
	 *         <b>target</b>
	 */
	public List<Link> getDefinedLinks(Artifact source, Artifact target) {
		return definedLinks.stream().filter(curDefinedLink -> {
			if (curDefinedLink.getSource().equals(source) && curDefinedLink.getTarget().equals(target)) {
				return true;
			} else {
				return false;
			}
		}).collect(Collectors.toList());
	}

	/**
	 * @return a list of link instances based on given <b>source</b>,
	 *         <b>target</b> and defined links
	 */	
	public List<Link> getLinkInstances(Artifact source, Artifact target, List<Link> listOfDefinedLinks) {
		List<Artifact> list = listOfDefinedLinks.stream().map(link -> link.getComplexType())
				.collect(Collectors.toList());
		return linkInstances.stream().filter(curInstance -> {
			// TODO switch curInstance.getInstance() instead of curInstance.getComplexType()
			if (list.contains(curInstance.getComplexType().getType()) && curInstance.getSource().equals(source)
					&& curInstance.getTarget().equals(target)) {
				return true;
			} else {
				return false;
			}
		}).collect(Collectors.toList());
	}

	/**
	 * @return a list of link instances
	 */
	public synchronized List<Link> getLinkInstances() {
		return Collections.unmodifiableList(linkInstances);
	}

	/**
	 * @return the link Package
	 */
	public synchronized Package getLinkPackage() {
		return linkPackage;
	}

	/**
	 * @param modelListener
	 *            the modelListener to be added
	 */
	public void addListener(ModelListener modelListener) {
		listeners.add(modelListener);
	}

	/**
	 * @param modelListener
	 *            the modelListener to be removed
	 */
	public void removeListener(ModelListener modelListener) {
		listeners.remove(modelListener);
	}

	private void fireDataPackagesChanged() {
		listeners.forEach(ModelListener::dataPackagesChanged);
	}

	private void fireLinkPackageChanged() {
		listeners.forEach(ModelListener::linkPackageChanged);
	}

	private void fireLinkContentChanged() {
		listeners.forEach(ModelListener::linkContentChanged);
	}
}