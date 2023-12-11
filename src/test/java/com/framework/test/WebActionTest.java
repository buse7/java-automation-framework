package com.framework.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.framework.driver.DRIVER_TYPE;
import com.framework.driver.Driver;
import com.framework.driver.WebCommonDriver.Builder;
import com.framework.page.GoogleMainPage;

import ch.qos.logback.classic.Level;

public class WebActionTest {
  private Driver driver = null;


  @BeforeClass
  public void setUpAll() {
    this.driver = new Builder(DRIVER_TYPE.WEB_CHROME_DRIVER)
        .driverName("driver")
        .maxWindowSize(true)
        .logLevel(Level.INFO)
        .build();
  }
  
  @Test
  public void tc1() {
    GoogleMainPage googleMainPage = new GoogleMainPage(driver);
    googleMainPage.Navigate();
    Assert.assertTrue(true);
  }

  @AfterClass
  public void tearDownAll() {this.driver.quit();}
}