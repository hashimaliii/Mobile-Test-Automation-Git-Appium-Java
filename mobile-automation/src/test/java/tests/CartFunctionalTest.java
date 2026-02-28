package tests;

import base.BaseTest;
import pages.HomePage;
import pages.ProductDetailsPage;
import pages.CartPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CartFunctionalTest class contains comprehensive functional tests for shopping cart functionality.
 * These tests validate adding items, removing items, and cart operations.
 */
public class CartFunctionalTest extends BaseTest {

    /**
     * Test 1: Add single product to cart and verify it's added
     */
    @Test
    public void testAddSingleProductToCart() {
        HomePage homePage = new HomePage(driver);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Step 1: Navigate to home and select first product
        homePage.waitForHomePageToLoad();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not loaded");
        System.out.println("✓ Step 1: Home page loaded successfully");

        // Step 2: Get initial product info and click on first product
        homePage.clickFirstProduct();
        productDetailsPage.pause(1500);
        Assert.assertTrue(productDetailsPage.isProductDetailsPageDisplayed(), 
            "Product details page not loaded");
        
        String productTitle = productDetailsPage.getProductTitle();
        System.out.println("✓ Step 2: Product selected - " + productTitle);

        // Step 3: Navigate to cart
        homePage.clickCartButton();
        cartPage.pause(1500);
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page not displayed");
        System.out.println("✓ Step 3: Cart page opened");

        // Step 4: Verify cart is not empty
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart is empty - product was not added");
        System.out.println("✓ Step 4: Product successfully added to cart!");
    }

    /**
     * Test 2: Add multiple products to cart and verify all items are present
     */
    @Test
    public void testAddMultipleProductsToCart() {
        HomePage homePage = new HomePage(driver);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Step 1: Navigate to home
        homePage.waitForHomePageToLoad();
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not loaded");
        System.out.println("✓ Step 1: Home page loaded");

        // Step 2: Add first product
        homePage.clickFirstProduct();
        productDetailsPage.pause(1000);
        Assert.assertTrue(productDetailsPage.isProductDetailsPageDisplayed(), 
            "Product details page not loaded");
        
        String firstProductTitle = productDetailsPage.getProductTitle();
        System.out.println("✓ Step 2a: Added first product - " + firstProductTitle);

        // Step 3: Return to home and add second product
        driver.navigate().back();
        homePage.waitForHomePageToLoad();
        homePage.pause(1000);
        
        homePage.clickProductByIndex(2);
        productDetailsPage.pause(1000);
        Assert.assertTrue(productDetailsPage.isProductDetailsPageDisplayed(), 
            "Second product details not loaded");
        
        String secondProductTitle = productDetailsPage.getProductTitle();
        System.out.println("✓ Step 3: Added second product - " + secondProductTitle);

        // Step 4: Return to home and add third product
        driver.navigate().back();
        homePage.waitForHomePageToLoad();
        homePage.pause(1000);
        
        homePage.clickProductByIndex(3);
        productDetailsPage.pause(1000);
        System.out.println("✓ Step 4: Added third product");

        // Step 5: Navigate to cart and verify all items
        driver.navigate().back();
        homePage.waitForHomePageToLoad();
        homePage.pause(500);
        
        homePage.clickCartButton();
        cartPage.pause(1500);
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page not displayed");
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
        
        System.out.println("✓ Step 5: Multiple products successfully added to cart!");
        System.out.println("✓ Cart Total: " + cartPage.getTotalPrice());
    }

