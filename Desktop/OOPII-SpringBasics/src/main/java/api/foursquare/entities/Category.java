package api.foursquare.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.*;

/**
 * Class that represents a Foursquare Category object.
 * @author delta
 * @see <a href="https://developer.foursquare.com/docs/api/venues/categories">Foursquare Docs: Category Request</a>
 * @see <a href="https://developer.foursquare.com/docs/resources/categories">Foursquare Docs: Category hierarchy</a>
 */
public class Category implements Serializable
{
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("pluralName")
    @Expose
    private String pluralName;

    @SerializedName("shortName")
    @Expose
    private String shortName;

    @SerializedName("icon")
    @Expose
    private Icon icon;

    @SerializedName("primary")
    @Expose
    private Boolean primary;

    @SerializedName("categories")
    @Expose
    private List<Category> subCategories;

    private final static long serialVersionUID = -5902990814146207098L;

    /**
     * @return The unique id of the category.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return The name of the category.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the pluralName of the category.
     */
    public String getPluralName()
    {
        return pluralName;
    }
    /**
     * @return the shortName of the category.
     */
    public String getShortName()
    {
        return shortName;
    }

    /**
     * @return the Icon object of the category.
     */
    public Icon getIcon()
    {
        return icon;
    }

    /**
     * @return if this is the primary category of a venue.
     * This field is null if a get venue categories request is made.
     */
    public Boolean isPrimary()
    {
        return primary;
    }

    /**
     * @return the subcategories of the category.
     * In case there aren't, null is returned.
     */
    public List<Category> getSubCategories()
    {
        return subCategories;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    @Override
    public String toString()
    {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pluralName='" + pluralName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", icon=" + icon +
                ", primary=" + primary +
                '}';
    }

}