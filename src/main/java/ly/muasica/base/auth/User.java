package ly.muasica.base.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * Representation of a User Class for Students of MUAS and CalyPoly.
 * @author Daniel Gabl
 *
 */
@Entity
public class User {
    
    /**
     * Created User IDs.
     */
    @Transient
    private static List<Long> ids = new ArrayList<>();
    
    /**
     * ID of an User.
     */
    @Id
    private Long id;
    
    /**
     * Name of an User.
     */
    private String name;
    
    /**
     * E-Mail-Address of an User.
     */
    private String mailaddr;
    
    /**
     * Cookie-Object of an User.
     */
    // TODO @Transient need to be removed in order to access it later via. DB
    @Transient
    private MyCookie cookie;
    
    /**
     * Default Constructor for Spring Boot.
     */
    public User(){};
    
    /**
     * Custom Constructor for an User.
     * @param mailaddr E-Mail-Address of a User
     */
    public User(String mailaddr) {
        String[] split = mailaddr.split("@");
        id = generateId();
        name = split[0];
        this.mailaddr = mailaddr;
    }
    
    /**
     * Getter for User ID.
     * @return User ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Getter for User Name.
     * @return User Name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Getter for User E-Mail-Address.
     * @return User E-Mail-Address
     */
    public String getMailaddr() {
        return mailaddr;
    }
    
    /**
     * Getter for User Cookie.
     * @return User Cookie
     */
    public MyCookie getCookie() {
        return cookie;
    }
    
    /**
     * Setter for User Cookie.
     * @param cookie New Cookie to store
     */
    public void setCookie(MyCookie cookie)  {
        this.cookie = cookie;
    }
    
    /**
     * Generated HashCode method.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    
    /**
     * Generated equals method.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (mailaddr == null) {
            if (other.mailaddr != null)
                return false;
        } else if (!mailaddr.equals(other.mailaddr))
            return false;
        return true;
    }
    
    /**
     * Generates an Unique, seven digit long User ID.
     * @return Unique seven digit long User ID.
     */
    private Long generateId() {
        long id = -1;
        while (id == -1 || ids.contains(id))  {
            String idStr = "";
            for (int c = 0; c < 7; c++)  {
                idStr += ThreadLocalRandom.current().nextInt(0, 9 + 1); // (min, max + 1);
            }
            if (idStr.charAt(0) == '0')  {
                idStr = "9" + idStr.substring(1);
            }
            id = Long.parseLong(idStr);
        }
        ids.add(id);
        return id;
    }
    
    @Override
    public String toString()  {
        return String.format("U:{ID: %d, Name: %s}", id, name);
    }

}
