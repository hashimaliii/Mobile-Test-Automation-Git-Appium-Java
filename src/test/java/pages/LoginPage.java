package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * LoginPage class represents the login page of the application.
 * This page contains locators and methods for interacting with the login screen.
 */
public class LoginPage extends BasePage {
    
    // Locators
    private final By usernameField = AppiumBy.xpath("//*[contains(@text, 'Username')]");
    private final By passwordField = AppiumBy.xpath("//*[contains(@text, 'Password')]");
    private final By loginButton = AppiumBy.xpath("//*[@text='Log In']");
    private final By loginMenuOption = AppiumBy.xpath("//*[@text='Log In']");
    private final By errorMessage = AppiumBy.xpath("//*[contains(@text, 'Incorrect')]");
    
    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    /**
     * Check if login page is displayed by verifying presence of username field
     */
    public boolean isLoginPageDisplayed() {
        return isElementDisplayed(usernameField);
    }

    /**
     * Enter username in the username field
     */
    public LoginPage enterUsername(String username) {
        sendKeys(usernameField, username);
        return this;
    }

    /**
     * Enter password in the password field
     */
    public LoginPage enterPassword(String password) {
        sendKeys(passwordField, password);
        return this;
    }

    /**
     * Click the login button
     */
    public void clickLoginButton() {
        waitForClickability(loginButton).click();
    }

    /**
     * Perform login with username and password
     */
    public void login(String username, String password) {
        enterUsername(username)
            .enterPassword(password);
        clickLoginButton();
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Check if username field is present
     */
    public boolean isUsernameFieldPresent() {
        return isElementDisplayed(usernameField);
    }

    /**
     * Check if password field is present
     */
    public boolean isPasswordFieldPresent() {
        return isElementDisplayed(passwordField);
    }
}
