package at.jku.sea.cloud.rest.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.jku.sea.cloud.Cloud;
import at.jku.sea.cloud.implementation.CloudFactory;
import at.jku.sea.cloud.rest.pojo.factory.PojoFactory;
import at.jku.sea.cloud.rest.server.factory.DesignSpaceFactory;
import at.jku.sea.cloud.rest.server.factory.NavigatorFactory;
import at.jku.sea.cloud.rest.server.factory.StreamFactory;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@Configuration
public class CloudHandlerConfiguration {

  @Bean
  public Cloud getCloud() {
    return CloudFactory.getInstance();
  }

  @Bean
  public PojoFactory getCloudPojoFactory() {
    return PojoFactory.getInstance();
  }

  @Bean
  DesignSpaceFactory getDSFactory() {
    return DesignSpaceFactory.getInstance();
  }

  @Bean
  NavigatorFactory getNavigatorFactory() {
    return NavigatorFactory.getInstance();
  }

  @Bean
  StreamFactory getStreamFactory() {
    return StreamFactory.getInstance();
  }
}
