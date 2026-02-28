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
                .setNoReset(false) // Changed to false to ensure a fresh app state
                .setAutoGrantPermissions(true);

        options.setAppWaitActivity("com.saucelabs.mydemoapp.android.view.activities.MainActivity");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        
        // Reset the app to ensure it's in the foreground and active
        driver.terminateApp("com.saucelabs.mydemoapp.android");
        driver.activateApp("com.saucelabs.mydemoapp.android");
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
        
        // Wait for MainActivity to fully load after app activation
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            AppiumBy.xpath("//android.view.ViewGroup[1]/android.widget.ImageView[1]")));
        
        // --- ADDED STABILITY COMMANDS ---
        // 1. Wake up the screen
        //driver.executeScript("mobile: shell", java.util.Map.of("command", "input keyevent KEYCODE_WAKEUP"));
        // 2. Unlock the screen (Swipe/Menu key)
        //driver.executeScript("mobile: shell", java.util.Map.of("command", "input keyevent 82"));
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}