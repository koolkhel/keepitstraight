package kis.data.impl;

import kis.data.AbstractEntityDAO;
import kis.model.AbstractEntity;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class AbstractEntityDAOImpl<T extends AbstractEntity>  implements AbstractEntityDAO<T> {
    protected Class<T> domainClass = getDomainClass();

    @Inject
    protected Session session;

    public void save(T entity) {
        session.persist(entity);
    }

    public void saveOrUpdate(T entity) {
        if (entity.getId() != null) {
            session.update(entity);
        } else {
            session.persist(entity);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        Criteria criteria = session.createCriteria(domainClass);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public T findById(Long id) {
        List<T> results = session.createCriteria(domainClass).add(Restrictions.eq("id", id)).list();
        if (results.size() == 0) {
            return null;
        } else {
            return results.get(0);
        }
    }

    public void delete(T entity) {
        session.delete(entity);
    }

    @SuppressWarnings("unchecked")
    public Long count() {
        Criteria criteria = session.createCriteria(domainClass);
        return (Long) criteria.setProjection(Projections.countDistinct("id")).list().get(0);
    }

    protected Session getSession() {
        return session;
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getDomainClass() {
        if (domainClass == null) {
            ParameterizedType thisType = (ParameterizedType) getClass()
                    .getGenericSuperclass();
            domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
        }
        return domainClass;
    }
}
