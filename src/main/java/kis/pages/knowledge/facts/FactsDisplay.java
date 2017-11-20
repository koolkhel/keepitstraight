package kis.pages.knowledge.facts;

import kis.model.Fact;
import kis.model.AttachedFile;
import kis.model.Project;
import kis.data.FactDAO;
import kis.data.AttachedFileDAO;
import kis.services.TextileWeaver;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Property;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 02.05.2009
 * Time: 20:26:00
 */
public class FactsDisplay {
    @Inject
    private Logger logger;

    @Inject
    private FactDAO factDAO;

    @Inject
    private TextileWeaver weaver;
    
    private Fact fact;

    @Inject
    private AttachedFileDAO attachedFileDAO;

    @Property
    private AttachedFile file;

    // почему-то onActivate без параметров отрабатывает первый
    public void onActivate() {
        
    }

    public void onActivate(Long factId) {
        if (null != factId) {
            fact = factDAO.findById(factId);
        }
    }

    public Long onPassivate() {
        return fact.getId();
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public String getFactText() {
        return weaver.weave(fact.getFactText());   
    }

    public StreamResponse onGetFile(Long id) {
        if (fact.getFiles().size() != 0) {
            return attachedFileDAO.findById(id).getStream();
        } else {
            return null;
        }
    }

    public List<AttachedFile> getFiles() {
        return fact.getFiles();
    }

    public String getProjectTitle() {
        if (fact.getProject() != null) {
            return fact.getProject().getTitle();
        } else {
            return "";
        }
    }

    public Long getProjectId() {
        Project project = fact.getProject();
        if (project != null) {
            return project.getId();
        } else {
            return null;
        }
    }
}
