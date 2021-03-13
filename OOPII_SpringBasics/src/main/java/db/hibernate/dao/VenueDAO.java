package db.hibernate.dao;

import api.foursquare.entities.Venue;
import db.entities.VenueDB;
import db.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO MAKE CHANGES
public class VenueDAO implements DAOInterface<VenueDB,String>
{

    private final Session session;
    private final Transaction tx;

    public VenueDAO()
    {
        this.session = HibernateUtil.getSession();
        this.tx = session.beginTransaction();
    }

    @Override
    public List<VenueDB> getAll()
    {
        return session.createQuery("FROM VenueDB" , VenueDB.class).list();
    }

    @Override
    public String insert(VenueDB element)
    {
        session.createNativeQuery(
                "INSERT IGNORE INTO venue VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, element.getId())
                .setParameter(2, element.getName())
                .setParameter(3, element.getAddress())
                .setParameter(4, element.getLat())
                .setParameter(5, element.getLng())
                .setParameter(6, element.getCategory())
                .executeUpdate();
        return null;
    }

    @Override
    public VenueDB findByPK(String primaryKey)
    {
        return null;
    }

    @Override
    public Set<String> insert(Set<VenueDB> elements)
    {
        return null;
    }

    @Override
    public void delete(String primaryKey)
    {
        VenueDB venue = new VenueDB();
        venue.setId(primaryKey);
        Transaction tx = session.beginTransaction();
        session.delete(venue);
        tx.commit();
    }


    public int deleteAll()
    {
        Query query = session.createNativeQuery("delete from venue");
        return query.executeUpdate();
    }

    @Override
    public void update(VenueDB element)
    {
        session.update(element);
    }

    public void close()
    {
        tx.commit();
        session.close();
    }
}
