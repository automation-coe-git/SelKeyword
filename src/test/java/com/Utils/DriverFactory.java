package com.Utils;

import org.openqa.selenium.WebDriver;

public class DriverFactory {

	ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	private DriverFactory() {
		
	}

	private static DriverFactory instance = new DriverFactory();

	public static DriverFactory getInstance() {
		return instance;
	}

	public WebDriver getDriver() {
		return driver.get();
	}

	public void setDriver(WebDriver driverParm) {
		driver.set(driverParm);
	}

	public void closeBrowser() {
		driver.get().close();
		
	}
}
