package com.pos.Shipment_Quotation;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ShipmentQuoteTest {

    private WebDriver driver;

    // Setup method to initialize WebDriver
    @BeforeMethod
    public void setUp()
    {
    	System.setProperty("webdriver.gecko.driver", "D:/JAVA/JAVA_WORKPLACE/Shipment_Quotation/Browser_Drivers/geckodriver.exe");
    	FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:/Program Files/Mozilla Firefox/firefox.exe");
    	driver = new FirefoxDriver(options);
    }

    @Test
    @Parameters({"fromCountry", "fromPostcode", "toCountry", "weight"})
    public void verifyShipmentQuoteCalculation(String fromCountry,String fromPostcode, String toCountry, String weight) throws InterruptedException
    {   
        // Step 1: Navigate to the rate calculator page.
        driver.get("https://pos.com.my/send/ratecalculator");
        
        // Wait for the page to be fully loaded.
        WebDriverWait wait_01 = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait_01.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
       
       // Step 2: User enter “Malaysia” as “From” country, and enter “35600” as the postcode.
       driver.findElement(By.cssSelector("input[placeholder='Postcode']")).sendKeys(fromPostcode);
       
       WebDriverWait wait_02 = new WebDriverWait(driver, Duration.ofSeconds(10));
       WebElement inputField = wait_02.until(ExpectedConditions.visibilityOfElementLocated(By.id("mat-input-0")));
       
       //Step 3: User enter “India” as “To” country, and leave the postcode empty
       inputField.click();
       inputField.clear();
       inputField.sendKeys(toCountry);
       Thread.sleep(5000);
       //driver.findElement(By.xpath("//html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/mat-option[1]/span[1]/div[1]/small[1]")).click();
       driver.findElement(By.xpath("//small[@title='India - IN']")).click();
       
       //Step 4: User enter 1 as the “Weight”, and user press Calculate.
       driver.findElement(By.xpath("//input[@placeholder='eg. 0.1kg']")).sendKeys("1");
       driver.findElement(By.xpath("//a[@type=' button']")).click();
       
       // Explicit wait using WebDriverWait
       Thread.sleep(5000);

       //Step 5: Verify user can see multiple quotes and shipments options available.
       List<WebElement> serviceTypes = driver.findElements(By.cssSelector("body > app-root:nth-child(2) > div:nth-child(1) > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > app-static-layout:nth-child(2) > app-rate-calculator-v2:nth-child(2) > div:nth-child(1) > div:nth-child(4) > div:nth-child(1)"));
       // Iterate over each service type
       for (WebElement serviceType : serviceTypes) {
           // Get the name of the service type
           String service = serviceType.getText().trim();

           // Print the service type and the count of quotations
           System.out.println("Service: " + service);
       }

    }

    // Teardown method to close the browser after the test
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
