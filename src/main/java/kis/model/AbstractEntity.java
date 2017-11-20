package kis.model;

import javax.persistence.*;

import org.apache.tapestry5.beaneditor.NonVisual;

@MappedSuperclass
public abstract class AbstractEntity {
	@Id
	@NonVisual
    @Basic
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
