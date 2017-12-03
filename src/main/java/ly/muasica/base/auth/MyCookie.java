package ly.muasica.base.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Representation of an Own Cookie Class.
 * 
 * @author Daniel Gabl
 *
 */
@Entity
public class MyCookie {
    
    /**
     * ID of an Cookie.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    /**
     * Value of a Cookie.
     */
    private String value;
    
    /**
     * Default Constructor for Spring Boot.
     */
    public MyCookie(){};
    
    /**
     * Custom Constructor for a Cookie.
     * @param user Belonging User
     */
    public MyCookie(Long userId, String mailAddr)  {
        value = generateToken(userId, mailAddr);
    }
    
    /**
     * Getter for a Cookie ID.
     * @return Cookie ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Getter for the Cookie Value.
     * @return Cookie Value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Generated HashCode method.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        MyCookie other = (MyCookie) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
    
    /**
     * Generates a Cookie / Token for an User.
     * Token is a MD5 sum over the User ID, the E-Mail-Address of the User and the current Unix Timestamp.
     * @param user Belonging User.
     * @return Value for the Cookie
     */
    private String generateToken(Long userId, String mailAddr)  {
        final long UNIXDIVISOR = 1000L;
        long unixTime = System.currentTimeMillis() / UNIXDIVISOR;
        String md5string = userId + mailAddr + unixTime;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            try {
                byte[] array = md.digest(md5string.getBytes("UTF-8"));
                
                StringBuffer sb = new StringBuffer();
                final int max8bit = 0xFF; // = 255
                final int twoPowerEight = 0x100; // 2^8 = 256
                final int cutTwoByte = 3;
                
                for (int i = 0; i < array.length; ++i) {
                  sb.append(Integer.toHexString((array[i] & max8bit) | twoPowerEight).substring(1, cutTwoByte));
                }
                String md5 = sb.toString();
                
                // Credit goes to https://stackoverflow.com/a/6565597
                
                return md5;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
