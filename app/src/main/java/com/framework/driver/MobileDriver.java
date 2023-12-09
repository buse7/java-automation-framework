package com.framework.driver;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class MobileDriver extends Driver {
  private AndroidDriver androidDriver;
  private IOSDriver iosDriver;
  private DesiredCapabilities capabilities;
  private AppiumDriverLocalService service;

  public static class Builder extends Driver.Builder<Builder> {
    private UiAutomator2Options option;

    public Builder(DRIVER_TYPE driverType, UiAutomator2Options option) {
      super(driverType);
      this.option = option;
    }

    @Override
    public MobileDriver build() throws Exception {
      return new MobileDriver(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }

  private MobileDriver(Builder builder) throws Exception {
    super(builder);

    if (System.getProperty("appium_server") == null || System.getProperty("appium_server") == "") {
      service = AppiumDriverLocalService.buildService(
        new AppiumServiceBuilder()
        .usingPort(4723)
        .withArgument(GeneralServerFlag.BASEPATH, "wd/hub")
      );
      service.start();

      if (this.driverType == DRIVER_TYPE.MOBILE_DRIVER_ANDROID) {
        this.androidDriver = new AndroidDriver(service.getUrl(), builder.option);
      } else {
        this.iosDriver = new IOSDriver(service.getUrl(), builder.option);
      }
    
    } else {
      String remoteUrl = System.getProperty("appium_server");
      this.service = null;

      if (this.driverType == DRIVER_TYPE.MOBILE_DRIVER_ANDROID) {
        this.androidDriver = new AndroidDriver(new URL(remoteUrl + "/wd/hub"), builder.option);
      } else {
        this.iosDriver = new IOSDriver(new URL(remoteUrl + "/wd/hub"), builder.option);
      }
    }

    if (this.driverType == DRIVER_TYPE.MOBILE_DRIVER_ANDROID) {
      this.androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    } else {
      this.iosDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
  }

  public AndroidDriver getAndroidDriver() {
    return this.androidDriver;
  }

  public IOSDriver getIOSDriver() {
    return this.iosDriver;
  }

  public void quit() {
    if (this.driverType == DRIVER_TYPE.MOBILE_DRIVER_ANDROID) {
      this.androidDriver.quit();
      logger.info("android driver quit...");
    } else {
      this.iosDriver.quit();
      logger.info("ios driver quit...");
    }
    

    if (this.service != null) {
      this.service.stop();
      logger.info("appium service stoped....");
    }
  }
}
