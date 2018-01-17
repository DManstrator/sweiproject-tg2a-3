package ly.muasica.base;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Own Logger Class Implementation.
 * @author Daniel Gabl
 *
 */
public class MyLogger {
    private static Logger LOGGER = Logger.getLogger(MyLogger.class.getName());
    private static boolean configured = false;
//    System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s - %4$s: %5$s%n");
//    %1$tF %1$tT %4$s %2$s %5$s%6$s%n
    
    /**
     * Getter for the Logger. Configures in the first step.
     * @return Logger Instance
     */
    public static Logger getLogger()  {
        if (!configured)  {
            boolean configureLogger = configureLogger();
            configured = configureLogger;
        }
        return LOGGER;
    }
    
    /**
     * Configuration of the Logger. Will be executed only once.
     * @return
     */
    private static boolean configureLogger() {
        try {
            LogManager.getLogManager().readConfiguration(MyLogger.class.getClassLoader().getResourceAsStream("logging.properties"));
            LogManager.getLogManager().addLogger(LOGGER);
        }  catch (IOException ex)  {
            System.err.println("WARNING: Could not open configuration file!");
            System.err.println("WARNING: Logging not configured (console output only)!");
            return false;
        }
        LOGGER.info("Configured Logger");
        return true;
    }
}
