package db.entities;

import api.openweathermap.entities.WeatherInfo;

import javax.persistence.*;

/**
 * Class that represents a city's weather in our database schema.
 * @author Mihalis Vlasopoulos
 */
@Entity
@Table(name="weather")
public class WeatherDB
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="weather_id")
    private int id;
    @Column(name="temperature", length = 6)
    private double temp;
    @Column(name="main", length = 45)
    private String main;
    @Column(name="detail", length = 50)
    private String detail;
    @Column(name = "icon_id", length = 3)
    private String icon;

    public int getId()
    {
        return id;
    }
    public double getTemp()
    {
        return temp;
    }
    public String getMain()
    {
        return main;
    }
    public String getDetail()
    {
        return detail;
    }
    public String getIcon()
    {
        return icon;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    public void setTemp(double temp)
    {
        this.temp = temp;
    }
    public void setMain(String main)
    {
        this.main = main;
    }
    public void setDetail(String detail)
    {
        this.detail = detail;
    }
    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    /**
     * Class that provides methods for converting an OpenWeatherMap WeatherInfo entity into
     * it's DB equivalent.
     * @author Mihalis Vlasopoulos
     */
    public static class WeatherFilter
    {
        /**
         * Filters an WeatherInfo object and converts it to an WeatherDB entity.
         * @param weather the OpenWeatherMap weather
         * @return the WeatherDB equivalent entity
         */
        public static WeatherDB filter(WeatherInfo weather)
        {
            String main = weather.getWeather().get(0).getMain();
            String detail = weather.getWeather().get(0).getDescription();
            String icon = weather.getWeather().get(0).getIcon();
            double temp = weather.getMain().getTemp();

            WeatherDB filteredWeather = new WeatherDB();
            filteredWeather.setMain(main);
            filteredWeather.setDetail(detail);
            filteredWeather.setIcon(icon);
            filteredWeather.setTemp(temp);

            return filteredWeather;
        }
    }
}
