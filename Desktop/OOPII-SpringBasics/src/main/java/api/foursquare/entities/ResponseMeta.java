package api.foursquare.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class that represents an meta object in a Foursquare response.
 * @author delta
 */
public class ResponseMeta
{
    @SerializedName("code")
    @Expose
    private final int code;

    @SerializedName("errorType")
    @Expose
    private final String errorType;

    @SerializedName("errorDetail")
    @Expose
    private final String errorDetail;

    @SerializedName("requestId")
    @Expose
    private final String requestId;

    public ResponseMeta(int code, String errorType, String errorDetail, String requestId)
    {
        this.code = code;
        this.errorType = errorType;
        this.errorDetail = errorDetail;
        this.requestId = requestId;
    }

    /**
     *
     * @return the HTTP status code.
     */
    public int getCode()
    {
        return code;
    }

    /**
     *
     * @return The error type or null if there wasn't any.
     */
    public String getErrorType()
    {
        return errorType;
    }

    /**
     * @return The error details or null if there wasn't any.
     */
    public String getErrorDetail()
    {
        return errorDetail;
    }

    /**
     *
     * @return The Foursquare request ID.
     */
    public String getRequestId()
    {
        return requestId;
    }
}
