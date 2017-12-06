package ly.muasica.base.activity.structure;

/**
 * Representation for a Tag.
 * @author Daniel Gabl
 *
 */
public class Tag {
    
	/**
	 * Prefix for a Tag.
	 */
	public static final String prefix = "#";
	
    /**
     * Name / Value of the Tag.
     */
    private String name;
    
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
    
    @Override
    public String toString()  {
    	return prefix + name;
    }
}
