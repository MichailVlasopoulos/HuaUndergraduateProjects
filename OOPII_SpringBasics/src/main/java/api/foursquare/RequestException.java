package api.foursquare;

/**
 * This exception is raised when an request cannot be completed.
 * @author delta
 */
public class RequestException extends Exception
{
    public RequestException(String message)
    {
        super(message);
    }
}
