package kis.pages.knowledge.domains;

import kis.model.Domain;
import kis.data.DomainDAO;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Persist;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 13.05.2009
 * Time: 16:45:47
 */
public class DomainsEdit {
    @Persist
    private Long id;

    @Inject private Logger logger;

    private Domain domain;

    @Inject private DomainDAO domainDAO;

    public void onActivate() {
        if (id != null) {
            domain = domainDAO.findById(id);
        }
    }

    public void onActivate(Long id) {
        this.id = id;
        domain = domainDAO.findById(id);
    }

    public Long onPassivate() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public List<Domain> getParents() {
          return domainDAO.getAllParents(domain.getId());
    }

    public Object onSuccess() {
        domainDAO.saveOrUpdate(domain);
        return DomainsIndex.class;
    }
}
