package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * MenuPage class represents the navigation menu of the application.
 * This page contains locators and methods for interacting with the menu screen.
 */
public class MenuPage extends BasePage {
    
    // Locators
    private final By menuDrawer = AppiumBy.id("com.saucelabs.mydemoapp.android:id/drawerMenu");
    private final By logInOption = AppiumBy.xpath("//*[@text='Log In']");
    private final By logOutOption = AppiumBy.xpath("//*[@text='Log Out']");
    private final By aboutOption = AppiumBy.xpath("//*[@text='About']");
    private final By webViewOption = AppiumBy.xpath("//*[@text='WebView']");
    private final By catalogOption = AppiumBy.xpath("//*[@text='Catalog']");
    private final By resetAppOption = AppiumBy.xpath("//*[@text='Reset App State']");
    
    public MenuPage(AndroidDriver driver) {
        super(driver);
    }

    /**
     * Wait for menu to be displayed
     */
    public MenuPage waitForMenuToDisplay() {
        waitForVisibility(menuDrawer);
        return this;
    }

    /**
     * Check if menu is displayed
     */
    public boolean isMenuDisplayed() {
        return isElementDisplayed(menuDrawer);
    }

    /**
     * Click on log in option
     */
    public void clickLogInOption() {
        waitForClickability(logInOption).click();
    }

    /**
     * Click on log out option
     */
    public void clickLogOutOption() {
        waitForClickability(logOutOption).click();
    }

    /**
     * Click on about option
     */
    public void clickAboutOption() {
        waitForClickability(aboutOption).click();
    }

    /**
     * Click on webview option
     */
    public void clickWebViewOption() {
        waitForClickability(webViewOption).click();
    }

    /**
     * Click on catalog option
     */
    public void clickCatalogOption() {
        waitForClickability(catalogOption).click();
    }

    /**
     * Click on reset app option
     */
    public void clickResetAppOption() {
        waitForClickability(resetAppOption).click();
    }

    /**
     * Check if log in option is displayed
     */
    public boolean isLogInOptionDisplayed() {
        return isElementDisplayed(logInOption);
    }

    /**
     * Check if log out option is displayed
     */
    public boolean isLogOutOptionDisplayed() {
        try {
            return isElementDisplayed(logOutOption);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Close menu by clicking outside or pressing back
     */
    public void closeMenu() {
        // Click on the content area (outside menu) to close it
        try {
            driver.navigate().back();
        } catch (Exception e) {
            System.out.println("Could not close menu");
        }
    }
}
