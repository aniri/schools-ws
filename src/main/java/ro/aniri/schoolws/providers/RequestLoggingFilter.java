package ro.aniri.schoolws.providers;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Logged
@Provider
public class RequestLoggingFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("====== Request Received ======");
        System.out.println(requestContext.getHeaders().toString());
        System.out.println(requestContext.getUriInfo().getQueryParameters().toString());
    }
}