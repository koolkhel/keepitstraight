package kis.data.impl;

import kis.data.DomainDAO;
import kis.model.Domain;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.hibernate.Session;
import org.slf4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DomainDAOImpl extends AbstractEntityDAOImpl<Domain> implements DomainDAO {
    private Logger logger;

    public DomainDAOImpl(Logger log, Session sess) {
        logger = log;
        session = sess;
    }

    @SuppressWarnings("unchecked")
    public List<Domain> getSubtree(final Long id) {
        // здесь когда-то жил баг
        List<Domain> children = getSession()
                .createCriteria(Domain.class)
                .createAlias("parent", "p")
                .add(Restrictions.eq("p.id", id))
                .list();
        return children;
    }

    @SuppressWarnings("unchecked")
    public List<Domain> getRootElements() {
        return getSession()
                .createCriteria(Domain.class)
                .add(Restrictions.isNull("parent"))
                .addOrder(Order.asc("description"))
                .list();
    }

    public List<Domain> getAllParents(Long id) {
        if (id != null) {
            Domain domain = findById(id);
            List<Domain> result = new ArrayList<Domain>();
            result.add(domain);
            lineupParents(domain, result);
            Collections.reverse(result);
            return result;
        } else {
            return null;
        }
    }

    protected void lineupParents(Domain domain, List<Domain> result) {
        if (domain.getParent() != null) {
            result.add(domain.getParent());
            lineupParents(domain.getParent(), result);
        }
    }
}
