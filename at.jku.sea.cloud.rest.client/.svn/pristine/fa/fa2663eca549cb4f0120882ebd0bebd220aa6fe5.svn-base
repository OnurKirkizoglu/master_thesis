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

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestPackage {

	private static Package PACKAGE;

	private static int ARTIFACTS_SIZE = 1;

	@BeforeClass
	public static void setUpBeforeClass() {
		PACKAGE = WS.getPackage(DataStorage.AT_PACKAGE_ID);
	}

	@After
	public void tearDown() {
		WS.rollbackAll();
	}

	@Test
	public void testGetArtifacts() {
		Collection<Artifact> artifacts = PACKAGE.getArtifacts();
		assertEquals(ARTIFACTS_SIZE, artifacts.size());
	}
}
