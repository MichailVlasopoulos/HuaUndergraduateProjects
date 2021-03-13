package api.foursquare;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import api.foursquare.entities.Category;
import api.foursquare.entities.Response;
import api.foursquare.entities.Venue;


/**
 * Client for sending requests to Foursquare and receive responses.
 * @author delta
 * @version 1.5
 * @since 2020-03-25
 */
public final class FoursquareClient
{
    private final String clientID;
    private final String clientSecret;
    private final String version;

/*
        client ID: WDU0DKZGVN5RBH2AVGXRXKLZAKK5P5N22TFN2Y2CJLVAXJMJ
        client Secret: 0ZCVF4XO3TNNN4SWJPXR1QMVO1DAXXUFPLO5KUVL1IUIUB5B"
        version: "20200309";
 */

    /**
     *Constructs a client with the given authentication credentials.
     * @param clientID The CLIENT_ID given by Foursquare.
     * @param clientSecret The CLIENT_SECRET given by Foursquare
     * @param version The version of the API to be used.
     * @see <a href="https://developer.foursquare.com/docs/api/configuration/authentication">Foursquare Docs</a>
     */
    public FoursquareClient(String clientID, String clientSecret, String version)
    {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.version = version;
    }

    /**
     * @return The Client ID.
     */
    public String getClientID()
    {
        return clientID;
    }

    /**
     * @return The Client Secret.
     */
    public String getClientSecret()
    {
        return clientSecret;
    }

    /**
     * @return The version used.
     */
    public String getVersion()
    {
        return version;
    }

    /**
     *Makes a GET Request to Foursquare and returns all supported venue categories.
     * @return An {@link Category} ArrayList with the categories.
     * @throws RequestException if a HTTP status different than 200-OK is returned or the host cannot be resolved.
     * @see <a href="https://developer.foursquare.com/docs/api/venues/categories">Foursquare Docs</a>
     */
    public ArrayList<Category> getVenueCategories() throws RequestException
    {
        Response response  = APIRequestBuilder.getVenueCategoriesResponse(this);

        String jsonCategories = new JSONObject(response.getResponse())
                                     .getJSONArray("categories")
                                     .toString();

        Gson parser = CustomGsonProvider.getGson();


        Type type = TypeToken.getParameterized(ArrayList.class, Category.class).getType();
        return parser.fromJson(jsonCategories, type);

    }

    /**
     *Makes a GET Request to Foursquare and returns all venues that match the supplied arguments.
     * The user is responsible for supplying valid parameters. In case of invalid parameters an
     * exception is thrown.
     * @param params the parameters of the request.
     * @return An {@link Venue} ArrayList with the venues.
     * @throws RequestException if a HTTP status different than 200-OK is returned or the host cannot be resolved.
     * @see <a href="https://developer.foursquare.com/docs/api/venues/search">Foursquare Docs</a>
     */
    public ArrayList<Venue> searchVenues(HashMap<String,String> params) throws RequestException
    {
        ArrayList<NameValuePair> parameters = new ArrayList<>();

        for(Map.Entry<String,String> param : params.entrySet())
        {
            NameValuePair queryParam = new BasicNameValuePair(param.getKey(),param.getValue());
            parameters.add(queryParam);
        }

        Response resp = APIRequestBuilder.getSearchVenuesResponse(this,parameters);

        Gson gson = new Gson();

        JSONArray venueJsonArray = new JSONObject(resp.getResponse()).getJSONArray("venues");
        String jsonStr = venueJsonArray.toString();

        Type type = TypeToken.getParameterized(ArrayList.class, Venue.class).getType();
        return gson.fromJson(jsonStr,type);
    }

