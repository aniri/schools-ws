package ro.aniri.schoolws;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SchoolsResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(SchoolsResource.class);
    }

    @Test
    public void testGetUsage() {
        Response response = target().path("schools").request("text/html").get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetByCountyValidCounty() {
        Response response = target().path("schools/licee").queryParam("judet", "AB").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetByCountyInvalidCounty() {
        Response response = target().path("schools/licee").queryParam("judet", "AAA").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetByCountyNonexistentCounty() {
        Response response = target().path("schools/licee").queryParam("judet", "AA").request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetByAreaValidParams() {
        Response response = target().path("schools/licee").queryParam("lat", "44.446419").queryParam("lng", "26.04394").queryParam("radius", "10000").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetByAreaInvalidLat() {
        Response response = target().path("schools/licee").queryParam("lat", "testing").queryParam("lng", "26.04394").queryParam("radius", "10000").request().get();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetByAreaInvalidLng() {
        Response response = target().path("schools/licee").queryParam("lat", "44.446419").queryParam("lng", "testing").queryParam("radius", "10000").request().get();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetByAreaInvalidRadius() {
        Response response = target().path("schools/licee").queryParam("lat", "44.446419").queryParam("lng", "26.04394").queryParam("radius", "testing").request().get();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testGetIncorrectParamNumber() {
        Response response = target().path("schools/licee").queryParam("lat", "23").request().get();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
