package utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import drivers.Drivers;
import log4j.LogClass;

/**
 * Abstract class where are stored Selenium, JavaScript and other methods to be
 * used in the projects classes
 * 
 * @author fabio.caccia
 *
 */
public abstract class AbstractCommands {
	/**
	 * @author syed sofia
	 * @param locator
	 * @return
	 */
	public String gettext(String locator) {
		return Drivers.driver.findElement(By.xpath(locator)).getText();
	}

	/**
	 * Clears web field and sets a value
	 * 
	 * @param locator the xpath of the web element
	 * @param value   the value to be set into the element
	 */
	public void clearAndSetValue(String locator, String value) {
		Drivers.driver.findElement(By.xpath(locator)).clear();
		Drivers.driver.findElement(By.xpath(locator)).sendKeys(value);
		ExcelDataProvider.logInfo(locator + ": inserisco valore: " + value);
		pass(locator + ": inserting value: " + value);
	}

	/**
	 * Clears a web element value such as inputs
	 * 
	 * @param locator the xpath of the web element
	 */
	public void clearElement(String locator) {
		Drivers.driver.findElement(By.xpath(locator)).clear();
		ExcelDataProvider.logInfo("Value di " + locator + " cancellato");
	}

	/**
	 * Clicks a web element
	 * 
	 * @param locator the xpath of the web element
	 */
	public void click(String locator) {
		Drivers.driver.findElement(By.xpath(locator)).click();
		ExcelDataProvider.logInfo("Click: " + locator);
		pass("Click on: " + locator);
	}

	/**
	 * Clicks a web element given its ID
	 * 
	 * @param idLocator the ID of the web element
	 */
	public void clickById(String idLocator) {
		Drivers.driver.findElement(By.id(idLocator)).click();
		ExcelDataProvider.logInfo("Click: " + idLocator);
		pass("Click on: " + idLocator);
	}

	/**
	 * Counts the number of browser windows that are currenty opened
	 * 
	 * @return the number of windows that are open
	 */
	public int countOpenedWindows() {
		ArrayList<String> tabs = new ArrayList<String>(Drivers.driver.getWindowHandles());
		ExcelDataProvider.logInfo("Windows opened " + tabs.size());
		return tabs.size();
	}


	/**
	 * Close a browser window
	 */
	public void driverClose() {
		pass("Closing browser window "+Drivers.driver.getCurrentUrl());
		Drivers.driver.close();
	}

	/**
	 * Opens an url
	 * 
	 * @param url the url to navigate
	 */
	public void driverGet(String url) {
		Drivers.driver.get(url);
		ExcelDataProvider.logInfo("Open " + url);
		pass("Open "+url);
	}

	/**
	 * Selects an element from a dropdown
	 * 
	 * @param locator the xpath of the web element (dropwodn)
	 * @param text    the visible text to be selected in the dropdown
	 */
	public void dropDownSelect(String locator, String text) {
		ExcelDataProvider.logInfo("Selecting " + text + " from " + locator);
		Select dropdown = new Select(Drivers.driver.findElement(By.xpath(locator)));
		dropdown.selectByVisibleText(text);
		pass("Selected "+text+" from dropdown "+locator);
	}

	/**
	 * Checks is two strings are equal
	 * 
	 * @param expected expected value
	 * @param found    found value
	 * @return true or false
	 */
	public boolean equalsBoolean(String expected, String found) {
		Boolean equals = expected.equals(found) ? true : false;
		return equals;
	}

	/**
	 * Checks if two strings are equal, if not then test is failed
	 * 
	 * @param expected expected value
	 * @param found    found value
	 */
	public void equalsOrFail(String expected, String found) {
		if (!expected.equals(found))
			invokeFail("Atteso: " + expected + " diverso da trovato: " + found);
		else
			ExcelDataProvider.logInfo("Atteso: " + expected + " = trovato: " + found);
	}

