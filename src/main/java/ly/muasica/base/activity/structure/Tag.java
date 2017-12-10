package ly.muasica.base.activity.structure;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Representation for a Tag.
 * @author Daniel Gabl
 *
 */
@Entity
public class Tag implements Serializable  {
    
    /**
     * For Serialization.
     */
    private static final long serialVersionUID = -3571498582340686743L;
    
    /**
     * Prefix for a Tag.
     */
    @Transient
    public static final String prefix = "#";  // default #
    
    /**
     * Name / Value of the Tag.
     */
    @Id
    private String name;
    
    /**
     * Default Constructor for Spring-Boot.
     */
    public Tag() {}
        
    /**
     * Constructor for a Tag.
     * @param name
     */
    public Tag(String name)  {
        this.name = name;
    }
    
    /**
     * Getter for a Tag.
     * @return Name of the Tag
     */
    public String getName()  {
        return name;
    }
    
    /**
     * Setter for a Tag.
     */
    public void setName(String name)  {
        this.name = name;
    }
    
    /**
     * toString-Implementation.
     */
    @Override
    public String toString()  {
        return String.format("T:{Content: %s}", prefix + name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tag other = (Tag) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.toLowerCase().equals(other.name.toLowerCase()))
            return false;
        return true;
    }
}
