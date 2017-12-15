package ly.muasica.base.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONException;
import org.json.JSONObject;

import ly.muasica.base.MyLogger;

/**
 * Class is responsible for sending EMails to a given recipient with a given subject and content.
 * @author Daniel Gabl
 *
 */
public class MailSender {
	
    /**
     * Logger Implementation.
     */
    private final static Logger LOGGER = MyLogger.getLogger();
    
    /**
     * User Name of Mail Account.
     */
    private static String username;
    
    /**
     * Password of Mail Account.
     */
    private static String password;
    
    /**
     * Hostname of (SMTP) Mailserver.
     */
    private static String hostname;
    
    /**
     * Port number.
     */
    private static int portnumber;
    
    /**
     * Flag for checking if Sender was initialized.
     */
    private static boolean initialized = false;
    
    /**
     * Read Data from Configuration File and initialize the sender with it.
     */
    private static void initialize()  {
        JSONObject obj;
        try {
            obj = new JSONObject(new String(Files.readAllBytes(Paths.get("src/main/resources/maildetails.json"))));
            String username = obj.getString("username");
            String password = obj.getString("password");
            String hostname = obj.getString("hostname");
            int portnumber = obj.getInt("portnmbr");
            
            MailSender.username = username;
            MailSender.password = password;
            MailSender.hostname = hostname;
            MailSender.portnumber = portnumber;
            
            MailSender.initialized = true;
        } catch (NoSuchFileException ex)  {
        	LOGGER.info("Mail Details not found, will be using Environment Variables");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Check if Sender was initialized.
     */
    private static void checkInit()  {
        if (!initialized)  {
            initialize();
        }
    }
    
    /**
     * Sending a Mail to a given recipient.
     * @param recipient Recipient of E-Mail
     * @param content Content of E-Mail (HTML is allowed)
     */
    public static void sendMail(String recipient, String content)  {
        sendMail(recipient, content, null);
    }
    
    /**
     * Sending a Mail to a given recipient.
     * @param recipient Recipient of E-Mail
     * @param content Content of E-Mail (HTML is allowed)
     * @param subject Subject of E-Mail
     */
    public static void sendMail(String recipient, String content, String subject)  {
        checkInit();
        String to = recipient;
        String from = "register@muasica.ly";
        
        // File cannot be found
        if (hostname == null)  {
        	// Environment Variables not set yet.
        	if (System.getenv("MAIL_PORT") == null)  {
	        	try {
	                String directory = System.getProperty("user.dir"); //aktuellen Workspace auslesen
	                directory += "/src/main/resources";
	                
	                Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + directory + "/setEnv.bat");
	            } catch (IOException e) {
	                LOGGER.info("Cannot execute Batch File to set Environment Varaibles");
	                return;
	            }
        	}
        	System.out.println("Using Env");
        	username = System.getenv("MAIL_ADDR");
        	password = System.getenv("MAIL_PASS");
        	hostname = System.getenv("MAIL_HOST");
        	try  {
        		portnumber = Integer.parseInt(System.getenv("MAIL_PORT"));
	        } catch (NumberFormatException ex)  {
	        	LOGGER.info("Mailing not possible (check Mail Details / Environment Varaibles)!");
	        	return;
	        }
        }
        
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", hostname);
        properties.setProperty("mail.smtp.socketFactory.port", String.valueOf(portnumber));
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", String.valueOf(portnumber));

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
        	protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from, "MUAS-i-Caly Team"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setContent(content, "text/html");

			// Get Session
			Transport transport = session.getTransport("smtp");
			
			// GMail username with or without @gmail.com
			// username or username@gmail.com
			transport.connect(username, password);
			
			// Send the Mail
			transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException mex) {
           mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
    }    
}