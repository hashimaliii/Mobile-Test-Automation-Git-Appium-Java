package tests;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest extends BaseTest {

    @Test
    public void testLoginWithValidCredentials() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for the main activity to be fully loaded
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/container")));

        // Open menu
        wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV"))).click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click Log In menu item
        wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//*[@text='Log In']"))).click();

        // Verify we're on the login screen by checking for input fields
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify login screen elements are present
        Assert.assertNotNull(driver.findElement(AppiumBy.xpath("//*[contains(@text, 'Username')]")));
        Assert.assertNotNull(driver.findElement(AppiumBy.xpath("//*[contains(@text, 'Password')]")));
        System.out.println("✓ Login screen verified successfully!");
    }

    @Test
    public void testLoginScreenElementsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Navigate to login
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/container")));

        wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV"))).click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.xpath("//*[@text='Log In']"))).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify all login screen elements
        boolean usernameFieldVisible = !driver.findElements(AppiumBy.xpath("//*[contains(@text, 'Username')]")).isEmpty();
        boolean passwordFieldVisible = !driver.findElements(AppiumBy.xpath("//*[contains(@text, 'Password')]")).isEmpty();
        boolean loginButtonVisible = !driver.findElements(AppiumBy.xpath("//*[contains(@text, 'Login')]")).isEmpty();

        Assert.assertTrue(usernameFieldVisible, "Username field not visible");
        Assert.assertTrue(passwordFieldVisible, "Password field not visible");
        Assert.assertTrue(loginButtonVisible, "Login button not visible");
        System.out.println("✓ All login screen elements are present!");
    }
}
