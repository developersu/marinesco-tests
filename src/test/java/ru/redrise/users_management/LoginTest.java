package ru.redrise.users_management;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest extends ScreenshotsMaker{
    private WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        driver = Suite.driver;
    }

    @BeforeMethod
    public void beforeMethod(){
        driver.get("http://localhost:8080/");
    }

    @Test(testName = "Login page availability",
        groups = "Login tests")
    public void checkAdminLogin() {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        WebElement header = driver.findElement(By.tagName("header"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(f -> header.isDisplayed());

        WebElement signInLink = driver.findElement(
                By.xpath("//nav[@id='header_right_block']/div[@class='block_inner']/ul/li/a[1]"));

        Assert.assertTrue(signInLink.getAttribute("href").endsWith("/login"));

        WebElement userNameInput = driver.findElement(By.xpath("//input[@id='username']"));

        Assert.assertEquals(userNameInput.getAttribute("type"), "text");

        WebElement psswdInput = driver.findElement(By.xpath("//input[@id='password']"));

        Assert.assertEquals(psswdInput.getAttribute("type"), "password");

        try{
            driver.findElement(By.xpath("//a[@id='logout']")).click();
        }
        catch (Exception ignore){}
    }

    @Test(priority = 1,
            testName = "Login Failure Test",
            groups = "Login tests")
    public void loginFailureCheck() {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        WebElement userNameInput = driver.findElement(By.xpath("//input[@id='username']"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(f -> userNameInput.isDisplayed());

        userNameInput.sendKeys("incorrect-values-set");

        WebElement psswdInput = driver.findElement(By.xpath("//input[@id='password']"));

        psswdInput.sendKeys("incorrect-values-set");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebElement settingsLink = driver.findElement(
                By.xpath("//span[@class='validationError']"));

        wait.until(f -> settingsLink.isDisplayed());

        try{
            driver.findElement(By.xpath("//a[@id='logout']")).click();
        }
        catch (Exception ignore){}
    }


    @Parameters({"admin_username", "admin_password"})
    @Test(testName = "Default admin login",
            groups = "Login tests")
    public void loginAsAdminCheck(String user, String password) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        WebElement userNameInput = driver.findElement(By.xpath("//input[@id='username']"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(f -> userNameInput.isDisplayed());

        userNameInput.sendKeys(user);

        WebElement psswdInput = driver.findElement(By.xpath("//input[@id='password']"));

        psswdInput.sendKeys(password);

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebElement settingsLink = driver.findElement(
                By.xpath("//nav[@id='header_right_block']/div[@class='block_inner']/ul/li/a[@href='/settings']"));

        wait.until(f -> settingsLink.isDisplayed());

        try{
            driver.findElement(By.xpath("//a[@id='logout']")).click();
        }
        catch (Exception ignore){}
    }
}
