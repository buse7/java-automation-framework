package com.framework.action;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

    /**
   * 최대 대기 시간 (seconds) 만큼 암시적으로 wait를 진행
   * 화면의 모든 Element가 페이지에 노출될 때까지 0.5초마다 Element load를 확인
   * 최대 대기 시간 이전에 모든 Element가 load된 경우 최대 대기 시간에 관계 없이 wait가 종료됨
   *
   * @param seconds 최대 대기 시간
   */
  public void implicitlyWait(int seconds) {
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    logger.atDebug().log("Apply implict wait for {} seconds", seconds);
  }

  /**
   * 특정 페이지로 이동
   *
   * @param url 대상 페이지 url
   */
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

  /**
   * Page 내 대상 Element 존재 여부 확인
   *
   * @param element 탐색할 Element
   * @return Boolean Element 존재 여부
   */  
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

  /**
   * 특정 시간 동안 Page 내 대상 Element 존재 여부 확인
   *
   * @param element     탐색할 Element
   * @param waitSeconds 최대 대기 시간
   * @return Boolean Element 존재 여부
   */  
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

  /**
   * Page 내 대상 Element locator 존재 여부 확인
   *
   * @param locator 탐색할 Element의 locator
   * @return Boolean Element locator 존재 여부
   */  
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

  /**
   * 특정 시간 동안 Page 내 대상 Element locator 존재 여부 확인
   *
   * @param locator     탐색할 Element의 locator
   * @param waitSeconds 최대 대기 시간
   * @return Boolean Element locator 존재 여부
   */  
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

  /**
   * Page 내 대상 Element가 존재하지 않는 지 확인
   *
   * @param element 존재하지 않음을 확인할 Element
   * @return Boolean Element 미존재 여부
   */
  public Boolean isNotElement(WebElement element) {
    try {
      wait.until(ExpectedConditions.invisibilityOf(element));
      logger.atDebug().log("Element does not found, Element Info : {}", element);
      return true;
    } catch (Exception e) {
      logger.atWarn().log("Element still exist, Element Info : {}", element);
      return false;
    }
  }

  /**
   * 특정 시간 동안 Page 내 대상 Element가 존재하지 않는 지 확인
   *
   * @param element     존재하지 않음을 확인할 Element
   * @param waitSeconds 최대 대기 시간
   * @return Boolean Element 미존재 여부
   */
  public Boolean isNotElement(WebElement element, int waitSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
      wait.until(ExpectedConditions.invisibilityOf(element));
      focusOn(element);
      logger.atDebug().log("Element does not found, Element Info : {}", element);
      return true;
    } catch (Exception e) {
      logger.atWarn().log("Element still exist, Element Info : {} for more than {} seconds", element, waitSeconds);
      return false;
    }
  }

  /**
   * Page 내 대상 Element locator가 존재하지 않는 지 확인
   *
   * @param locator 존재하지 않음을 확인할 Element의 locator
   * @return Boolean Element 미존재 여부
   */
  public Boolean isNotElement(By locator) {
    try {
      wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
      logger.atDebug().log("Element does not found, Locator Info : {}", locator);
      return true;
    } catch (Exception e) {
      logger.atWarn().log("Element still exist, Locator Info : {}", locator);
      return false;
    }
  }

  /**
   * 특정 시간 동안 Page 내 대상 Element locator가 존재하지 않는 지 확인
   *
   * @param locator     존재하지 않음을 확인할 Element의 locator
   * @param waitSeconds 최대 대기 시간
   * @return Boolean Element 미존재 여부
   */
  public Boolean isNotElement(By locator, int waitSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
      wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
      logger.atDebug().log("Element does not found, Locator Info : {}", locator);
      return true;
    } catch (Exception e) {
      logger.atWarn().log("Element still exist, Locator Info : {} for more than {} seconds", locator, waitSeconds);
      return false;
    }
  }

  /**
   * Page 내 대상 Element list가 존재하지 않는 지 확인
   *
   * @param elements 존재하지 않음을 확인할 Element list
   * @return Boolean Element list 미존재 여부
   */
  public Boolean isNotElements(List<WebElement> elements) {
    try {
      wait.until(ExpectedConditions.invisibilityOfAllElements(elements));
      logger.atDebug().log("Element list does not found, Element List : {}", elements);
      return true;
    } catch (Exception e) {
      logger.atWarn().log("Element list still found, Element List : {}", elements);
      return false;
    }
  }

  /**
   * 특정 시간 동안 Page 내 대상 Element list가 존재하지 않는 지 확인
   *
   * @param elements    존재하지 않음을 확인할 Element list
   * @param waitSeconds 최대 대기 시간
   * @return Boolean Element list 미존재 여부
   */
  public Boolean isNotElements(List<WebElement> elements, int waitSeconds) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
      wait.until(ExpectedConditions.invisibilityOfAllElements(elements));
      logger.atDebug().log("Element list does not found, Element List : {}", elements);
      return true;
    } catch (Exception e) {
      logger.atWarn().log("Element list still found, Element List : {} for more than {} seconds", elements, waitSeconds);
      return false;
    }
  }

  /**
   * Page 내 대상 Element가 click 가능한 Element인지 확인
   *
   * @param element click 가능한 지 확인할 Element
   * @return Boolean Element click 가능 여부
   */
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

  /**
   * 특정 시간 동안 Page 내 대상 Element가 click 가능한 Element인지 확인
   *
   * @param element     click 가능한 지 확인할 Element
   * @param waitSeconds 최대 대기 시간
   * @return Boolean Element click 가능 여부
   */
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

  /**
   * Page 내 대상 Element locator가 click 가능한 Element인지 확인
   *
   * @param locator click 가능한 지 확인할 Element locator
   * @return Boolean Element locator click 가능 여부
   */
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

  /**
   * 특정 시간 동안 Page 내 대상 Element locator가 click 가능한 Element인지 확인
   *
   * @param locator     click 가능한 지 확인할 Element locator
   * @param waitSeconds 최대 대기 시간
   * @return Boolean Element locator click 가능 여부
   */  
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

  /**
   * Page 내 대상 Element의 Parent가 존재하는 지 확인
   *
   * @param element 탐색할 parent의 child element
   * @return Boolean parent가 존재하는지 여부
   */
  public Boolean isParent(WebElement element) {
    return isElement(getParent(element));
  }

  /**
   * Page 내 대상 Element locator의 Parent가 존재하는 지 확인
   *
   * @param locator 탐색할 parent의 child element의 locator
   * @return Boolean parent가 존재하는지 여부
   */
  public Boolean isParent(By locator) {
    return isElement(getParent(locator));
  }

  /**
   * Page 내 대상 Element의 child가 존재하는 지 확인
   *
   * @param element child가 존재하는 지 확인할 element
   * @return Boolean child가 존재하는지 여부
   */
  public Boolean isChild(WebElement element) {
    return getAllChildren(element) != null && getAllChildren(element).size() > 0;
  }

  /**
   * Page 내 대상 Element locator의 child가 존재하는 지 확인
   *
   * @param locator child가 존재하는 지 확인할 element의 locator
   * @return Boolean child가 존재하는지 여부
   */
  public Boolean isChild(By locator) {
    return getAllChildren(locator) != null && getAllChildren(locator).size() > 0;
  }

  /**
   * 대상 Element의 children를 반환
   *
   * @param locator 대상 Element
   * @return List<WebElement> 대상 Element의 children
   */
  public List<WebElement> getElements(By locator) {
    try {
      return driver.findElements(locator);
    } catch (NoSuchElementException e) {
      logger.atWarn().log("Element does not found, Locator Info : {}", locator);
      return null;
    }
  }

  /**
   * 대상 Element의 Parent Element를 반환
   *
   * @param element 대상 Element
   * @return WebElement 대상 Element의 Parent Element
   */
  public WebElement getParent(WebElement element) {
    return js.getParent(element);
  }

  /**
   * 대상 Element locator의 Parent Element를 반환
   *
   * @param locator 대상 Element의 locator
   * @return WebElement 대상 Element의 Parent Element
   */
  public WebElement getParent(By locator) {
    try {
      return driver.findElement(locator).findElement(By.xpath("./.."));
    } catch (NoSuchElementException e) {
      logger.atWarn().log("Element does not found, Locator Info : {}", locator);
      return null;
    }
  }

  /**
   * 대상 Element locator의 특정 child를 반환
   *
   * @param parentElement 대상 Element
   * @param childLocator  특정 child의 locator
   * @return WebElement 대상 Element의 특정 Child Element
   */
  public WebElement getChild(WebElement parentElement, By childLocator) {
    try {
      return parentElement.findElement(childLocator);
    } catch (NoSuchElementException e) {
      logger.atWarn().log("Child of element does not found, Child Locator : {}, Parent Element Info : {}", childLocator, parentElement);
      return null;
    }
  }

  /**
   * 대상 Element의 첫번째 child를 반환
   *
   * @param element 대상 Element
   * @return WebElement 대상 Element의 첫번쨰 Child Element
   */

  public WebElement getFirstChild(WebElement element) {
    try {
      return element.findElement(By.xpath(".//*[1]"));
    } catch (NoSuchElementException e) {
      logger.atWarn().log("First child of element does not found, Element Info : {}", element);
      return null;
    }
  }

  /**
   * 대상 Element locator의 첫번째 child를 반환
   *
   * @param locator 대상 Element의 locator
   * @return WebElement 대상 Element locator의 첫번쨰 Child Element
   */
  public WebElement getFirstChild(By locator) {
    try {
      return driver.findElement(locator).findElement(By.xpath(".//*[1]"));
    } catch (NoSuchElementException e) {
      logger.atWarn().log("First child of element does not found, Locator Info : {}", locator);
      return null;
    }
  }

  /**
   * 대상 Element의 첫번째 children을 반환
   *
   * @param element 대상 Element
   * @return WebElement 대상 Element의 첫번쨰 Children Element
   */
  public List<WebElement> getFirstChildren(WebElement element) {
    try {
      return element.findElements(By.xpath("./*"));
    } catch (NoSuchElementException e) {
      logger.atWarn().log("First child of element does not found, Element Info : {}", element);
      return null;
    }
  }

  /**
   * 대상 Element locator의 첫번째 children을 반환
   *
   * @param locator 대상 Element의 locator
   * @return WebElement 대상 Element의 첫번쨰 Children Element
   */
  public List<WebElement> getFirstChildren(By locator) {
    try {
      return driver.findElement(locator).findElements(By.xpath("./*"));
    } catch (NoSuchElementException e) {
      logger.atWarn().log("First child of element does not found, Locator Info : {}", locator);
      return null;
    }
  }

  /**
   * 대상 Element의 모든 children을 반환
   *
   * @param element 대상 Element
   * @return WebElement 대상 Element의 모든 Children Element
   */
  public List<WebElement> getAllChildren(WebElement element) {
    try {
      return element.findElements(By.xpath(".//*"));
    } catch (NoSuchElementException e) {
      logger.atWarn().log("Child of element does not found, Element Info : {}", element);
      return null;
    }
  }

  /**
   * 대상 Element locator의 모든 children을 반환
   *
   * @param locator 대상 Element의 locator
   * @return WebElement 대상 Element의 모든 Children Element
   */
  public List<WebElement> getAllChildren(By locator) {
    try {
      return driver.findElement(locator).findElements(By.xpath(".//*"));
    } catch (NoSuchElementException e) {
      logger.atWarn().log("Child of element does not found, Locator Info : {}", locator);
      return null;
    }
  }

  /**
   * 대상 Element 클릭
   * Element 클릭 전 Element가 click 가능한지 확인 후 클릭
   *
   * @param element 클릭할 Element
   */
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

  /**
   * 대상 locator의 Element 클릭
   * Element 클릭 전 Element가 click 가능한지 확인 후 클릭
   *
   * @param locator 클릭할 Element의 locator
   */
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

  /**
   * 특정 시간 동안 Element가 클릭 가능한지 확인 후 대상 Element 클릭
   *
   * @param element     클릭할 Element
   * @param waitSeconds 최대 대기 시간
   */
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

  /**
   * 특정 시간 동안 locator의 Element가 클릭 가능한지 확인 후 대상 Element 클릭
   *
   * @param locator     클릭할 Element의 locator
   * @param waitSeconds 최대 대기 시간
   */  
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

  /**
   * from Element를 to Element 위치로 드래그앤드롭
   *
   * @param from Source(드래그) Element
   * @param to Target(드롭) Element
   */
  public void dragAndDrop(WebElement from, WebElement to) {
    if (isClickableElement(from)) {
      new Actions(driver)
          .moveToElement(from)
          .pause(Duration.ofSeconds(1))
          .clickAndHold(from)
          .pause(Duration.ofSeconds(1))
          .moveByOffset(1, 0)
          .moveToElement(to)
          .moveByOffset(1, 0)
          .pause(Duration.ofSeconds(1))
          .release().perform();
      logger.atInfo().log("drag and drop from {} to {}", from, to);
    } else {
      if (from.isEnabled() && to.isEnabled()) {
        js.dragAndDrop(from, to);
        logger.atInfo().log("JS drag and drop from {} to {}", from, to);
      } else {
        logger.atError().log("Unable to click element, Element Info : {}, {}", from, to);
        throw new NoSuchElementException("Element 탐색 불가");
      }
    }
  }

  /**
   * Element list 클릭
   *
   * @param elementList 클릭할 Element list
   */
  public void clickElements(List<WebElement> elementList) {
    elementList.forEach(this::click);
  }

  /**
   * Start Position에서 End Position으로 Scroll 이동
   *
   * @param startPosition Scroll 시작 지점
   * @param endPosition   Scroll 종료 지점
   */
  public void scroll(int startPosition, int endPosition) {
    js.scroll(startPosition, endPosition);
    logger.atDebug().log("Scroll from {} to {}", startPosition, endPosition);
  }

  /**
   * 아래로 스크롤
   */
  public void scrollDown() {
    js.scroll(0, (Math.round(js.getInnerHeight() * (2 / 3f))));
    logger.atDebug().log("Scroll down");
  }

  /**
   * 맨 위로 스크롤
   */
  public void scrollToTop() {
    js.scrollToTop();
    logger.atDebug().log("Scroll to top");
  }

  /**
   * 맨 아래로 스크롤
   */
  public void scrollToBottom() {
    js.scrollToBottom();
    logger.atDebug().log("Scroll to bottom");
  }

  /**
   * 위로 스크롤
   */
  public void scrollUp() {
    js.scroll(0, -(Math.round(js.getInnerHeight() * (2 / 3f))));
    logger.atDebug().log("Scroll up");
  }

  /**
   * 특정 Element가 노출되도록 스크롤
   *
   * @param element 대상 Element
   */
  public void scrollToElement(WebElement element) {
    int x = element.getRect().x;
    int y = element.getRect().y;

    scroll(x, y);
    focusOn(element);
    logger.atDebug().log("Scroll to element, Element Info : {}", element);
  }

  /**
   * 특정 locator의 Element가 노출되도록 스크롤
   *
   * @param locator 대상 Element의 locator
   */
  public void scrollToElement(By locator) {
    focusOn(driver.findElement(locator));
    logger.atDebug().log("Scroll to element, Locator Info : {}", locator);
  }  

  /**
   * 특정 Element에 특정 값 입력
   *
   * @param element    대상 Element
   * @param keyToSends 입력 값
   */  
  public void type(WebElement element, CharSequence... keyToSends) {
    element.sendKeys(keyToSends);
    logger.atInfo().log("Type [{}] in element, Element Info : {}", keyToSends, element);
  }

  /**
   * 특정 locator의 Element에 특정 값 입력
   *
   * @param locator    대상 Element의 locator
   * @param keyToSends 입력 값
   */  
  public void type(By locator, CharSequence... keyToSends) {
    driver.findElement(locator).sendKeys(keyToSends);
    logger.atInfo().log("Type [{}] in element, Locator Info : {}", keyToSends, locator);
  }

  /**
   * 특정 Element에 특정 값 입력
   *
   * @param element    대상 Element
   * @param isClear    기존 입력 값 clear
   * @param keyToSends 입력 값
   */
  public void type(WebElement element, Boolean isClear, CharSequence... keyToSends) {
    if (isClear) {
      element.clear();
    }
    element.sendKeys(keyToSends);
    logger.atInfo().log("Type [{}] in element, Element Info : {}", keyToSends, element);
  }

  /**
   * 특정 locator의 Element에 특정 값 입력
   *
   * @param locator    대상 Element의 locator
   * @param isClear    기존 입력 값 clear
   * @param keyToSends 입력 값
   */
  public void type(By locator, Boolean isClear, CharSequence... keyToSends) {
    if (isClear) {
      driver.findElement(locator).clear();
    }
    driver.findElement(locator).sendKeys(keyToSends);
    logger.atInfo().log("Type [{}] in element, Locator Info : {}", keyToSends, locator);
  }

  /**
   * 페이지 갱신
   */
  public void refresh() {
    driver.navigate().refresh();
    logger.atInfo().log("Page refresh");
  }

  /**
   * 페이지 뒤로 가기
   */
  public void back() {
    driver.navigate().back();
    logger.atInfo().log("Page back");
  }

  /**
   * 페이지 앞으로 가기
   */
  public void forward() {
    driver.navigate().forward();
    logger.atInfo().log("Page forward");
  }

  /**
   * Page에 특정 Cookie 값 저장
   *
   * @param cookie 저장할 cookie 정보
   */
  public void addCookie(Cookie cookie) {
    driver.manage().addCookie(cookie);
    logger.atDebug().log("Add cookie {}", cookie);
  }

  /**
   * Page에 저장된 특정 Cookie 값을 반환
   *
   * @param key Cookie Key 값
   */
  public Cookie getCookie(String key) {
    return driver.manage().getCookieNamed(key);
  }

  /**
   * Page에 저장된 모든 Cookie 값을 반환
   *
   * @return Set<Cookie> 페이지에 저장된 모든 Cookie 값
   */
  public Set<Cookie> getAllCookies() {
    return driver.manage().getCookies();
  }

  /**
   * 특정 Cookie 값 삭제
   *
   * @param key 삭제할 Cookie의 Key 값
   */
  public void deleteCookie(String key) {
    driver.manage().deleteCookieNamed(key);
    logger.atDebug().log("Delete cookie {}", key);
  }

  /**
   * 특정 Cookie 값 삭제
   *
   * @param cookie 삭제할 Cookie
   */
  public void deleteCookie(Cookie cookie) {
    driver.manage().deleteCookie(cookie);
    logger.atDebug().log("Delete cookie {}", cookie);
  }

  /**
   * 페이지에 저장된 모든 Cookie 값 삭제
   */
  public void deleteAllCookies() {
    driver.manage().deleteAllCookies();
    logger.atDebug().log("Delete all cookie");
  }
  
    /**
   * 페이지 title이 expected title과 일치할 때까지 일정 시간 동안 대기
   *
   * @param title expected title
   */
  public Boolean isTitle(String title) {
    try {
      return wait.until(ExpectedConditions.titleIs(title));
    } catch (TimeoutException e) {
      logger.atWarn().log("Page title does not match [{}]", title);
      return false;
    }
  }

  /**
   * 페이지 title에 특정 문구가 포함될 때까지 일정 시간 동안 대기
   *
   * @param fraction title에 포함되는 특정 문구
   */
  public Boolean isTitleContains(String fraction) {
    try {
      return wait.until(ExpectedConditions.titleContains(fraction));
    } catch (TimeoutException e) {
      logger.atWarn().log("Page title does not contain [{}]", fraction);
      return false;
    }
  }

  /**
   * 현재 page의 title 정보를 반환
   *
   * @return String title
   */
  public String getTitle() {
    return driver.getTitle();
  }

  /**
   * 페이지 url이 expected url과 일치할 때까지 일정 시간 동안 대기
   *
   * @param url expected url
   */
  public Boolean isUrl(String url) {
    try {
      return wait.until(ExpectedConditions.urlToBe(url));
    } catch (TimeoutException e) {
      logger.atWarn().log("Url does not match [{}]", url);
      return false;
    }
  }

  /**
   * 페이지 url에 특정 문구가 포함 될 때까지 일정 시간 동안 대기
   *
   * @param fraction url에 포함될 특정 문구
   */
  public Boolean isUrlContains(String fraction) {
    try {
      return wait.until(ExpectedConditions.urlContains(fraction));
    } catch (TimeoutException e) {
      logger.atWarn().log("Url does not contain [{}]", fraction);
      return false;
    }
  }

  /**
   * 페이지 url이 regex matching 될 때까지 일정 시간 동안 대기
   *
   * @param regex url에 matching 할 regex
   */
  public Boolean isUrlMatches(String regex) {
    try {
      return wait.until(ExpectedConditions.urlMatches(regex));
    } catch (TimeoutException e) {
      logger.atWarn().log("Url does not matches [{}]", regex);
      return false;
    }
  }

  /**
   * 특정 element의 select option 중 text와 동일한 옵션 선택
   * @param element 대상 element
   * @param text    선택할려는 option의 text
   */
  public void selectByVisibleText(WebElement element, String text) {
    Select select = new Select(element);
    select.selectByVisibleText(text);
    logger.atInfo().log("Select option [{}], Element Info : {}", text, element);
  }

  /**
   * 특정 locator의 select option 중 text와 동일한 옵션 선택
   * @param locator 대상 element의 locator
   * @param text    선택할려는 option의 text
   */
  public void selectByVisibleText(By locator, String text) {
    Select select = new Select(driver.findElement(locator));
    select.selectByVisibleText(text);
    logger.atInfo().log("Select option [{}], Locator Info : {}", text, locator);
  }
  
  /**
   * 특정 element의 select option 중 index 번째 선택
   * @param element 대상 element
   * @param index   선택할려는 option의 index
   */
  public void selectByIndex(WebElement element, int index) {
    Select select = new Select(element);
    select.selectByIndex(index);
    logger.atInfo().log("Select option [{}], Element Info : {}", index, element);
  }

  /**
   * 특정 locator의 select option 중 index 번째 선택
   * @param locator 대상 element의 locator
   * @param index   선택할려는 option의 index
   */
  public void selectByIndex(By locator, int index) {
    Select select = new Select(driver.findElement(locator));
    select.selectByIndex(index);
    logger.atInfo().log("Select option [{}], Locator Info : {}", index, locator);
  }
  
  /**
   * 특정 element의 select option중 value 값을 선택
   * @param element 대상 element
   * @param value   선택할려는 option의 value
   */
  public void selectByValue(WebElement element, String value) {
    Select select = new Select(element);
    select.selectByValue(value);
    logger.atInfo().log("Select option [{}], Element Info : {}", value, element);
  }

  /**
   * 특정 locator의 select option중 value 값을 선택
   * @param locator 대상 element의 locator
   * @param value   선택할려는 option의 value
   */
  public void selectByValue(By locator, String value) {
    Select select = new Select(driver.findElement(locator));
    select.selectByValue(value); 
    logger.atInfo().log("Select option [{}], Locator Info : {}", value, locator);
  }

  /**
   * 특정 Element에 path 경로의 있는 파일 업로드
   * @param element 대상 element
   * @param path    파일 경로
   */
  public void uploadFile(WebElement element, CharSequence... path) {
    element.sendKeys(path);
    logger.atInfo().log("UploadFile [{}] element, Path Info : {}", element, path);
  }

  /**
   * 특정 locator에 path 경로의 있는 파일 업로드
   * @param locator 대상 element의 locator
   * @param path    파일 경로
   */
  public void uploadFile(By locator, CharSequence... path) {
    driver.findElement(locator).sendKeys(path);
    logger.atInfo().log("UploadFile [{}] element, Path Info : {}", locator, path);
  }

  /**
  * 현재 페이지의 url을 반환
  */
  public String getCurrentUrl() {
    return driver.getCurrentUrl();
  }

  /**
   * 현재 페이지의 session id를 반환
   */
  public SessionId getSessionId() {
      return ((RemoteWebDriver) driver).getSessionId();
  }

  /**
   * title 탭으로 이동
   */
  public void switchToTab(String title) {
    switchToWindow(title);
    logger.atDebug().log("Switch to [{}] tab", title);
  }

  /**
   * 가장 최근 생성된 탭으로 이동
   */
  public void switchToLatestTab() {
    switchToLatestWindow();
    logger.atDebug().log("Switch to latest tab");
  }

  /**
   * 최초 생성된 탭으로 이동
   */
  public void switchToFirstTab() {
    switchToFirstWindow();
    logger.atDebug().log("Switch to first tab");
  }

  /**
   * title 윈도우로 이동
   */
  public void switchToWindow(String title) {
    List<String> windows = new ArrayList<>(driver.getWindowHandles());
    for (String windowHandle : windows) {
      driver.switchTo().window(windowHandle);
      if (driver.getTitle().equals(title))
        break;
    }

    logger.atDebug().log("Switch to [{}] window", title);
  }

  /**
   * 가장 최근 생성된 윈도우로 이동
   */
  public void switchToLatestWindow() {
    List<String> windowHandles = new ArrayList<>(driver.getWindowHandles());

    String newWindow = windowHandles.size() >= 2 ? windowHandles.get(windowHandles.size() - 2) : windowHandles.get(0);
    driver.switchTo().window(newWindow);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    logger.atDebug().log("Switch to latest window");
  }

  /**
   * 최초 생성된 윈도우로 이동
   */
  public void switchToFirstWindow() {
    String firstWindow = driver.getWindowHandles().stream().reduce((first, second) -> first).orElse(null);
    driver.switchTo().window(firstWindow);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    logger.atDebug().log("Switch to first window");
  }

  /**
   * 새로운 탭 생성
   */
  public void newTab() {
    driver.switchTo().newWindow(WindowType.TAB);
    logger.atDebug().log("Switch to new tab");
  }

  /**
   * 현재 포커스가 있는 탭 종료
   * 이전에 포커스가 있었던 탭으로 자동으로 이동됨
   */
  public void closeTab() {
    closeWindow();
    logger.atDebug().log("Close current tab");
  }


  /**
   * 새로운 윈도우 생성
   */
  public void newWindow() {
    driver.switchTo().newWindow(WindowType.WINDOW);
    logger.atDebug().log("Open new window");
  }

  /**
   * 현재 포커스가 있는 윈도우 종료
   * 이전에 포커스가 있었던 윈도우로 자동으로 이동됨
   */
  public void closeWindow() {
    String currentWindow = driver.getWindowHandle();
    String beforeWindow = driver.getWindowHandles().stream().filter(handle -> !handle.equals(currentWindow)).findFirst().get();
    driver.close();
    driver.switchTo().window(beforeWindow);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    logger.atDebug().log("Close current window");
  }

  /**
   * 현재 포커스가 있는 윈도우 종료
   * originalWindow로 포커스 이동
   *
   * @param targetWindow 포커스를 이동하고자 하는 윈도우
   */
  public void closeWindow(String targetWindow) {
    driver.close();
    driver.switchTo().window(targetWindow);
    logger.atDebug().log("Close current window and go to [{}] window", targetWindow);
  }

  /**
   * 윈도우, 탭 handles를 반환
   */
  public Set<String> getHandles() {
    return driver.getWindowHandles();
  }

  /**
   * 대상 Element의 id 값을 반환함
   *
   * @param element 대상 Element
   */
  public String getId(WebElement element) {
    return isElement(element) ? element.getAttribute("id") != null ? element.getAttribute("id") : null : null;
  }


  /**
   * 대상 Element의 class 값을 반환함
   *
   * @param element 대상 Element
   */
  public String getClassName(WebElement element) {
    return isElement(element) ? element.getAttribute("class") != null ? element.getAttribute("class") : null : null;
  }

  /**
   * 대상 Element의 enable 여부를 반환함
   *
   * @param element 대상 Element
   */
  public Boolean isEnable(WebElement element) {
    return isElement(element) && element.isEnabled();
  }

  /**
   * 대상 Element의 enable 여부를 반환함
   *
   * @param locator 대상 Element의 locator
   */
  public Boolean isEnable(By locator) {
    return isElement(locator) && driver.findElement(locator).isEnabled();
  }

  /**
   * 대상 Element의 selected 여부를 반환함
   *
   * @param element 대상 Element
   */
  public Boolean isSelected(WebElement element) {
    return isElement(element) && element.isSelected();
  }

  /**
   * 대상 Element의 selected 여부를 반환함
   *
   * @param locator 대상 Element의 locator
   */
  public Boolean isSelected(By locator) {
    return isElement(locator) && driver.findElement(locator).isSelected();
  }

  /**
   * 대상 Element의 visible 여부를 반환함
   *
   * @param element 대상 Element
   */
  public Boolean isVisible(WebElement element) {
    return isElement(element);
  }

  /**
   * 대상 Element의 visible 여부를 반환함
   *
   * @param locator 대상 Element의 locator
   */
  public Boolean isVisible(By locator) {
    return isElement(locator);
  }

  /**
   * 대상 Element의 display 여부를 반환함
   *
   * @param element 대상 Element
   */
  public Boolean isDisplayed(WebElement element) {
    return isElement(element) && element.isDisplayed();
  }

  /**
   * 대상 Element의 display 여부를 반환함
   *
   * @param locator 대상 Element의 locator
   */
  public Boolean isDisplayed(By locator) {
    return isElement(locator) && driver.findElement(locator).isDisplayed();
  }

  /**
   * 대상 Element의 특정 attribute를 반환함
   *
   * @param element   대상 Element
   * @param attribute 대상 attribute
   */
  public String getAttribute(WebElement element, String attribute) {
    return isElement(element) ? element.getAttribute(attribute) != null ? element.getAttribute(attribute) : null : null;
  }

  /**
   * 대상 Element의 특정 attribute를 반환함
   *
   * @param locator   대상 Element의 locator
   * @param attribute 대상 attribute
   */
  public String getAttribute(By locator, String attribute) {
    return isElement(locator) ? driver.findElement(locator).getAttribute(attribute) != null ? driver.findElement(locator).getAttribute(attribute) : null : null;
  }

  /**
   * 대상 Element의 value를 반환함
   *
   * @param element 대상 Element
   */
  public String getValue(WebElement element) {
    return isElement(element) ? element.getAttribute("value") : null;
  }

  /**
   * 대상 Element의 value를 반환함
   *
   * @param locator 대상 Element의 locator
   */
  public String getValue(By locator) {
    return isElement(locator) ? driver.findElement(locator).getAttribute("value") : null;
  }

  /**
   * 대상 Element의 text를 반환함
   *
   * @param element 대상 Element
   */
  public String getText(WebElement element) {
    return isElement(element) ? element.getText() : null;
  }

  /**
   * 대상 Element의 text를 반환함
   *
   * @param locator 대상 Element의 locator
   */
  public String getText(By locator) {
    return isElement(locator) ? driver.findElement(locator).getText() : null;
  }

  /**
   * 대상 Element의 hyperLink를 반환함
   *
   * @param element 대상 Element
   */
  public String getHyperLink(WebElement element) {
    return isElement(element) ? element.getAttribute("href") : null;
  }

  /**
   * 대상 Element의 hyperLink를 반환함
   *
   * @param locator 대상 Element의 locator
   */
  public String getHyperLink(By locator) {
    return isElement(locator) ? driver.findElement(locator).getAttribute("href") : null;
  }

  /**
   * 현재 탐색 중인 Element로 이동 및 표시
   *
   * @param element 클릭할 Element list
   */  
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

  public class JavaScriptAction {
    private final JavascriptExecutor je;
    private final WebDriver driver;

    public JavaScriptAction(WebDriver driver) {
      this.driver = driver;
      je = (JavascriptExecutor) driver;
    }

    /**
     * 특정 js script 명령어 처리
     *
     * @param script js 명령어
     */
    public Object execute(String script) {
      return je.executeScript(script);
    }

    /**
     * 특정 js script 명령어 처리
     *
     * @param script js 명령어
     * @param args   js 명령어 처리 대상
     */
    public Object execute(String script, Object... args) {
      return je.executeScript(script, args);
    }

    /**
     * 특정 Element에 highlight 설정
     * Element highlight를 수행자에게 보여주기 위해 0.2초간 sleep이 적용됨
     *
     * @param element 특정 Element
     */
    public void setHighlight(WebElement element) {
      je.executeScript("arguments[0].style.border = 'solid 4px red'", element);

      try {
        Thread.sleep(200L);
      } catch (InterruptedException e) {
        // TODO: handle exception
        Assert.fail(e.getMessage());
      }
    }

    /**
     * 특정 Element에 설정된 highlight 제거
     *
     * @param element 특정 Element
     */
    public void removeHighlight(WebElement element) {
      je.executeScript("arguments[0].style.border = ''", element);
    }

    /**
     * js로 특정 Element 클릭
     *
     * @param element 특정 Element
     */
    public void click(WebElement element) {
      je.executeScript("arguments[0].click()", element);
    }

    /**
     * js로 특정 Element 클릭
     *
     * @param locator 특정 Element의 locator
     */
    public void click(By locator) {
      je.executeScript("arguments[0].click()", driver.findElement(locator));
    }

    /**
     * js로 Alert 생성
     *
     * @param text Alert Text
     */
    public void makeAlert(String text) {
      je.executeScript("alert('" + text + "')");
    }

    /**
    * js로 Confirm 생성
    *
    * @param text Confirm Text
    */
    public void makeConfirm(String text) {
      je.executeScript("confirm('" + text + "')");
    }

    /**
     * js로 현재 페이지의 domain을 반환
     */
    public String getCurrentDomatin() {
      return je.executeScript("return document.domain") + "";
    }

    /**
    * js로 현재 페이지의 url을 반환
    */
    public String getCurrentUrl() {
      return je.executeScript("return document.URL") + "";
    }

    /**
     * js로 현재 페이지의 title을 반환
     */
    public String getTitle() {
      return je.executeScript("return document.title") + "";
    }

    /**
     * js로 대상 url로 페이지 이동
     *
     * @param url 대상 url
     */
    public void goTo(String url) {
      if (!url.startsWith("http")) {
        url = "https://" + url;
      }
      je.executeScript("window.location = '" + url + "'");
      logger.atInfo().log("go to {}", url);
    }

    /**
     * js로 대상 url로 페이지 이동
     *
     * @param url 대상 url
     */
    public void get(String url) {
      goTo(url);
    }

    /**
     * js로 페이지 스크롤
     *
     * @param x start position
     * @param y end position
     */
    public void scroll(int x, int y) {
      je.executeScript("window.scrollTo(" + x + ", " + y + ")");
    }

    /**
     * js로 페이지 맨 위로 스크롤
     */
    public void scrollToTop() {
      je.executeScript("window.scrollTo(0,0)");
    }

    /**
     * js로 페이지 맨 아래로 스크롤
     */
    public void scrollToBottom() {
      je.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    /**
     * js로 InnerHeight 값 반환
     */
    public int getInnerHeight() {
      return Integer.parseInt(je.executeScript("return window.innerHeight") + "");
    }

    /**
     * js로 페이지 갱신
     */
    public void refresh() {
      je.executeScript("location.reload()");
    }

    /**
     * js로 Element에 특정 값 입력
     *
     * @param element 특정 Element
     * @param value   특정 값
     */
    public void setValue(WebElement element, String value) {
      je.executeScript("arguments[0].value = " + value + "", element);
    }

    /**
     * js로 특정 url의 새 윈도우 생성
     *
     * @param url 특정 url
     */
    public void newWindows(String url) {
      je.executeScript("window.open(" + url + ")");
    }

    /**
     * js로 새 탭 생성
     */
    public void newTab() {
      je.executeScript("window.open('about:black', '_black');");
    }

    /**
     * 특정 Session 제거
     *
     * @param key 특정 Session의 key
     */
    public void removeSession(String key) {
      je.executeScript("window.sessionStorage.removeItem(" + key + ")");
    }

    /**
     * 현재 페이지에 특정 Session이 존재하는지 확인
     *
     * @param key 특정 Session의 key
     */
    public Boolean isSession(String key) {
      return !(je.executeScript("return window.sessionStorage.getItem(" + key + ")") == null);
    }

    /**
     * 특정 Session의 정보를 반환
     *
     * @param key 특정 Session의 key
     */
    public String getSession(String key) {
      return (String) je.executeScript("return window.sessionStorage.getItem(" + key + ")");
    }

    /**
     * 현재 페이지에 특정 Session을 설정함
     *
     * @param key   특정 Session의 key
     * @param value 특정 Session의 value
     */
    public void setSession(String key, Object value) {
      je.executeScript(String.format("window.sessionStorage.setItem('%s', '%s');", key, value));
    }

    /**
     * 현재 페이지에 특정 Session을 설정함
     *
     * @param key   특정 Session의 key
     * @param value 특정 Session의 value
     */
    public void clearAllSession() {
      je.executeScript("window.sessionStorage.clear()");
      logger.atInfo().log("Clear all Sessions");
    }

    /**
     * 대상 Element의 Parent Element 정보를 반환
     *
     * @param element 대상 Element
     */
    public WebElement getParent(WebElement element) {
      try {
        return (WebElement) je.executeScript("return arguments[0].parentNode", element);
      } catch (ClassCastException e) {
        // TODO: handle exception
        logger.atWarn().log("Cannot find element {}'s parent", element);
        return null;
      }
    }

    /**
     * 현재 화면의 Position X 값을 반환
     */
    public int getPositionX() {
      String positionX = je.executeScript("return window.scrollX") + "";
      if (positionX.contains(".")) {
        positionX = positionX.split("\\.")[0];
      }
      return Integer.parseInt(positionX);
    }

    /**
     * 현재 화면의 Position Y 값을 반환
     */
    public int getPositionY() {
      String positionY = je.executeScript("return window.scrollY") + "";
      if (positionY.contains(".")) {
        positionY = positionY.split("\\.")[0];
      }
      return Integer.parseInt(positionY);
    }
    /**
     * from Element를 to Element 위치로 드래그앤드롭
     *
     * @param from Source(드래그) Element
     * @param to Target(드롭) Element
     */
    public void dragAndDrop(WebElement from, WebElement to) {
      je.executeScript("function createEvent(typeOfEvent) {\n" + "var event =document.createEvent(\"CustomEvent\");\n"
          + "event.initCustomEvent(typeOfEvent,true, true, null);\n" + "event.dataTransfer = {\n" + "data: {},\n"
          + "setData: function (key, value) {\n" + "this.data[key] = value;\n" + "},\n"
          + "getData: function (key) {\n" + "return this.data[key];\n" + "}\n" + "};\n" + "return event;\n"
          + "}\n" + "\n" + "function dispatchEvent(element, event,transferData) {\n"
          + "if (transferData !== undefined) {\n" + "event.dataTransfer = transferData;\n" + "}\n"
          + "if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n"
          + "} else if (element.fireEvent) {\n" + "element.fireEvent(\"on\" + event.type, event);\n" + "}\n"
          + "}\n" + "\n" + "function simulateHTML5DragAndDrop(element, destination) {\n"
          + "var dragStartEvent =createEvent('dragstart');\n" + "dispatchEvent(element, dragStartEvent);\n"
          + "var dropEvent = createEvent('drop');\n"
          + "dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n"
          + "var dragEndEvent = createEvent('dragend');\n"
          + "dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" + "}\n" + "\n"
          + "var source = arguments[0];\n" + "var destination = arguments[1];\n"
          + "simulateHTML5DragAndDrop(source,destination);", from, to);
      logger.atInfo().log("JS drag and drop from {} to {}", from, to);
    }
  }

  public class Alert {
    private final WebDriver driver;

    public Alert(WebDriver driver) {
      this.driver = driver;
    }

    /**
     * 현재 페이지에 Alert이 존재할 때까지 일정 시간 동안 대기
     */
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

    /**
     * Alert의 accept 버튼 클릭
     */
    public void accept() {
      if (!isAlert()) {
        logger.atError().log("Alert does found");
        throw new NoAlertPresentException("Cannot find alert");
      }

      driver.switchTo().alert().accept();
      logger.atDebug().log("Click the accept button on the alert");
    }

    /**
     * Alert의 Text를 반환
     */
    public String getText() {
      if (!isAlert()) {
        logger.atError().log("Alert does found");
        throw new NoAlertPresentException("Cannot find alert");
      }

      return driver.switchTo().alert().getText();
    }

    /**
     * Alert에 특정 Text를 입력
     *
     * @param keyToSends 입력할 특정 Text
     */
    public Alert type(String keyToSends) {
      if (!isAlert()) {
        logger.atError().log("Alert does found");
        throw new NoAlertPresentException("Cannot find alert");
      }

      driver.switchTo().alert().sendKeys(keyToSends);
      logger.atDebug().log("Type text {} in the alert", keyToSends);
      return this;
    }
  }

  public class Confirm extends Alert {
    public Confirm(WebDriver driver) {
      super(driver);
    }

    /**
     * Confirm의 dismiss 버튼 클릭
     */
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

    /**
     * 특정 iframe으로 이동
     *
     * @param frameId 대상 iframe의 id
     */
    public void switchTo(String frameId) {
      if (isElement(By.xpath("//iframe[@id='" + frameId + "'']"))) {
        driver.switchTo().frame(frameId);
        logger.atInfo().log("Swtich to frame of [{}]", frameId);
      } else {
        logger.atError().log("Cannot switch to frame of [{}]", frameId);
        throw new NoSuchElementException("Cannot find iFrame Element");
      }
    }

    /**
     * 특정 iframe으로 이동
     *
     * @param name 대상 iframe의 name
     */
    public void switchToFrameByName(String name) {
      if (isElement(By.xpath("//iframe[@name='" + name + "'']"))) {
        driver.switchTo().frame(name);
        logger.atInfo().log("Swtich to frame of [{}]", name);
      } else {
        logger.atError().log("Cannot switch to frame of [{}]", name);
        throw new NoSuchElementException("Cannot find iFrame Element");
      }
    }

    /**
     * 특정 iframe으로 이동
     *
     * @param frameIndex 대상 iframe의 index
     */
    public void switchTo(int frameIndex) {
      driver.switchTo().frame(frameIndex);
    }

    /**
     * 특정 iframe으로 이동
     *
     * @param element 대상 iframe element
     */
    public void switchTo(WebElement element) {
      if (isElement(element)) {
        driver.switchTo().frame(element);
        logger.atInfo().log("Switch to frame of [{}]", element);
      } else {
        logger.atError().log("Cannot switch to frame of [{}]", element);
        throw new NoSuchFrameException("Cannot find iFrame Element");
      }
    }

    /**
     * 특정 page의 default Content Page로 이동
     *
     * @param page 이동 대상 page 정보
     */
    public <T> T defaultContent(T page) {
      driver.switchTo().defaultContent();
      logger.atInfo().log("Switch to [{}] page", page);
      return page;
    }

    /**
     * 특정 page의 default Content Page로 이동
     */
    public void defaultContent() {
      driver.switchTo().defaultContent();
      logger.atInfo().log("Switch to default content");
    }

  }

  /**
   * Headless를 사용 중인지 확인
   */
  private Boolean checkHeadless() {
    Boolean result = this._driver.getHeadless() || (System.getProperty("headelss") != null && Boolean.parseBoolean(System.getProperty("headless")));

    if (result) {
      logger.atInfo().log("Using headless mode");
    } else {
      logger.atInfo().log("Not using headless mode");
    }

    return result;
  }
}