	/**
	 * Execute a JavaScript script on a web element
	 * 
	 * @param locator the xpath of the web element
	 * @param js      the javascript action to be executed
	 */
	public void executeJS(String locator, String js) {
		((JavascriptExecutor) Drivers.driver).executeScript(js, Drivers.driver.findElement(By.xpath(locator)));
		ExcelDataProvider.logInfo("Script js: " + js + " su " + locator);
		pass("Script js: " + js + " su " + locator);
	}

	/**
	 * Checks if a web element exists
	 * 
	 * @param locator the xpath of the web element
	 * @return true or false
	 */
	public Boolean existElement(String locator) {
		int elementiTrovati = Drivers.driver.findElements(By.xpath(locator)).size();
		Boolean exist = elementiTrovati > 0 ? true : false;
		if (elementiTrovati > 1) {
			LogClass.logger.warn("Trovati " + elementiTrovati + " " + locator);
			ExcelDataProvider.logInfo("Trovati " + elementiTrovati + " " + locator);
		}
		ExcelDataProvider.logInfo("Esistenza elemento: " + locator + ": " + exist);
		return exist;
	}

	/**
	 * Checks if a web element exists, if not then test is faile
	 * 
	 * @param locator the xpath of the web element
	 */
	public void existOrFail(String locator) {
		ExcelDataProvider.logInfo("Esistenza elemento: " + locator);
		if (Drivers.driver.findElements(By.xpath(locator)).size() < 1)
			invokeFail(locator + " non trovato in pagina");
	}

	/**
	 * Checks if a web element exist. If yes then reports a passed step. If no then
	 * the test is failed.
	 * 
	 * @param locator the xpath of the web element
	 */
	public void existPassOrFail(String locator) {
		int el = Drivers.driver.findElements(By.xpath(locator)).size();
		if (el == 1)
			pass(locator + ": exists");
		else
			invokeFail(locator + " found " + el + " elements");
	}

	/**
	 * Invokes fail status with a message
	 * 
	 * @param message the message associated to the failed status
	 */
	public void fail(String message) {
		invokeFail(message);
	}

	/**
	 * Invokes fail status with a message in a red label
	 * 
	 * @param message
	 */
	public void failLabel(String message) {
		Markup m = MarkupHelper.createLabel(message, ExtentColor.RED);
		LogClass.logger.error(message);
		ExcelDataProvider.test.fail(m);
		Assert.fail(message);
	}

	/**
	 * Returns the attribute value of a web element
	 * 
	 * @param locator   the xpath of the web element
	 * @param attribute the attribute to check
	 * @return the value of the attribute
	 */
	public String getAttribute(String locator, String attribute) {
		String att = Drivers.driver.findElement(By.xpath(locator)).getAttribute(attribute);
		ExcelDataProvider.logInfo(locator + ": ottengo attributo " + attribute + ": " + att);
		return att;
	}

	/**
	 * Gets the number of opened browser windows/tabs
	 * 
	 * @return number of tabs opened
	 */
	public int getNumberOfWindowsOpen() {
		Set<String> allWindowHandles = Drivers.driver.getWindowHandles();
		ArrayList<String> tabs = new ArrayList<String>(allWindowHandles);
		ExcelDataProvider.logInfo("Browser windows open: " + tabs.size());
		return (tabs.size());
	}

	/**
	 * Returns a web element
	 * 
	 * @param locator the xpath of the web element
	 * @return the web element
	 */
	public WebElement getWebElement(String locator) {
		WebElement e = Drivers.driver.findElement(By.xpath(locator));
		ExcelDataProvider.logInfo("Trovo elemento: " + locator);
		return e;
	}

	/**
	 * Returns a list of web elements
	 * 
	 * @param locator the xpath of the web elements
	 * @return the list of web elements
	 */
	public List<WebElement> getWebElementsList(String locator) {
		List<WebElement> elementsList = Drivers.driver.findElements(By.xpath(locator));
		return elementsList;
	}

	/**
	 * Writes an info message into console and logger
	 * 
	 * @param message the info message
	 */
	public void info(String message) {
		ExcelDataProvider.logInfo(message);
	}

	/**
	 * Retrieves the inner text of a web element
	 * 
	 * @param locator the xpath of the web element
	 * @return the inner text
	 */
	public String innerText(String locator) {
		return (Drivers.driver.findElement(By.xpath(locator)).getAttribute("innerText"));
	}