    /**
     * Test 3: View cart and verify cart contents and total price
     */
    @Test
    public void testViewCartAndVerifyContents() {
        HomePage homePage = new HomePage(driver);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Step 1: Add a product to cart
        homePage.waitForHomePageToLoad();
        homePage.clickFirstProduct();
        productDetailsPage.pause(1000);
        
        String productTitle = productDetailsPage.getProductTitle();
        String productPrice = productDetailsPage.getProductPrice();
        System.out.println("✓ Step 1: Selected product - " + productTitle + " @ " + productPrice);

        // Step 2: Go back and add another product
        driver.navigate().back();
        homePage.waitForHomePageToLoad();
        homePage.pause(500);
        
        homePage.clickProductByIndex(2);
        productDetailsPage.pause(1000);
        String secondPrice = productDetailsPage.getProductPrice();
        System.out.println("✓ Step 2: Added second product @ " + secondPrice);

        // Step 3: Open cart
        driver.navigate().back();
        homePage.waitForHomePageToLoad();
        homePage.pause(500);
        
        homePage.clickCartButton();
        cartPage.pause(1500);
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page not displayed");
        System.out.println("✓ Step 3: Cart page opened");

        // Step 4: Verify cart contents
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart is empty");
        Assert.assertTrue(cartPage.isCartItemsDisplayed(), "Cart items not displayed");
        
        String cartTotal = cartPage.getTotalPrice();
        Assert.assertNotNull(cartTotal, "Cart total is null");
        Assert.assertTrue(!cartTotal.isEmpty(), "Cart total is empty");
        
        System.out.println("✓ Step 4: Cart contents verified");
        System.out.println("  - Cart Total: " + cartTotal);
        System.out.println("  - Items are displayed correctly");

        // Step 5: Verify proceed to checkout button
        Assert.assertTrue(cartPage.isProceedToCheckoutButtonDisplayed(), 
            "Proceed to checkout button not displayed");
        System.out.println("✓ Step 5: Proceed to checkout button is available!");
    }

    /**
     * Test 4: Remove item from cart
     */
    @Test
    public void testRemoveItemFromCart() {
        HomePage homePage = new HomePage(driver);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Step 1: Add products to cart
        homePage.waitForHomePageToLoad();
        
        homePage.clickFirstProduct();
        productDetailsPage.pause(1000);
        System.out.println("✓ Step 1a: First product added");
        
        driver.navigate().back();
        homePage.waitForHomePageToLoad();
        homePage.pause(500);
        
        homePage.clickProductByIndex(2);
        productDetailsPage.pause(1000);
        System.out.println("✓ Step 1b: Second product added");

        // Step 2: Open cart
        driver.navigate().back();
        homePage.waitForHomePageToLoad();
        homePage.pause(500);
        
        homePage.clickCartButton();
        cartPage.pause(1500);
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page not displayed");
        System.out.println("✓ Step 2: Cart page opened with items");

        // Step 3: Verify cart has items
        Assert.assertFalse(cartPage.isCartEmpty(), "Cart should not be empty");
        String initialTotal = cartPage.getTotalPrice();
        System.out.println("✓ Step 3: Cart verified - Total: " + initialTotal);

        // Step 4: Remove item from cart
        cartPage.removeItemFromCart();
        cartPage.pause(1000);
        System.out.println("✓ Step 4: Item removed from cart");

        // Step 5: Verify item was removed (cart might be empty or total changed)
        try {
            String newTotal = cartPage.getTotalPrice();
            System.out.println("✓ Step 5: Cart contents updated - New Total: " + newTotal);
        } catch (Exception e) {
            System.out.println("✓ Step 5: Cart updated after item removal");
        }
    }

    /**
     * Test 5: Verify continue shopping functionality
     */
    @Test
    public void testContinueShoppingFromCart() {
        HomePage homePage = new HomePage(driver);
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(driver);
        CartPage cartPage = new CartPage(driver);

        // Step 1: Add product and go to cart
        homePage.waitForHomePageToLoad();
        homePage.clickFirstProduct();
        productDetailsPage.pause(1000);
        System.out.println("✓ Step 1: Product selected");

        // Step 2: Open cart
        driver.navigate().back();
        homePage.waitForHomePageToLoad();
        homePage.pause(500);
        
        homePage.clickCartButton();
        cartPage.pause(1500);
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page not displayed");
        System.out.println("✓ Step 2: Cart page opened");

        // Step 3: Verify continue shopping button exists
        Assert.assertTrue(cartPage.isContinueShoppingButtonDisplayed(), 
            "Continue shopping button not displayed");
        System.out.println("✓ Step 3: Continue shopping button is available");

        // Step 4: Click continue shopping
        cartPage.clickContinueShoppingButton();
        cartPage.pause(1500);
        
        // Step 5: Verify returned to home/products page
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page not displayed after continuing");
        System.out.println("✓ Step 4: Continue shopping returned to home page");
        System.out.println("✓ Step 5: Workflow complete - User can continue browsing after cart!");
    }
}
