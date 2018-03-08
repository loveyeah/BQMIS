package power.ejb.run.securityproduction.safesupervise;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ToolLogger
{
	public static final Logger logger;
	static {
		logger = Logger.getLogger("tools");
		logger.setLevel(Level.ALL);
	}
	
	public static void log(String info,Level level,Throwable ex){
		logger.log(level,info,ex);
	}
	
	public static Logger getLogger(){
		return logger;
	}
}