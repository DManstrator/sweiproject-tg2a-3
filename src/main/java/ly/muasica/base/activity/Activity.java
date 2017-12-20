package ly.muasica.base.activity;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ly.muasica.base.activity.structure.Tag;
import ly.muasica.base.auth.User;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String text;
    private String title;
    
    @Column(length=511) // to bypass store problems
    private User author;

	private ArrayList<Tag> tags;
//    private List<Attachment> attachments;  // TODO Write Attachment Class

    public Activity (){};

    public Activity(String text, String title, ArrayList<Tag> tags, User author) {
        this.text = text;
        this.title = title;
        this.tags = tags;
        this.author = author;
//        String[] tagArr = tags.split("\\s*");
//        this.tags = new ArrayList<>();
//        for (String tag : tagArr)  {
//            String[] split = tag.split(Tag.prefix);
//            String content = "";
//            if (split.length > 1)  {
//                content = split[1];  //    # in String
//            }  else  {
//                content = split[0];  // no # in String
//            }
//            this.tags.add(new Tag(content));
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
    
    public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
    
    public String getTagsAsString()  {
        if (tags.isEmpty())  {
            return new String();
        }
        StringBuilder builder = new StringBuilder();
        for (Tag tag : tags)  {
            builder.append(Tag.prefix + tag.getName() + " ");
        }
        return builder.deleteCharAt(builder.lastIndexOf(" ")).toString();  // Remove last Space
    }

//    public List<Attachment> getAttachments() {
//        return attachments;
//    }
//
//    public void setAttachments(List<Attachment> attachments) {
//        this.attachments = attachments;
//    }
}