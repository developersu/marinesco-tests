package ru.redrise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;

public class ReportListener implements ITestListener {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onFinish(ITestContext testContext) {
        log.info("PASSED TEST CASES");
        testContext.getPassedTests().getAllResults()
                .forEach(result ->
                    log.info(result.getName()));

        log.info("FAILED TEST CASES");
        testContext.getFailedTests().getAllResults()
                .forEach(result ->
                    log.info(result.getName()));

        log.info(
                "Test completed on: " + testContext.getEndDate().toString());
    }

}