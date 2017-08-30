package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.exceptions.ArtifactDeadException;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestProject {

	private static Project PROJECT;

	private static int PACKAGES_SIZE = 0;
	private static int ARTIFACTS_SIZE = 0;

	@BeforeClass
	public static void setUpBeforeClass() {
		PROJECT = WS.getProject(DataStorage.TEST_PROJECT_ID);
	}

	@After
	public void tearDown() {
		WS.rollbackAll();
	}

	@Test
	public void testAddArtifact() {
		Artifact artifact = WS.createArtifact();
		int old_size = PROJECT.getArtifacts().size();
		PROJECT.addArtifact(WS, artifact);
		int new_size = PROJECT.getArtifacts().size();
		assertEquals(old_size + 1, new_size);
	}

	@Test(expected = ArtifactDeadException.class)
	public void testAddArtifactArtifactDead() {
		Artifact artifact = WS.createArtifact();
		PROJECT.delete(WS);
		PROJECT.addArtifact(WS, artifact);
	}

	@Test
	public void testRemoveArtifact() {
		Artifact artifact = WS.createArtifact();
		PROJECT.addArtifact(WS, artifact);
		int old_size = PROJECT.getArtifacts().size();
		PROJECT.removeArtifact(WS, artifact);
		int new_size = PROJECT.getArtifacts().size();
		assertEquals(old_size - 1, new_size);
	}

	@Test(expected = ArtifactDeadException.class)
	public void testRemoveArtifactArtifactDead() {
		Artifact artifact = WS.createArtifact();
		PROJECT.delete(WS);
		PROJECT.removeArtifact(WS, artifact);
	}

	@Test
	public void testGetPackages() {
		Collection<Package> packages = PROJECT.getPackages();
		assertEquals(PACKAGES_SIZE, packages.size());
	}

	@Test
	public void testGetArtifacts() {
		Collection<Artifact> artifacts = PROJECT.getArtifacts();
		assertEquals(ARTIFACTS_SIZE, artifacts.size());
	}
}
