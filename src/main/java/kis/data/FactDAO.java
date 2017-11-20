package kis.data;

import kis.model.Fact;
import kis.model.Project;
import kis.model.Domain;

import java.util.List;

public interface FactDAO extends AbstractEntityDAO<Fact> {
    public Integer countFactsByProject(Project project);

    public List<Fact> getFactsByProject(Project project);

    public List<Fact> getFactsByDomain(Domain domain);

    public Integer countFactsByDomain(Domain domain);
}
