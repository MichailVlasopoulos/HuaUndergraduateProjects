package api.foursquare.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Class that represents a Foursquare LabeledLatLng object.
 * @author delta
 * @see <a href="https://developer.foursquare.com/docs/api/venues/search">Foursquare Docs</a>
 */
public class LabeledLatLng implements Serializable
{
        @SerializedName("label")
        @Expose
        private String label;

        @SerializedName("lat")
        @Expose
        private double lat;

        @SerializedName("lng")
        @Expose
        private double lng;

        private final static long serialVersionUID = -4855238559772006611L;

        /**
         * @return the label.
         */
        public String getLabel()
        {
                return label;
        }

        /**
         * @return the latitude.
         */
        public double getLat()
        {
                return lat;
        }

        /**
         * @return the longitude.
         */
        public double getLng()
        {
                return lng;
        }

        @Override
        public String toString()
        {
                return "LabeledLatLng{" +
                        "label='" + label + '\'' +
                        ", lat=" + lat +
                        ", lng=" + lng +
                        '}';
        }
}
