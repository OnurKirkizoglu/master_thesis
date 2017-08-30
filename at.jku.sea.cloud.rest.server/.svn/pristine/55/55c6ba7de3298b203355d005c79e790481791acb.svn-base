package at.jku.sea.cloud.rest.server.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "at.jku.sea.cloud.rest.server.controller", "at.jku.sea.cloud.rest.server.configuration", "at.jku.sea.cloud.rest.server.test.controller",
    "at.jku.sea.cloud.rest.server.test.configuration" })
@EnableAutoConfiguration
public class TestServer {

  private static ConfigurableApplicationContext context;

  public static void main(String[] args) {
    SpringApplication.run(TestServer.class, args);
  }

  public static void start(String... args) {
    if (!isRunning()) {
      context = SpringApplication.run(TestServer.class, args);
    }
  }

  public static void stop() {
    if (isRunning()) {
      SpringApplication.exit(context);
      context = null;
    }
  }

  public static boolean isRunning() {
    return context != null;
  }
}
