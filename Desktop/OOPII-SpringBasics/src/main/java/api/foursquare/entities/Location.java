package api.foursquare.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class that represents a Foursquare Location object.
 * @author delta
 * @see <a href="https://developer.foursquare.com/docs/api/venues/search">Foursquare Docs</a>
 */
public class Location implements Serializable
{
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    @SerializedName("labeledLatLngs")
    @Expose
    private List<LabeledLatLng> labeledLatLngs = null;

    @SerializedName("distance")
    @Expose
    private int distance;

    @SerializedName("postalCode")
    @Expose
    private String postalCode;

    @SerializedName("cc")
    @Expose
    private String cc;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("formattedAddress")
    @Expose
    private List<String> formattedAddress = null;

    @SerializedName("crossStreet")
    @Expose
    private String crossStreet;

    private final static long serialVersionUID = -9863546099146547L;

    /**
     * @return the address of the venue.
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @return the latitude of the venue.
     */
    public double getLat()
    {
        return lat;
    }

    /**
     * @return the longitude of the venue.
     */
    public double getLng()
    {
        return lng;
    }

    /**
     * @return the {@link LabeledLatLng} object.
     */
    public List<LabeledLatLng> getLabeledLatLngs()
    {
        return labeledLatLngs;
    }

    /**
     * @return the distance of the venue from the user.
     */
    public int getDistance()
    {
        return distance;
    }

    /**
     * @return the postal code of the venue.
     */
    public String getPostalCode()
    {
        return postalCode;
    }

    /**
     * @return the country code of the venue's country.
     */
    public String getCc()
    {
        return cc;
    }

    /**
     * @return the city where the venue is located.
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @return the state where the venue is located.
     */
    public String getState()
    {
        return state;
    }

    /**
     * @return the country where the venue is located.
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * @return the formatted address of the venue.
     */
    public List<String> getFormattedAddress()
    {
        return formattedAddress;
    }

    /**
     * @return the cross street of the venue.
     */
    public String getCrossStreet()
    {
        return crossStreet;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.lat, lat) == 0 &&
                Double.compare(location.lng, lng) == 0 &&
                distance == location.distance &&
                Objects.equals(address, location.address) &&
                Objects.equals(labeledLatLngs, location.labeledLatLngs) &&
                Objects.equals(postalCode, location.postalCode) &&
                Objects.equals(cc, location.cc) &&
                Objects.equals(city, location.city) &&
                Objects.equals(state, location.state) &&
                Objects.equals(country, location.country) &&
                Objects.equals(formattedAddress, location.formattedAddress) &&
                Objects.equals(crossStreet, location.crossStreet);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(address, lat, lng, labeledLatLngs, distance, postalCode, cc, city, state, country, formattedAddress, crossStreet);
    }

    @Override
    public String toString()
    {
        return "Location{" +
                "address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", labeledLatLngs=" + labeledLatLngs +
                ", distance=" + distance +
                ", postalCode='" + postalCode + '\'' +
                ", cc='" + cc + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", formattedAddress=" + formattedAddress +
                ", crossStreet='" + crossStreet + '\'' +
                '}';
    }
}