package api.openweathermap.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind
{
    @SerializedName("speed")
    @Expose
    private Double speed;

    @SerializedName("deg")
    @Expose
    private Integer deg;

    public Double getSpeed()
    {
        return speed;
    }

    public Integer getDeg()
    {
        return deg;
    }
}