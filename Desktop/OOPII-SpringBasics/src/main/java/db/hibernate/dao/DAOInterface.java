package db.hibernate.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface DAOInterface<T,PK extends Serializable>
{
    T findByPK(PK primaryKey );
    List<T> getAll();
    PK insert(T element);
    Set<PK> insert(Set<T> elements);
    void delete(PK primaryKey);
    void update(T element);
    //void closeConnection();
}
