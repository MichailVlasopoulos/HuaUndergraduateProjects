package db.hibernate.dao;

import db.entities.CategoryDB;
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

public class CategoryDAO implements DAOInterface<CategoryDB,String>
{
    private final Session session;

    public CategoryDAO()
    {
        this.session = HibernateUtil.getSession();
    }

    @Override
    public CategoryDB findByPK(String primaryKey)
    {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CategoryDB> criteria = cb.createQuery(CategoryDB.class);

        Root<CategoryDB> categoryRoot = criteria.from(CategoryDB.class);
        criteria.select(categoryRoot);
        criteria.where(cb.equal(categoryRoot.get("category_id"), primaryKey));

        Query<CategoryDB> query = session.createQuery(criteria);
        List<CategoryDB> resultList = query.getResultList();
        if(resultList.isEmpty())
            return null;
        else
            return resultList.get(0);
    }

    @Override
    public List<CategoryDB> getAll()
    {
        return session.createQuery("from CategoryDB" , CategoryDB.class).list();
    }

    @Override
    public String insert(CategoryDB element)
    {
        Transaction tx = session.beginTransaction();
        String id = (String)session.save(element);
        tx.commit();
        return id;
    }

    @Override
    public Set<String> insert(Set<CategoryDB> element)
    {
        Set<String> ids = new HashSet<>();
        Transaction tx = session.beginTransaction();
        for(CategoryDB category : element)
        {
            String id = (String)session.save(category);
            ids.add(id);
        }
        tx.commit();
        return ids;
    }

    @Override
    public void delete(String primaryKey)
    {
        CategoryDB category = new CategoryDB();
        category.setCategory_id(primaryKey);
        Transaction tx = session.beginTransaction();
        session.delete(category);
        tx.commit();
    }

    public int deleteAll()
    {
        Transaction tx = session.beginTransaction();
        Query query = session.createNativeQuery("delete from category");
        int rowsAffected = query.executeUpdate();
        tx.commit();
        return rowsAffected;
    }

    @Override
    public void update(CategoryDB element)
    {
        Transaction tx = session.beginTransaction();
        session.update(element);
        tx.commit();
    }

    public List<CategoryDB> viewMainCategories()
    {
        Query<CategoryDB> query = session.createNativeQuery("SELECT * FROM VIEW_PARENT_CATEGORIES",CategoryDB.class);
        return query.list();
    }

    public String getParentCategoryID(CategoryDB category)
    {
        return (String)session.createNativeQuery("SELECT parent_category FROM category WHERE category_id = ?")
                .setParameter(1,category.getCategory_id())
                .getSingleResult();
    }

    public void closeConnection()
    {
        if (session != null)
            session.close();
    }
}
