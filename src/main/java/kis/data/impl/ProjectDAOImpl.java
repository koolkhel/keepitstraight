package kis.data.impl;

import kis.model.Project;
import kis.data.AbstractEntityDAO;
import kis.data.ProjectDAO;

import java.util.List;

import org.hibernate.criterion.Restrictions;

/**
 * Created by
 * User: yury
 * at
 * Date: 08.05.2009
 * Time: 17:43:43
 */
public class ProjectDAOImpl extends AbstractEntityDAOImpl<Project>
        implements ProjectDAO {
    
    public List<Project> findOpenedProjects() {
        return getSession().createCriteria(Project.class).add(Restrictions.eq("opened", true)).list();
    }
}
