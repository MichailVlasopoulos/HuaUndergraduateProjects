package rest.user;

import com.recombee.api_client.RecombeeClient;
import com.recombee.api_client.api_requests.*;
import com.recombee.api_client.exceptions.ApiException;
import db.entities.AbstractUserDB;
import db.entities.CityDB;
import db.hibernate.dao.CityDAO;
import db.hibernate.dao.UserDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.*;

@Path("/")
public class UserREST
{
    @GET
    @Path("/recombee")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() throws ApiException
    {
        RecombeeClient client = new RecombeeClient("deltavlass-dev","YSKfmjBxoAbvNUQ2Sl0KTSwReZRVmJDpXT6zsAV43BSGrzLR2yZ8UbU2dDUQbrCN");
        client.send(new DeleteItem("1"));
        client.send(new DeleteItem("2"));
        client.send(new DeleteItem("4"));
        client.send(new DeleteItem("5"));
        return "ok";
    }

    @GET
    @Path("/ShowCities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showCities()
    {
        CityDAO dao = new CityDAO();
        List<CityDB> cities = dao.getAll();
        dao.closeConnection();

        return Response.ok(cities).build();
    }

    @GET
    @Path("/Recommendations")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecommendation(@QueryParam("userID") Integer userID)
    {
        UserDAO dao = new UserDAO();
        AbstractUserDB user = dao.findByPK(userID);
        dao.closeConnection();
        return Response.ok().entity(user.RecommendCities()).build();
    }

}