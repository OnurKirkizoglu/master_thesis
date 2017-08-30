package at.jku.sea.cloud.rest.server.configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import at.jku.sea.cloud.rest.server.controller.InterceptorX;

//@Configuration
//@EnableWebMvc
//@ComponentScan(basePackages="at.jku.sea.cloud.rest.server.controller")
public class InterceptorConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new InterceptorX());
	}
}
