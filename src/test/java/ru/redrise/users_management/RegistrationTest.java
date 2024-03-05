package ru.redrise.users_management;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import ru.redrise.utils.TwoColumnsExcelReader;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class RegistrationTest extends ScreenshotsMaker{
    //private final Logger log = LoggerFactory.getLogger(getClass());

    private String registeredUserLogin;

    private WebDriver driver;
    private WebDriverWait wait;

    private final HashMap<String, String> allMyHandyErrors = new HashMap<>();

    @BeforeClass
    public void beforeClass() throws Exception{
        driver = Suite.driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        Arrays.stream(TwoColumnsExcelReader.getFromFile(true, "error_messages.xlsx", "Лист1"))
                .forEach(e -> allMyHandyErrors.put(
                        ((String) e[0]).trim(), ((String) e[1]).trim())
                );
    }

    @BeforeMethod
    public void beforeMethod(){
        driver.get("http://localhost:8080/");
        logout();
    }

    @Parameters({"test_user_login", "test_user_password", "test_user_displayedname"})
    @Test(priority = 3,
            testName = "Registration - positive",
            dependsOnGroups = "registration_configuration")
    public void regularRegistration(String user, String password, String displayedName) throws Exception{
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        Random random = new Random();

        for (int i = 0; registerAttempt(user+random.nextInt(9999), password, displayedName); i++){
            if (i >= 10)
                throw new Exception("Unable to register new user");
        }
    }
    private boolean registerAttempt(String user, String password, String displayedName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        pickUserNameField().sendKeys(user);
        pickPasswordField().sendKeys(password);
        pickPasswordConfirmField().sendKeys(password);
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        try{
            WebElement alreadyExistsError = driver.findElement(By.xpath("//span[@class='validationError']"));
            wait.until(f -> alreadyExistsError.isDisplayed());

            return true;
        }
        catch (Exception ignore){ }

        registeredUserLogin = user;

        return false;
    }

    @Parameters({"test_user_password", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - already registered",
            dependsOnGroups = "registration_configuration")
    public void duplicateRegistration(String password, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        WebElement registerLink = driver.findElement(By.xpath("//div[@class='container base']/a[1]"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(f -> registerLink.isDisplayed());

        registerLink.click();

        Assert.assertTrue(registerAttempt(registeredUserLogin, password, displayedName));

        WebElement error = driver.findElement(By.xpath("//span[@id='duplicate_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("duplicate_error"));
    }

    @Parameters({"test_user_password", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - failure - no name",
            dependsOnGroups = "registration_configuration")
    public void registrationFailureNoName1(String password, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickPasswordField().sendKeys(password);
        pickPasswordConfirmField().sendKeys(password);
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='username_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertTrue(registerAttempt(registeredUserLogin, password, displayedName));
    }

    @Parameters({"test_user_password", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - failure - wrong name - short",
            dependsOnGroups = "registration_configuration")
    public void registrationFailureShortName(String password, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickUserNameField().sendKeys(getRandomLettersName(2));
        pickPasswordField().sendKeys(password);
        pickPasswordConfirmField().sendKeys(password);
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='username_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("username_error"));
    }

    @Parameters({"test_user_password", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - failure - wrong name - huge",
            dependsOnGroups = "registration_configuration")
    public void registrationFailureBigName(String password, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickUserNameField().sendKeys(getRandomLettersName(33));
        pickPasswordField().sendKeys(password);
        pickPasswordConfirmField().sendKeys(password);
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='username_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("username_error"));
    }

    @Test(priority = 4,
            testName = "Registration - failure - Correct name only entered",
            expectedExceptions = { NoSuchElementException.class },
            dependsOnGroups = "registration_configuration")
    public void registrationFailuresCorrectNameOnly() throws Exception{
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickUserNameField().sendKeys(getRandomLettersName(10));
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='username_error']"));
        wait.until(f -> error.isDisplayed());
    }

    // PASSWORD TESTING

    @Parameters({"not_being_registered_login", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - failure - no passwords",
            dependsOnGroups = "registration_configuration")
    public void registrationFailureNoPassword1(String name, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickUserNameField().sendKeys(name);
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='password_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("password_error"));
    }

    @Parameters({"not_being_registered_login", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - failure - wrong passord - short",
            dependsOnGroups = "registration_configuration")
    public void registrationFailureShortPassword(String name, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        String shortPassword = getRandomLettersName(7);

        pickUserNameField().sendKeys(name);
        pickPasswordField().sendKeys(shortPassword);
        pickPasswordConfirmField().sendKeys(shortPassword);
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='password_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("password_error"));
    }

    @Parameters({"not_being_registered_login", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - failure - wrong passord - huge",
            dependsOnGroups = "registration_configuration")
    public void registrationFailureBigPassword(String name, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        String shortPassword = getRandomLettersName(33);

        pickUserNameField().sendKeys(name);
        pickPasswordField().sendKeys(shortPassword);
        pickPasswordConfirmField().sendKeys(shortPassword);
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='password_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("password_error"));
    }

    @Parameters({"test_user_password"})
    @Test(priority = 4,
            testName = "Registration - failure - Correct password only entered",
            expectedExceptions = { NoSuchElementException.class },
            dependsOnGroups = "registration_configuration")
    public void registrationFailuresCorrectPasswordOnly(String password) throws Exception{
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickPasswordField().sendKeys(password);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='password_error']"));
        wait.until(f -> error.isDisplayed());
    }

    @Parameters({"test_user_password"})
    @Test(priority = 4,
            testName = "Registration - failure - Correct password (both) only entered",
            expectedExceptions = { NoSuchElementException.class },
            dependsOnGroups = "registration_configuration")
    public void registrationFailuresCorrectPasswordsOnly(String password) throws Exception{
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickPasswordField().sendKeys(password);
        pickPasswordConfirmField().sendKeys(password);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='password_error']"));
        wait.until(f -> error.isDisplayed());
    }

    // Repeat password field tests

    @Parameters({"not_being_registered_login", "test_user_password", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - failure - no duplicate password",
            dependsOnGroups = "registration_configuration")
    public void registrationFailureNoDuplicatePassword(String name, String password, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickUserNameField().sendKeys(name);
        pickPasswordField().sendKeys(password);
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='password_dup_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("password_dup_error"));
    }

    @Parameters({"not_being_registered_login", "test_user_password", "test_user_displayedname"})
    @Test(priority = 4,
            testName = "Registration - failure - password no match",
            dependsOnGroups = "registration_configuration")
    public void registrationWrongDuplicPassword(String name, String password, String displayedName) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickUserNameField().sendKeys(name);
        pickPasswordField().sendKeys(password);
        pickPasswordConfirmField().sendKeys("42test");
        pickDisplaynameField().sendKeys(displayedName);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='password_dup_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("password_dup_error"));
    }

    // DISPLAY NAME TESTING

    @Parameters({"not_being_registered_login", "test_user_password"})
    @Test(priority = 4,
            testName = "Registration - failure - no display name",
            dependsOnGroups = "registration_configuration")
    public void registrationFailureNoDisplayName(String name, String password) {
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickUserNameField().sendKeys(name);
        pickPasswordField().sendKeys(password);
        pickPasswordConfirmField().sendKeys(password);
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='displayname_error']"));
        wait.until(f -> error.isDisplayed());

        Assert.assertEquals(error.getText(), allMyHandyErrors.get("displayname_error"));
    }

    @Test(priority = 4,
            testName = "Registration - failure - Correct display name only entered",
            expectedExceptions = { NoSuchElementException.class },
            dependsOnGroups = "registration_configuration")
    public void registrationFailuresCorrectDisplayNameOnly() throws Exception{
        this.latestTestMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        openRegistrationPage();

        pickDisplaynameField().sendKeys("test test test");
        pickRegisterButton().click();

        WebElement error = driver.findElement(By.xpath("//span[@id='displayname_error']"));
        wait.until(f -> error.isDisplayed());
    }

    private void openRegistrationPage(){
        WebElement header = driver.findElement(By.tagName("header"));
        wait.until(f -> header.isDisplayed());
        WebElement registerLink = driver.findElement(By.xpath("//div[@class='container base']/a[1]"));
        registerLink.click();
    }

    private WebElement pickUserNameField(){
        return driver.findElement(By.xpath("//input[@id='username']"));
    }

    private WebElement pickPasswordField(){
        return driver.findElement(By.xpath("//input[@id='password']"));
    }

    private WebElement pickPasswordConfirmField() {
        return driver.findElement(By.xpath("//input[@id='passwordConfirm']"));
    }

    private WebElement pickDisplaynameField() {
        return driver.findElement(By.xpath("//input[@id='displayname']"));
    }

    private WebElement pickRegisterButton() {
        return driver.findElement(By.xpath("//button[@id='register_submit']"));
    }

    private String getRandomLettersName(int length){
        if (length <= 0)
            return "";

        Random random = new Random();
        StringBuilder name = new StringBuilder((char) random.nextInt(0x41, 0x5A));

        for (int i = 0; i < length; i++)
            name.append((char) random.nextInt(0x41, 0x5A));

        return name.toString();
    }

    private void logout(){
        try{
            driver.findElement(By.xpath("//a[@id='logout']")).click();
        }
        catch (Exception ignore){}
    }
}
