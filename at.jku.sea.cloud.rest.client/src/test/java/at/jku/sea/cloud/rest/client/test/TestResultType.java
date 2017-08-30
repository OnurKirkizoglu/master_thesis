package at.jku.sea.cloud.rest.client.test;

import static at.jku.sea.cloud.rest.client.test.TestAll.WS;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import at.jku.sea.cloud.Artifact;
import at.jku.sea.cloud.CollectionArtifact;
import at.jku.sea.cloud.MetaModel;
import at.jku.sea.cloud.Owner;
import at.jku.sea.cloud.Package;
import at.jku.sea.cloud.Project;
import at.jku.sea.cloud.Tool;

public class TestResultType {

	@After
	public void tearDown() {
		WS.rollbackAll();
	}

	@Test
	public void GetArtifactWhereTypeIsArtifact() {
		Artifact a = WS.getArtifact(150);
		assertTrue(a instanceof Artifact);
	}

	@Test
	public void GetArtifactWhereTypeIsCollectionArtifact() {
		Artifact a = WS.getArtifact(219);
		assertTrue(a instanceof CollectionArtifact);
	}

	@Test
	public void GetArtifactWhereTypeIsMetaModel() {
		Artifact a = WS.getArtifact(49);
		assertTrue(a instanceof MetaModel);
	}

	@Test
	public void GetArtifactWhereTypeIsPackage() {
		Artifact a = WS.getArtifact(15);
		assertTrue(a instanceof Package);
	}

	@Test
	public void GetArtifactWhereTypeIsProject() {
		Artifact a = WS.getArtifact(105);
		assertTrue(a instanceof Project);
	}

	@Test
	public void GetArtifactWhereTypeIsOwner() {
		Artifact a = WS.getArtifact(21);
		assertTrue(a instanceof Owner);
	}

	@Test
	public void GetArtifactWhereTypeIsTool() {
		Artifact a = WS.getArtifact(30);
		assertTrue(a instanceof Tool);
	}

}
