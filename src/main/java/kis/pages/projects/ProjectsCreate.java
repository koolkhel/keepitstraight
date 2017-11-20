package kis.pages.projects;

import kis.data.ProjectDAO;
import kis.model.Project;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Property;

import java.util.Date;

/**
 * Created by
 * User: yury
 * at
 * Date: 12.05.2009
 * Time: 21:12:15
 */
public class ProjectsCreate {
    @Property
    private String projectTitle;

    @Property
    private String projectDescription;

    @Inject
    private ProjectDAO projectDAO;

    public Object onSuccessFromCreateProject() {
        Project project = new Project();
        project.setTitle(projectTitle);
        project.setDescription(projectDescription);
        project.setOpened(true);
        project.setStart(new Date()); // текущая дата
        projectDAO.save(project);
        return ProjectsIndex.class;
    }
}
