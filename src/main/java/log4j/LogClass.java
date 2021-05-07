package log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Class to set a Log4J logger: file and console
 * @author fabio.caccia
 *
 */
public class LogClass
{
	/**
	 * logger initialization
	 */
	public static Logger logger = LogManager.getLogger(LogClass.class);

}