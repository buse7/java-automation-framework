package com.framework.action;

import org.slf4j.Logger;
import com.framework.driver.DRIVER_TYPE;
import com.framework.driver.Driver;
import com.framework.driver.WebCommonDriver;
import com.framework.driver.MobileDriver;
import com.framework.listener.Log;

public class CommonAction {
  protected Driver _driver;

  protected Logger logger = Log.getInstance().getLogger();
  
  public CommonAction(Driver driver) {
    this._driver = driver;
  }

  public Object getDriver() {
    if (this._driver.getDriverType() == DRIVER_TYPE.WEB_CHROME_DRIVER) {
      WebCommonDriver driver = (WebCommonDriver) this._driver;
      return driver.get();
    } else if (this._driver.getDriverType() == DRIVER_TYPE.MOBILE_DRIVER_ANDROID) {
      MobileDriver driver = (MobileDriver) this._driver;
      return driver.getAndroidDriver();
    } else if (this._driver.getDriverType() == DRIVER_TYPE.MOBILE_DRIVER_IOS) {
      MobileDriver driver = (MobileDriver) this._driver;
      return driver.getIOSDriver();
    }
    return null;
  }
}