    /**
     * Builder class that gets responses from Foursquare in {@link Response} format.
     * @author delta
     */
    private final static class APIRequestBuilder
    {
        /**
         * Gets the response of the Get Venue Categories GET request.
         *
         * @param client the client to be used.
         * @return The Response object.
         * @throws RequestException if communication with Foursquare fails or if another unknown I/O exception occurs.
         */
        public static Response getVenueCategoriesResponse(FoursquareClient client) throws RequestException
        {
            URI requestURI = null;
            try
            {
                requestURI = createURI(client, GroupsEnum.VENUES, EndpointsEnum.CATEGORIES, null);
            }
            catch (URISyntaxException ignored) { }

            String response;
            try
            {
                response = HttpRequestClient.getResponse(requestURI);
            }
            catch (IOException e)
            {
                throw new RequestException(e.getMessage());
            }

            Gson parser = CustomGsonProvider.getGson();

            return parser.fromJson(response, Response.class);
        }

        /**
         * Gets the response of the Get Venues GET request
         * @param client the client to be used
         * @param params the query parameters.
         * @return The Response object.
         * @throws RequestException if communication with Foursquare fails or if another unknown I/O exception occurs.
         */
        public static Response getSearchVenuesResponse(FoursquareClient client, ArrayList<NameValuePair> params) throws RequestException
        {

            URI requestURI = null;
            try
            {
                requestURI = createURI(client, GroupsEnum.VENUES, EndpointsEnum.SEARCH, params);
            }
            catch (URISyntaxException ignored) { } //It is guaranteed to succeed

            String response;
            try
            {
                response = HttpRequestClient.getResponse(requestURI);
            }
            catch (IOException | RequestException e)
            {
                throw new RequestException(e.getMessage());
            }

            Gson parser = CustomGsonProvider.getGson();

            return parser.fromJson(response, Response.class);
        }

        /**
         * Creates the URI of an API request.
         *
         * @param api      The client to be used.
         * @param group    The group that will be reached.
         * @param endpoint The endpoint that will be reached.
         * @param params   The query parameters of the URI.
         * @return The request URI.
         * @throws URISyntaxException if one or more parameters do not fulfill the URI specifications.
         * @see <a href="https://developer.foursquare.com/docs/api/endpoints"></a>
         */
        public static URI createURI(FoursquareClient api, GroupsEnum group,
                                    EndpointsEnum endpoint, List<NameValuePair> params) throws URISyntaxException
        {
            URIBuilder uri_build = new URIBuilder("https://api.foursquare.com");

            uri_build.setPathSegments("v2", group.value, endpoint.value);

            uri_build.addParameter("client_id", api.getClientID());
            uri_build.addParameter("client_secret", api.getClientSecret());
            uri_build.addParameter("v", api.getVersion());


            if (params != null)
                uri_build.addParameters(params);

            return uri_build.build();
        }

        /**
         * The Foursquare supported Groups enum.
         * @see <a href="https://developer.foursquare.com/docs/api/endpoints">Foursquare Docs</a>
         */
        public enum GroupsEnum
        {
            VENUES();

            final String value;

            GroupsEnum()
            {
                this.value = "venues";
            }
        }

        /**
         * The Foursquare supported Endpoints enum.
         * @see <a href="https://developer.foursquare.com/docs/api/endpoints">Foursquare Docs</a>
         */
        public enum EndpointsEnum
        {
            SEARCH("search"),
            CATEGORIES("categories");

            final String value;

            EndpointsEnum(String name)
            {
                this.value = name;
            }
        }

    }

    /**
     * Simple HTTP Client that makes the requests.
     * @author delta
     */
    private final static class HttpRequestClient
    {
        /**
         * Makes an HTTP GET Request to the given URI.
         * @param requestURI The URI to make the request.
         * @return The response in String format.
         * @throws IOException if a connection cannot be established.
         * @throws RequestException if a HTTP status different than 200-OK is returned.
         */
        public static String getResponse(URI requestURI) throws IOException, RequestException
        {
            HttpGet request = new HttpGet(requestURI);

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request))
            {
                if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                    throw new RequestException("Error: HTTP Status code " + response.getStatusLine().getStatusCode()
                            + " " + response.getStatusLine().getReasonPhrase());

                return EntityUtils.toString(response.getEntity());
            }
        }
    }
}