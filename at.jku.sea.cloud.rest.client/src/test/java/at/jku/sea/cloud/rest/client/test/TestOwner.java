package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.ADMIN_PASSWORD;
import static at.jku.sea.cloud.rest.client.test.TestAll.OWNER;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestOwner {

	private static int ARTIFACTS_SIZE = 0;

	@BeforeClass
	public static void setUpBeforeClass() {

	}

	@After
	public void tearDown() {
		WS.rollbackAll();
	}

	@Test
	public void testGetArtifacts() {
		Collection<Artifact> artifacts = OWNER.getArtifacts();
		assertEquals(ARTIFACTS_SIZE, artifacts.size());
	}
}
