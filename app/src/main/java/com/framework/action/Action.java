package com.framework.action;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.framework.driver.Driver;
import com.framework.driver.WebCommonDriver;

public class Action extends CommonAction {
  private final WebDriver driver;
  WebDriverWait wait;
  public JavaScriptAction js;

  public Alert alert;
  public Confirm confirm;
  public PopUp popup;
  public IFrame iFrame;

  public Boolean isHeadless;

  public Action(Driver driver) {
    super(driver);

    this.driver = this.getDriver();
    this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    int defaultWaitSeonds = 10;
    wait = new WebDriverWait(this.driver, Duration.ofSeconds(defaultWaitSeonds));

    js = new JavaScriptAction(this.driver);
    alert = new Alert(this.driver);
    confirm = new Confirm(this.driver);
    popup = new PopUp(this.driver);
    iFrame = new IFrame(this.driver);
    isHeadless = checkHeadless();
  }

  public void implicitlyWait(int seconds) {
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    logger.atDebug().log("Apply implict wait for {} seconds", seconds);
  }

  public void goTo(String url) {
    logger.atDebug().log("Current Page : {}", getCurrentUrl());
    driver.get(url);
    logger.atInfo().log("Go to : {}", url);
  }

  @Override
  public WebDriver getDriver() {
    WebCommonDriver driver = (WebCommonDriver) this._driver;
    return driver.get();
  }

  public Boolean isElement(WebElement element) {
    try {
      wait.until(ExpectedConditions.visibilityOf(element));
      focusOn(element);
      logger.atDebug().log("Find element, Element info : {}", element);
      return true;
    } catch (Exception e) {
      // TODO: handle exception
      logger.atWarn().log("Cannot find element, Element Info : {}", element);
      return false;
    }
  }

