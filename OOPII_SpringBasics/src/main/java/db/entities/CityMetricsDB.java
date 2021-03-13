package db.entities;

import db.hibernate.dao.CategoryDAO;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that represents a City's metrics based on our database schema
 * @author delta
 */
@Entity
@Table(name ="venue_metrics")
public class CityMetricsDB implements Serializable
{
    private static final long serialVersionUID = 9137752310002352644L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metrics_id")
    private int id;

    @Column(name = "entertainment_venues")
    private int entertainment_venues = 0;

    @Column(name = "university_venues")
    private int university_venues = 0;

    @Column(name = "event_venues")
    private int event_venues = 0;

    @Column(name = "food_venues")
    private int food_venues = 0;

    @Column(name = "professional_venues")
    private int professional_venues = 0;

    @Column(name = "nightlife_venues")
    private int nightlife_venues = 0;

    @Column(name = "outdoor_venues")
    private int outdoor_venues = 0;

    @Column(name = "shop_venues")
    private int shop_venues = 0;

    @Column(name = "travel_venues")
    private int travel_venues = 0;

    @Column(name = "residence_venues")
    private int residence_venues = 0;

    @Transient
    //This HashMap will map each category with the field that it represents
    private transient static HashMap<String, Field> fieldMapper = new HashMap<>();

    /* Calculate once, make the mapping and save many lines of code.
       It is the only part that needs to be changed in a foursquare category hierarchy alteration
    */
    static
    {
        /* Holds all the fields of the class.
           See more at https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Field.html
         */
        Field[] classFields = CityMetricsDB.class.getDeclaredFields();

        fieldMapper.put("4d4b7104d754a06370d81259", classFields[2]);
        fieldMapper.put("4d4b7105d754a06372d81259", classFields[3]);
        fieldMapper.put("4d4b7105d754a06373d81259", classFields[4]);
        fieldMapper.put("4d4b7105d754a06374d81259", classFields[5]);
        fieldMapper.put("4d4b7105d754a06375d81259", classFields[6]);
        fieldMapper.put("4d4b7105d754a06376d81259", classFields[7]);
        fieldMapper.put("4d4b7105d754a06377d81259", classFields[8]);
        fieldMapper.put("4d4b7105d754a06378d81259", classFields[9]);
        fieldMapper.put("4d4b7105d754a06379d81259", classFields[10]);
        fieldMapper.put("4e67e38e036454776db1fb3a", classFields[11]);
    }

    public int getId()
    {
        return id;
    }
    public int getEntertainment_venues()
    {
        return entertainment_venues;
    }
    public int getUniversity_venues()
    {
        return university_venues;
    }
    public int getEvent_venues()
    {
        return event_venues;
    }
    public int getFood_venues()
    {
        return food_venues;
    }
    public int getProfessional_venues()
    {
        return professional_venues;
    }
    public int getNightlife_venues()
    {
        return nightlife_venues;
    }
    public int getOutdoor_venues()
    {
        return outdoor_venues;
    }
    public int getShop_venues()
    {
        return shop_venues;
    }
    public int getTravel_venues()
    {
        return travel_venues;
    }
    public int getResidence_venues()
    {
        return residence_venues;
    }

    public void setEntertainment_venues(int entertainment_venues)
    {
        this.entertainment_venues = entertainment_venues;
    }
    public void setUniversity_venues(int university_venues)
    {
        this.university_venues = university_venues;
    }
    public void setEvent_venues(int event_venues)
    {
        this.event_venues = event_venues;
    }
    public void setFood_venues(int food_venues)
    {
        this.food_venues = food_venues;
    }
    public void setProfessional_venues(int professional_venues)
    {
        this.professional_venues = professional_venues;
    }
    public void setNightlife_venues(int nightlife_venues)
    {
        this.nightlife_venues = nightlife_venues;
    }
    public void setOutdoor_venues(int outdoor_venues)
    {
        this.outdoor_venues = outdoor_venues;
    }
    public void setShop_venues(int shop_venues)
    {
        this.shop_venues = shop_venues;
    }
    public void setTravel_venues(int travel_venues)
    {
        this.travel_venues = travel_venues;
    }
    public void setResidence_venues(int residence_venues)
    {
        this.residence_venues = residence_venues;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityMetricsDB that = (CityMetricsDB) o;
        return id == that.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    /**
     * Calculates the metrics of the venue data using reflection.
     */
    protected void calculateVenueMetrics(Set<VenueDB> venues)
    {
        //Create a map where AtomicIntegers will serve as counters.
        HashMap<String, AtomicInteger> values = new HashMap<>();

        //Get all the main IDs (keys) from fieldMapper.
        Set<String> mainIDs = fieldMapper.keySet();

        //Initialize the map
        for (String id : mainIDs)
            values.put(id, new AtomicInteger(0));

        /* We need DB access to get the parent ID ( Foursquare doesn't provide it )
           This is the moment where all the customizations in the standard Foursquare Category entity make sense.
         */

        CategoryDAO dao = new CategoryDAO();
        //For every venue of the city
        for (VenueDB venue : venues)
        {
            //Get its' parent category ID from the database
            String parentID = dao.getParentCategoryID(venue.getCategory());
            //We know where it belongs so increase the counter of the main category
            values.get(parentID).incrementAndGet();
        }
        dao.closeConnection();

            /*From this point we know the metrics. All that remains is to put the numbers in the fields.
              Are we going to do this for each field ? No bro.
             */

        //For each category's metrics
        for (Map.Entry<String, AtomicInteger> entry : values.entrySet())
        {
            try
            {
                //Find the field that represents that counter and assign the counter to it
                fieldMapper.get(entry.getKey()).setInt(this, entry.getValue().get());
            }
            catch (IllegalAccessException ignored)
            {
            }
        }
    }
}
