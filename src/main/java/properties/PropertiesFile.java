package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import utils.ExcelDataProvider;


/** This class manages properties files
 * @author fabio.caccia
 *
 */
public class PropertiesFile {
	/**
	 * User directory
	 */
	static String dir = System.getProperty("user.dir");
	/**
	 * config.properties file path
	 */
	static String filePath = "\\src\\main\\resources\\properties\\config.properties";
	
	/**
	 * Properties objectD
	 */
	static Properties prop = new Properties();

	/** Creates a properties file
	 * @param path the path and name of the file to be created
	 */
	public static void createPropertiesFile(String path)  {

		File myObj = new File(path);
		try {
			if (myObj.createNewFile()) {
				ExcelDataProvider.logInfo("File creato: " + myObj.getName());
			} else {
				ExcelDataProvider.logInfo("File esiste: "+path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Returns a property value from a given file and a given key
	 * @param key the key assigend to the value
	 * @param path the file path
	 * @return the property value
	 */
	public static String getProperty(String key,String path) {	
		try {
			InputStream input = new FileInputStream(path);
			prop.load(input);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	/** Returns a property value from the config.properties file
	 * @param key the key assigned to desired value
	 * @return the property value
	 */
	public static String getProperty(String key) {	
		try {
			InputStream input = new FileInputStream(dir+filePath);
			prop.load(input);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	/** Writes a property into a specified file
	 * @param k the key 
	 * @param v the value assigned to the key
	 * @param path the file path
	 */
	public static void setProperty(String k, String v, String path) {
		try {
			OutputStream output = new FileOutputStream(path);
			prop.setProperty(k, v);
			prop.store(output, null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}

	/**  Writes a property into config.properties file
	 * @param k the key
	 * @param v the valye
	 */
	public static void setProperty(String k, String v) {
		try {
			OutputStream output = new FileOutputStream(dir+filePath);
			prop.setProperty(k, v);
			prop.store(output, null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}
}
