package api.openweathermap.entities;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherInfo
{
    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("weather")
    @Expose
    private final List<Weather> weather = null;

    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("visibility")
    @Expose
    private Integer visibility;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("dt")
    @Expose
    private Integer dt;

    @SerializedName("sys")
    @Expose
    private Sys sys;

    @SerializedName("timezone")
    @Expose
    private Integer timezone;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("cod")
    @Expose
    private Integer cod;

    public Coord getCoord()
    {
        return coord;
    }

    public List<Weather> getWeather()
    {
        return weather;
    }

    public String getBase()
    {
        return base;
    }

    public Main getMain()
    {
        return main;
    }

    public Integer getVisibility()
    {
        return visibility;
    }

    public Wind getWind()
    {
        return wind;
    }

    public Clouds getClouds()
    {
        return clouds;
    }

    public Integer getDt()
    {
        return dt;
    }

    public Sys getSys()
    {
        return sys;
    }

    public Integer getTimezone()
    {
        return timezone;
    }

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Integer getCod()
    {
        return cod;
    }
}
