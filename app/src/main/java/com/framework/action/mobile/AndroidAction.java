package com.framework.action.mobile;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.framework.action.Action;
import com.framework.action.Terminal;
import com.framework.driver.Driver;
import com.framework.driver.MobileDriver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;

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
  
  public Boolean isClickAble(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("clickable"));
  }

  public Boolean isChecked(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("checked"));
  }

  public Boolean isCheckable(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("checkable"));
  }

  public Boolean isEnable(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("enabled"));
  }

  public Boolean isFocusable(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("focusable"));
  }

  public Boolean isFocused(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("focused"));
  }

  public Boolean isLongClickable(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("long-clickable"));
  }

  public Boolean isSelected(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("selected"));
  }

  public Boolean isDisplayed(WebElement element) {
    return isElement(element) && Boolean.parseBoolean(element.getAttribute("disaplyed"));
  }

  public String getBound(WebElement element) {
    return isElement(element) ? element.getAttribute("bounds") : null;
  }

  public ApplicationState queryAppState(String packageName) {
    return driver.queryAppState(packageName);
  }

  public void launch(String packageName) {
    driver.activateApp(packageName);
    logger.info("Launch the app, PackageName : {}", packageName);
  }

  public void terminate(String packageName) {
    driver.terminateApp(packageName);
    logger.info("Terminate the app, PackageName : {}", packageName);
  }

  public void reStart(String packageName) {
    terminate(packageName);
    launch(packageName);
    logger.info("Restart the app, PackageName : {}", packageName);
  }

  public void backKey() {
    driver.navigate().back();
    logger.debug("Click android soft Back key button");
  }

  public String getCurrentActivity() {
    return driver.currentActivity();
  }

  public void unLock() {
    adb.screen(true);
    Terminal.run("adb shell input touchscreen swipe 930 1580 930 380", 10);
    logger.atDebug().log("UnLock Device");
  }

  public Boolean isKeyBoardShown() {
    return driver.isKeyboardShown();
  }

  public AndroidAction openNotifications() {
    driver.openNotifications();
    logger.atDebug().log("Open Notification");
    return this;
  }

  public void longClick(WebElement element) {
    if (isLongClickable(element)) {
      logger.atError().log("Unable to click element, Element : {}", element);
      throw new NoSuchElementException("It is not longClickalbe");
    }

    actions.clickAndHold(element).release().build();
    logger.atDebug().log("Long click element, Element : {}", element);
  }

  public void doubleClick(WebElement element) {
    if (isClickAble(element)) {
      logger.atError().log("Unable to click element, Element : {}", element);
      throw new NoSuchElementException("It is not longClickalbe");
    }

    actions.doubleClick(element);
    logger.atDebug().log("Double click element, Element : {}", element);
  }

  public void moveToElement(WebElement element, int xOffset, int yOffset) {
    if (isClickAble(element)) {
      logger.atError().log("Unable to click element, Element : {}", element);
      throw new NoSuchElementException("It is not longClickalbe");
    }

    actions.moveToElement(element, xOffset, yOffset);
    logger.atDebug().log("Move element, Element: {}, X Coordinate : {}, Y Coordinate : {}", element, xOffset, yOffset);
  }

  public void dragAndDrop(WebElement element, int xOffset, int yOffset) {
    if (isClickAble(element)) {
      logger.atError().log("Unable to click element, Element : {}", element);
      throw new NoSuchElementException("It is not longClickalbe");
    }

    actions.dragAndDropBy(element, xOffset, yOffset);
    logger.atDebug().log("Drag and drop element, Element: {}, X Coordinate : {}, Y Coordinate : {}", element, xOffset, yOffset);
  }
}
