package com.framework.action.mobile;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.action.Action;
import com.framework.driver.Driver;
import com.framework.driver.MobileDriver;

import io.appium.java_client.ios.IOSDriver;

public class IosAction extends Action {
  private final IOSDriver driver;
  WebDriverWait wait;
  Actions actions;

  public IosAction(Driver driver) {
    super(driver);

    this.driver = this.getDriver();

    int defaultWaitSeonds = 10;

    wait = new WebDriverWait(this.driver, Duration.ofSeconds(defaultWaitSeonds));
    actions = new Actions(this.driver);
  }

  @Override
  public IOSDriver getDriver() {
    MobileDriver driver = (MobileDriver) this._driver;
    return driver.getIOSDriver();
  }

  public void focusOn(WebElement element) {
    try {
      new Actions(driver).moveToElement(element).perform();

    } catch (Exception ignore) {

    }
  } 
}
