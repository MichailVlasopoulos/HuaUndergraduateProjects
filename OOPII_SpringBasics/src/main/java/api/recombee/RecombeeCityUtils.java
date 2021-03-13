package api.recombee;

import com.recombee.api_client.RecombeeClient;
import com.recombee.api_client.api_requests.DeleteItem;
import com.recombee.api_client.api_requests.RecommendItemsToUser;
import com.recombee.api_client.api_requests.SetItemValues;
import com.recombee.api_client.bindings.Recommendation;
import com.recombee.api_client.bindings.RecommendationResponse;
import com.recombee.api_client.exceptions.ApiException;
import db.entities.CityDB;
import db.entities.CityMetricsDB;
import db.hibernate.dao.CityDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecombeeCityUtils
{
    public static void insertOrUpdate(CityDB city, Integer id) throws ApiException
    {
        RecombeeClient client = new RecombeeClient("deltavlass-dev","YSKfmjBxoAbvNUQ2Sl0KTSwReZRVmJDpXT6zsAV43BSGrzLR2yZ8UbU2dDUQbrCN");
        Map<String,Object> cityMap = ConversionUtils.toRecombeeCity(city);

        client.send(new SetItemValues(id.toString(), cityMap)
                .setCascadeCreate(true));

    }

    public static void delete(Integer id) throws ApiException
    {
        RecombeeClient client = new RecombeeClient("deltavlass-dev","YSKfmjBxoAbvNUQ2Sl0KTSwReZRVmJDpXT6zsAV43BSGrzLR2yZ8UbU2dDUQbrCN");
        client.send(new DeleteItem(id.toString()));
    }

    public static ArrayList<CityDB> recommendCitiesToUser(String userID, int limit) throws ApiException
    {
        RecombeeClient client = new RecombeeClient("deltavlass-dev","YSKfmjBxoAbvNUQ2Sl0KTSwReZRVmJDpXT6zsAV43BSGrzLR2yZ8UbU2dDUQbrCN");
        RecommendationResponse response = client.send(new RecommendItemsToUser(userID,limit));

        ArrayList<CityDB> cityList = new ArrayList<>();
        CityDAO dao = new CityDAO();
        for(Recommendation recomCity : response)
        {
            Integer key = Integer.parseInt(recomCity.getId());
            CityDB city = dao.findByPK(key);
            cityList.add(city);
        }

        return cityList;
    }

    private static class ConversionUtils
    {
        private static Map<String,Object> toRecombeeCity(CityDB city)
        {
            Map<String,Object> map = new HashMap<>();
            map.put("city_name",city.getName());
            map.put("lat",city.getLat());
            map.put("lng",city.getLng());
            map.put("weather_main",city.getCityWeather().getMain());

            CityMetricsDB metrics = city.getMetrics();
            map.put("entertainment_venues",metrics.getEntertainment_venues());
            map.put("event_venues",metrics.getEvent_venues());
            map.put("food_venues",metrics.getFood_venues());
            map.put("university_venues",metrics.getUniversity_venues());
            map.put("nightlife_venues",metrics.getNightlife_venues());
            map.put("professional_venues",metrics.getProfessional_venues());
            map.put("residence_venues",metrics.getResidence_venues());
            map.put("shop_venues",metrics.getShop_venues());
            map.put("travel_venues",metrics.getTravel_venues());
            map.put("outdoor_venues",metrics.getOutdoor_venues());

            return map;
        }
    }
}
