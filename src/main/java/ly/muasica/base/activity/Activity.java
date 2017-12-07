package ly.muasica.base.activity;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import ly.muasica.base.activity.structure.Tag;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String text;
    private String title;
    
    private ArrayList<Tag> tags;
//    private List<Attachment> attachments;  // TODO Write Attachment Class

    public Activity (){};

    public Activity(String text, String title, ArrayList<Tag> tags) {
        this.text = text;
        this.title = title;
        this.tags = tags;
//        String[] tagArr = tags.split("\\s*");
//        this.tags = new ArrayList<>();
//        for (String tag : tagArr)  {
//        	String[] split = tag.split(Tag.prefix);
//        	String content = "";
//        	if (split.length > 1)  {
//        		content = split[1];  //    # in String
//        	}  else  {
//        		content = split[0];  // no # in String
//        	}
//        	this.tags.add(new Tag(content));
//        }
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

	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
	
	public boolean addTag(Tag tag)  {
		return tags.add(tag);
	}
	
	public String getTagsAsString()  {
		StringBuilder builder = new StringBuilder();
		for (Tag tag : tags)  {
			builder.append(Tag.prefix + tag.getName());
		}
		return builder.toString();
	}

//	public List<Attachment> getAttachments() {
//		return attachments;
//	}
//
//	public void setAttachments(List<Attachment> attachments) {
//		this.attachments = attachments;
//	}
}