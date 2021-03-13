package db.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * An abstract class describing a user.
 * @author delta , Mihalis Vlasopoulos
 */
@Entity
@Table(name = "user")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractUserDB
{
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name = "user_name" , length = 50)
    protected String name;

    @Column(name = "age")
    protected int age;

    @Transient
    protected String userType;

    //These fields are constantly changing

    @Transient
    protected double currentLat;

    @Transient
    protected double currentLng;

    //TODO Likely, we will need an enum here
    @Transient
    protected String weatherPreference;

    @OneToOne(targetEntity = UserPreferenceDB.class , cascade = CascadeType.ALL)
    @JoinTable
            (
                    name = "user_category_preference",
                    joinColumns = { @JoinColumn(name = "user_id") },
                    inverseJoinColumns = { @JoinColumn(name = "preference_id") }
            )
    protected UserPreferenceDB userPreference;

    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public int getAge()
    {
        return age;
    }
    public String getUserType()
    {
        return userType;
    }
    public double getCurrentLat()
    {
        return currentLat;
    }
    public double getCurrentLng()
    {
        return currentLng;
    }
    public String getWeatherPreference()
    {
        return weatherPreference;
    }
    public UserPreferenceDB getUserPreference()
    {
        return userPreference;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setAge(int age)
    {
        this.age = age;
    }
    public void setUserType(String userType)
    {
        this.userType = userType;
    }
    public void setCurrentLat(double currentLat)
    {
        this.currentLat = currentLat;
    }
    public void setCurrentLng(double currentLng)
    {
        this.currentLng = currentLng;
    }
    public void setWeatherPreference(String weatherPreference)
    {
        this.weatherPreference = weatherPreference;
    }
    public void setUserPreference(UserPreferenceDB userPreference)
    {
        this.userPreference = userPreference;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUserDB that = (AbstractUserDB) o;
        return id == that.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    /**
     * Abstract method for simple city recommendations
     * @return an ArrayList containing the recommended cities in descending order
     */
    public abstract ArrayList<CityDB> RecommendCities();


    /**
     * Abstract method for recommending cities with weather filter applied
     * @param weatherPreference what type of weather the user wants
     * @return an ArrayList containing the recommended cities in descending order , in respect to the weather filter
     */
    public abstract ArrayList<CityDB> RecommendCities(String weatherPreference);

}
