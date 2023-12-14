package com.framework.driver;

import java.nio.file.Paths;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import lombok.Getter;

public class PlayWrightDriver extends Driver {
  private Playwright driver;
  private Browser browser;
  private BrowserContext context;

  public static class Builder extends Driver.Builder<Builder> {
    public Builder(DRIVER_TYPE driverType) {
      super(driverType);
    }

    @Override
    public PlayWrightDriver build() {
      return new PlayWrightDriver(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }

  private PlayWrightDriver(Builder builder) {
    super(builder);
    
    this.driver = Playwright.create();
    if (builder.driverType == DRIVER_TYPE.WEB_CHROME_DRIVER) {
      this.browser = driver.chromium().launch(new LaunchOptions().setHeadless(headless).setChannel("chrome"));
    } else if (builder.driverType == DRIVER_TYPE.WEB_FIREFOX_DRIVER) {
      this.browser = driver.firefox().launch(new LaunchOptions().setHeadless(headless).setChannel("firefox"));
    }

    NewContextOptions contextOptions = new NewContextOptions();

    if (!maxWindowSize) {
      contextOptions = contextOptions.setViewportSize(width, height);
    }

    if (isRecordVideo) {
      contextOptions = contextOptions.setRecordVideoDir(Paths.get("video/")).setRecordVideoSize(648, 480);
    }
    this.context = this.browser.newContext(contextOptions);
  }

  public void quit() {
    this.driver.close();
  }
  public Browser getBrowser() {return this.browser;}
  public Playwright get() {return this.driver;}

}
