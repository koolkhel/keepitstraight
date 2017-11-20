package kis.pages.projects;

import kis.model.Project;
import kis.data.ProjectDAO;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Date;

/**
 * Created by
 * User: yury
 * at
 * Date: 12.05.2009
 * Time: 21:12:54
 */
public class ProjectsEdit {
    @Property
    private Project project;

    @Inject
    private ProjectDAO projectDAO;

    public Object onSuccess() {
        projectDAO.saveOrUpdate(project);
        return ProjectsIndex.class;
    }

    public void onActivate(Long id) {
        project = projectDAO.findById(id);
    }

    public Long onPassivate() {
        return project.getId();
    }

    public Object onFinishProject() {
        project.setOpened(false);
        project.setFinish(new Date());
        projectDAO.saveOrUpdate(project);
        return this;
    }
}
