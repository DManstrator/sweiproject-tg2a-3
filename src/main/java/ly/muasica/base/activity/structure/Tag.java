package ly.muasica.base.activity.structure;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	public static final String prefix = "#";
	
	/**
	 * Id of a Tag.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
    /**
     * Name / Value of the Tag.
     */
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
     * Getter for the Tag-Id.
     * @return Id of Tag
     */
    public long getId()  {
    	return id;
    }
    
    /**
     * Getter for a Tag.
     * @return Name of the Tag
     */
    public String getName()  {
        return name;
    }
    
    @Override
    public String toString()  {
    	return prefix + name;
    }
}
