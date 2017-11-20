package kis.data;

import kis.model.AbstractEntity;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import java.util.List;

public interface AbstractEntityDAO<T extends AbstractEntity> {
    @CommitAfter
    public void save(T entity);

    @CommitAfter
    public void saveOrUpdate(T entity);

    public T findById(Long id);

    public List<T> findAll();

    /**
     * считает, сколько всего есть базовой сущности в базе данных
     * @return
     */
    public Long count();

    @CommitAfter
    public void delete(T entity);
}
