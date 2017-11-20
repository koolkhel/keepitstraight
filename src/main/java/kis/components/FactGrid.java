package kis.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.slf4j.Logger;
import kis.model.Fact;
import kis.model.Domain;
import kis.model.Project;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 12.05.2009
 * Time: 19:17:01
 *
 * Грид, показывающий факты с правильно расставленными ссылками и так далее
 */
public class FactGrid {
    @Parameter(required=true, allowNull = false)
    private GridDataSource facts;

    @Inject
    private Logger logger;

    private Fact fact;

    public GridDataSource getFacts() {
        return facts;
    }

    public void setFacts(GridDataSource source) {
        this.facts = source;
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

    public Object getProjectId() {
        Project project = fact.getProject();
        if (project != null) {
            return project.getId();
        } else {
            return null;
        }
    }
}
