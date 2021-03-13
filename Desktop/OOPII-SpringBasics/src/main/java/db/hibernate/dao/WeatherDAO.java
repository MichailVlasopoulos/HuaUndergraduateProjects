package db.hibernate.dao;

import db.entities.WeatherDB;
import db.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mihalis Vlasopoulos
 */
public class WeatherDAO implements DAOInterface<WeatherDB,Integer>
{
    @Override
    public WeatherDB findByPK(Integer primaryKey)
    {
        return null;
    }

    private final Session session;
    private final Transaction tx;

    public WeatherDAO()
    {
        this.session = HibernateUtil.getSession();
        this.tx = session.beginTransaction();
    }

    @Override
    public List<WeatherDB> getAll()
    {
        return null;
    }

    @Override
    public Integer insert(WeatherDB element)
    {
        return (Integer)session.save(element);
    }

    @Override
    public Set<Integer> insert(Set<WeatherDB> elements)
    {
        return null;
    }

    @Override
    public void delete(Integer primaryKey)
    {
        WeatherDB weather = new WeatherDB();
        weather.setId(primaryKey);

        Transaction tx = session.beginTransaction();
        session.delete(weather);
        tx.commit();
    }

    @Override
    public void update(WeatherDB element)
    {
        session.update(element);
    }

    public void closeConnection()
    {
        session.close();
    }

}
