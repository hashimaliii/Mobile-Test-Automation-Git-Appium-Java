package tests;

import base.BaseTest;
import pages.HomePage;
import pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CartTest class tests the cart functionality using Page Object Model (POM).
 * All page interactions are handled through page classes for better maintainability.
 */
public class CartTest extends BaseTest {

    @Test
    public void testCartButtonIsClickable() {
        // Initialize home page
        HomePage homePage = new HomePage(driver);

        // Wait for home page and verify cart button
        homePage.waitForHomePageToLoad();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not displayed");
        Assert.assertTrue(homePage.isCartButtonClickable(), "Cart button is not clickable");
        System.out.println("✓ Cart button is clickable!");
    }

    @Test
    public void testCartIconPresent() {
        // Initialize home page
        HomePage homePage = new HomePage(driver);

        // Wait for home page and verify cart icon
        homePage.waitForHomePageToLoad();
        Assert.assertTrue(homePage.isCartIconDisplayed(), "Cart icon is not displayed");
        System.out.println("✓ Cart icon is present on the header!");
    }

    @Test
    public void testAddToCartFunctionality() {
        // Initialize pages
        HomePage homePage = new HomePage(driver);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);

        // Navigate to product details
        homePage.waitForHomePageToLoad();
        Assert.assertTrue(homePage.isProductListDisplayed(), "Product list not displayed");
        
        // Click on first product
        homePage.clickFirstProduct();
        
        // Verify product details page loaded
        productDetailsPage.pause(1000);
        Assert.assertTrue(productDetailsPage.isProductDetailsPageDisplayed(), 
            "Product details page not loaded");
        Assert.assertTrue(productDetailsPage.isAddToCartButtonDisplayed(), 
            "Add to cart button not displayed");
        System.out.println("✓ Product detail page loaded successfully and Add to Cart button is visible!");
    }
}
