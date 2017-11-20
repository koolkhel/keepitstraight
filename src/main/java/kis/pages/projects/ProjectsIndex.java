package kis.pages.projects;

import kis.model.Project;
import kis.data.ProjectDAO;

import java.util.List;

import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.services.Cookies;
import org.slf4j.Logger;

/**
 * Created by
 * User: yury
 * at
 * Date: 08.05.2009
 * Time: 0:26:06
 */
public class ProjectsIndex {
    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private PropertyAccess access;

    @Persist
    private Project project;

    @Inject
    private Logger logger;

    @Inject
    private Cookies cookies;

    private Integer counter;

    private List<Project> projects;

    public ProjectsIndex() {
    }

    public List<Project> getProjects() {
        return projectDAO.findAll();
    }


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
