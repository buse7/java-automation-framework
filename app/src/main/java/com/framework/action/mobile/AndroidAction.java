package com.framework.action.mobile;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.action.Action;
import com.framework.driver.Driver;
import com.framework.driver.MobileDriver;

import io.appium.java_client.android.AndroidDriver;

public class AndroidAction extends Action {
  private final AndroidDriver driver;
  WebDriverWait wait;
  Actions actions;

  public AndroidAction(Driver driver) {
    super(driver);

    this.driver = this.getDriver();

    int defaultWaitSeonds = 10;

    wait = new WebDriverWait(this.driver, Duration.ofSeconds(defaultWaitSeonds));
    actions = new Actions(this.driver);
  }

  @Override
  public AndroidDriver getDriver() {
    MobileDriver driver = (MobileDriver) this._driver;
    return driver.getAndroidDriver();
  }

  public void focusOn(WebElement element) {
    try {
      new Actions(driver).moveToElement(element).perform();
      
    } catch (Exception ignore) {
      // TODO: handle exception
    }
  }
}
