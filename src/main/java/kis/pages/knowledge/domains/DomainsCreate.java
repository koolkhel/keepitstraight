package kis.pages.knowledge.domains;

import kis.model.Domain;
import kis.data.DomainDAO;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ComponentResources;

/**
 * Created by
 * User: yury
 * at
 * Date: 13.05.2009
 * Time: 16:42:19
 *
 * создание домена
 */
public class DomainsCreate {
    @Persist
    private Long parentId;

    @Persist
    private boolean newRoot;

    private Domain newDomain;

    @Inject private DomainDAO domainDAO;

    @Inject private ComponentResources componentResources;

    public void onPrepare() {
        newDomain = new Domain();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Domain> getParents() {
        if (!newRoot) {
            return domainDAO.getAllParents(parentId);
        } else {
            return null;
        }
    }

    public Domain getNewDomain() {
        return newDomain;
    }

    public void setNewDomain(Domain newDomain) {
        this.newDomain = newDomain;
    }

    public Object onSuccess() {
        componentResources.discardPersistentFieldChanges();
        Domain parent = null;
        if (!newRoot) {
            parent = domainDAO.findById(parentId);
            newDomain.setParent(parent);
            parent.getChildren().add(newDomain);
        } else {
            newDomain.setParent(null);
        }
        domainDAO.save(newDomain);
        if (parent != null) {
            domainDAO.saveOrUpdate(parent);
        }
        return DomainsIndex.class;
    }

    public boolean isNewRoot() {
        return newRoot;
    }

    public void setNewRoot(boolean newRoot) {
        this.newRoot = newRoot;
    }
}
