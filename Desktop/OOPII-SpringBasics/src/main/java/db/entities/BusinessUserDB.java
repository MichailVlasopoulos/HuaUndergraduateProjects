package db.entities;

import api.recombee.RecombeeCityUtils;
import api.recombee.RecombeeUserUtils;
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
@DiscriminatorValue("businessman")
public class BusinessUserDB extends AbstractUserDB
{
    public BusinessUserDB()
    {
        this.userType = "businessman";
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
        //Recombee will be able to produce different results with weather filter applied
        return null;
    }
}
