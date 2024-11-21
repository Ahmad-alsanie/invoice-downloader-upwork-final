package com.carls.service;

import com.carls.config.UpworkCredentials;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpworkAutomationService {

    @Autowired
    private UpworkCredentials credentials;

    private WebDriver driver;

    // Setup WebDriver with options
    public void setupWebDriver() {
        ChromeOptions options = new ChromeOptions();

        // Set realistic browser behaviors
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");

        // Set download directory preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", System.getProperty("user.dir"));
        prefs.put("download.prompt_for_download", false);
        prefs.put("plugins.always_open_pdf_externally", true);
        options.setExperimentalOption("prefs", prefs);

        // Initialize WebDriver
        driver = new ChromeDriver(options);

        // Disable WebDriver detection using JavaScript
        ((JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined});");
    }

    // Login to Upwork
    public void loginToUpwork() {
        try {
            setupWebDriver();

            // Navigate to the login page
            driver.get("https://www.upwork.com/ab/account-security/login");

            WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(30));

            // Wait for email input field to become available
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login_username")));

            // Fill in email
            emailField.sendKeys(credentials.getEmail());
            emailField.submit();

            // Wait for Cloudflare human check to complete (can be refined or solved with CAPTCHA-solving API if needed)
            Thread.sleep(15000); // This is an arbitrary wait; replace with proper handling.

            // Wait for password input field
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login_password")));

            // Fill in password
            passwordField.sendKeys(credentials.getPassword());
            passwordField.submit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigate to the transaction history page and download invoices
    public void navigateAndDownloadInvoices() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Navigate to transaction history page
            driver.get("https://www.upwork.com/nx/payments/reports/transaction-history/");

            // Wait for the download invoices button to appear
            WebElement downloadButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@data-qa='download-invoices']")
            ));

            // Click the download button
            downloadButton.click();

            // Wait for download to complete (adjust this based on download speeds)
            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Quit WebDriver
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
