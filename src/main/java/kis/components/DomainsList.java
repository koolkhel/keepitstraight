package kis.components;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Property;
import org.slf4j.Logger;
import kis.data.DomainDAO;
import kis.model.Domain;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 13.05.2009
 * Time: 14:59:33
 *
 * список доменов для страницы domainsindex
 */
public class DomainsList {
    @Inject
    private DomainDAO domainDAO;

    @Inject
    private Logger logger;

    @Property
    private Domain rootDomain;

    @BeginRender
    public void beginRender(MarkupWriter writer) {

    }

    @AfterRender
    public void afterRender(MarkupWriter writer) {
        
    }

    public List<Domain> getRootDomains() {
        return domainDAO.getRootElements();
    }
}
