package ro.aniri.schoolws.providers;

import java.io.IOException;
import java.security.Principal;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {

        // Extract and validate the API key from the request
        String apiKey = requestContext.getUriInfo().getQueryParameters().getFirst("key");
        
        final String user = (isValid(apiKey)) ? "admin" : "user";

        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return user;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return user.equals(role);
            }

            @Override
            public boolean isSecure() {
                return requestContext.getUriInfo()
                        .getRequestUri()
                        .getScheme().equalsIgnoreCase("https");
            }

            @Override
            public String getAuthenticationScheme() {
                return "CUSTOM";
            }
        });
    }

    private boolean isValid(String apiKey) {
        return apiKey != null && apiKey.equals("123456");
    }
}