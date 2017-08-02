package ro.aniri.schoolws;

import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.annotation.JacksonFeatures;
import ro.aniri.schoolws.entity.School;
import ro.aniri.schoolws.providers.Logged;

/**
 * Root resource (exposed at "schools" path)
 */
@Stateless
@Path("schools")
public class SchoolsResource {
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ro.aniri.schoolWS.PU");
    EntityManager em = emf.createEntityManager();

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getUsage() {
        StringBuilder html = new StringBuilder("<h1>Reteaua unitatilor scolare 2016-2017</h1>");
        html.append("<h2>Usage</h2>");
        html.append("<p>/licee?judet=AB -> two letter county code or B for Bucharest -> returns schools from county in json format</p>");
        html.append("<p>/licee?lat=CENTER_LATITUDE_VALUE&lng=CENTER_LONGITUDE_VALUE&radius=VALUE_IN_METRES -> returns schools from selected area in json format</p>");
        html.append("<p>add &key=123456 to view complete results</p>");
                
        return Response.ok(html.toString()).build();
    }
    
    @GET
    @Logged
    @Path("licee")
    @Produces(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response getSchoolsByParams(@Context UriInfo uriInfo, @Context SecurityContext sc) {
        
        String judet = uriInfo.getQueryParameters().getFirst("judet");
        if (judet != null)
            return getResponseForCounty(judet, sc); 
        
        return getResponseForArea(uriInfo, sc);
    }
    
    private Response getResponseForCounty(String judet, SecurityContext sc){
        if (!isValidCounty(judet)){
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        Query query = buildCountyQuery(sc, judet);

        return returnQueryResults(query);
    }
    
    private Response getResponseForArea(UriInfo uriInfo, SecurityContext sc){
        String lat_str = uriInfo.getQueryParameters().getFirst("lat");
        String lng_str = uriInfo.getQueryParameters().getFirst("lng");
        String radius_str = uriInfo.getQueryParameters().getFirst("radius");
        
        if (lat_str == null || lng_str == null || radius_str == null)
            return Response.status(Status.BAD_REQUEST).build();
        
        double lat, lng;
        int radius;
            
        try{
            lat = Double.parseDouble(lat_str);
            lng = Double.parseDouble(lng_str);
            radius = Integer.parseInt(radius_str);
        } catch (NumberFormatException e){
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        Query query = buildAreaQuery(sc, lat, lng, radius);

        return returnQueryResults(query);
    }
    
    private void limitQueryResultsForUser(SecurityContext sc, Query query){
        // limit number of results in case the api key was not provided
        if (sc.isUserInRole("user")){
            query.setMaxResults(6);
        }
    }

    private boolean isValidCounty(String judet) {
        String pattern = "^[A-Z]{2}$";
        return judet.equals("B") || judet.matches(pattern);
    }

    private Query buildCountyQuery(SecurityContext sc, String judet) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root e = cq.from(School.class);

        cq.where(cb.equal(e.get("judet"), judet));
        
        Query query = em.createQuery(cq);
        limitQueryResultsForUser(sc, query);
        
        return query;
    }

    private Query buildAreaQuery(SecurityContext sc, double lat, double lng, int radius) {
        // get aproximate max and min lat and lng
        // https://stackoverflow.com/questions/7477003/calculating-new-longtitude-latitude-from-old-n-meters
        double oneMeterInDegrees = 1.0/111000;
        
        double diff_lat = radius * oneMeterInDegrees;
        double diff_lng = radius * oneMeterInDegrees / Math.cos(lat * Math.PI/180);

        double max_lat  = lat  + diff_lat;
        double max_lng = lng + diff_lng;
        double min_lat  = lat - diff_lat;
        double min_lng = lng - diff_lng;
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root e = cq.from(School.class);

        cq.where(cb.and
            (cb.and(cb.ge(e.get("lat"), min_lat), cb.le(e.get("lat"), max_lat))),
            (cb.and(cb.ge(e.get("lng"), min_lng), cb.le(e.get("lng"), max_lng)))
            );
        
        Query query = em.createQuery(cq);
        
        limitQueryResultsForUser(sc, query);
        
        return query;
    }

    private Response returnQueryResults(Query query) {
        final List<School> list = query.getResultList();

        if (list.size() > 0)
            return Response.ok(list).build();
             
        return Response.status(Status.NOT_FOUND).build();
    }
}
