package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;

import drivers.Drivers;
import log4j.LogClass;

/** This is the core class of the framework.
 *  Here are used TestNG annotations.
 *  Java Reflection is used to launch projects classes.
 * @author fabio.caccia
 *
 */
public class ExcelDataProvider {

	/**
	 * test context which contains all the information for a given test run. An
	 * instance of this context is passed to the test listeners so they can query information about
	 * their environment
	 */
	public static ITestContext context;
	/**
	 * Html report
	 */
	public static ExtentSparkReporter htmlReporter;
	/**
	 * Extent report
	 */
	public static ExtentReports extent;
	/**
	 * Extent test
	 */
	public static ExtentTest test;
	/**
	 * Project path
	 */
	public static String projectPath= System.getProperty("user.dir");

	/**
	 * Iteration from parameter
	 */
	public static String iteration;
	/**
	 * Establish if to stop execution on fail
	 */
	public static String exitOnFail;
	/**
	 * The class order of execution
	 */
	public static String classOrder;
	/**
	 * Test name
	 */
	public static String testName;
	/**
	 * Iterations to be executed
	 */
	public static String iterationsToRun;
	/**
	 * Iterations that are consecutive
	 */
	public String[] itersConsecutive;
	/**
	 * Ordered array of iterations to run
	 */
	public List<Integer> iterations = new ArrayList<Integer>();
	
	public static String reportName;
	public static String reportPathAndName;

	public static String email;

	/**
	 * Browser to be opened
	 */
	public static String browser;

	/** This method reads the parameter values from the xml TestNG file and executes the <b>esegui()</b> method.
	 * Iterations to run are checked here. If there are iterations to be skipped, an execption will be launched.
	 * TestName, browser and class order are set here.
	 * Each test class is launched and all of its variables are set.
	 *  
	 * @param mapdata the data values from Excel, given by the data provider
	 * @param context test context which contains all the information for a given test run. An
	 * instance of this context is passed to the test listeners so they can query information about
	 * their environment
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IOException
	 */
	@Test(dataProvider = "test1data")
	public void test(Map<String, String> mapdata,ITestContext context) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, IOException  {

		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");  
		Date date = new Date(); 
		String data = formatter.format(date);
		
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd_MM_yyyy");  
		Date date2 = new Date(); 
		String data2 = formatter2.format(date2);

		try {
			reportName=context.getName()+" Iteration "+ mapdata.get("iteration") +" "+data+".html";
			String pathNameReport =projectPath+"\\src\\main\\resources\\report\\"+data2+"\\"+reportName;
			String jsonPath=projectPath+"\\src\\main\\resources\\report\\"+data2+"\\"+data+".json";
			reportPathAndName=pathNameReport;
			htmlReporter = new ExtentSparkReporter(pathNameReport);
			htmlReporter.config().setTimelineEnabled(true);
			JsonFormatter json = new JsonFormatter(jsonPath);
			extent = new ExtentReports();
			extent.createDomainFromJsonArchive(jsonPath);
			extent.attachReporter(json, htmlReporter);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		test = extent.createTest(context.getName()+", Iteration: "+ mapdata.get("iteration") +" - "+mapdata.get("note"), "Browser: "+context.getCurrentXmlTest().getParameter("browser"));
		exitOnFail=context.getCurrentXmlTest().getParameter("exitonfail");

		/**
		 * Imposto le iterazioni che devono essere eseguite
		 */
		iterationsToRun = context.getCurrentXmlTest().getParameter("iterationstorun");
		String[] iters=iterationsToRun.split(",");
		for (int i = 0; i < iters.length; i++) {
			if(iters[i].contains("-")) {
				itersConsecutive = iters[i].split("-");
				for (int j = Integer.parseInt(itersConsecutive[0]); j < Integer.parseInt(itersConsecutive[1])+1; j++) 
					iterations.add(j);
			}
			else
				iterations.add(Integer.parseInt(iters[i]));
		}

		browser=context.getCurrentXmlTest().getParameter("browser");
		iteration=mapdata.get("iteration");
		classOrder=context.getCurrentXmlTest().getParameter("classorder");
		testName=context.getName();
		email=context.getCurrentXmlTest().getParameter("email");

		if(!iterations.contains(Integer.parseInt(iteration)))
			throw new SkipException("Iteration skipped: "+iteration);

		Drivers.avviaDriver(browser);
		Drivers.driver.manage().window().maximize();
		ExcelDataProvider.logInfo("Open browser: "+browser);

		File file = new File(context.getCurrentXmlTest().getParameter("resource"));
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(1);
		wb.close();

		int rowCount= sheet.getLastRowNum();

		DataFormatter df = new DataFormatter();
		for (int i = 0; i < rowCount+1; i++) {

			String key = df.formatCellValue(sheet.getRow(i).getCell(0));
			String className = df.formatCellValue(sheet.getRow(i).getCell(1));

			Class<?> c = Class.forName(context.getCurrentXmlTest().getParameter("package")+"."+className);
			Object instance = c.getDeclaredConstructor().newInstance();

			for (Map.Entry<String, String> entry : mapdata.entrySet()) {
				if(entry.toString().split("\\.")[0].equals(key.split("\\.")[0])) {
					Field field = c.getDeclaredField(entry.getKey().substring(entry.getKey().indexOf(".")+1));
					LogClass.logger.info("variable: "+field);
					field.set(instance,entry.getValue());
				}
			}

			Field f = c.getDeclaredField("skip");
			f.setAccessible(true);
			Object skipValue = f.get(instance);

			if(skipValue==null) {
				Method method = c.getDeclaredMethod("esegui");
				ExcelDataProvider.logInfo("Invoke method \"esegui\" from class "+c.toString());
				method.invoke(instance); 
			}
			else 
				ExcelDataProvider.logInfo("Class "+c.getName()+ " skipped");
		}

		Drivers.driver.quit(); 

				try {
					extent.flush();
				} 
				catch(Exception e){
					e.printStackTrace();
				}
	}


	/** Reads the Excel file for data-driven testing and saves values into data provider
	 * 
	 * @param context test context which contains all the information for a given test run. An
	 * instance of this context is passed to the test listeners so they can query information about
	 * their environment
	 * @return the values from the Excel file
	 * @throws IOException
	 */
	@DataProvider(name = "test1data")
	public Object[][] getData(ITestContext context) throws IOException {
		Object[][] obj = null;
		try {
			File file = new File(context.getCurrentXmlTest().getParameter("resource"));
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			wb.close();

			int rowCount= sheet.getLastRowNum();
			int colCount = sheet.getRow(0).getLastCellNum();

			obj = new Object[colCount-1][1];
			DataFormatter df = new DataFormatter();
			for (int i = 0; i < colCount-1; i++) {
				Map<String, String> datamap= new HashMap<String, String>();
				for (int j = 0; j < rowCount+1; j++) {
					String a = df.formatCellValue(sheet.getRow(j).getCell(0));
					String b = df.formatCellValue(sheet.getRow(j).getCell(i+1));

					if(!b.equals(""))
						datamap.put(a, b);	
				}
				obj[i][0] =datamap;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}

	/** Reports an info message into console and log4j.
	 * @param message the message to be reported
	 */
	public static void logInfo(String message) {
		LogClass.logger.info(message);
		//		ExcelDataProvider.test.info(message);
	}

	/** Reports an info message into console and log4j.
	 *  Reports the same message as a pass message
	 * @param message the message
	 */
	public static void logPass(String message) {
		LogClass.logger.info("PASS: "+message);
		ExcelDataProvider.test.pass(message);
	}
}
