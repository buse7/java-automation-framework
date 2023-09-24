package com.framework.page.mobile;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebElement;

import com.framework.action.mobile.AndroidAction;
import com.framework.driver.Driver;
import com.framework.driver.MobileDriver;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class AndroidTestPage {
  private MobileDriver driver;
  private AndroidAction action;

  @FindBy(id = "net.bucketplace:id/logoContainer")
  WebElement titleImg;

  @FindBy(id = "net.bucketplace:id/emailLogInText")
  WebElement emailLoginBtn;

  @FindBy(id = "net.bucketplace:id/title")
  WebElement emailLoginPageTitle;

  @FindBy(xpath = "//android.widget.FrameLayout[@resource-id='net.bucketplace:id/emailField']//android.widget.AutoCompleteTextView")
  WebElement emailInput;

  @FindBy(xpath = "//android.widget.FrameLayout[@resource-id='net.bucketplace:id/passwordField']//android.widget.AutoCompleteTextView")
  WebElement passwordInput;

  @FindBy(id = "net.bucketplace:id/loginButton")
  WebElement loginBtn;

  @FindBy(id = "net.bucketplace:id/main_container")
  WebElement mainPage;

  @FindBy(id = "net.bucketplace:id/dialogLayout")
  WebElement dialogModal;

  @FindBy(xpath = "//android.widget.TextView[@text='나중에 받기']")
  WebElement postPoneBtn;

  public AndroidTestPage(Driver target_driver) {
    this.driver = (MobileDriver) target_driver;
    this.action = new AndroidAction(target_driver);

    PageFactory.initElements(new AppiumFieldDecorator(this.action.getDriver()), this);
  }

  public Boolean isPresentTitleImg() {
    return this.action.isElement(titleImg);
  }

  public AndroidTestPage clickEmailLogin() {
    this.action.waitClick(emailLoginBtn, 5);
    return this;
  }

  public String getEmailLoginBtnText() {
    return this.action.getText(emailLoginBtn);
  }

  public Boolean isPresentEmailLoginPageTitle() {
    return this.action.getText(emailLoginPageTitle).contains("이메일 로그인");
  }

  public AndroidTestPage inputEmail(String email) {
    this.action.type(emailInput, true, email);
    return this;
  }

  public AndroidTestPage inputPassword(String password) {
    this.action.type(passwordInput, true, password);
    return this;
  }

  public AndroidTestPage clickLoginBtn() {
    if (this.action.isElement(loginBtn, 5)) {
      this.action.waitClick(loginBtn, 5);
      return this;
    }
    return null;
  }

  public Boolean isPresentMainPage() {
    return this.action.isElement(mainPage, 5);
  }

  public Boolean isPresentDialog() {
    return this.action.isDisplayed(dialogModal);
  }

  public AndroidTestPage clickPostPoneBtn() {
    if (this.action.isElement(postPoneBtn, 5)) {
      this.action.waitClick(postPoneBtn, 5);
      return this;
    }
    return null;
  }
}
