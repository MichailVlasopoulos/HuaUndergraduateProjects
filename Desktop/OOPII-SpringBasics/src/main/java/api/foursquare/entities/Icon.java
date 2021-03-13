package api.foursquare.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class that represents an Foursquare category icon object.
 * @author delta
 * @see <a href="https://developer.foursquare.com/docs/api/venues/categories">Foursquare Docs</a>
 */
public class Icon implements Serializable
{
    @SerializedName("prefix")
    @Expose
    private URI prefix;

    @SerializedName("suffix")
    @Expose
    private String suffix;

    private final static long serialVersionUID = 3975151974470719788L;

    public URI getPrefix()
    {
        return prefix;
    }

    /**
     * Returns the full URI of the icon.
     * Please note that it returns only the 64x64 icon.
     * @return the URI.
     */
    public URI getUri()
    {
        try
        {
            return new URI(prefix + "bg_64" + suffix);
        }
        catch (URISyntaxException e)
        {
            return null;
        }
    }

    /**
     * @return the suffix.
     */
    public String getSuffix()
    {
        return suffix;
    }

    @Override
    public String toString()
    {
        return "Icon{" +
                "prefix=" + prefix +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