	/**
	 * Retrieves the inner text of a web element that is displayed
	 * 
	 * @param locator the xpath of the web element
	 * @return the inner text
	 */
	public String innerTextDisplayed(String locator) {
		String innerT = "";
		int foundElements = Drivers.driver.findElements(By.xpath(locator)).size();
		boolean done = false;
		for (int i = 1; i < foundElements; i++) {
			locator = "(" + locator + ")[" + i + "]";
			if (isDisplayed(locator)) {
				innerT = innerText(locator);
				done = true;
			}
			if (done)
				break;
		}
		return innerT;
	}

	/**
	 * Checks if a web element is checked
	 * 
	 * @param locator
	 * @return true or false
	 */
	public boolean isChecked(String locator) {
		boolean checked = Drivers.driver.findElement(By.xpath(locator)).isSelected() ? true : false;
		return checked;
	}

	/**
	 * Checks if a web element is displayed
	 * 
	 * @param locator
	 * @return true or false
	 */
	public boolean isDisplayed(String locator) {
		boolean displayed = Drivers.driver.findElement(By.xpath(locator)).isDisplayed() ? true : false;
		return displayed;
	}

	/**
	 * Checks if a web element is dispalyed If not then thes is failed If yes it is
	 * reported a successful step case
	 * 
	 * @param locator
	 */
	public void isDisplayedPassOrFail(String locator) {
		if (Drivers.driver.findElement(By.xpath(locator)).isDisplayed())
			pass(locator + ": visualizzato");
		else
			invokeFail(locator + ": non visualizzato");
	}

	/**
	 * Invokes a fail status with a message
	 * 
	 * @param failReason the reasone the test failed
	 */
	public void invokeFail(String failReason) {
		ExcelDataProvider.test.fail(failReason);
		LogClass.logger.error(failReason);
		Assert.fail(failReason);
	}

	/**
	 * Executest a click with JavaScript
	 * 
	 * @param locator the xpath of the web element
	 */
	public void jsClick(String locator) {
		JavascriptExecutor executor = (JavascriptExecutor) Drivers.driver;
		executor.executeScript("arguments[0].click();", Drivers.driver.findElement(By.xpath(locator)));
		ExcelDataProvider.logInfo("Script js: click on " + locator);
		pass("Script js: click on " + locator);
	}

	/**
	 * Execute a click with JavaScript, given the element id
	 * 
	 * @param IDlocator the ID of the web element
	 */
	public void jsClickById(String IDlocator) {
		JavascriptExecutor executor = (JavascriptExecutor) Drivers.driver;
		executor.executeScript("arguments[0].click();", Drivers.driver.findElement(By.id(IDlocator)));
		ExcelDataProvider.logInfo("Script js: click on " + IDlocator);
		pass("Script js: click on " + IDlocator);
	}

	/**
	 * Simulates the mouse over action on a web element
	 * 
	 * @param locator the xpath of the web element
	 */
	public void mouseOver(String locator) {
		pass("Mouse Over on "+locator);
		Actions action = new Actions(Drivers.driver);
		action.moveToElement(getWebElement(locator)).perform();
	}

	/**
	 * Opens an url
	 * 
	 * @param url the url to navigate to
	 */
	public void navigateTo(String url) {
		Drivers.driver.navigate().to(url);
		ExcelDataProvider.logInfo("Nagivate to " + url);
		pass("Open "+url);
	}

