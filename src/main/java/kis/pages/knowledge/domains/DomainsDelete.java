package kis.pages.knowledge.domains;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Persist;
import kis.data.DomainDAO;
import kis.data.FactDAO;
import kis.model.Domain;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 13.05.2009
 * Time: 16:45:56
 */
public class DomainsDelete {
    @Inject private DomainDAO domainDAO;

    private String message;

    private Domain domain;

    public void onActivate() {
        domain = domainDAO.findById(id);        
    }

    @Persist
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object onSubmitFromYes() {
        if (domain.getFacts().size() == 0) {
            Domain parent = null;
            //if ((parent = domain.getParent()) != null) {
            //    parent.getChildren().remove(domain);
            //}
            domainDAO.delete(domain);
            //domainDAO.saveOrUpdate(parent);
        } else {
            message = "Нельзя удалять домены, в которых содержатся факты";
            return this;
        }
        return DomainsIndex.class;
    }

    public Object onSubmitFromNo() {
        return DomainsIndex.class;
    }

    public List<Domain> getDomains() {
        return domainDAO.getAllParents(id);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