  public Boolean isElement(WebElement element, int waitSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
      wait.until(ExpectedConditions.visibilityOf(element));
      focusOn(element);
      logger.atDebug().log("Find element, Element info : {}", element);
      return true;
    } catch (Exception e) {
      // TODO: handle exception
      logger.atWarn().log("Cannot find element, Element Info : {}", element);
      return false;
    }
  }

  public Boolean isElement(By locator) {
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      focusOn(driver.findElement(locator));
      logger.atDebug().log("Find element, Locator info : {}", locator);
      return true;
    } catch (Exception e) {
      // TODO: handle exception
      logger.atWarn().log("Cannot find element, Locator Info : {}", locator);
      return false;
    }
  }

  public Boolean isElement(By locator, int waitSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
      wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      focusOn(driver.findElement(locator));
      logger.atDebug().log("Find element, Locator info : {}", locator);
      return true;
    } catch (Exception e) {
      // TODO: handle exception
      logger.atWarn().log("Cannot find element, Locator Info : {}", locator);
      return false;
    }
  }

  public Boolean isDisplayed(WebElement element) {
    return isElement(element) && element.isDisplayed();
  }

  public Boolean isDisplayed(By locator) {
    return isElement(locator) && driver.findElement(locator).isDisplayed();
  }

  public Boolean isClickableElement(WebElement element) {
    try {
      wait.until(ExpectedConditions.elementToBeClickable(element));
      logger.atDebug().log("Element is clickable, Element Info : {}", element);
      return true;
    } catch (NoSuchElementException | TimeoutException e) {
      logger.atWarn().log("Element is not clickabled, Element Info : {}", element);
      return false;
    }
  }

  public Boolean isClickableElement(WebElement element, int waitSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
      wait.until(ExpectedConditions.elementToBeClickable(element));
      logger.atDebug().log("Element is clickable, Element Info : {}", element);
      return true;
    } catch (NoSuchElementException | TimeoutException e) {
      logger.atWarn().log("Element is not clickabled, Element Info : {}", element);
      return false;
    }
  }

  public Boolean isClickableElement(By locator) {
    try {
      wait.until(ExpectedConditions.elementToBeClickable(locator));
      logger.atDebug().log("Element is clickable, Locator Info : {}", locator);
      return true;
    } catch (NoSuchElementException | TimeoutException e) {
      logger.atWarn().log("Element is not clickabled, Locator Info : {}", locator);
      return false;
    }
  }

  public Boolean isClickableElement(By locator, int waitSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
      wait.until(ExpectedConditions.elementToBeClickable(locator));
      logger.atDebug().log("Element is clickable, Locator Info : {}", locator);
      return true;
    } catch (NoSuchElementException | TimeoutException e) {
      logger.atWarn().log("Element is not clickabled, Locator Info : {}", locator);
      return false;
    }
  }

  public List<WebElement> getChildren(WebElement element) {
    return element.findElements(By.xpath(".//*"));
  }

  public List<WebElement> getChildren(By locator) {
    return driver.findElement(locator).findElements(By.xpath(".//*"));
  }

  public List<WebElement> getFirstChildren(WebElement element) {
    return element.findElements(By.xpath("./*"));
  }

  public List<WebElement> getFirstChildren(By locator) {
    return driver.findElement(locator).findElements(By.xpath("./*"));
  }

  public void click(WebElement element) {
    if (isClickableElement(element)) {
      focusOn(element);
      element.click();
      logger.atDebug().log("Click element, Element Info : {}", element);
    } else {
      if (element.isEnabled()) {
        js.click(element);
        logger.atInfo().log("JS click element, Element Info : {}", element);
      } else {
        logger.atError().log("Unable to click element, Element Info : {}", element);
        throw new NoSuchElementException("Element 탐색 불가");
      }
    }
  }

  public void click(By locator) {
    if (isClickableElement(locator)) {
      focusOn(driver.findElement(locator));
      driver.findElement(locator).click();
      logger.atDebug().log("Click element, Locator Info : {}", locator);
    } else {
      if (driver.findElement(locator).isEnabled()) {
        js.click(locator);
        logger.atInfo().log("JS click element, Locator Info : {}", locator);
      } else {
        logger.atError().log("Unable to click element, Locator Info : {}", locator);
        throw new NoSuchElementException("Element 탐색 불가");
      }
    }
  }

  public void waitClick(WebElement element, int waitSeconds) {
    if (isClickableElement(element, waitSeconds)) {
      focusOn(element);
      element.click();
      logger.atDebug().log("Click element, Element Info : {}", element);
    } else {
      if (element.isEnabled()) {
        js.click(element);
        logger.atInfo().log("JS click element, Element Info : {}", element);
      } else {
        logger.atError().log("Unable to click element, Element Info : {} for more than {} seconds", element,
            waitSeconds);
        throw new NoSuchElementException("Element 탐색 불가");
      }
    }
  }

  public void waitClick(By locator, int waitSeconds) {
    if (isClickableElement(locator, waitSeconds)) {
      focusOn(driver.findElement(locator));
      driver.findElement(locator).click();
      logger.atDebug().log("Click element, Locator Info : {}", locator);
    } else {
      if (driver.findElement(locator).isEnabled()) {
        js.click(locator);
        logger.atInfo().log("JS click element, Locator Info : {}", locator);
      } else {
        logger.atError().log("Unable to click element, Locator Info : {} for more then {} seconds", locator,
            waitSeconds);
        throw new NoSuchElementException("Element 탐색 불가");
      }
    }
  }

  public void type(WebElement element, CharSequence... keyToSends) {
    element.sendKeys(keyToSends);
    logger.atInfo().log("Type [{}] in element, Element Info : {}", keyToSends, element);
  }

  public void type(By locator, CharSequence... keyToSends) {
    driver.findElement(locator).sendKeys(keyToSends);
    logger.atInfo().log("Type [{}] in element, Locator Info : {}", keyToSends, locator);
  }

  public void type(WebElement element, Boolean isClear, CharSequence... keyToSends) {
    if (isClear) {
      element.clear();
    }
    element.sendKeys(keyToSends);
    logger.atInfo().log("Type [{}] in element, Element Info : {}", keyToSends, element);
  }

  public void type(By locator, Boolean isClear, CharSequence... keyToSends) {
    if (isClear) {
      driver.findElement(locator).clear();
    }
    driver.findElement(locator).sendKeys(keyToSends);
    logger.atInfo().log("Type [{}] in element, Locator Info : {}", keyToSends, locator);
  }

  public void uploadFile(WebElement element, CharSequence... path) {
    element.sendKeys(path);
    logger.atInfo().log("UploadFile [{}] element, Path Info : {}", element, path);
  }

  public void uploadFile(By locator, CharSequence... path) {
    driver.findElement(locator).sendKeys(path);
    logger.atInfo().log("UploadFile [{}] element, Path Info : {}", locator, path);
  }

  public String getText(WebElement element) {
    return isElement(element) ? element.getText() : null;
  }

  public String getText(By locator) {
    return isElement(locator) ? driver.findElement(locator).getText() : null;
  }

  public void focusOn(WebElement element) {
    try {
      new Actions(driver).moveToElement(element).perform();
      if (!isHeadless) {
        js.setHighlight(element);
        js.removeHighlight(element);
      }
    } catch (Exception ignored) {
      // TODO: handle exception
    }
  }

  public String getCurrentUrl() {
    return driver.getCurrentUrl();
  }

  public class JavaScriptAction {
    private final JavascriptExecutor je;
    private final WebDriver driver;

    public JavaScriptAction(WebDriver driver) {
      this.driver = driver;
      je = (JavascriptExecutor) driver;
    }

    public Object execute(String script) {
      return je.executeScript(script);
    }

    public Object execute(String script, Object... args) {
      return je.executeScript(script, args);
    }

    public void setHighlight(WebElement element) {
      je.executeScript("arguments[0].style.border = 'solid 4px red'", element);

      try {
        Thread.sleep(200L);
      } catch (InterruptedException e) {
        // TODO: handle exception
        Assert.fail(e.getMessage());
      }
    }

    public void removeHighlight(WebElement element) {
      je.executeScript("arguments[0].style.border = ''", element);
    }

    public void click(WebElement element) {
      je.executeScript("arguments[0].click()", element);
    }

    public void click(By locator) {
      je.executeScript("arguments[0].click()", driver.findElement(locator));
    }

    public void makeAlert(String text) {
      je.executeScript("alert('" + text + "')");
    }

    public void makeConfirm(String text) {
      je.executeScript("confirm('" + text + "')");
    }

    public String getCurrentDomatin() {
      return je.executeScript("return document.domain") + "";
    }

    public String getCurrentUrl() {
      return je.executeScript("return document.URL") + "";
    }

    public String getTitle() {
      return je.executeScript("return document.title") + "";
    }

    public void goTo(String url) {
      if (!url.startsWith("http")) {
        url = "https://" + url;
      }
      je.executeScript("window.location = '" + url + "'");
      logger.atInfo().log("go to {}", url);
    }

    public void get(String url) {
      goTo(url);
    }

    public void scroll(int x, int y) {
      je.executeScript("window.scrollTo(" + x + ", " + y + ")");
    }

    public void scrollToTop() {
      je.executeScript("window.scrollTo(0,0)");
    }

    public void scrollToBottom() {
      je.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    public int getInnerHeight() {
      return Integer.parseInt(je.executeScript("return window.innerHeight") + "");
    }

    public void refresh() {
      je.executeScript("location.reload()");
    }

    public void setValue(WebElement element, String value) {
      je.executeScript("arguments[0].value = " + value + "", element);
    }

    public void newWindows(String url) {
      je.executeScript("window.open(" + url + ")");
    }

    public void newTab() {
      je.executeScript("window.open('about:black', '_black');");
    }

    public void removeSession(String key) {
      je.executeScript("window.sessionStorage.removeItem(" + key + ")");
    }

    public Boolean isSession(String key) {
      return !(je.executeScript("return window.sessionStorage.getItem(" + key + ")") == null);
    }

    public String getSession(String key) {
      return (String) je.executeScript("return window.sessionStorage.getItem(" + key + ")");
    }

    public void setSession(String key, Object value) {
      je.executeScript(String.format("window.sessionStorage.setItem('%s', '%s');", key, value));
    }

    public void clearAllSession() {
      je.executeScript("window.sessionStorage.clear()");
      logger.atInfo().log("Clear all Sessions");
    }

    public WebElement getParent(WebElement element) {
      try {
        return (WebElement) je.executeScript("return arguments[0].parentNode", element);
      } catch (ClassCastException e) {
        // TODO: handle exception
        logger.atWarn().log("Cannot find element {}'s parent", element);
        return null;
      }
    }

    public int getPositionX() {
      String positionX = je.executeScript("return window.scrollX") + "";
      if (positionX.contains(".")) {
        positionX = positionX.split("\\.")[0];
      }
      return Integer.parseInt(positionX);
    }

    public int getPositionY() {
      String positionY = je.executeScript("return window.scrollY") + "";
      if (positionY.contains(".")) {
        positionY = positionY.split("\\.")[0];
      }
      return Integer.parseInt(positionY);
    }
  }

  public class Alert {
    private final WebDriver driver;

    public Alert(WebDriver driver) {
      this.driver = driver;
    }

    public Boolean isAlert() {
      try {
        wait.until(ExpectedConditions.alertIsPresent());
        return true;
      } catch (TimeoutException | NoAlertPresentException e) {
        // TODO: handle exception
        logger.atWarn().log("Alert does not found");
        return false;
      }
    }

    public void accept() {
      if (!isAlert()) {
        logger.atError().log("Alert does found");
        throw new NoAlertPresentException("Cannot find alert");
      }

      driver.switchTo().alert().accept();
      logger.atDebug().log("Click the accept button on the alert");
    }

    public String getText() {
      if (!isAlert()) {
        logger.atError().log("Alert does found");
        throw new NoAlertPresentException("Cannot find alert");
      }

      return driver.switchTo().alert().getText();
    }

    public void type(String keyToSends) {
      if (!isAlert()) {
        logger.atError().log("Alert does found");
        throw new NoAlertPresentException("Cannot find alert");
      }

      driver.switchTo().alert().sendKeys(keyToSends);
    }
  }

  public class Confirm extends Alert {
    public Confirm(WebDriver driver) {
      super(driver);
    }

    public void dismiss() {
      if (!isAlert()) {
        logger.atError().log("Confirm does not found");
        throw new NoAlertPresentException("Cannot find confirm");
      }

      driver.switchTo().alert().dismiss();
      logger.atDebug().log("Click the dismiss button on the confirm");
    }
  }

  public class PopUp {
    private final WebDriver driver;

    public PopUp(WebDriver driver) {
      this.driver = driver;
    }

    public void switchToPopUp() {
      String main = driver.getWindowHandle();
      List<String> windows = new ArrayList<String>(driver.getWindowHandles());
      for(String window : windows){
        if(!(window.equals(main))){
          driver.switchTo().window(window);
        }
      }
    }
  }

  public class IFrame {
    WebDriver driver;

    public IFrame(WebDriver driver) {
      this.driver = driver;
    }

    public void switchTo(String frameId) {
      if (isElement(By.xpath("//iframe[@id='" + frameId + "'']"))) {
        driver.switchTo().frame(frameId);
        logger.atInfo().log("Swtich to frame of [{}]", frameId);
      } else {
        logger.atError().log("Cannot switch to frame of [{}]", frameId);
        throw new NoSuchElementException("Cannot find iFrame Element");
      }
    }

    public void switchToFrameByName(String name) {
      if (isElement(By.xpath("//iframe[@name='" + name + "'']"))) {
        driver.switchTo().frame(name);
        logger.atInfo().log("Swtich to frame of [{}]", name);
      } else {
        logger.atError().log("Cannot switch to frame of [{}]", name);
        throw new NoSuchElementException("Cannot find iFrame Element");
      }
    }

    public void switchTo(int frameIndex) {
      driver.switchTo().frame(frameIndex);
    }

    public void switchTo(WebElement element) {
      if (isElement(element)) {
        driver.switchTo().frame(element);
        logger.atInfo().log("Switch to frame of [{}]", element);
      } else {
        logger.atError().log("Cannot switch to frame of [{}]", element);
        throw new NoSuchFrameException("Cannot find iFrame Element");
      }
    }

    public <T> T defaultContent(T page) {
      driver.switchTo().defaultContent();
      logger.atInfo().log("Switch to [{}] page", page);
      return page;
    }

    public void defaultContent() {
      driver.switchTo().defaultContent();
      logger.atInfo().log("Switch to default content");
    }

  }

  private Boolean checkHeadless() {
    Boolean result = System.getProperty("headelss") != null && Boolean.parseBoolean(System.getProperty("headless"));

    if (result) {
      logger.atInfo().log("Using headless mode");
    } else {
      logger.atInfo().log("Not using headless mode");
    }

    return result;
  }
}
