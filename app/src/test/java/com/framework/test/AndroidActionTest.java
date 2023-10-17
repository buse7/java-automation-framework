package com.framework.test;

import java.time.Duration;

import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.framework.action.CommonAction;
import com.framework.action.Terminal;
import com.framework.driver.DRIVER_TYPE;
import com.framework.driver.Driver;
import com.framework.driver.MobileDriver.Builder;
import com.framework.listener.Log;
import com.framework.page.mobile.AndroidTestPage;
import com.framework.util.PropertyHandler;

import ch.qos.logback.classic.Level;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

public class AndroidActionTest {
  private Driver driver = null;
  Logger logger = Log.getInstance().getLogger();

  @BeforeClass
  public void setUpAll() throws Exception {
    UiAutomator2Options options = new UiAutomator2Options()
        .setUdid(Terminal.adb.getTarget())
        .setNoReset(false)
        .setNewCommandTimeout(Duration.ZERO)
        .setAppPackage("net.bucketplace")
        .setPlatformName("Android")
        .setPlatformVersion(Terminal.adb.getDeviceAndroidVersion())
        .setApp(PropertyHandler.getProperties("apppath"));

    try {
      this.driver = new Builder(DRIVER_TYPE.MOBILE_DRIVER_ANDROID, options)
          .driverName("driver")
          .isRecordVideo(false)
          .logLevel(Level.INFO)
          .build();
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }
  
  @Test
  public void androidLoginTest() {
    SoftAssert softAssert = new SoftAssert();
    CommonAction action = new CommonAction(this.driver);
    AndroidDriver realDriver = (AndroidDriver) action.getDriver();
    AndroidTestPage mobileTestPage = new AndroidTestPage(this.driver);


    mobileTestPage.clickEmailLogin();
    softAssert.assertTrue(mobileTestPage.isPresentEmailLoginPageTitle());

    mobileTestPage.inputEmail(PropertyHandler.getProperties("id"));
    mobileTestPage.inputPassword(PropertyHandler.getProperties("password"));
    mobileTestPage.clickLoginBtn();

    if (mobileTestPage.isPresentDialog()) {
      mobileTestPage.clickPostPoneBtn();
    }

    softAssert.assertTrue(mobileTestPage.isPresentMainPage());
    softAssert.assertAll();
  }

  @AfterClass
  public void tearDownlAll() {this.driver.quit();}
  
}
