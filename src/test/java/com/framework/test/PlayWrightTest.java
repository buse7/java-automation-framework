package com.framework.test;

import ch.qos.logback.classic.Level;
import com.framework.action.Action;
import com.framework.driver.DRIVER_TYPE;
import com.framework.driver.Driver;
import com.framework.driver.PlayWrightDriver;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PlayWrightTest {
    private PlayWrightDriver driver = null;

    @BeforeClass
    public void setUpAll() {
        this.driver = new PlayWrightDriver.Builder(DRIVER_TYPE.WEB_CHROME_DRIVER)
                .driverName("driver")
                .isRecordVideo(true)
                .maxWindowSize(true)
                .build();
    }

    @Test
    public void tc1() {
        try (Page page = this.driver.getBrowser().newPage()) {
            page.navigate("https://www.naver.com");
        }
        Assert.fail();
        System.out.println(this.driver.getDriverType());
    }

    @AfterClass
    public void tearDownAll() {this.driver.quit();}

}