	/**
	 * Opens a new browser tab and navigates
	 * 
	 * @param url the url to navigate
	 */
	public void openTab(String url) {

		((JavascriptExecutor) Drivers.driver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(Drivers.driver.getWindowHandles());
		Drivers.driver.switchTo().window(tabs.get(tabs.size() - 1)); // serve per mettere il focus al tab aperto, da 1 a
		// n
		Drivers.driver.get(url);
		ExcelDataProvider.logInfo("Opening new tab: " + url);
		pass("Opening new tab: " + url);
	}

	/**
	 * Reports a successful step case
	 * 
	 * @param message descriptive message
	 */
	public void pass(String message) {
		ExcelDataProvider.logPass(message);
	}

	/**
	 * Reports a successful step case in a green label
	 * 
	 * @param message descriptive message
	 */
	public void passLabel(String message) {
		Markup m = MarkupHelper.createLabel(message, ExtentColor.GREEN);
		info(m.toString());
		ExcelDataProvider.test.pass(m);
	}

	/**
	 * Reports a successful step case writing a numbered list
	 * 
	 * @param strings a List of strings
	 */
	public void passList(List<String> strings) {
		Markup m = MarkupHelper.createOrderedList(strings);
		ExcelDataProvider.test.pass(m);
		LogClass.logger.info(m.toString());
	}

	/**
	 * Writes a fail message into html reporter
	 * and logger, but does not interrupt test execution
	 * @param m the message to write
	 */
	public void redMessage(String m) {
		ExcelDataProvider.test.fail(m);
		LogClass.logger.error(m);
	}

	/**
	 * Scrolls into view a web element using JavaScript
	 * 
	 * @param locator the xpath of the web element
	 */
	public void scrollIntoViewJS(String locator) {
		WebElement element = Drivers.driver.findElement(By.xpath(locator));
		((JavascriptExecutor) Drivers.driver).executeScript("arguments[0].scrollIntoView(true);", element);
		waiting(1);
		ExcelDataProvider.logInfo("Scroll into view " + locator);
	}

	/**
	 * Sets a value on a web element
	 * 
	 * @param locator the xpath of the web element
	 * @param value   the value to be set
	 */
	public void setValue(String locator, String value) {
		Drivers.driver.findElement(By.xpath(locator)).sendKeys(value);
		ExcelDataProvider.logInfo(locator + ": inserting value: " + value);
		pass(locator + ": inserting value: " + value);
	}

	/**
	 * Sets a value with JavaScript
	 * 
	 * @param locator the xpath of the web element
	 * @param value   the value to be set
	 */
	public void setValueJS(String locator, String value) {
		String js = "arguments[0].setAttribute('value','" + value + "')";
		((JavascriptExecutor) Drivers.driver).executeScript(js, Drivers.driver.findElement(By.xpath(locator)));
		ExcelDataProvider.logInfo("Script js: " + js + " su " + locator + " set: " + value);
		pass("Script js: " + js + " on " + locator + " set: " + value);
	}

	/**
	 * Compares two strings and reports a pass test or fail test
	 * 
	 * @param exepectd          expected value
	 * @param found             found value
	 * @param valoreConfrontato if set, this variable will be used to describe the
	 *                          outcome more clearly
	 */
	public void StringComparePassOrFail(String exepectd, String found, String... valoreConfrontato) {
		String valore = valoreConfrontato.length > 0 ? valoreConfrontato[0] : "";

		if (exepectd.equals(found))
			pass(valore + " ==> Atteso: " + exepectd + " trovato: " + found);
		else
			invokeFail(valore + " ==> Atteso: " + exepectd + " trovato: " + found);
	}

	/**
	 * Checks if two strings are equal
	 * 
	 * @param expected the expected value
	 * @param found    the found value
	 * @return true or false
	 */
	public Boolean stringCompare(String expected, String found) {
		boolean equals = expected.equals(found) ? true : false;
		ExcelDataProvider.logInfo("Atteso: " + expected + ", trovato: " + found);
		return equals;
	}

	/**
	 * Submits a web element
	 * 
	 * @param locator the xpath of the web element
	 */
	public void submit(String locator) {
		Drivers.driver.findElement(By.xpath(locator)).submit();
		ExcelDataProvider.logInfo(locator + ": submit");
		pass(locator + ": submit");
	}

	/**
	 * Switches the WebDriver focus into the new window
	 */
	public void switchToNewWindow() {
		for (String winHandle : Drivers.driver.getWindowHandles()) {
			Drivers.driver.switchTo().window(winHandle);
			ExcelDataProvider.logInfo("Window:" + winHandle.toString());
			Drivers.driver.manage().window().maximize();
			ExcelDataProvider.logInfo("Switched to window:" + winHandle.toString());
		}
	}

	/**
	 * Switches the WebDriver focus into a window that contains an element
	 * 
	 * @param locator
	 */
	public void switchToWindowContainingElement(String locator) {
		boolean found = false;
		for (String winHandle : Drivers.driver.getWindowHandles()) {
			Drivers.driver.switchTo().window(winHandle);
			Drivers.driver.manage().window().maximize();

			if (existElement(locator)) {
				found = true;
				pass("Switched to window: " + Drivers.driver.getTitle() + " containing " + locator);
				break;
			}
		}
		if (!found)
			failLabel(locator + " was not found in any of the browser windows");
	}

	/**
	 * Switches the WebDriver focus to a given window
	 * 
	 * @param index the index of the window to be switched into, start from 0
	 */
	public void switchToWindowIndex(int index) {
		Set<String> windowHandles = Drivers.driver.getWindowHandles();
		List<String> windowStrings = new ArrayList<>(windowHandles);
		String reqWindow = windowStrings.get(index);
		Drivers.driver.switchTo().window(reqWindow);
		ExcelDataProvider.logInfo("Switched to " + Drivers.driver.getTitle());
	}

	/**
	 * Switches the WebDriver focus to a window whose url cointains a given string
	 * 
	 * @param String the string that must be present in the window url
	 */
	public void switchToWindowUrlContains(String url) {
		for (String winHandle : Drivers.driver.getWindowHandles()) {
			Drivers.driver.switchTo().window(winHandle);
			if(Drivers.driver.getCurrentUrl().contains(url)) {
				pass("Switched to window: "+Drivers.driver.getTitle()+".\n"+url+" found");
				break;
			}
		}
	}


	/**
	 * Takes a screenshot of a web element and saves it into the html report
	 * 
	 * @param message a message to add to the screenshot
	 * @param locator the xpath of the web element
	 * @throws IOException
	 */
	public void takeScreenshotElementLoadReport(String message, String locator) throws IOException {
		ExcelDataProvider.test.pass(message, MediaEntityBuilder
				.createScreenCaptureFromBase64String(AbstractCommands.takeScreenShotElement(locator)).build());
	}

	/**
	 * Takes a screenshot of the page and saves it into the html report
	 * 
	 * @throws IOException
	 */
	public void takeScreenShotLoadReport() throws IOException {
		ExcelDataProvider.test.pass("",
				MediaEntityBuilder.createScreenCaptureFromBase64String(AbstractCommands.takeScreenShot()).build());
	}

	/**
	 * Takes a screenshot of the page and saves it into the html report
	 * 
	 * @param message a message to add to the screenshot
	 * @throws IOException
	 */
	public void takeScreenShotLoadReport(String message) throws IOException {
		ExcelDataProvider.test.pass(message,
				MediaEntityBuilder.createScreenCaptureFromBase64String(AbstractCommands.takeScreenShot()).build());
	}

	/**
	 * Takes a screenshot of the page and saves it into the html report, as failed
	 * step. Test will be considered failed
	 * 
	 * @param message a message to add to the screenshot
	 * @throws IOException
	 */
	public void takeScreenShotLoadReportFail(String message) throws IOException {
		ExcelDataProvider.test.fail(message,
				MediaEntityBuilder.createScreenCaptureFromBase64String(AbstractCommands.takeScreenShot()).build());
		Assert.fail("Error");
	}

	/**
	 * Takes a screenshot of a web element
	 * 
	 * @param locator the xpath of the web element
	 * @return destination string of BASE64 file
	 * @throws IOException
	 */
	static public String takeScreenShotElement(String locator) throws IOException {

		String dest = ((TakesScreenshot) Drivers.driver.findElement(By.xpath(locator)))
				.getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + dest;
	}

	/**
	 * Takes a screenshot of a page
	 * 
	 * @return destination string of BASE64 file
	 * @throws IOException
	 */
	static public String takeScreenShot() throws IOException {
		String dest = ((TakesScreenshot) Drivers.driver).getScreenshotAs(OutputType.BASE64);
		return "data:image/jpg;base64, " + dest;
	}

	/**
	 * Takes a screenshot of a page in png format
	 * 
	 * @return the path of the save file
	 * @throws IOException
	 */
	public String takeScreenshotPNG() throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		String data = formatter.format(date);
		File scrFile = ((TakesScreenshot) Drivers.driver).getScreenshotAs(OutputType.FILE);
		// File destinationFile = new
		// File(System.getProperty("user.dir")+"\\src\\main\\resources\\screenshots\\"+data+".png");
		FileHandler.copy(scrFile,
				new File(System.getProperty("user.dir") + "\\src\\main\\resources\\screenshots\\" + data + ".png"));
		return System.getProperty("user.dir") + "\\src\\main\\resources\\screenshots\\" + data + ".png";
	}

