package ru.redrise.users_management;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class RegistrationAllowTest extends ScreenshotsMaker {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void beforeClass(){
        driver = Suite.driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @BeforeMethod
    public void beforeMethod(){
        driver.get("http://localhost:8080/");
    }

    @AfterMethod
    public void logout(){
        try{
            driver.findElement(By.xpath("//a[@id='logout']")).click();
        }
        catch (Exception ignore){}
    }


    @Parameters({"admin_username", "admin_password"})
    @Test(testName = "Check registration NOT allowed",
        priority = 1,
        groups = "registration_configuration")
    public void checkRegNotAllowed(String user, String password){
        latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        loginAsAdmin(user, password);
        openSettings();
        setRegistrationAllowed(false);

        Assert.assertTrue(getRegistrationLink().getAttribute("href").endsWith("/true"));

        // logout; alternate element picking approach
        driver
                .findElement(By.xpath("//nav[@id='header_right_block']/div[@class='block_inner']/ul/li[4]/a[1]"))
                .click();

        WebElement registerLink = driver.findElement(By.xpath("//div[@class='container base']/a[1]"));

        wait.until(f -> registerLink.isDisplayed());

        registerLink.click();

        WebElement text = driver.findElement(By.tagName("h1"));
        wait.until(f -> text.isDisplayed());

        Assert.assertTrue(text.getText().contains("Currently we're closed to new registrations"));
    }

    @Parameters({"admin_username", "admin_password"})
    @Test(testName = "Check registration allowed",
        priority = 2,
        groups = "registration_configuration")
    public void checkRegAllowed(String user, String password){
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        loginAsAdmin(user, password);
        openSettings();
        setRegistrationAllowed(true);

        Assert.assertTrue(getRegistrationLink().getAttribute("href").endsWith("/false"));
        // deep validation would be done within other tests
    }

    private void loginAsAdmin(String user, String password){
        WebElement userNameInput = driver.findElement(By.xpath("//input[@id='username']"));

        wait.until(f -> userNameInput.isDisplayed());

        userNameInput.sendKeys(user);

        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }
    private void openSettings(){
        WebElement settingsLink = driver.findElement(
                By.xpath("//nav[@id='header_right_block']/div[@class='block_inner']/ul/li/a[@href='/settings']"));

        wait.until(f -> settingsLink.isDisplayed());

        settingsLink.click();
    }
    private WebElement getRegistrationLink(){
        return driver.findElement(By.xpath("//a[@id='registration']"));
    }
    private void setRegistrationAllowed(boolean isAllowed){
        WebElement registrationLink = getRegistrationLink();
        if (registrationLink.getAttribute("href").endsWith(isAllowed ? "/true" : "/false")) {
            registrationLink.click();

            wait.until(f -> getRegistrationLink().isDisplayed());
        }
    }
}
