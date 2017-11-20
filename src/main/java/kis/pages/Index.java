package kis.pages;

import kis.data.DomainDAO;
import kis.model.Domain;
import kis.pages.knowledge.facts.FactsSubmit;
import kis.pages.knowledge.Search;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.RenderSupport;
import org.slf4j.Logger;

/**
 * Start page of application keepitstraight.
 */
public class Index {
    @Inject
    private Logger logger;

    @Inject
    private DomainDAO domainDAO;

    @SuppressWarnings("unused")
    @Property
    private String newFactText;

    @SuppressWarnings("unused")
    @Property
    private String searchFact;

    @InjectPage
    private FactsSubmit submitFacts;

    @Inject
    private RenderSupport renderSupport;

    public void onActivate() {
        // фокус на большое поле ввода
        // renderSupport.autofocus(FieldFocusPriority.IN_ERROR, "submitFacts");
    }

    public Object onSubmitFromSubmitFact() {
        submitFacts.setFactText(newFactText);
        return submitFacts;
    }

    @InjectPage
    private Search searchPage;

    public Object onSubmitFromSearchFact() {
        searchPage.setSearchString(searchFact);
        return searchPage;
    }
}
