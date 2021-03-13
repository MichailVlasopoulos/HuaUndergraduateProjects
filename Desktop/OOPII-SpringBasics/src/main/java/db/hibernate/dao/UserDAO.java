package db.hibernate.dao;

import db.entities.AbstractUserDB;
import db.entities.CategoryDB;
import db.entities.TouristUserDB;
import db.hibernate.HibernateUtil;
import db.hibernate.dao.DAOInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDAO implements DAOInterface<AbstractUserDB,Integer>
{
    private final Session session;

    public UserDAO()
    {
        this.session = HibernateUtil.getSession();
    }

    @Override
    public AbstractUserDB findByPK(Integer primaryKey)
    {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<AbstractUserDB> criteria = cb.createQuery(AbstractUserDB.class);

        Root<AbstractUserDB> categoryRoot = criteria.from(AbstractUserDB.class);
        criteria.select(categoryRoot);
        criteria.where(cb.equal(categoryRoot.get("id"), primaryKey));

        Query<AbstractUserDB> query = session.createQuery(criteria);
        List<AbstractUserDB> resultList = query.getResultList();
        if(resultList.isEmpty())
            return null;
        else
            return resultList.get(0);
    }

    @Override
    public List<AbstractUserDB> getAll()
    {
        return session.createQuery("from AbstractUserDB" , AbstractUserDB.class).list();
    }

    @Override
    public Integer insert(AbstractUserDB element)
    {
        Transaction tx = session.beginTransaction();
        Integer id = (Integer)session.save(element);
        tx.commit();
        return id;
    }

    @Override
    public Set<Integer> insert(Set<AbstractUserDB> elements)
    {
        Set<Integer> ids = new HashSet<>();
        Transaction tx = session.beginTransaction();
        for(AbstractUserDB user : elements)
        {
            Integer id = (Integer)session.save(user);
            ids.add(id);
        }
        tx.commit();
        return ids;
    }

    @Override
    public void delete(Integer primaryKey)
    {
        //TODO not 100% correct
        AbstractUserDB user =  new TouristUserDB();
        user.setId(primaryKey);

        Transaction tx = session.beginTransaction();
        session.delete(user);
        tx.commit();
    }

    @Override
    public void update(AbstractUserDB element)
    {
        session.update(element);
    }

    public void closeConnection()
    {
        if (session != null)
            session.close();
    }
}
