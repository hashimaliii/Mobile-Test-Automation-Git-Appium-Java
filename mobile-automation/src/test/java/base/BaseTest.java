package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
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
        // Use an absolute path check to ensure CI finds the APK regardless of the runner's start point
        File app = new File(System.getProperty("user.dir") + "/../app/mda-2.2.0-25.apk");
        
        // If your 'app' folder is INSIDE 'mobile-automation', use this instead:
        // File app = new File(System.getProperty("user.dir") + "/app/mda-2.2.0-25.apk");

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setDeviceName("emulator-5554")
                .setApp(app.getAbsolutePath())
                // --- ADD THESE THREE LINES ---
                .setAppPackage("com.saucelabs.mydemoapp.android")
                .setAppActivity("com.saucelabs.mydemoapp.android.view.activities.SplashActivity")
                .setAppWaitDuration(Duration.ofSeconds(60)) // Give it a full minute to launch
                // -----------------------------
                .setNoReset(true)
                .setAutoGrantPermissions(true);

        // Added the /v1/ suffix which some Appium 2.x server configurations prefer
        // But 127.0.0.1:4723 is usually fine for default installs
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}