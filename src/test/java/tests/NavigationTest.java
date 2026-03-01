package tests;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class NavigationTest extends BaseTest {

    @Test
    public void testMenuDrawerOpens() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for main activity
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/container")));

        // Click menu button
        WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV")));
        
        menuButton.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify menu items are visible
        java.util.List<WebElement> menuItems = driver.findElements(
            AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView//*[@resource-id='com.saucelabs.mydemoapp.android:id/itemTV']"));

        Assert.assertTrue(menuItems.size() > 0, "Menu items not found");
        System.out.println("✓ Menu drawer opened with " + menuItems.size() + " menu items!");
    }

    @Test
    public void testSortingOptionAvailable() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for main activity
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/container")));

        // Verify sort button is clickable
        WebElement sortButton = wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/sortIV")));

        Assert.assertTrue(sortButton.isDisplayed());
        Assert.assertTrue(sortButton.isEnabled());
        System.out.println("✓ Sort/Filter button is available!");
    }

    @Test
    public void testHeaderElementsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for main activity
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/container")));

        // Verify header elements
        WebElement appLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/mTvTitle")));

        WebElement menuButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV")));

        WebElement sortButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/sortIV")));

        WebElement cartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartRL")));

        Assert.assertTrue(appLogo.isDisplayed());
        Assert.assertTrue(menuButton.isDisplayed());
        Assert.assertTrue(sortButton.isDisplayed());
        Assert.assertTrue(cartButton.isDisplayed());
        System.out.println("✓ All header elements are present and visible!");
    }
}
