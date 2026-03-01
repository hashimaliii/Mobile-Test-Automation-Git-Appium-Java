package tests;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import pages.MenuPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest class tests the login functionality using Page Object Model (POM).
 * All page interactions are handled through page classes for better maintainability.
 */
public class LoginTest extends BaseTest {

    @Test
    public void testLoginWithValidCredentials() {
        // Initialize pages
        HomePage homePage = new HomePage(driver);
        MenuPage menuPage = new MenuPage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Verify home page is loaded
        homePage.waitForHomePageToLoad();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not displayed");

        // Open menu and navigate to login
        homePage.clickMenuButton();
        menuPage.waitForMenuToDisplay();
        menuPage.clickLogInOption();

        // Verify login page is displayed
        loginPage.pause(1000);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page not displayed");
        Assert.assertTrue(loginPage.isUsernameFieldPresent(), "Username field not present");
        Assert.assertTrue(loginPage.isPasswordFieldPresent(), "Password field not present");
        System.out.println("✓ Login screen verified successfully!");
    }

    @Test
    public void testLoginScreenElementsPresent() {
        // Initialize pages
        HomePage homePage = new HomePage(driver);
        MenuPage menuPage = new MenuPage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // Navigate to login page using POM
        homePage.waitForHomePageToLoad();
        homePage.clickMenuButton();
        menuPage.waitForMenuToDisplay();
        menuPage.clickLogInOption();

        // Verify all login screen elements are present
        loginPage.pause(1000);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page not displayed");
        Assert.assertTrue(loginPage.isUsernameFieldPresent(), "Username field not visible");
        Assert.assertTrue(loginPage.isPasswordFieldPresent(), "Password field not visible");
        System.out.println("✓ All login screen elements are present!");
    }
}
