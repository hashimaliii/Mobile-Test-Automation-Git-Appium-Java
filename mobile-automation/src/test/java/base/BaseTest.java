package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected AndroidDriver driver;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        String projectRoot = System.getProperty("user.dir");
        File app = new File(projectRoot + "/app/mda-2.2.0-25.apk");

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setDeviceName("emulator-5554")
                .setApp(app.getAbsolutePath())
                .setAppPackage("com.saucelabs.mydemoapp.android")
                .setAppActivity("com.saucelabs.mydemoapp.android.view.activities.SplashActivity")
                .setAppWaitDuration(Duration.ofSeconds(60))
                .setNoReset(false)
                .setAutoGrantPermissions(true);

        options.setAppWaitActivity("com.saucelabs.mydemoapp.android.view.activities.MainActivity");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        
        // Add small delay before terminating to ensure app is ready
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Gracefully terminate and reactivate app with extended timeout
        try {
            driver.terminateApp("com.saucelabs.mydemoapp.android");
            // Wait longer for app termination to complete
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Warning: App termination had issues: " + e.getMessage());
        }
        
        // Activate app and wait for it to be ready
        try {
            driver.activateApp("com.saucelabs.mydemoapp.android");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Warning: App activation had issues: " + e.getMessage());
        }
        
        // Hide keyboard if shown
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
            }
        } catch (Exception e) {
            // Keyboard may not be shown, ignore
        }
        
        // Wait for MainActivity to fully load - Using longer timeout for CI/CD stability
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.xpath("//android.view.ViewGroup[1]/android.widget.ImageView[1]")));
        } catch (Exception e) {
            System.out.println("Warning: Initial element wait timed out, continuing: " + e.getMessage());
        }
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}