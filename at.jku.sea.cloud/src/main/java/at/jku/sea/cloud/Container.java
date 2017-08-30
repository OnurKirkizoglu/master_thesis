package at.jku.sea.cloud;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import at.jku.sea.cloud.exceptions.ArtifactDeadException;

/**
 * A collection of {@link Artifact}s belonging to the same containing unit (e.g., file, folder). An {@link Artifact} can only be in <b>one</b> package (Package 1:n Artifact).
 * 
 * A package is not a {@link CollectionArtifact} due to that a {@link CollectionArtifact} is version controlled collection of arbitrary objects (which coincidentally and not necessarily can be {@link Artifact}).
 */
public interface Container extends Artifact {

  public boolean containsArtifact(final Artifact artifact);

  /**
   * Adds in a {@link Workspace} an {@link Artifact} to the metamodel.
   * 
   * @param workspace
   *          the {@link Workspace} in which the {@link Artifact} should be added to the metamodel.
   * @param artifact
   *          the specified {@link Artifact} to be added.
   * @throws ArtifactDeadException
   *           if the metamodel is dead.
   */
  public void addArtifact(final Workspace workspace, final Artifact artifact) throws ArtifactDeadException;

  public void addArtifacts(final Workspace workspace, final Collection<Artifact> artifacts) throws ArtifactDeadException;

  /**
   * Removes in a {@link Workspace} an {@link Artifact} from the metamodel.
   * 
   * @param workspace
   *          the {@link Workspace} in which the {@link Artifact} should be removed from the metamodel.
   * @param artifact
   *          the {@link Artifact} to be removed.
   * @throws ArtifactDeadException
   *           if the metamodel is dead.
   */
  public void removeArtifact(final Workspace workspace, final Artifact artifact) throws ArtifactDeadException;

  /**
   * Returns the collection of {@link Artifact} in this container.
   * 
   * @return the collection of {@link Artifact} in this container.
   */
  public Collection<Artifact> getArtifacts();

  public Collection<Artifact> getArtifacts(final Filter filter);

  public Collection<Artifact> getArtifactsWithProperty(final String propertyName, final Object propertyValue, boolean alive, final Filter filter);

  public Collection<Artifact> getArtifactsWithProperty(final Map<String, Object> propertyToValue, boolean alive, final Filter filter);

  public Collection<Artifact> getArtifactsWithReference(Artifact artifact, final Filter filter);

  public Map<Artifact, Set<Property>> getArtifactAndProperties();

  public Map<Artifact, Map<String, Object>> getArtifactPropertyMap();

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final Filter filter);

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final String propertyName, final Object propertyValue, boolean alive, final Filter filter);

  public Map<Artifact, Map<String, Object>> getArtifactsAndPropertyMap(final Map<String, Object> propertyToValue, boolean alive, final Filter filter);

  public Collection<Object[]> getArtifactRepresentations();

  public Map<Object[], Set<Object[]>> getArtifactPropertyMapRepresentations();

  public class Filter {
    private Owner owner;
    private Tool tool;

    private Artifact artifact;
    private CollectionArtifact collection;
    private Project project;

    public Artifact getArtifact() {
      return artifact;
    }

    public Filter setArtifact(Artifact artifact) {
      this.artifact = artifact;
      return this;
    }

    public CollectionArtifact getCollection() {
      return collection;
    }

    public Filter setCollection(CollectionArtifact collection) {
      this.collection = collection;
      return this;
    }

    public Project getProject() {
      return project;
    }

    public Filter setProject(Project project) {
      this.project = project;
      return this;
    }

    public Owner getOwner() {
      return owner;
    }

    public Filter setOwner(Owner owner) {
      this.owner = owner;
      return this;
    }

    public Tool getTool() {
      return tool;
    }

    public Filter setTool(Tool tool) {
      this.tool = tool;
      return this;
    }
  }
}
