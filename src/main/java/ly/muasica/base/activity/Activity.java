package ly.muasica.base.activity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ly.muasica.base.activity.structure.Attachment;
import ly.muasica.base.activity.structure.Tag;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String text;
//    private List<Tag> tags;  // TODO Write Tag Class
//    private List<Attachment> attachments;  // TODO Write Attachment Class
    private String title;

    public Activity (){};

    public Activity(String text, String title) {
        this.text = text;
        this.title = title;
//        this.tags = tags;
//        this.attachments = attachments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//	public List<Tag> getTags() {
//		return tags;
//	}
//
//	public void setTags(List<Tag> tags) {
//		this.tags = tags;
//	}
//
//	public List<Attachment> getAttachments() {
//		return attachments;
//	}
//
//	public void setAttachments(List<Attachment> attachments) {
//		this.attachments = attachments;
//	}
}