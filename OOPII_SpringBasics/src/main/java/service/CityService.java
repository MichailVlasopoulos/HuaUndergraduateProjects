package service;

import api.foursquare.FoursquareClient;
import api.foursquare.RequestException;
import api.foursquare.entities.Venue;
import api.openweathermap.APIRequestException;
import api.openweathermap.OpenWeatherMapClient;
import api.openweathermap.entities.WeatherInfo;
import api.recombee.RecombeeCityUtils;
import com.recombee.api_client.exceptions.ApiException;
import db.entities.CityDB;
import db.entities.VenueDB;
import db.entities.WeatherDB;
import db.hibernate.dao.CityDAO;
import org.hibernate.HibernateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CityService
{
    private CityDAO dao;

    public CityService()
    {
        dao = new CityDAO();
    }

    public void insertCity(String coords)
    {

        CityDB city = createCity(coords);
        Integer id;

        //TODO Synchronize better our DB with Hibernate
        id = dao.insert(city);

        try
        {
            RecombeeCityUtils.insertOrUpdate(city,id);
        }
        catch (ApiException e)
        {
            throw new RuntimeException();
        }

    }

    public void deleteCity(Integer id)
    {
        dao.delete(id);

        try
        {
            RecombeeCityUtils.delete(id);
        }
        catch (ApiException e)
        {
            throw new RuntimeException();
        }
    }

    public void closeService()
    {
        dao.closeConnection();
    }

    private CityDB createCity(String coords)
    {
        FoursquareClient client = new FoursquareClient("WDU0DKZGVN5RBH2AVGXRXKLZAKK5P5N22TFN2Y2CJLVAXJMJ",
                "0ZCVF4XO3TNNN4SWJPXR1QMVO1DAXXUFPLO5KUVL1IUIUB5B",
                "20200310");

        HashMap<String, String> params = new HashMap<>();
        params.put("ll", coords);
        params.put("limit", "50");

        ArrayList<Venue> venues;
        try
        {
            venues = client.searchVenues(params);
        }
        catch (RequestException e)
        {
            throw new RuntimeException(); //TODO
        }

        ArrayList<VenueDB> db = VenueDB.FoursquareVenueFilter.filter(venues);

        OpenWeatherMapClient weatherClient = new OpenWeatherMapClient("e093b4e1ccec91d03ac523d9a0a810e0");

        String[] splitted = coords.split(",");
        double lat = Double.parseDouble(splitted[0]);
        double lng = Double.parseDouble(splitted[1]);

        WeatherDB filteredWeather;
        String cityName;
        try
        {
            WeatherInfo weather = weatherClient.getWeather(lat, lng);
            cityName = weather.getName();
            filteredWeather = WeatherDB.WeatherFilter.filter(weather);
        }
        catch (APIRequestException e)
        {
            throw new RuntimeException();
        }

        CityDB cityEntity = new CityDB();

        cityEntity.setName(cityName);
        cityEntity.setLat(lat);
        cityEntity.setLng(lng);
        cityEntity.setCityWeather(filteredWeather);
        cityEntity.setVenuesAndCalculateMetrics(new HashSet<>(db));

        return cityEntity;
    }
}
