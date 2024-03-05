package ru.redrise.users_management;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class Suite{
    static ChromeDriver driver;

    @BeforeSuite
    public void beforeSuite() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1024, 768));
    }

    @AfterSuite
    public void afterSuite() {
        driver.close();
    }
}
