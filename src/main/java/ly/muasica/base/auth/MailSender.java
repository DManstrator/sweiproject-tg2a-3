package ly.muasica.base.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

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

/**
 * Class is responsible for sending EMails to a given recipient with a given subject and content.
 * @author Daniel Gabl
 *
 */
public class MailSender {
    
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
        String host = MailSender.hostname;
        
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", String.valueOf(portnumber));
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
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
           Transport.send(message);
        } catch (MessagingException mex) {
           mex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
    }
    
}
