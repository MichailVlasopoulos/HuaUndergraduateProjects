package api.foursquare.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class that represents a Foursquare Venue object.
 * @author delta
 * @see <a href="https://developer.foursquare.com/docs/api/venues/search">Foursquare Docs</a>
 */
public class Venue implements Serializable
{
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    @SerializedName("referralId")
    @Expose
    private String referralId;

    @SerializedName("hasPerk")
    @Expose
    private boolean hasPerk;

    private final static long serialVersionUID = 6612306117690495466L;

    /**
     * @return the venue's ID.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the venue's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the venue's location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * @return a list containing the venue's categories.
     */
    public List<Category> getCategories()
    {
        return categories;
    }

    /**
     * @return the referral id of the venue.
     */
    public String getReferralId()
    {
        return referralId;
    }

    /**
     * @return if the venue has a perk.
     */
    public Boolean getHasPerk()
    {
        return hasPerk;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return id.equals(venue.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    @Override
    public String toString()
    {
        return "Venue{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", categories=" + categories +
                ", referralId='" + referralId + '\'' +
                ", hasPerk=" + hasPerk +
                '}';
    }
}
