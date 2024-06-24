package dev.whitescan.consolefilter.share;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Shared logic for the LogFilter.
 *
 * @author Whitescan
 * @since 1.0.0
 */
public class ConsoleFilterShare {

    public static final String COMMAND_RELOAD = "consolefilter.reload";

    public static void cleanupLogs(Logger logger, long keepLogs) {

        File logDir = new File("logs");

        int deletedFiles = 0;

        if (logDir.exists()) {

            for (File file : logDir.listFiles()) {

                long diff = new Date().getTime() - file.lastModified();

                if (diff > keepLogs) {
                    file.delete();
                    deletedFiles++;
                }

            }

            if (deletedFiles > 0) {
                logger.info("Log cleanup completed. Purged " + deletedFiles + " files older than " + keepLogs + " days.");
                return;
            }

        }

        logger.info("No logs have been purged...");

    }

}
