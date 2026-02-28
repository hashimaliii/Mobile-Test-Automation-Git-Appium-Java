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
        // Since you're in 'mobile-automation', 'app' is right here!
        String projectRoot = System.getProperty("user.dir");
        File app = new File(projectRoot + "/app/mda-2.2.0-25.apk");
    
        // Safety check: print the path if it fails so we can see it in CI logs
        if (!app.exists()) {
            throw new RuntimeException("APK NOT FOUND! Path attempted: " + app.getAbsolutePath());
        }
    
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setDeviceName("emulator-5554")
                .setApp(app.getAbsolutePath())
                // Keeps the app from timing out on slow cloud runners
                .setAppPackage("com.saucelabs.mydemoapp.android")
                .setAppActivity("com.saucelabs.mydemoapp.android.view.activities.SplashActivity")
                .setAppWaitActivity("com.saucelabs.mydemoapp.android.view.activities.MainActivity")
                .setAppWaitDuration(Duration.ofSeconds(60))
                .setNoReset(true)
                .setAutoGrantPermissions(true); 
    
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