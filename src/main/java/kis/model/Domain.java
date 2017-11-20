package kis.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Предметная область
 * @author yury
 *
 */
@Entity(name="domains")
public class Domain extends AbstractEntity {
    public void setId(Long id) {
        this.id = id;
    }

    public Domain() {
		setChildren(new ArrayList<Domain>());
        setFacts(new ArrayList<Fact>());
	}
	
	public Domain(String description) {
		this();
		this.description = description;
	}
	
	@Column(length=255)
	private String description;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parent")
	private Domain parent;
	
	@OneToMany(mappedBy="parent",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<Domain> children;
	
	@ManyToMany
	@JoinTable(name="facts_domains",
			joinColumns = {
			@JoinColumn(name="domain_id")
	},inverseJoinColumns = {
			@JoinColumn(name="fact_id")
	})
	private List<Fact> facts;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Domain getParent() {
		return parent;
	}

	public void setParent(Domain parent) {
		this.parent = parent;
	}

	public List<Domain> getChildren() {
		return children;
	}

	public void setChildren(List<Domain> children) {
		this.children = children;
	}

    public List<Fact> getFacts() {
        return facts;
    }

    public void setFacts(List<Fact> facts) {
        this.facts = facts;
    }
}
