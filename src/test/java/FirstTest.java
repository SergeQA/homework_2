import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FirstTest {

    private static AppiumDriver<MobileElement> driver;

    public static void main(String[] args) throws MalformedURLException {
        Logger logger = LoggerFactory.getLogger(FirstTest.class);

        String pathToOurDir = System.getProperty("user.dir");
        File appFile = new File(pathToOurDir + "/app/android/Contacts.apk");

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android"); //cmd + d
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Any");
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium"); // optional
        desiredCapabilities.setCapability(MobileCapabilityType.APP, appFile);
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 500);
        desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        desiredCapabilities.setCapability("autoAcceptAlerts", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        // TEST 1

        String name = "Garance Epperson";

        String locator = String.format("//*[@text='%s']", name);
        MobileElement element = driver.findElement(By.xpath(locator));
        element.click();

        // waiting

        WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
        MobileElement detailName = (MobileElement) webDriverWait.until(
                ExpectedConditions.visibilityOf(driver.findElementById("com.jayway.contacts:id/detail_name")));
        assert detailName.getText().equals(name);

        MobileElement phoneNumber = driver.findElementById("com.jayway.contacts:id/phonenumber");
        assert phoneNumber.getText().equals("+1(747)-8330134");

        driver.closeApp();


        // TEST 2

        driver.launchApp();
        String locatorForContact = "com.jayway.contacts:id/name";
        List<MobileElement> elements = driver.findElements(By.id(locatorForContact));
        MobileElement lastElement;
        MobileElement firstElement;

        if (!elements.isEmpty()) {
            logger.info("Contacts were found");
            lastElement = elements.get(elements.size() - 1);
            firstElement = elements.get(0);
        } else {
            throw new IllegalStateException("No contacts!");
        }

        for (int i = 0; i < 10; i++) {
            List<MobileElement> contactsBeforeScroll = driver.findElements(By.id(locatorForContact));
            String lastContact = contactsBeforeScroll.get(contactsBeforeScroll.size() - 1).getText();
            logger.info(String.format("Last contact is %s", lastContact));

            new TouchAction(driver)
                    .press(lastElement)
                    .moveTo(firstElement)
                    .release()
                    .perform();

            List<MobileElement> contactsAfterScroll = driver.findElements(By.id(locatorForContact));
            String lastContactAfterScroll = contactsAfterScroll.get(contactsAfterScroll.size() - 1).getText();
            logger.info(String.format("Last contact after scroll is %s", lastContactAfterScroll));

            if (lastContact.equals(lastContactAfterScroll)) {
                logger.info("Test passed!");
                return;
            } else if (i == 9) {
                throw new IllegalStateException("Scroll is failed!");
            }
        }
        driver.closeApp();

        // TEST 3

        driver.launchApp();
        String locatorForButton = "com.jayway.contacts:id/fab";
        MobileElement button = driver.findElement(By.id(locatorForButton));
        button.click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        assert alert.getText().equals("Not implemented!");
        driver.closeApp();

        // TEST 4

        driver.launchApp();
        String locatorForSearch = "com.jayway.contacts:id/main_search";
        MobileElement searchField = driver.findElement(By.id(locatorForSearch));
        String locatorForFoundContact = "com.jayway.contacts:id/name";
        searchField.click();
        searchField.sendKeys("jenn");
        List<MobileElement> foundContacts = driver.findElements(By.id(locatorForFoundContact));
        int foundNumber = foundContacts.size();
        for (int j = 0, j < foundNumber, j++){

        }


    }
}
