package kis.pages.projects;

import kis.model.Project;
import kis.model.Fact;
import kis.data.ProjectDAO;
import kis.data.FactDAO;
import kis.components.FactGrid;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 12.05.2009
 * Time: 19:02:39
 */
public class ProjectsDetail {
    private Long id;

    private Project project;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private FactDAO factDAO;

    public Object onDeleteProject() {
        // для связанных фактов ставим родителя 0
        List<Fact> facts = getFacts();
        for (Fact fact : facts) {
            fact.setProject(null);
            factDAO.saveOrUpdate(fact);
        }
        projectDAO.delete(project);
        return ProjectsIndex.class;
    }
    
    public void onActivate(Long id) {
        this.id = id;
        project = projectDAO.findById(id);
    }

    public Integer getFactCount() {
        return factDAO.countFactsByProject(project);
    }

    public List<Fact> getFacts() {
        return factDAO.getFactsByProject(project);
    }

    public Long onPassivate() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
