package com.framework.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.framework.action.Action;
import com.framework.driver.Driver;

public class GoogleMainPage {
  
  @FindBy(name = "q")
  private WebElement searchInputBox;
  private Driver driver = null;
  private Action action = null;

  public final String url = "https://www.google.com";

  public GoogleMainPage(Driver target_driver) {
    this.driver = target_driver;
    this.action = new Action(target_driver);


    PageFactory.initElements(this.action.getDriver(), this);
  }

  public GoogleMainPage Navigate() {
    this.action.getDriver().get(url);
    return this;
  }
  
}
