package kis.components;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.BindingConstants;
import kis.model.Project;
import kis.util.GenericSelectModel;
import kis.data.ProjectDAO;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 10.05.2009
 * Time: 14:08:30
 *
 * компонент, похоже, не работает, но прикрутить бы его надо, пожалуй
 */
public class ProjectBeanSelect {
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "false")
    @Property
    private boolean openedOnly;

    @Inject
    private ProjectDAO projectDAO;

    @Inject
    private Cookies cookies;
    
    @Inject
    private PropertyAccess access;

    private GenericSelectModel<Project> projects;

    @Persist
    private Project project;

    public void setupRender() {
        List<Project> pr = null;
        if (openedOnly) {
            pr = projectDAO.findOpenedProjects();
        } else {
            pr = projectDAO.findAll();
        }
        projects = new GenericSelectModel<Project>(
                pr,
                Project.class,
                "title",
                "id",
                access);
        String cookie = cookies.readCookieValue("projectId");
        if (cookie != null) {
            project = projectDAO.findById(Long.parseLong(cookie));
        }
    }

    public GenericSelectModel<Project> getProjects() {
        return projects;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
