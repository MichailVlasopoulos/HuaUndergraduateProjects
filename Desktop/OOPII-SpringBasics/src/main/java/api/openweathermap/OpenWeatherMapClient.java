package api.openweathermap;

import api.openweathermap.entities.WeatherInfo;
import com.google.gson.Gson;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * JavaDoc to be added later.
 * @author Mihalis Vlasopoulos
 */
public class OpenWeatherMapClient
{
    /*
        API key: e093b4e1ccec91d03ac523d9a0a810e0
     */
    private final String APIkey;

    public OpenWeatherMapClient(String APIkey)
    {
        this.APIkey = APIkey;
    }

    public String getAPIkey()
    {
        return APIkey;
    }

    public WeatherInfo getWeather(String cityName) throws APIRequestException
    {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + APIkey;

        try (CloseableHttpClient httpclient = HttpClients.createDefault())
        {
            HttpGet request = new HttpGet(url);
            //Executing the Get request
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();

            if (status != 200)
            {
                throw new APIRequestException(response.getStatusLine().getReasonPhrase());
            }

            String jsonString = EntityUtils.toString(entity);
            Gson gson = new Gson();
            WeatherInfo obj = gson.fromJson(jsonString, WeatherInfo.class);

            return obj;

        }
        catch (IOException e)
        {
            throw new APIRequestException(e.getMessage());
        }
    }

    public WeatherInfo getWeather(double lat, double lon) throws APIRequestException
    {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + APIkey;

        try (CloseableHttpClient httpclient = HttpClients.createDefault())
        {
            HttpGet request = new HttpGet(url);
            //Executing the Get request
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();

            if (status != 200)
            {
                throw new APIRequestException(response.getStatusLine().getReasonPhrase());
            }

            String jsonString = EntityUtils.toString(entity);
            Gson gson = new Gson();
            WeatherInfo obj = gson.fromJson(jsonString, WeatherInfo.class);
            return obj;

        }
        catch (IOException e)
        {
            throw new APIRequestException(e.getMessage());
        }
    }


}
