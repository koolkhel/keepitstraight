package kis.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import kis.model.Domain;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 01.05.2009
 * Time: 17:01:23
 *
 * Нужен, чтобы показывать предметные области списком через запятую
 *
 * TODO вообще как-то нужно родителей ещё показывать
 */
public class DomainsEnum {
    @Parameter(required = true)
    private List<Domain> domains;

    @Inject
    private Logger logger;

    @Inject
    private ComponentResources componentResources;

    /**
     * вообще тут ссылки можно сделать на страницы, соответствующие
     * предметным областям
     * @param writer
     */
    public void beginRender(MarkupWriter writer) {
        if ((domains != null) && domains.size() != 0) {
            Link link = componentResources.createPageLink("knowledge/domains/detail",
                    false, domains.get(0).getId());
            writer.element("a", "href", link.toAbsoluteURI());
            writer.write(domains.get(0).getDescription());
            writer.end();

            for (int i = 1; i < domains.size(); i++) {
                writer.write(", ");
                link = componentResources.createPageLink("knowledge/domains/detail",
                    false, domains.get(i).getId());
                writer.element("a", "href", link.toAbsoluteURI());
                writer.write(domains.get(i).getDescription());
                writer.end();
            }
        }

        //writer.end говорить не надо, потому что мы тэгов не открывали
    }

    public List<Domain> getDomains() {
        return domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }
}
