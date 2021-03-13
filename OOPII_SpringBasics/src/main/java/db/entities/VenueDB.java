package db.entities;

import api.foursquare.entities.Category;
import api.foursquare.entities.Venue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that represents a Foursquare venue in our database schema.
 * @author delta
 */
@Entity
@Table(name = "venue")
public class VenueDB
{
    @Id
    @Column(name = "venue_id" , length = 24)
    private String id;

    @Column(name = "venue_name" , length = 50)
    private String name;

    @OneToOne(targetEntity = CategoryDB.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private CategoryDB category;

    @Column(name = "lat" , precision = 16 , scale = 14)
    private double lat;

    @Column(name = "lng" , precision = 16 , scale = 14)
    private double lng;

    @Column(name = "address" , length = 50)
    private String address;

    public String getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public CategoryDB getCategory()
    {
        return category;
    }
    public double getLat()
    {
        return lat;
    }
    public double getLng()
    {
        return lng;
    }
    public String getAddress()
    {
        return address;
    }

    public void setId(String id)
    {
        this.id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setCategory(CategoryDB category)
    {
        this.category = category;
    }
    public void setLat(double lat)
    {
        this.lat = lat;
    }
    public void setLng(double lng)
    {
        this.lng = lng;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VenueDB venueDB = (VenueDB) o;
        return id.equals(venueDB.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    /**
     * Class that provides methods for converting a Foursquare venue entity into
     * it's DB equivalent
     * @author delta
     */
    public static class FoursquareVenueFilter
    {
        /**
         * Converts a ArrayList of Foursquare's venue entities into their DB equivalent ones.
         * @param venues the Foursquare venues
         * @return the DB entity equivalent version ArrayList
         */
        public static ArrayList<VenueDB> filter(ArrayList<Venue> venues)
        {
            ArrayList<VenueDB> venuesDB = new ArrayList<>();

            for(Venue venObj : venues)
            {
                VenueDB obj = new VenueDB();
                obj.setId(venObj.getId());
                obj.setName(venObj.getName());
                obj.setAddress(venObj.getLocation().getAddress());
                obj.setLat(venObj.getLocation().getLat());
                obj.setLng(venObj.getLocation().getLng());

                if(venObj.getCategories().isEmpty())
                    continue;

                for(Category cat : venObj.getCategories())
                {
                    if(cat.isPrimary())
                    {
                        obj.setCategory(CategoryDB.FoursquareCategoryFilter.filter(cat,null));
                        break;
                    }
                }

                venuesDB.add(obj);
            }

            return venuesDB;
        }
    }
}
