package at.jku.sea.cloud.rest.server.configuration;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.jku.sea.cloud.rest.server.handler.ArtifactHandler;
import at.jku.sea.cloud.rest.server.handler.CloudHandler;
import at.jku.sea.cloud.rest.server.handler.CollectionArtifactHandler;
import at.jku.sea.cloud.rest.server.handler.ContainerHandler;
import at.jku.sea.cloud.rest.server.handler.MapArtifactHandler;
import at.jku.sea.cloud.rest.server.handler.MapEntryHandler;
import at.jku.sea.cloud.rest.server.handler.MetaModelHandler;
import at.jku.sea.cloud.rest.server.handler.OwnerHandler;
import at.jku.sea.cloud.rest.server.handler.PackageHandler;
import at.jku.sea.cloud.rest.server.handler.ProjectHandler;
import at.jku.sea.cloud.rest.server.handler.PropertyHandler;
import at.jku.sea.cloud.rest.server.handler.ResourceHandler;
import at.jku.sea.cloud.rest.server.handler.ToolHandler;
import at.jku.sea.cloud.rest.server.handler.VersionHandler;
import at.jku.sea.cloud.rest.server.handler.WorkspaceHandler;
import at.jku.sea.cloud.rest.server.handler.navigator.NavigatorHandler;
import at.jku.sea.cloud.rest.server.handler.stream.StreamHandler;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Configuration
public class CloudControllerConfiguration {

  @Bean
  public WorkspaceHandler getWorkspaceHandler() {
    return new WorkspaceHandler();
  }

  @Bean
  public CloudHandler getCloudHandler() {
    return new CloudHandler();
  }

  @Bean
  public ArtifactHandler getArtifactHandler() {
    return new ArtifactHandler();
  }

  @Bean
  public PropertyHandler getPropertyHandler() {
    return new PropertyHandler();
  }

  @Bean
  public VersionHandler getVersionHandler() {
    return new VersionHandler();
  }

  @Bean
  public OwnerHandler getOwnerHandler() {
    return new OwnerHandler();
  }

  @Bean
  public ToolHandler getToolHandler() {
    return new ToolHandler();
  }

  @Bean
  public PackageHandler getPackageHandler() {
    return new PackageHandler();
  }

  @Bean
  public ProjectHandler getProjectHandler() {
    return new ProjectHandler();
  }

  @Bean
  public MetaModelHandler getMetaModelHandler() {
    return new MetaModelHandler();
  }

  @Bean
  public CollectionArtifactHandler getCollectionArtifactHandler() {
    return new CollectionArtifactHandler();
  }

  @Bean
  public MapArtifactHandler getMapArtifactHandler() {
    return new MapArtifactHandler();
  }

  @Bean
  public MapEntryHandler getMapEntryHandler() {
    return new MapEntryHandler();
  }

  @Bean
  public ContainerHandler getContainerHandler() {
    return new ContainerHandler();
  }

  @Bean
  public ResourceHandler getResourceHandler() {
    return new ResourceHandler();
  }

  @Bean
  public NavigatorHandler getNavigatorHandler() {
    return new NavigatorHandler();
  }

  @Bean
  public StreamHandler getStreamHandler() {
    return new StreamHandler();
  }
  
  @Bean
  public Logger getLogger() {
	  return Logger.getGlobal();
  }
}
