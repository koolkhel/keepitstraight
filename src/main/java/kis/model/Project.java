package kis.model;

import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Column;
import java.util.Date;

/**
 * Created by
 * User: yury
 * at
 * Date: 08.05.2009
 * Time: 2:07:09
 */
@Entity(name="projects")
public class Project extends AbstractEntity {
    /**
     * название проекта
     */
    private String title;

    /**
     * описание проекта
     */
    @Type(type = "text")
    private String description;

    /**
     * дата начала проекта
     */
    private Date start;

    /**
     * дата конца проекта
     */
    private Date finish;

    /**
     * не настал ли ещё finish
     */
    private boolean opened;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
