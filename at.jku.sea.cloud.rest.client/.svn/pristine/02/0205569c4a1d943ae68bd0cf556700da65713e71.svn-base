package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.TOOL;
import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.DataStorage;
import at.jku.sea.cloud.rest.client.RestCloud;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
public class TestTool {

	private static int ARTIFACTS_SIZE_ECLIPSE = 2;
	private static int ARTIFACTS_SIZE_ROOT = 36;

	@BeforeClass
	public static void setUpBeforeClass() {

	}

	@After
	public void tearDown() {
		WS.rollbackAll();
	}

	@Test
	public void testGetArtifacts() {
		Collection<Artifact> artifacts = TOOL.getArtifacts();
		assertEquals(ARTIFACTS_SIZE_ECLIPSE, artifacts.size());

		artifacts = RestCloud.getInstance().getTool(DataStorage.ROOT_TOOL_ID).getArtifacts();
		assertEquals(ARTIFACTS_SIZE_ROOT, artifacts.size());
	}
}
