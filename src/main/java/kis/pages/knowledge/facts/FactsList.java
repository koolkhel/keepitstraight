package kis.pages.knowledge.facts;

import kis.data.FactDAO;
import kis.model.Fact;
import kis.model.Domain;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.beaneditor.BeanModel;

import java.util.List;
import java.util.ArrayList;

public class FactsList {
    private Fact fact;

    @Inject
    private FactDAO factDAO;

    public List<Fact> getFacts() {
        return factDAO.findAll();
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public List<Domain> getDomains() {
        return fact.getDomains();
    }

    public String getProjectTitle() {
        if (fact.getProject() == null) {
            return "";
        } else {
            return fact.getProject().getTitle();
        }
    }
}
