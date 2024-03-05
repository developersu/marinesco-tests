package ru.redrise.users_management;

import org.openqa.selenium.OutputType;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public abstract class ScreenshotsMaker{
    protected String latestTestMethodName;

    @AfterMethod
    public void takeScreenshot() throws IOException {
        final String finalFileLocation = String.format("/tmp/tests-output/%s - %s.png",
                getClass().toString().replaceAll("^.*\\.", ""), latestTestMethodName);

        File screenshot = Suite.driver.getScreenshotAs(OutputType.FILE);
        File finalLocation = new File(finalFileLocation);

        if (!finalLocation.getParentFile().exists())
            Assert.assertTrue(finalLocation.getParentFile().mkdirs());

        Files.move(screenshot.toPath(), finalLocation.toPath(), StandardCopyOption.REPLACE_EXISTING);

        this.latestTestMethodName = "";
    }
}
