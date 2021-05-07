package drivers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class is used to get the browsers options
 * @author fabio.caccia
 *
 */
public class BrowserCapabilities {
		
	/**
	 * the user.dir system property
	 */
	static String dir = System.getProperty("user.dir");
	/**
	 * Properties object
	 */
	static Properties prop = new Properties();
	
	/**
	 * Opens the properties file for the browser and store the properties into a map
	 * @param browser the browser
	 * @return map the map where the properties are stored into
	 * @throws IOException
	 */
	public static Map<Object, Object> getProperties(String browser) throws IOException {	
		
		InputStream input = new FileInputStream(dir+"\\src\\main\\resources\\properties\\"+browser+".properties");
		prop.load(input);	
		Map<Object, Object> map = new HashMap<Object, Object>(prop);
		prop.putAll(map);
		prop.clear();
		return map;
	}
	
	/**
	 * Opens the properties file for the browser and return the property, given its key
	 * @param key the property key
	 * @param browser the browser
	 * @return the property value
	 * @throws IOException
	 */
	public static String getProperty(String key,String browser) throws IOException {
		
		InputStream input = new FileInputStream(dir+"\\src\\main\\resources\\properties\\"+browser+".properties");
		prop.load(input);
		System.out.println(key+": "+prop.getProperty(key));
		return prop.getProperty(key);
	}
	
	/**
	 * Set all the properties for the browser
	 * @param browser the browser
	 */
	public static void setProperties(String browser) {
		try {
			OutputStream output = new FileOutputStream(dir+"\\src\\main\\resources\\properties\\"+browser+".properties");
			prop.setProperty("browser", browser);
			prop.store(output, null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}	
	}
}
