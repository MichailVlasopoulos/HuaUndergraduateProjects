package api.foursquare.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class that represents a Foursquare JSON Response.
 * @author delta
 */
public class Response
{
    @SerializedName("meta")
    @Expose
    private final ResponseMeta meta;

    @SerializedName("response")
    @Expose
    private final String response;

    /**
     * @return the response meta in JSON String.
     */
    public ResponseMeta getMeta()
    {
        return meta;
    }

    /**
     * @return the response in JSON String
     */
    public String getResponse()
    {
        return response;
    }

    public Response(ResponseMeta meta, String response)
    {
        this.meta = meta;
        this.response = response;
    }
}

