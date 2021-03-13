package db.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Class that represents the preferences of a user based on our database schema
 * @author delta , Mihalis Vlasopoulos
 */
@Entity
@Table(name = "category_preference")
public class UserPreferenceDB implements Serializable
{
    private static final long serialVersionUID = -4777467492937464329L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private int id;

    @Column(name = "likes_entertainment")
    private int likes_entertainment;

    @Column(name = "likes_events")
    private int likes_events;

    @Column(name = "likes_food")
    private int likes_food;

    @Column(name = "likes_nightlife")
    private int likes_nightlife;

    @Column(name = "likes_outdoors")
    private int likes_outdoors;

    @Column(name = "likes_professional")
    private int likes_professional;

    @Column(name = "likes_residences")
    private int likes_residences;

    @Column(name = "likes_shops")
    private int likes_shops;

    @Column(name = "likes_travel")
    private int likes_travel;

    @Column(name = "likes_universities")
    private int likes_universities;

    public int getId()
    {
        return id;
    }
    public int getLikes_entertainment()
    {
        return likes_entertainment;
    }
    public int getLikes_events()
    {
        return likes_events;
    }
    public int getLikes_food()
    {
        return likes_food;
    }
    public int getLikes_nightlife()
    {
        return likes_nightlife;
    }
    public int getLikes_outdoors()
    {
        return likes_outdoors;
    }
    public int getLikes_professional()
    {
        return likes_professional;
    }
    public int getLikes_residences()
    {
        return likes_residences;
    }
    public int getLikes_shops()
    {
        return likes_shops;
    }
    public int getLikes_travel()
    {
        return likes_travel;
    }
    public int getLikes_universities()
    {
        return likes_universities;
    }

    public void setLikes_entertainment(int likes_entertainment)
    {
        this.likes_entertainment = likes_entertainment;
    }
    public void setLikes_events(int likes_events)
    {
        this.likes_events = likes_events;
    }
    public void setLikes_food(int likes_food)
    {
        this.likes_food = likes_food;
    }
    public void setLikes_nightlife(int likes_nightlife)
    {
        this.likes_nightlife = likes_nightlife;
    }
    public void setLikes_outdoors(int likes_outdoors)
    {
        this.likes_outdoors = likes_outdoors;
    }
    public void setLikes_professional(int likes_professional)
    {
        this.likes_professional = likes_professional;
    }
    public void setLikes_residences(int likes_residences)
    {
        this.likes_residences = likes_residences;
    }
    public void setLikes_shops(int likes_shops)
    {
        this.likes_shops = likes_shops;
    }
    public void setLikes_travel(int likes_travel)
    {
        this.likes_travel = likes_travel;
    }
    public void setLikes_universities(int likes_universities)
    {
        this.likes_universities = likes_universities;
    }
}
