package kis.data;

import kis.model.Project;

import java.util.List;

/**
 * Created by
 * User: yury
 * at
 * Date: 08.05.2009
 * Time: 17:43:19
 */
public interface ProjectDAO extends AbstractEntityDAO<Project> {
    /**
     * проекты, которые с opened = true
     * @return
     */
    List<Project> findOpenedProjects();
}
