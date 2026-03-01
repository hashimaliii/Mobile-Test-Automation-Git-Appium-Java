package tests;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class AccessibilityTest extends BaseTest {

    @Test
    public void testProductImageContentDescription() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for products to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV")));

        // Find product images with content descriptions
        java.util.List<WebElement> productImages = driver.findElements(
            AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView//*[@resource-id='com.saucelabs.mydemoapp.android:id/productIV']"));

        Assert.assertTrue(productImages.size() > 0, "No product images found");

        // Check if product images have content descriptions for accessibility
        for (WebElement image : productImages) {
            String contentDesc = image.getAttribute("content-desc");
            Assert.assertNotNull(contentDesc, "Product image missing content description");
            Assert.assertFalse(contentDesc.isEmpty(), "Product image content description is empty");
        }

        System.out.println("✓ All " + productImages.size() + " product images have accessibility descriptions!");
    }

    @Test
    public void testUIElementsAccessibility() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for main activity
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/container")));

        // Check menu button accessibility
        WebElement menuButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV")));

        String menuContentDesc = menuButton.getAttribute("content-desc");
        Assert.assertNotNull(menuContentDesc);
        Assert.assertFalse(menuContentDesc.isEmpty());

        // Check cart button accessibility  
        WebElement cartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartRL")));

        String cartContentDesc = cartButton.getAttribute("content-desc");
        Assert.assertNotNull(cartContentDesc);
        Assert.assertFalse(cartContentDesc.isEmpty());

        System.out.println("✓ UI elements have proper accessibility descriptions!");
        System.out.println("  - Menu: " + menuContentDesc);
        System.out.println("  - Cart: " + cartContentDesc);
    }

    @Test
    public void testScreenReaderFocusability() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for main activity
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/container")));

        // Find all clickable elements that should be focusable
        java.util.List<WebElement> focusableElements = driver.findElements(
            AppiumBy.xpath("//*[@focusable='true']"));

        Assert.assertTrue(focusableElements.size() > 0, "No focusable elements found");
        System.out.println("✓ Screen has " + focusableElements.size() + " focusable elements for screen readers!");
    }

    @Test
    public void testAppLaunchAnnouncement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for app logo to show successful launch
        WebElement appLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/mTvTitle")));

        Assert.assertTrue(appLogo.isDisplayed());

        // Verify app logo has accessibility information
        String logoContentDesc = appLogo.getAttribute("content-desc");
        Assert.assertNotNull(logoContentDesc);
        
        System.out.println("✓ App launched successfully!");
        System.out.println("  App Logo Content Description: " + logoContentDesc);
    }
}
