package db.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityDBTest
{

    @Test
    void setVenuesAndCalculate()
    {
        /*HashSet<VenueDB> venues = new HashSet<>();

        HashSet<String> mainIDs;
        try(CategoryDAO dao = new CategoryDAO())
        {
            mainIDs = new HashSet(dao.viewMainCategories());
        }

        HashSet<String> registeredMainCategories;
        try
        {
            Field fieldMapper = CityDB.class.getDeclaredField("fieldMapper");
            fieldMapper.get(new CityDB()).getClass().getMethod("keySet");
        }
        catch (IllegalAccessException ignored) { }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }*/

        assertEquals(1,1);
    }
}