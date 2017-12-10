package ly.muasica.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    
    private static List<String> urls = new ArrayList<>(Arrays.asList(
            //"http://muasicaly.herokuapp.com/",
            "http://localhost:8080"
            ));
    
    private static boolean openInBrowser(String url)  {
        try {
            Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler "
                            + url);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        for (String url : urls)  {
            openInBrowser(url);
        }
    }
}
