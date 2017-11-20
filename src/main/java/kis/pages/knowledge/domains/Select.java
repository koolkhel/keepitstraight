package kis.pages.knowledge.domains;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ComponentResources;
import org.slf4j.Logger;
import kis.data.TreeDAO;
import kis.data.DomainDAO;
import kis.components.DomainCombos;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 13.05.2009
 * Time: 15:21:37
 */
public class Select {
    @Inject private Logger logger;
    
    @Component
    private DomainCombos domainCombos;

    @Inject
    private ComponentResources componentResources;

    /**
     * 0 -- add
     * 1 -- Edit
     * 2 -- delete
     */
    @Persist
    private Integer type;

    public void onActivate(Integer type) {
        this.type = type;
    }

    public String getPageTitle() {
        switch(type) {
            case 0: return "Добавление предметной области";
            case 1: return "Редактирование предметной области";
            case 2: return "Удаление предметной области";
        }
        return "unknown";
    }

    @InjectPage
    private DomainsCreate create;

    @InjectPage
    private DomainsDelete delete;

    @InjectPage
    private DomainsEdit edit;

    @Inject
    private DomainDAO domainDAO;

    public TreeDAO getDomainDAO() {
        return domainDAO;
    }

    public Object onSubmitFromSubmit() {
        List<Long> ids = domainCombos.getDomainIds();
        domainCombos.reset();
        Long id;
        logger.warn("O_O");
        if (ids.size() == 0) {
            // создаём корневой домен
            id = 0L;
        } else {
            id = ids.get(0);
        }
        componentResources.discardPersistentFieldChanges();
        switch(type) {
            case 0: {
                if (id == 0) {
                    create.setNewRoot(true);
                } else {
                    create.setNewRoot(false);
                    create.setParentId(id);
                }
                logger.warn("select id - " + id);
                create.setParentId(id);
                return create;
            }
            case 1: {
                edit.setId(id);
                return edit;
            }
            case 2: {
                delete.setId(id);
                return delete;
            }
        }
        return null; // ну, по идее сюда попасть не должно
    }

    public Object onSubmitFromCancel() {
        return DomainsIndex.class;
    }

    public boolean isAddDomain() {
        return type == 0;
    }

    public boolean isEditDomain() {
        return type == 1;
    }

    public boolean isDeleteDomain() {
        return type == 2;
    }
}
