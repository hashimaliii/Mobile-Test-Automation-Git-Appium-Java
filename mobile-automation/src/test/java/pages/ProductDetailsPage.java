package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * ProductDetailsPage class represents the product details page of the application.
 * This page contains locators and methods for interacting with the product details screen.
 */
public class ProductDetailsPage extends BasePage {
    
    // Locators
    private final By productTitle = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV");
    private final By productPrice = AppiumBy.id("com.saucelabs.mydemoapp.android:id/priceTV");
    private final By productDescription = AppiumBy.id("com.saucelabs.mydemoapp.android:id/descTV");
    private final By addToCartButton = AppiumBy.xpath("//*[@text='Add To Cart']");
    private final By backButton = AppiumBy.xpath("//android.widget.ImageView[@content-desc='Back button']");
    private final By productImage = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productIV");
    private final By quantityField = AppiumBy.id("com.saucelabs.mydemoapp.android:id/qtyTV");
    
    public ProductDetailsPage(AndroidDriver driver) {
        super(driver);
    }

    /**
     * Wait for product details page to load
     */
    public ProductDetailsPage waitForProductDetailsPageToLoad() {
        waitForVisibility(productTitle);
        return this;
    }

    /**
     * Check if product details page is displayed
     */
    public boolean isProductDetailsPageDisplayed() {
        return isElementDisplayed(productTitle);
    }

    /**
     * Get product title
     */
    public String getProductTitle() {
        return getText(productTitle);
    }

    /**
     * Get product price
     */
    public String getProductPrice() {
        return getText(productPrice);
    }

    /**
     * Get product description
     */
    public String getProductDescription() {
        return getText(productDescription);
    }

    /**
     * Check if product image is displayed
     */
    public boolean isProductImageDisplayed() {
        return isElementDisplayed(productImage);
    }

    /**
     * Click add to cart button
     */
    public void clickAddToCartButton() {
        waitForClickability(addToCartButton).click();
    }

    /**
     * Click back button
     */
    public void clickBackButton() {
        waitForClickability(backButton).click();
    }

    /**
     * Check if add to cart button is displayed
     */
    public boolean isAddToCartButtonDisplayed() {
        try {
            return isElementDisplayed(addToCartButton);
        } catch (Exception e) {
            // Button might not be visible, try alternative locators
            try {
                By altLocator = AppiumBy.xpath("//android.widget.Button[contains(@text, 'Add')]");
                return isElementDisplayed(altLocator);
            } catch (Exception e2) {
                return false;
            }
        }
    }

    /**
     * Get quantity value
     */
    public String getQuantity() {
        return getText(quantityField);
    }
}
