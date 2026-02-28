package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage class contains common methods and utilities for all page objects.
 * This serves as the base class for all page object classes in the framework.
 */
public class BasePage {
    protected AndroidDriver driver;
    protected WebDriverWait wait;
    private static final long DEFAULT_WAIT_TIME = 30;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_TIME));
    }

    /**
     * Wait for an element to be visible and return it
     */
    protected WebElement waitForVisibility(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Wait for an element to be clickable and return it
     */
    protected WebElement waitForClickability(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    /**
     * Wait for an element to be present in DOM
     */
    protected WebElement waitForPresence(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * Click on an element with wait for clickability
     */
    protected void click(By by) {
        waitForClickability(by).click();
    }

    /**
     * Send keys to an element
     */
    protected void sendKeys(By by, String text) {
        WebElement element = waitForVisibility(by);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from an element
     */
    protected String getText(By by) {
        return waitForVisibility(by).getText();
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By by) {
        try {
            return waitForVisibility(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(By by) {
        try {
            return waitForVisibility(by).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Custom wait with specified duration
     */
    protected WebElement waitForVisibility(By by, long timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Pause execution for specified milliseconds
     */
    protected void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scroll down to find an element
     */
    protected void scrollToElement(By by) {
        try {
            waitForPresence(by);
        } catch (Exception e) {
            driver.executeScript("mobile: scroll", 
                java.util.Map.of("direction", "down", "strategy", "accessibility id"));
        }
    }
}
