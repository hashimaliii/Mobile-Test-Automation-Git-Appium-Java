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
        // 1. Locate the APK file in your new 'app' folder
        File app = new File("app/mda-2.2.0-25.apk");

        // 2. Configure the Emulator and Appium settings
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp(app.getAbsolutePath())
                .setAutoGrantPermissions(true); 

        // 3. Connect to the local Appium Server
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        // 4. Close the app after the test
        if (driver != null) {
            driver.quit();
        }
    }
}