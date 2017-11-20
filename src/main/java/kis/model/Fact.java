package kis.model;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.hibernate.annotations.Type;

@Entity(name="facts")
public class Fact extends AbstractEntity {
    public Fact() {
        domains = new ArrayList<Domain>();
        files = new ArrayList<AttachedFile>();
    }
	
	@Column(name="title",length=255)
	private String factTitle;
	
	@Column(name="content")
	@Type(type="text")
    @Validate("required")
	private String factText;

    /*----------------------------------------------------------------
	 * связанные объекты напрямую не показываются, надо делать компонент (или несколько компонентов),
	 * которые будут рисовать всё, как надо
	 * ----------------------------------------------------------------
	 */

	@Property
	@JoinTable(name="facts_domains",joinColumns = {
			@JoinColumn(name="fact_id")
	},inverseJoinColumns = {
			@JoinColumn(name="domain_id")
	})
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Domain> domains;
	
	@Property
	private String author;

    @JoinColumn(name="project")
    @ManyToOne
    private Project project;

    @JoinTable(name="facts_files",joinColumns = {
            @JoinColumn(name="fact_id")
    },inverseJoinColumns = {
            @JoinColumn(name="file_id")
    })
    @ManyToMany(fetch=FetchType.LAZY, targetEntity = AttachedFile.class)
    private List<AttachedFile> files;

    public List<AttachedFile> getFiles() {
        return files;
    }

    public void setFiles(List<AttachedFile> files) {
        this.files = files;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

    public List<Domain> getDomains() {
        return domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fact fact = (Fact) o;

        if (author != null ? !author.equals(fact.author) : fact.author != null) return false;
        if (domains != null ? !domains.equals(fact.domains) : fact.domains != null) return false;
        if (factText != null ? !factText.equals(fact.factText) : fact.factText != null) return false;
        if (factTitle != null ? !factTitle.equals(fact.factTitle) : fact.factTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = factTitle != null ? factTitle.hashCode() : 0;
        result = 31 * result + (factText != null ? factText.hashCode() : 0);
        result = 31 * result + (domains != null ? domains.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }
}
