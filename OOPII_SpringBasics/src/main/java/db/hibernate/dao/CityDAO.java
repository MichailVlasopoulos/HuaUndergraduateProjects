package db.hibernate.dao;

import db.entities.CategoryDB;
import db.entities.CityDB;
import db.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CityDAO implements DAOInterface<CityDB,Integer>
{
    private final Session session;

    public CityDAO()
    {
        this.session = HibernateUtil.getSession();;
    }

    @Override
    public CityDB findByPK(Integer primaryKey)
    {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CityDB> criteria = cb.createQuery(CityDB.class);

        Root<CityDB> cityRoot = criteria.from(CityDB.class);
        criteria.select(cityRoot);
        criteria.where(cb.equal(cityRoot.get("id"), primaryKey));

        Query<CityDB> query = session.createQuery(criteria);
        List<CityDB> resultList = query.getResultList();
        if(resultList.isEmpty())
            return null;
        else
            return resultList.get(0);
    }

    @Override
    public List<CityDB> getAll()
    {
        return session.createQuery("from CityDB" , CityDB.class).list();
    }

    @Override
    public Integer insert(CityDB element)
    {
        Transaction tx = session.beginTransaction();
        Integer id = (Integer)session.save(element);
        tx.commit();
        return id;
    }

    @Override
    public Set<Integer> insert(Set<CityDB> elements)
    {
        Set<Integer> ids = new HashSet<>();
        Transaction tx = session.beginTransaction();
        for(CityDB city : elements)
        {
            Integer id = (Integer)session.save(city);
            ids.add(id);
        }
        tx.commit();
        return ids;
    }

    @Override
    public void delete(Integer primaryKey)
    {
        CityDB city = new CityDB();
        city.setId(primaryKey);
        Transaction tx = session.beginTransaction();
        session.delete(city);
        tx.commit();
    }

    @Override
    public void update(CityDB element)
    {
        Transaction tx = session.beginTransaction();
        session.update(element);
        tx.commit();
    }

    public void closeConnection()
    {
        if (session != null)
            session.close();
    }
}
