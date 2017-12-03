package ly.muasica.base.activity.structure;

/**
 * Representation for a Tag.
 * @author Daniel Gabl
 *
 */
public class Tag {
    
    /**
     * Name / Value of the Tag.
     */
    private String name;
    
    /**
     * Constructor for a Tag.
     * @param name
     */
    Tag(String name)  {
        this.name = name;
    }
    
    /**
     * Getter for a Tag.
     * @return Name of the Tag
     */
    public String getName()  {
        return name;
    }
}
