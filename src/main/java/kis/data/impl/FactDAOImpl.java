package kis.data.impl;

import kis.data.FactDAO;
import kis.model.Fact;
import kis.model.Project;
import kis.model.Domain;
import org.hibernate.Criteria;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import java.util.List;

public class FactDAOImpl extends AbstractEntityDAOImpl<Fact> implements FactDAO {
    /*
    * в первом приближении тут методов вроде как и нет :)
    */
    @SuppressWarnings("uncheched")
    public Integer countFactsByProject(Project project) {
        return (Integer) getSession()
                .createCriteria(Fact.class)
                .createAlias("project", "p")
                .add(Restrictions.eq("p.id", project.getId()))
                .setProjection(Projections.countDistinct("id"))
                .list()
                .get(0);
    }

    @SuppressWarnings("uncheched")
    public Integer countFactsByDomain(Domain domain) {
        return (Integer) getSession()
                .createCriteria(Fact.class)
                .createAlias("domain", "d")
                .add(Restrictions.eq("d.id", domain.getId()))
                .setProjection(Projections.countDistinct("id"))
                .list()
                .get(0);
    }

    public List<Fact> getFactsByDomain(Domain domain) {
        return getSession()
                .createCriteria(Fact.class)
                .createAlias("domain", "d")
                .add(Restrictions.eq("d.id", domain.getId()))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }

    public List<Fact> getFactsByProject(Project project) {
        return getSession()
                .createCriteria(Fact.class)
                .createAlias("project", "p")
                .add(Restrictions.eq("p.id", project.getId()))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }
}
