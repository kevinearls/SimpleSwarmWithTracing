package com.example.demoopentracing.rest.endpoints;

import com.example.demoopentracing.rest.services.BackendService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;


@Path("/hello")
public class HelloWorldEndpoint {

	@Inject
	private BackendService backendService;

	@GET
	@Produces("text/plain")
	public Response doGet() throws InterruptedException {
		String action = backendService.action();
		String responseText = "Hello from Wildfly Swarm at " + LocalDateTime.now() + " action: " + action + "\n";
		Response response = Response.ok(responseText)
				.build();
		return response;
	}
}