	/**
	 * Returns the text content of a web element
	 * 
	 * @param locator the xpath of the web element
	 * @return the text content
	 */
	public String textContent(String locator) {
		String textContent = Drivers.driver.findElement(By.xpath(locator)).getAttribute("textContent");
		ExcelDataProvider.logInfo("Recupero textContent di " + locator + ": " + textContent);
		return textContent;
	}

	/**
	 * Casts a double number into BigDecimal format
	 * 
	 * @param n the number to be casted
	 * @return the BigDecimal value
	 */
	public BigDecimal toBigDecimal(double n) {
		BigDecimal num = BigDecimal.valueOf(n);
		return num;
	}

	/**
	 * Waits a given amount of time in seconds
	 * 
	 * @param time to wait in seconds
	 */
	public void waiting(int time) {
		ExcelDataProvider.logInfo("Waiting " + time + " seconds.");
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Waits a web element to be clickable
	 * 
	 * @param locator the xpath of the web element
	 */
	public void waitClickable(String locator) {
		ExcelDataProvider.logInfo("Wait to be clickable: " + locator);
		WebDriverWait wait = new WebDriverWait(Drivers.driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
	}

	/**
	 * Waits a web element to be displayed
	 * 
	 * @param locator  the xpath of the web element
	 * @param attempts number of attempts to perform
	 */
	public void waitDisplayed(String locator, int attempts) {
		if (Drivers.driver.findElements(By.xpath(locator)).size() == 1) {
			while (!Drivers.driver.findElement(By.xpath(locator)).isDisplayed() && attempts > 0) {
				waiting(1);
				attempts--;
				ExcelDataProvider.logInfo(locator + " ancora non visible, tentativi " + attempts);
			}
		}
	}

	/**
	 * Waits a web element to be not visible There two waits separated in order to
	 * avoid that an element is discharge and relaoded immidiately after. It can
	 * cause a fail execution if element is still visible after specified seconds.
	 * 
	 * @param locator the xpath of the web element
	 * @param seconds time of waiting
	 */
	public void waitToBeNotVisible(String locator, int seconds) {
		ExcelDataProvider.logInfo("Wait element to be invisible " + locator + " seconds: " + seconds);
		WebDriverWait wait = new WebDriverWait(Drivers.driver, Duration.ofSeconds(seconds));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
		waiting(2);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
	}

	/**
	 * Waits a web element to be visible
	 * 
	 * @param locator  the xpath of the web element
	 * @param attempts number of attempts to try
	 */
	public void waitVisibile(String locator, int attempts) {
		ExcelDataProvider.logInfo("Waiting " + locator + " to be visible, " + attempts + " attempts");
		while (Drivers.driver.findElements(By.xpath(locator)).size() < 1 && attempts > 0) {
			waiting(1);
			attempts--;
			ExcelDataProvider.logInfo(locator + " visible, attempts " + attempts);
		}
	}

	/**
	 * Waits a web element to be visible within 10 seconds.
	 * 
	 * @param locator  the xpath of the web element
	 */
	public void waitVisible(String locator) {
		ExcelDataProvider.logInfo("Waiting " + locator + " to be visible, 10 seconds");
		WebDriverWait wait = new WebDriverWait(Drivers.driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
	}
}
