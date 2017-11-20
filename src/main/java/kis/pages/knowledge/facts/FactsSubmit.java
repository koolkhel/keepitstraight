package kis.pages.knowledge.facts;

import kis.data.*;
import kis.model.*;
import kis.components.DomainCombos;
import kis.pages.knowledge.facts.FactsList;
import kis.util.GenericSelectModel;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.FieldFocusPriority;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.apache.tapestry5.services.Cookies;
import org.slf4j.Logger;

public class FactsSubmit {
    @Inject private Logger logger;

    @Inject private FactDAO factDAO;

    @Inject private DomainDAO domainDAO;

    private String factTitle;

    @Persist private String factText;

    private GenericSelectModel<Project> projects;

    @Inject private ProjectDAO projectDAO;

    private Project project;

    @Inject private PropertyAccess access;

    @Environmental private RenderSupport renderSupport;

    @Inject private ComponentResources resources;

    @Component(id = "domaincombos")
    private DomainCombos domainCombos;

    @Inject private Cookies cookies;

    @Property private UploadedFile file;

    @Inject private AttachedFileDAO attachedFileDAO;

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

    public void afterRender() {
        renderSupport.autofocus(FieldFocusPriority.IN_ERROR, "factTitle");
    }

    public Object onSuccessFromSubmitFact() {
        Fact fact = new Fact();
        if (file != null) {
            AttachedFile attach = new AttachedFile(file);
            attachedFileDAO.save(attach);
            fact.getFiles().add(attach);
        }

        cookies.writeCookieValue("projectId", Long.toString(project.getId()));
        fact.setFactTitle(factTitle);
        fact.setFactText(factText);
        // это мы так типа элегантно читаем данные из компонента
        fact.setAuthor(cookies.readCookieValue("username"));
        fact.setProject(project);
        for (Long id : domainCombos.getDomainIds()) {
            if (id != -1) {
                Domain domain = domainDAO.findById(id);
                fact.getDomains().add(domain);
            }
        }
        factDAO.saveOrUpdate(fact);
        return FactsList.class;
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

     public String getFactTitle() {
        return factTitle;
    }

    public void setFactTitle(String factTitle) {
        this.factTitle = factTitle;
    }

    public String getFactText() {
        return factText;
    }

    public void setFactText(String factText) {
        this.factText = factText;
    }

    public TreeDAO getDomainDAO() {
        return domainDAO;
    }
}
