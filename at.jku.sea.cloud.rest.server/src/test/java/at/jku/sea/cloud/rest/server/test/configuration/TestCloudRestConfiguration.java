package at.jku.sea.cloud.rest.server.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.jku.sea.cloud.rest.server.test.handler.TestCloudHandler;

@Configuration
public class TestCloudRestConfiguration {

  @Bean
  public TestCloudHandler getTestCloudHandler() {
    return new TestCloudHandler();
  }
}
