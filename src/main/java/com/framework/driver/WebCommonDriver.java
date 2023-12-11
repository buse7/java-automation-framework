package com.framework.driver;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebCommonDriver extends Driver {
  private final WebDriver driver;

  public static class Builder extends Driver.Builder<Builder> {
    public Builder(DRIVER_TYPE driverType) {
      super(driverType);
    }

    @Override
    public WebCommonDriver build() {
      return new WebCommonDriver(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }

  private WebCommonDriver(Builder builder) {
    super(builder);

    if (this.driverVersion != null) {
      WebDriverManager.chromedriver().driverVersion(driverVersion).setup();
    } else {
      WebDriverManager.chromedriver().setup();
    }

    ChromeOptions options = new ChromeOptions();
    options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

    options.addArguments("--remote-allow-origins=*");

    if (this.driverVersion != null) {
      options.setBrowserVersion(driverVersion);
    }

    if (windowPosition != null) {
      options.addArguments(String.format("--window-position=%s", windowPosition));
    } else {
      options.addArguments("--window-position=0,0");
    }

    if (headless || (System.getProperty("headless") != null && Boolean.parseBoolean(System.getProperty("headless")))) {
      options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080", "--ignore-certificate-errors",
          "--no-sandbox", "--disable-dev-shm-usage");
      options.addArguments("--lang=ko",
          "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.5615.49 Safari/537.36");
    }

    if (this.cameraPermissionByPass) {
      options.addArguments("allow-file-access-from-files");
      options.addArguments("use-fake-device-for-media-stream");
      options.addArguments("use-fake-ui-for-media-stream");
      options.addArguments("--disalbe-features=EnableEphemeralFlashPermission");
    }

    if (maxWindowSize) {
      this.driver = new ChromeDriver(options);
      this.driver.manage().window().maximize();
      return;
    } else {
      options.addArguments(String.format("--window-size=%d,%d", this.width, this.height));
    }

    this.driver = new ChromeDriver(options);
  }

  public WebDriver get() {
    return this.driver;
  }

  public void quit() {
    this.driver.quit();
  }
}