package tests;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class ProductBrowsingTest extends BaseTest {

    @Test
    public void testProductsDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for products page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV")));

        // Verify products title is visible
        WebElement productsTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV")));

        Assert.assertTrue(productsTitle.isDisplayed());
        Assert.assertEquals(productsTitle.getText(), "Products");
        System.out.println("✓ Products page title verified!");
    }

    @Test
    public void testProductListNotEmpty() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for product list to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV")));

        // Check if products are visible in the RecyclerView
        java.util.List<WebElement> products = driver.findElements(
            AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView//*[@resource-id='com.saucelabs.mydemoapp.android:id/productIV']"));

        Assert.assertTrue(products.size() > 0, "No products found on the page");
        System.out.println("✓ Product list contains " + products.size() + " products!");
    }

    @Test
    public void testProductDetailsVisible() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for product list to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV")));

        // Verify product details like title and price are visible
        java.util.List<WebElement> productTitles = driver.findElements(
            AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView//*[@resource-id='com.saucelabs.mydemoapp.android:id/titleTV']"));
        
        java.util.List<WebElement> productPrices = driver.findElements(
            AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView//*[@resource-id='com.saucelabs.mydemoapp.android:id/priceTV']"));

        Assert.assertTrue(productTitles.size() > 0, "No product titles found");
        Assert.assertTrue(productPrices.size() > 0, "No product prices found");
        System.out.println("✓ Product details visible - Found " + productTitles.size() + " titles and " + productPrices.size() + " prices!");
    }
}
