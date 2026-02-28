package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppLaunchTest extends BaseTest {

    @Test
    public void testAppLaunchesSuccessfully() {
        // Since BaseTest handles the setup, the app will launch before this runs.
        // We will just print a message and assert true to ensure TestNG is working.
        System.out.println("The Sauce Labs app has launched successfully!");
        Assert.assertTrue(true, "App launch sanity check failed.");
        
        // Pausing for 3 seconds so you can visually see the app on your emulator before it closes
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}