package api.openweathermap;

public class APIRequestException extends Exception
{
    public APIRequestException(String message)
    {
        super(message);
    }
}
