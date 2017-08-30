package at.jku.sea.cloud.rest.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Dominik Muttenthaler
 * @author Florian Weger
 */
@ComponentScan({ "at.jku.sea.cloud.rest.server.controller", "at.jku.sea.cloud.rest.server.configuration" })
@EnableAutoConfiguration
public class Server {
	
	private static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
	  // show DB Manager
//		org.hsqldb.util.DatabaseManagerSwing.main(new String[] {
//				  "--url",  "jdbc:hsqldb:mem:xdb", "--noexit"
//				});
		SpringApplication.run(Server.class, args);
	}
	
	public static void start(String... args) {
		if(!isRunning()) {
			context = SpringApplication.run(Server.class, args);
		}
	}
	
	public static void stop() {
		if(isRunning()) {
			SpringApplication.exit(context);
			context = null;
		}
	}
	
	public static boolean isRunning() {
		return context != null;
	}
}