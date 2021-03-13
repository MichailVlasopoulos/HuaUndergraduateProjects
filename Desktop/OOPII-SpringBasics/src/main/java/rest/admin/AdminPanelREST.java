package rest.admin;

import api.foursquare.FoursquareClient;
import api.foursquare.RequestException;

import api.foursquare.entities.Category;

import db.entities.*;
import db.hibernate.dao.UserDAO;
import db.hibernate.dao.CategoryDAO;
import service.CityService;
import service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/")
public class AdminPanelREST
{
    @PUT
    @Path("/InsertCategories")
    public Response storeCategories()
    {
        FoursquareClient client = new FoursquareClient("WDU0DKZGVN5RBH2AVGXRXKLZAKK5P5N22TFN2Y2CJLVAXJMJ",
                "0ZCVF4XO3TNNN4SWJPXR1QMVO1DAXXUFPLO5KUVL1IUIUB5B",
                "20200310");

        ArrayList<Category> categories;
        try
        {
            categories = client.getVenueCategories();
        }
        catch (RequestException e)
        {
            return Response.status(500, "Cannot receive a response from the OpenWeatherMap API").build();
        }


        LinkedHashSet<CategoryDB> categoriesDB = CategoryDB.FoursquareCategoryFilter.filter(categories);

        CategoryDAO dao = new CategoryDAO();
        dao.deleteAll();
        Set<String> ids = dao.insert(categoriesDB);
        dao.closeConnection();

        return Response.status(200, "All categories added").build();
    }

    @PUT
    @Path("/InsertCity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response storeCity(@QueryParam("coords") String coords)
    {
        CityService serv = new CityService();
        serv.insertCity(coords);
        serv.closeService();

        return Response.ok().build();
    }

    @DELETE
    @Path("/DeleteCity")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCity(@QueryParam("cityID") Integer id)
    {
        CityService serv = new CityService();
        serv.deleteCity(id);
        serv.closeService();

        return Response.ok().build();
    }

    @PUT
    @Path("/InsertUser")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createUser(@QueryParam("name") String name)
    {
        TouristUserDB user1 = new TouristUserDB();
        user1.setName(name);
        user1.setAge(19);
        user1.setCurrentLat(38.54);
        user1.setCurrentLng(29.58);
        user1.setWeatherPreference("Cloud");

        UserPreferenceDB pref = new UserPreferenceDB();

        pref.setLikes_entertainment(3);
        pref.setLikes_events(4);
        pref.setLikes_food(5);
        pref.setLikes_nightlife(6);
        pref.setLikes_outdoors(5);
        pref.setLikes_professional(2);
        pref.setLikes_residences(4);
        pref.setLikes_shops(5);
        pref.setLikes_travel(7);
        pref.setLikes_universities(3);

        user1.setUserPreference(pref);

        UserService serv = new UserService();
        serv.createUser(user1);
        serv.closeService();

        return Response.ok().build();
    }

    @DELETE
    @Path("/DeleteUser")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@QueryParam("id") Integer id)
    {
        UserService serv = new UserService();
        serv.deleteUser(id);
        serv.closeService();

        return Response.ok().build();
    }

}
