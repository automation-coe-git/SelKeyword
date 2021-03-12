package com.Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportNG {
	
	static ExtentReports extent;
	
	public static ExtentReports setupExtentReport(String browser) throws Exception {
		
		ExtentSparkReporter htmlReporter;
		extent = new ExtentReports();

		htmlReporter = new ExtentSparkReporter(Resources.CreateFileWithTimeStamp(browser));
		extent.attachReporter(htmlReporter);
		
		htmlReporter.config().setDocumentTitle("Test Automation Report");
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setReportName("Test Report");
		
		extent.setSystemInfo("Executed on Browser: ",browser);
		extent.setSystemInfo("Executed on OS: ", System.getProperty("os.name"));
		extent.setSystemInfo("Executed by User: ", System.getProperty("user.name"));

		return extent;
	}


}
