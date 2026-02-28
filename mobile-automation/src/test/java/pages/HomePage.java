package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * HomePage class represents the main/home page of the application.
 * This page contains locators and methods for interacting with the home screen.
 */
public class HomePage extends BasePage {
    
    // Locators
    private final By menuButton = AppiumBy.id("com.saucelabs.mydemoapp.android:id/menuIV");
    private final By cartButton = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartRL");
    private final By cartIcon = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartIV");
    private final By container = AppiumBy.id("com.saucelabs.mydemoapp.android:id/container");
    private final By productList = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productRV");
    private final By firstProduct = AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id='com.saucelabs.mydemoapp.android:id/productRV']//android.widget.RelativeLayout[1]");
    
    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    /**
     * Wait for home page to load completely
     */
    public HomePage waitForHomePageToLoad() {
        waitForPresence(container);
        return this;
    }

    /**
     * Check if home page is displayed
     */
    public boolean isHomePageDisplayed() {
        return isElementDisplayed(container);
    }

    /**
     * Click on menu button
     */
    public void clickMenuButton() {
        waitForClickability(menuButton).click();
    }

    /**
     * Click on cart button
     */
    public void clickCartButton() {
        waitForClickability(cartButton).click();
    }

    /**
     * Check if cart icon is displayed
     */
    public boolean isCartIconDisplayed() {
        return isElementDisplayed(cartIcon);
    }

    /**
     * Check if cart button is clickable and enabled
     */
    public boolean isCartButtonClickable() {
        return isElementEnabled(cartButton) && isElementDisplayed(cartButton);
    }

    /**
     * Click on a product by index
     */
    public void clickProductByIndex(int index) {
        By productLocator = AppiumBy.xpath(
            "//androidx.recyclerview.widget.RecyclerView[@resource-id='com.saucelabs.mydemoapp.android:id/productRV']//android.widget.RelativeLayout[" + index + "]"
        );
        waitForClickability(productLocator).click();
    }

    /**
     * Click on first product
     */
    public void clickFirstProduct() {
        clickProductByIndex(1);
    }

    /**
     * Check if product list is displayed
     */
    public boolean isProductListDisplayed() {
        return isElementDisplayed(productList);
    }
}
