package kis.pages.knowledge.domains;

import kis.model.Domain;
import kis.model.Fact;
import kis.data.DomainDAO;
import kis.data.FactDAO;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 13.05.2009
 * Time: 20:09:21
 */
public class DomainsDetail {
    private Long id;

    private Domain domain;

    @Inject private DomainDAO domainDAO;

    @Inject private FactDAO factDAO;

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

    public List<Fact> getFacts() {
        return domain.getFacts();
    }

    public Integer getFactCount() {
        return domain.getFacts().size();
    }

    public List<Domain> getDomains() {
        return domainDAO.getAllParents(id);
    }
}
