package kis.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import kis.data.DomainDAO;
import kis.model.Domain;

/**
 * Created by
 * User: yury
 * at
 * Date: 13.05.2009
 * Time: 15:07:32
 *
 * может и лишний, но так прикольнее.
 *
 * показывает рекурсивно детей данного домена и выводит результат списком
 */
public class DomainChildrenList {
    @Inject
    private ComponentResources componentResources;

    @Inject
    private DomainDAO domainDAO;

    @Property
    @Parameter(required=true)
    private Domain rootDomain;
    
    @BeginRender
    public void beginRender(MarkupWriter writer) {
        writeDomainTree(rootDomain, writer);
    }

    protected void writeDomainTree(Domain root, MarkupWriter writer) {
        Link link;
        link = componentResources.createPageLink("knowledge/domains/detail", false, root.getId());
        writer.element("li");
        writer.element("a", "href", link.toAbsoluteURI());
        writer.write(root.getDescription());
        writer.end(); // a href
        writer.write(": " + root.getFacts().size() + " facts");
        writer.element("ol");
        for (Domain child : root.getChildren()) {
            writeDomainTree(child, writer);
        }
        writer.end(); // ol
        writer.end(); // li
    }
}
