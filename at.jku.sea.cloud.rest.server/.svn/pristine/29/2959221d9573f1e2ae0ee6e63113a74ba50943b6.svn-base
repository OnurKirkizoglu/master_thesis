package at.jku.sea.cloud.rest.server.controller;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class InterceptorX extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println(request.getMethod());
		MyRequestWrapper w = new MyRequestWrapper(request);
		
		BufferedReader r = w.getReader();
		System.out.println(r.readLine());

		return super.preHandle(request, response, handler);
	}
}
