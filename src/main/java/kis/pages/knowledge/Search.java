package kis.pages.knowledge;

import kis.model.Fact;
import kis.data.FactDAO;
import kis.services.TextileWeaver;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Disjunction;
import org.slf4j.Logger;

/**
 * Created by
 * User: yury
 * at
 * Date: 14.05.2009
 * Time: 18:12:44
 *
 * самое умное место, по идее, должно быть тут
 */
public class Search {
    // в первый раз в проекте Session за пределами DAO
    @Inject private Session session;

    @Inject private FactDAO factDAO;

    @Inject private TextileWeaver weaver;

    @Inject private Logger logger;

    private String searchString;

    private Fact fact;

    public void onActivate(String search) {
        searchString = search;
    }

    public String onPassivate() {
        return searchString;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Integer getFilesCount() {
        return fact.getFiles().size();
    }

    public String getFactBody() {
        return weaver.weave(fact.getFactText());
    }

    @SuppressWarnings("unchecked")
    public List<Fact> getResults() {
        logger.warn(String.format("search string - \"%s\"", searchString));
        if (searchString == null  || "".equals(searchString.trim())) {
            return factDAO.findAll();
        }
        Criteria criteria = session.createCriteria(Fact.class)
                .createAlias("project", "p")
                .createAlias("domains", "d")
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        Conjunction factTexts = Restrictions.conjunction();
        for (String substr : searchString.split(" ")) {
            factTexts.add(Restrictions.ilike("factText", substr, MatchMode.ANYWHERE));
        }
        Disjunction factTitles = Restrictions.disjunction();
        for (String substr : searchString.split(" ")) {
            factTitles.add(Restrictions.ilike("factTitle", substr, MatchMode.ANYWHERE));
        }
        Disjunction domainsDescriptions = Restrictions.disjunction();
        for (String substr : searchString.split(" ")) {
            domainsDescriptions.add(Restrictions.ilike("d.description", substr, MatchMode.ANYWHERE));
        }
        Disjunction total = Restrictions.disjunction();
        total.add(factTexts);
        total.add(domainsDescriptions);
        total.add(factTitles);
        criteria.add(total);

        return criteria.list();    
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }
}
