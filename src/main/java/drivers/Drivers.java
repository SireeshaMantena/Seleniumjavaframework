package drivers;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import utils.ExcelDataProvider;

/** This class is used to set the browser options and to start the WebDriver
 * 
 */
public class Drivers {

	/**
	 * The current web driver
	 */
	static public WebDriver driver;
	/**
	 * Directory of the drivers file
	 */
	static public String driverPath=System.getProperty("user.home")+"\\drivers";

	/** Load the options and start the browser
	 * @param browser the browser to be started
	 */
	public static void avviaDriver(String browser) {		

		try {
			if(browser.equals("chrome")) {
				ChromeOptions opt = new ChromeOptions();
				Map<Object, Object> map=BrowserCapabilities.getProperties(browser);
				System.setProperty("webdriver.chrome.driver", driverPath+"\\chromedriver\\chromedriver.exe");
				Iterator<?> it = map.entrySet().iterator();
				ExcelDataProvider.logInfo(browser+" options: ");
				while (it.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry pair = (Map.Entry)it.next();
					opt.addArguments(pair.getKey().toString());
					ExcelDataProvider.logInfo(pair.getKey().toString());
				}
				map.clear();
				driver = new ChromeDriver(opt);
			}

			if(browser.equals("firefox")) {
				Map<Object, Object> map=BrowserCapabilities.getProperties(browser);
				System.setProperty("webdriver.gecko.driver", driverPath+"\\geckodriver\\geckodriver.exe");

				FirefoxProfile profile = new FirefoxProfile();

				System.out.println(browser+" options: ");
				Iterator<?> it = map.entrySet().iterator();
				while (it.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry pair = (Map.Entry)it.next();
					ExcelDataProvider.logInfo(pair.getKey()  + "=" + pair.getValue());
					profile.setPreference(pair.getKey().toString() , pair.getValue().toString());
				}
				map.clear();
				FirefoxOptions opt = new FirefoxOptions();
				opt.setProfile(profile);
				driver = new FirefoxDriver(opt);   		
			}

			if(browser.equals("ie")) {
				Map<Object, Object> map=BrowserCapabilities.getProperties(browser);
				System.setProperty("webdriver.ie.driver", driverPath+"\\IEDriverServer\\IEDriverServer.exe");
				InternetExplorerOptions cap = new InternetExplorerOptions();
				ExcelDataProvider.logInfo(browser+" options: ");
				Iterator<?> it = map.entrySet().iterator();
				while (it.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry pair = (Map.Entry)it.next();
					ExcelDataProvider.logInfo(pair.getKey()  + "=" + pair.getValue());
					cap.setCapability((String) pair.getKey(),Boolean.parseBoolean(pair.getValue().toString()));
				}
				map.clear();
				driver = new InternetExplorerDriver(cap);

			}
			if(browser.equals("edge")) {
				System.setProperty("webdriver.edge.driver", driverPath+"\\MicrosoftWebDriver\\msedgedriver.exe");
				EdgeOptions opt = new EdgeOptions();
				Map<Object, Object> map=BrowserCapabilities.getProperties(browser);
				Iterator<?> it = map.entrySet().iterator();
				ExcelDataProvider.logInfo(browser+" options: ");
				while (it.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry pair = (Map.Entry)it.next();
					opt.addArguments(pair.getKey().toString());
					ExcelDataProvider.logInfo(pair.getKey().toString());
				}
				driver = new EdgeDriver(opt);
			}
		}

		catch(Exception e){
			System.out.println(e.getStackTrace());
			System.out.println(e.getMessage());
			e.printStackTrace();
			Assert.fail("fail Drivers", e);
		}
	}
}

