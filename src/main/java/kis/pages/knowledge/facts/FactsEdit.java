package kis.pages.knowledge.facts;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.Cookies;
import org.slf4j.Logger;
import kis.data.FactDAO;
import kis.data.ProjectDAO;
import kis.model.Fact;
import kis.model.Project;
import kis.util.GenericSelectModel;

/**
 * Created by
 * User: yury
 * at
 * Date: 14.05.2009
 * Time: 15:33:09
 *
 * автором будет считаться тот, кто редактировал факт последним
 */
public class FactsEdit {
    @Inject private FactDAO factDAO;

    @Inject private Logger logger;

    @Inject private ProjectDAO projectDAO;

    @Inject private PropertyAccess access;

    @Inject private Cookies cookies;

    private GenericSelectModel<Project> projects;

    private Project project;
    
    private Long id;

    private Fact fact;

    public void onPrepare() {
        projects = new GenericSelectModel<Project>(
                // типа только открытые проекты
                projectDAO.findOpenedProjects(),
                Project.class,
                "title",
                "id",
                access);
        String cookie = cookies.readCookieValue("projectId");
        if (cookie != null) {
            project = projectDAO.findById(Long.parseLong(cookie));
            if (!project.isOpened()) {
                project = null;
            }
        }
    }

    public Object onSuccessFromEditFact() {
        fact.setProject(project);
        factDAO.saveOrUpdate(fact);
        return FactsList.class; // надо как-то на сам факт лучше вернуться, ну да ладно
    }

    public void onActivate(Long id) {
        this.id = id;
        fact = factDAO.findById(id);
    }

    public Long onPassivate() {
        return id;
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public GenericSelectModel<Project> getProjects() {
        return projects;
    }

    public void setProjects(GenericSelectModel<Project> projects) {
        this.projects = projects;
    }

    public Object onDeleteFact() {
        factDAO.delete(fact);
        return FactsList.class;
    }
}
