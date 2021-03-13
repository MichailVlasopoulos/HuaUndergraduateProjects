package db.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents a City entity in our database schema.
 * @author delta, Mihalis Vlasopoulos
 */
@Entity
@Table(name = "city")
public class CityDB implements Serializable
{
    private static final long serialVersionUID = 6284849014950069653L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private int id;

    @Column(name = "city_name")
    private String name;

    @Column(name = "lat" , precision = 16 , scale = 14)
    private double lat;

    @Column(name = "lng" , precision = 16 , scale = 14)
    private double lng;

    @ManyToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinTable
    (
            name = "city_venues",
            joinColumns = { @JoinColumn(name = "city_id") },
            inverseJoinColumns = { @JoinColumn(name = "venue_id") }
    )
    private Set<VenueDB> venues;

    @OneToOne(targetEntity = WeatherDB.class, cascade = CascadeType.ALL)
    @JoinTable
            (
                    name = "city_weather",
                    joinColumns = { @JoinColumn(name = "city_id") },
                    inverseJoinColumns = { @JoinColumn(name = "weather_id") }
            )
    private WeatherDB cityWeather;

    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @JoinTable
            (
                    name = "city_venue_metrics",
                    joinColumns = { @JoinColumn(name = "city_id") },
                    inverseJoinColumns = { @JoinColumn(name = "metrics_id") }
            )
    private CityMetricsDB metrics;


    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public double getLat()
    {
        return lat;
    }
    public double getLng()
    {
        return lng;
    }
    public Set<VenueDB> getVenues()
    {
        return venues;
    }
    public WeatherDB getCityWeather()
    {
        return cityWeather;
    }
    public CityMetricsDB getMetrics()
    {
        return metrics;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setLat(double lat)
    {
        this.lat = lat;
    }
    public void setLng(double lng)
    {
        this.lng = lng;
    }
    public void setVenuesAndCalculateMetrics(Set<VenueDB> venues)
    {
        this.venues = venues;
        this.metrics = new CityMetricsDB();
        this.metrics.calculateVenueMetrics(venues);
    }
    public void setCityWeather(WeatherDB cityWeather)
    {
        this.cityWeather = cityWeather;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityDB cityDB = (CityDB) o;
        return name.equals(cityDB.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name);
    }
}
