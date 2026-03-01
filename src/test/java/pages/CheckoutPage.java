package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * CheckoutPage class represents the checkout page of the application.
 * This page contains locators and methods for interacting with the checkout screen.
 */
public class CheckoutPage extends BasePage {
    
    // Locators
    private final By checkoutTitle = AppiumBy.xpath("//*[@text='Checkout']");
    private final By fullNameField = AppiumBy.xpath("//*[contains(@text, 'Full Name')]");
    private final By emailField = AppiumBy.xpath("//*[contains(@text, 'Email')]");
    private final By addressField = AppiumBy.xpath("//*[contains(@text, 'Address')]");
    private final By cityField = AppiumBy.xpath("//*[contains(@text, 'City')]");
    private final By zipField = AppiumBy.xpath("//*[contains(@text, 'Zip')]");
    private final By countryField = AppiumBy.xpath("//*[contains(@text, 'Country')]");
    private final By placeOrderButton = AppiumBy.xpath("//*[@text='Place Order']");
    private final By paymentMethodField = AppiumBy.xpath("//*[contains(@text, 'Payment')]");
    private final By billingAddressCheckbox = AppiumBy.xpath("//*[contains(@text, 'Use Billing')]");
    private final By orderSummary = AppiumBy.id("com.saucelabs.mydemoapp.android:id/orderSummaryTV");
    
    public CheckoutPage(AndroidDriver driver) {
        super(driver);
    }

    /**
     * Wait for checkout page to load
     */
    public CheckoutPage waitForCheckoutPageToLoad() {
        waitForVisibility(checkoutTitle);
        return this;
    }

    /**
     * Check if checkout page is displayed
     */
    public boolean isCheckoutPageDisplayed() {
        return isElementDisplayed(checkoutTitle);
    }

    /**
     * Enter full name
     */
    public CheckoutPage enterFullName(String fullName) {
        sendKeys(fullNameField, fullName);
        return this;
    }

    /**
     * Enter email
     */
    public CheckoutPage enterEmail(String email) {
        sendKeys(emailField, email);
        return this;
    }

    /**
     * Enter address
     */
    public CheckoutPage enterAddress(String address) {
        sendKeys(addressField, address);
        return this;
    }

    /**
     * Enter city
     */
    public CheckoutPage enterCity(String city) {
        sendKeys(cityField, city);
        return this;
    }

    /**
     * Enter zip code
     */
    public CheckoutPage enterZipCode(String zipCode) {
        sendKeys(zipField, zipCode);
        return this;
    }

    /**
     * Enter country
     */
    public CheckoutPage enterCountry(String country) {
        sendKeys(countryField, country);
        return this;
    }

    /**
     * Fill checkout form with all details
     */
    public CheckoutPage fillCheckoutForm(String fullName, String email, String address, 
                                          String city, String zipCode, String country) {
        enterFullName(fullName)
            .enterEmail(email)
            .enterAddress(address)
            .enterCity(city)
            .enterZipCode(zipCode)
            .enterCountry(country);
        return this;
    }

    /**
     * Click place order button
     */
    public void clickPlaceOrderButton() {
        waitForClickability(placeOrderButton).click();
    }

    /**
     * Check if place order button is displayed
     */
    public boolean isPlaceOrderButtonDisplayed() {
        return isElementDisplayed(placeOrderButton);
    }

    /**
     * Check if form fields are displayed
     */
    public boolean areFormFieldsDisplayed() {
        return isElementDisplayed(fullNameField) && 
               isElementDisplayed(emailField) &&
               isElementDisplayed(addressField);
    }

    /**
     * Get order summary text
     */
    public String getOrderSummary() {
        try {
            return getText(orderSummary);
        } catch (Exception e) {
            return "";
        }
    }
}
