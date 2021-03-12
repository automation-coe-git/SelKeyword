package com.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.Test.Keywords;
import com.Utils.BrowserFactory;
import com.Utils.DriverFactory;
import com.Utils.ExtentFactory;
import com.Utils.ExtentReportNG;
import com.Utils.GetScreenShot;
import com.Utils.Log4j;
import com.Utils.Resources;
import com.Utils.Xls_Reader;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;


public class TestController extends Resources {

	String TestSuites = testSuite;
	Xls_Reader s = new Xls_Reader(testSuite);
	BrowserFactory browserFactory = new BrowserFactory();
	
	@Parameters("browser")
	@BeforeTest
	public void LaunchApplication(String browser) throws Exception {
		DriverFactory.getInstance().setDriver(browserFactory.createBrowserInstance(browser));
		DriverFactory.getInstance().getDriver().manage().window().maximize();
		DriverFactory.getInstance().getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

	}

	@AfterTest
	public void tearDown() {
		ExtentFactory.getInstance().removeExtentObject();
	}

	@BeforeClass
	public void initBrowser() throws IOException {
		
		Initialize();

	}

	@Parameters("browser")
	@Test
	public void TestCaseController(String browser) throws Exception {

		DOMConfigurator.configure("log4j.xml");

		@SuppressWarnings("unused")
		String TCStatus = "Pass";
		ExtentReports extent;
		ExtentTest test;
		String nonstTestdata,nonstDescription,nonstTestCaseName,nostTestCaseDesc;
		extent = ExtentReportNG.setupExtentReport(browser);  
		
		String runSuiteName = suiteProperties.getProperty("runSuite");
		testcaseSheetName = suiteProperties.getProperty("testcaseSheetName");
		for (int TC = 2; TC <= suiteData.getRowCount(runSuiteName); TC++) {

			testCaseDescription = suiteData.getCellData(runSuiteName, "Description", TC);
			nostTestCaseDesc = testCaseDescription;
			String runMode = suiteData.getCellData(runSuiteName, "RunMode", TC);
			testCaseName = suiteData.getCellData(runSuiteName, "TestCaseName", TC);
			nonstTestCaseName = testCaseName;

			if (runMode.equals("Y")) {

				String TSStatus = "Pass";

				System.out.println("SuiteData.getRowCount(TestCaseID)" + suiteData.getRowCount(testcaseSheetName));
				test = extent.createTest(nonstTestCaseName, nostTestCaseDesc);
				ExtentFactory.getInstance().setExtent(test);  
				for (int TS = 2; TS <= suiteData.getRowCount(testcaseSheetName); TS++) {
					testcase_ID = suiteData.getCellData(testcaseSheetName, "Test Case ID", TS);
					if (testcase_ID.equals(testCaseName)) {
						keyword = suiteData.getCellData(testcaseSheetName, "Keyword", TS);
						webElement = suiteData.getCellData(testcaseSheetName, "WebElement", TS);
						proceedOnFail = suiteData.getCellData(testcaseSheetName, "ProceedOnFail", TS);
						testStepID = suiteData.getCellData(testcaseSheetName, "TestStepID", TS);
						descriptionOfTest = suiteData.getCellData(testcaseSheetName, "Description", TS);
						nonstDescription = descriptionOfTest;
						testDataField = suiteData.getCellData(testcaseSheetName, "TestDataField", TS);
						testData = testStepData.GetTestData("MasterTestData", testcase_ID, testDataField, "Testdata");
						nonstTestdata = testData;
						Log4j.startTestCase(testcaseSheetName, keyword, webElement, testData);
						Method method = Keywords.class.getMethod(keyword);
						TSStatus = (String) method.invoke(method);

						if (TSStatus.contains("Failed")) {
							String filename = "TestCases" + testStepID + "[" + testData + "]";
							TCStatus = TSStatus;
							Log4j.error(testCaseName);
							String screenShot = GetScreenShot.capture(DriverFactory.getInstance().getDriver(),
									filename);
							ExtentFactory.getInstance().getExtent().fail(
									//"<font color='black'style='font-size:12px' </font> " +  descriptionOfTest + " - " + testData,
									MediaEntityBuilder.createScreenCaptureFromPath(screenShot,
											"<font color='black'style='font-size:12px' </font> " +" Unable to "+ nonstDescription + " - "
													+ nonstTestdata +" - "+ Log4j.error(nonstTestCaseName))
											.build());

						} else {

							ExtentFactory.getInstance().getExtent().log(Status.PASS, "<font color='black'style='font-size:12px' </font> " + nonstDescription + " - " + nonstTestdata);
						}
						extent.flush();

						if (proceedOnFail.equals("N")) {
							break;
						}
					}

				}

			}
		}

	}
}
