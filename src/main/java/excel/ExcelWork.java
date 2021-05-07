package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import utils.ExcelDataProvider;

/** This class creates and updates the report.xls file for each test project
 * @author fabio.caccia
 *
 */
public class ExcelWork {

	/**
	 * Excel fil directory
	 */
	static public String excelFilePath = ExcelDataProvider.projectPath+"\\src\\main\\resources\\excel\\report.xls";

	
	/**
	 * Creates the file if there is none
	 */
	public static void createFile() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("report");

		try {
			FileOutputStream out = 
					new FileOutputStream(new File(excelFilePath));

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("Row ID");
			header.createCell(1).setCellValue("Test Name");
			header.createCell(2).setCellValue("Iteration");
			header.createCell(3).setCellValue("Browser");
			header.createCell(4).setCellValue("Test Result");
			header.createCell(5).setCellValue("Data Inizio");
			header.createCell(6).setCellValue("Data Fine");
			header.createCell(7).setCellValue("Durata");

			workbook.write(out);
			out.close();
			workbook.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Updates the file 
	 * @param testName the name of the test
	 * @param iteration the number of the iteration
	 * @param browser the browser executed
	 * @param testResult the result
	 * @param startDate the date the test started
	 * @param testDate the date the test ended
	 * @param durata the test time execution (duration)
	 */
	public static void updateFile(String testName, String iteration, String browser, String testResult, String startDate, String testDate, String durata) {
		File f = new File(excelFilePath);
		if(!f.exists() && !f.isDirectory()) 
			createFile();

		try {
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			String[][] reportTest = 
				{
						{testName, iteration, browser, testResult, startDate, testDate, durata},	
				};

			int rowCount = sheet.getLastRowNum();

			for (String[] aReport : reportTest) {
				Row row = sheet.createRow(++rowCount);

				int columnCount = 0;

				Cell cell = row.createCell(columnCount);
				cell.setCellValue(rowCount);

				for (String field : aReport) {
					cell = row.createCell(++columnCount);
					cell.setCellValue(field);
				}
			}

			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(excelFilePath);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
			ExcelDataProvider.logInfo("Report excel updated");
		} catch (IOException | EncryptedDocumentException ex) {
			ex.printStackTrace();
		}
	}
}

