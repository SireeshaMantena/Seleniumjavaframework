package listeners;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.MediaEntityBuilder;

import drivers.Drivers;
import log4j.LogClass;
import utils.AbstractCommands;
import utils.ExcelDataProvider;


/** Implementation of the TestNG Listeners.
 * @author fabio.caccia
 *
 */
public class TestNGListeners implements ITestListener{

	/**
	 *  Project path.
	 */
	String testPath =  ExcelDataProvider.projectPath;
	/**
	 * Test name.
	 */
	String testName = testPath.substring(testPath.lastIndexOf("\\")+1);
	/**
	 * Time date of test start.
	 */
	private String startDate;

	/** returns the time the test started.
	 * @return the time the test started 
	 */
	public String getStartDate() {
		return startDate;
	}

	/** sets the time the test started.
	 * @param startDate.
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	/**
	 * Calculates test duration.
	 * @param startDate the time the test started.
	 * @param endDate the time the test ended.
	 * @return duration.
	 * @throws ParseException
	 */
	public String duration(String startDate, String endDate) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
		Date d1 = null;
		Date d2 = null;
		d1 = format.parse(startDate);
		d2 = format.parse(endDate);
		long diff = d2.getTime() - d1.getTime();
		long diffSeconds = diff / 1000 % 60;  
		long diffMinutes = diff / (60 * 1000) % 60; 
		long diffHours = diff / (60 * 60 * 1000);

		return diffHours+":"+diffMinutes+":"+diffSeconds;
	}


	/** Returns a date in a given format.
	 * @return a time date.
	 */
	public String getDate() {             
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date(); 
		String data = formatter.format(date);
		return data;
	}

	/**
	 * Actions to be executed at the start of the test.
	 */
	public void onTestStart(ITestResult result) {
		LogClass.logger.info("Test start: "+testName);
		setStartDate(getDate());
	}

	/**
	 * Actions to be executed on success case.
	 * Reports are updated.
	 */
	public void onTestSuccess(ITestResult result) {
		LogClass.logger.info("Test ok: "+testName);
		ExcelDataProvider.test.pass("Test ok: "+testName);
		Reporter.log("OK");
		
	}
	

	/**
	 * Actions to be executed on failure case.
	 * Reports are updated.
	 * If exitonfail is yes then closes the browser and quit execution.
	 * If exitonfail is no will update results and continue.
	 */
	public void onTestFailure(ITestResult result)  {
		try {	
			for (String winHandle : Drivers.driver.getWindowHandles()) {
				Drivers.driver.switchTo().window(winHandle);
				ExcelDataProvider.logInfo("Window:" +winHandle.toString());
				ExcelDataProvider.logInfo("Switched to window:" +winHandle.toString());
				ExcelDataProvider.test.fail(Drivers.driver.getCurrentUrl(),MediaEntityBuilder.createScreenCaptureFromBase64String(AbstractCommands.takeScreenShot()).build());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String risultato = result.getThrowable().getCause().toString();
		System.out.println(risultato);
		result.getThrowable().printStackTrace();

		risultato = risultato.contains("\n") ? risultato.substring(0, risultato.indexOf("\n")) : risultato;
		ExcelDataProvider.test.fail(risultato);

		ExcelDataProvider.test.fail(result.getThrowable().toString());
		LogClass.logger.fatal("Test failed: "+testName);


		ExcelDataProvider.extent.flush();
		
		File file = new File(ExcelDataProvider.reportPathAndName);
		if (file.exists())
			file.renameTo(new File(ExcelDataProvider.reportPathAndName.replace(".html", "_FAIL_.html")));
		
		if(ExcelDataProvider.exitOnFail.equals("yes")) {
			Drivers.driver.quit();
			ExcelDataProvider.logInfo("exitOnFail: si");
		}
	}

	/**
	 * Actions to be executed on skipped case.
	 * Reports are updated.
	 */
	public void onTestSkipped(ITestResult result) {
		LogClass.logger.info("Iteration skipped: "+testName);
		ExcelDataProvider.test.skip("Iteration skipped: "+testName);
		//		try {
		//			ExcelWork.updateFile(testName, ExcelDataProvider.iteration,ExcelDataProvider.browser, "SKIP", getStartDate(), getDate(),duration(getStartDate(),getDate()));
		//		} catch (ParseException e) {
		//			e.printStackTrace();
		//			Assert.fail("update file excel",e);
		//		}
	}

	/**
	 * Actions to be executed once the suite is finished.
	 */
	public void onFinish(ITestContext context) {
		LogClass.logger.info("Suite finished: "+testName);
	}
}
