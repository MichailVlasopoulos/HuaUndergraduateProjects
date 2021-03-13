package api.recombee;

import com.recombee.api_client.RecombeeClient;
import com.recombee.api_client.api_requests.DeleteUser;
import com.recombee.api_client.api_requests.SetUserValues;
import com.recombee.api_client.exceptions.ApiException;
import db.entities.AbstractUserDB;
import db.entities.UserPreferenceDB;

import java.util.HashMap;
import java.util.Map;

public class RecombeeUserUtils
{
    public static void insert(String id , AbstractUserDB user) throws ApiException
    {
        RecombeeClient client = new RecombeeClient("deltavlass-dev","YSKfmjBxoAbvNUQ2Sl0KTSwReZRVmJDpXT6zsAV43BSGrzLR2yZ8UbU2dDUQbrCN");

        Map<String,Object> values = ConversionUtils.toRecombeeUser(user);

        client.send(new SetUserValues(id, values)
                .setCascadeCreate(true));
    }

    public static void delete(String id) throws ApiException
    {
        RecombeeClient client = new RecombeeClient("deltavlass-dev","YSKfmjBxoAbvNUQ2Sl0KTSwReZRVmJDpXT6zsAV43BSGrzLR2yZ8UbU2dDUQbrCN");

        client.send(new DeleteUser(id));
    }

    private static class ConversionUtils
    {
        private static Map<String,Object> toRecombeeUser(AbstractUserDB user)
        {
            Map<String,Object> map = new HashMap<>();
            map.put("user_name",user.getName());
            map.put("user_type",user.getUserType());
            map.put("age",user.getAge());

            UserPreferenceDB preference = user.getUserPreference();
            map.put("likes_entertainment",preference.getLikes_entertainment());
            map.put("likes_events",preference.getLikes_events());
            map.put("likes_food",preference.getLikes_food());
            map.put("likes_universities",preference.getLikes_universities());
            map.put("likes_nightlife",preference.getLikes_universities());
            map.put("likes_professional",preference.getLikes_professional());
            map.put("likes_residences",preference.getLikes_residences());
            map.put("likes_shops",preference.getLikes_shops());
            map.put("likes_travel",preference.getLikes_travel());
            map.put("likes_outdoors",preference.getLikes_outdoors());

            return map;
        }
    }
}
