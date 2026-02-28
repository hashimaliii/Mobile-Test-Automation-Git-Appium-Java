package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * CartPage class represents the shopping cart page of the application.
 * This page contains locators and methods for interacting with the cart screen.
 */
public class CartPage extends BasePage {
    
    // Locators
    private final By cartTitle = AppiumBy.xpath("//*[@text='My Cart']");
    private final By cartItems = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartRL");
    private final By cartItemsList = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartRV");
    private final By proceedToCheckoutButton = AppiumBy.xpath("//*[@text='Proceed To Checkout']");
    private final By continueShoppingButton = AppiumBy.xpath("//*[@text='Continue Shopping']");
    private final By emptyCartMessage = AppiumBy.xpath("//*[@text='Please add items to proceed']");
    private final By cartBadge = AppiumBy.id("com.saucelabs.mydemoapp.android:id/cartBadgeTV");
    private final By totalPrice = AppiumBy.id("com.saucelabs.mydemoapp.android:id/totalTV");
    private final By removeButton = AppiumBy.xpath("//android.widget.ImageView[@content-desc='Remove from cart']");
    
    public CartPage(AndroidDriver driver) {
        super(driver);
    }

    /**
     * Wait for cart page to load
     */
    public CartPage waitForCartPageToLoad() {
        waitForVisibility(cartTitle);
        return this;
    }

    /**
     * Check if cart page is displayed
     */
    public boolean isCartPageDisplayed() {
        return isElementDisplayed(cartTitle);
    }

    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        return isElementDisplayed(emptyCartMessage);
    }

    /**
     * Check if cart items are displayed
     */
    public boolean isCartItemsDisplayed() {
        return isElementDisplayed(cartItemsList);
    }

    /**
     * Click on proceed to checkout button
     */
    public void clickProceedToCheckoutButton() {
        waitForClickability(proceedToCheckoutButton).click();
    }

    /**
     * Click on continue shopping button
     */
    public void clickContinueShoppingButton() {
        waitForClickability(continueShoppingButton).click();
    }

    /**
     * Check if proceed to checkout button is displayed
     */
    public boolean isProceedToCheckoutButtonDisplayed() {
        return isElementDisplayed(proceedToCheckoutButton);
    }

    /**
     * Check if continue shopping button is displayed
     */
    public boolean isContinueShoppingButtonDisplayed() {
        return isElementDisplayed(continueShoppingButton);
    }

    /**
     * Get cart badge count
     */
    public String getCartBadgeCount() {
        try {
            return getText(cartBadge);
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * Get total price
     */
    public String getTotalPrice() {
        return getText(totalPrice);
    }

    /**
     * Remove item from cart
     */
    public void removeItemFromCart() {
        try {
            waitForClickability(removeButton).click();
        } catch (Exception e) {
            System.out.println("Remove button not found or not clickable");
        }
    }
}
