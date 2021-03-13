package service;

import api.recombee.RecombeeUserUtils;
import com.recombee.api_client.exceptions.ApiException;
import db.entities.AbstractUserDB;
import db.hibernate.dao.UserDAO;
import org.hibernate.HibernateException;

public class UserService
{
    private UserDAO dao = new UserDAO();

    public void createUser(AbstractUserDB user)
    {
        Integer id;
        try
        {
            id = dao.insert(user);
        }
        catch (HibernateException e)
        {
            throw new RuntimeException();
        }

        try
        {
            RecombeeUserUtils.insert(id.toString() ,user);
        }
        catch (ApiException e)
        {
            throw new RuntimeException();
        }

    }

    public void deleteUser(Integer id)
    {
        try
        {
            dao.delete(id);
        }
        catch (HibernateException e)
        {
            throw new RuntimeException();
        }

        try
        {
            RecombeeUserUtils.delete(id.toString());
        }
        catch (ApiException e)
        {
            throw new RuntimeException();
        }
    }

    public void closeService()
    {
        dao.closeConnection();
    }
}
