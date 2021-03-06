package ly.muasica.base.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ly.muasica.base.MyLogger;

/**
 * Controller for the Authorization.
 * The controller creates and stores Users, it also creates and verifies Tokens for Users.
 * It can returns a Users or all Users of the service and also checks if an E-Mail-Address of a User is valid.
 * The controller also creates and sets cookies.
 * 
 * @author Daniel Gabl
 *
 */
@RestController
@RequestMapping()
public class AuthController {
    
    /**
     * Logger Implementation.
     */
    private final static Logger LOGGER = MyLogger.getLogger();
    
    /**
     * Database for User Objects.
     */
    @Autowired
    private AuthRepository authRepository;
    
    /**
     * Returns a List of all Users in the Database.
     * @return a List of all Users in the Database
     */
    @GetMapping("/users")
    public ArrayList<User> listAll() {
        // TODO Cookie Value should not be displayed to Browser
        ArrayList<User> users = new ArrayList<>();
        authRepository.findAll().forEach(user -> users.add(user));
        return users;
    }
    
    /**
     * Finds a specific User in the Database
     * @param id User ID to look for
     * @return Nullable User Object
     */
    @GetMapping("/verify/{id}")
    public User find(@PathVariable Long id) {
        User user = authRepository.findOne(id);
        return user;
    }
    
    /**
     * Checks if a user already exists or creates it.
     * Also checks if the eMail is correct (from MUAS or CalyPoly).
     * @param input User Object automatically created by Spring Boot
     * @return Correct User Object with the E-Mail-Address from the input or null if eMail is wrong
     */
    @PostMapping("/verify")
    public User create(@RequestBody User input) {
        String inputAddr = input.getMailaddr();
        boolean valid = isValidMailAddress(inputAddr);
        if (valid)  {
            ArrayList<User> users = listAll();
            if (!users.contains(input))  {
                User user = new User(inputAddr);
                MyCookie myCookie = new MyCookie(user.getId(), user.getMailaddr());
                user.setCookie(myCookie);
                users.add(user);
                String[] hosts = {"http://localhost:8080", "https://muasicaly.herokuapp.com"};
                String requestUrl;
                for (String host : hosts)  {
                    requestUrl = String.format("%s/verify/?user=%s&token=%s", host, user.getId(), myCookie.getValue());
                    System.out.println(requestUrl);
                }
                
                String username = user.getUsername();
                Long userid = user.getId();
                String value = myCookie.getValue();
                String content = String.format(getMailContent(), username, userid, value, userid, value);
                String subject = "[MUAS-i-Caly] Registration";
                MailSender.sendMail(inputAddr, content, subject);
                return authRepository.save(user);
            }  else  {
                for (User user : users)  {
                    if (input.equals(user))  {
                        String username = user.getUsername();
                        Long userid = user.getId();
                        String value = user.getCookie().getValue();
                        String content = String.format(getMailContent(), username, userid, value, userid, value);
                        String subject = "[MUAS-i-Caly] Re-transmission of Login Link";
                        MailSender.sendMail(inputAddr, content, subject);
                    }
                }
            }
        }  else  {
            LOGGER.info(String.format("Given E-Mail-Address %s is not valid!", inputAddr));
        }
        return null;
    }
    
    /**
     * Reads the HTML content from the default Registration Page
     * @return Content of default registration page
     */
    private String getMailContent() {
        String content = new String();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/regMail.html"));
            for (String line : lines)  {
                content += line + System.lineSeparator();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    
    
    // TODO Overshadows upper RequestMapping when "/" in the end of "verify", verify() is never called
    /**
     * Verifies a Client with User-ID and Token.
     * @param id ID of the User
     * @param token Token belonging to the User
     * @param response HttpServletResponse to set a Cookie
     * @return true if the verification was successful, else false
     * @throws Exception In Case something goes wrong
     */
    @RequestMapping("/verify/")
    public boolean verify(@RequestParam(value="user") String id, @RequestParam(value="token") String token,
    		HttpServletResponse response) throws Exception  {
        boolean valid = false;
        ArrayList<User> users = listAll();
        for (User user : users)  {
            Long idLong = Long.parseLong(id);
            if (user.getId().equals(idLong) && user.getCookie().getValue().equals(token))  {
                valid = true;
            }
        }
        if (valid)  {
            setCookie(token, response);  // Do service call passing the response
        }  else  {
           LOGGER.info("User or Cookie are not valid!"); 
        }
        return valid;
    }
    
    /**
     * Sets a Cookie for a Client.
     * @param token Token for the Cookie
     * @param response Response-Object to set the cookie
     */
    private void setCookie(String token, HttpServletResponse response)  {
        final String cookieName = "MUASiCaly";
        final String cookieValue = token;

        Cookie cookie = createCookie(cookieName, cookieValue);
        response.addCookie(cookie);
    }
    
    /**
     * Creates a cookie with a given Token for exactly 4 years.
     * @param cookieName Name of the Cookie
     * @param cookieValue Value of the Cookie (Token of User)
     * @return Cookie for the Website for 4 years
     */
    private Cookie createCookie(String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        int day = 60*60*24;
        cookie.setPath("/");  // The cookie is visible to all the pages in the directory you specify, and all the pages in that directory's subdirectories
        cookie.setMaxAge(3*365*day + 366*day);  // A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits. A zero value causes the cookie to be deleted.
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // determines whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL
        return cookie;
    }
    
    /**
     * Checks if a given E-Mail-Address is valid (from MUAS or CalyPoly).
     * @param addr E-Mail-Address to check
     * @return true if E-Mail-Address is valid, else false
     */
    private boolean isValidMailAddress(String addr)  {
        String[] split = addr.split("@");
        if (split.length < 2)  {
            return false;
        }
        
        String domain = split[1];
        if (domain.toLowerCase().equals("hm.edu") || domain.toLowerCase().equals("calypoly.edu"))  {
            return true;
        }
        return false;
    }

}