package db.entities;

import api.recombee.RecombeeCityUtils;
import com.recombee.api_client.exceptions.ApiException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;

/**
 * Class that describes an user travelling for business purposes.
 * @author delta , Mihalis Vlasopoulos
 */
@Entity
@Table(name = "user")
@DiscriminatorValue("tourist")
public class TouristUserDB extends AbstractUserDB
{
    public TouristUserDB()
    {
        this.userType = "tourist";
    }

    @Override
    public ArrayList<CityDB> RecommendCities()
    {
        //Will be implemented better
        try
        {
            return RecombeeCityUtils.recommendCitiesToUser(Integer.toString(id),5);
        }
        catch (ApiException e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public ArrayList<CityDB> RecommendCities(String weatherPreference)
    {
        //Recombee logic here
        return null;
    }
}
