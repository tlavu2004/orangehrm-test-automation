package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.MyInfoPage;
import org.testng.annotations.Test;

public class SimpleTC001Test extends BaseTest {

    @Test
    public void testTC001Only() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  TEST TC001 ONLY");
        System.out.println("══════════════════════════════════════════\n");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("orangehrm", "OrangeHRM@123");
        System.out.println("✅ Login");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Navigate
        MyInfoPage myInfoPage = new MyInfoPage(driver);
        myInfoPage.navigateToMyInfo();
        myInfoPage.navigateToPersonalDetails();
        System.out.println("✅ Navigate to My Info");

        try {
            Thread.sleep(5000); // Wait longer for page load
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Current URL: " + driver.getCurrentUrl());
        
        // Wait for firstName field to be visible (means page loaded)
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
                org.openqa.selenium.By.cssSelector("input[name='firstName']")));
            System.out.println("✅ Page fully loaded (firstName visible)");
        } catch (Exception e) {
            System.out.println("❌ Page not loaded properly: " + e.getMessage());
        }

        // Update name - SỬ DỤNG GIÁ TRỊ KHÁC với data hiện tại
        System.out.println("\n📍 Updating name with UNIQUE values...");
        long timestamp = System.currentTimeMillis();
        String firstName = "Test" + (timestamp % 1000);
        String middleName = "M" + (timestamp % 100);
        String lastName = "User" + (timestamp % 1000);
        
        System.out.println("Will update to: " + firstName + " " + middleName + " " + lastName);
        myInfoPage.updateName(firstName, middleName, lastName);
        System.out.println("✅ Name updated");

        // Wait a bit
        try {   
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check button state BEFORE clicking
        System.out.println("\n📍 Checking Save button state...");
        try {
            // Try multiple selectors
            org.openqa.selenium.WebElement saveBtn = null;
            String usedSelector = "";
            
            try {
                saveBtn = driver.findElement(org.openqa.selenium.By.cssSelector("button[type='submit']"));
                usedSelector = "button[type='submit']";
            } catch (Exception e1) {
                try {
                    saveBtn = driver.findElement(org.openqa.selenium.By.xpath("//button[contains(@class,'oxd-button--secondary')]"));
                    usedSelector = "button with class oxd-button--secondary";
                } catch (Exception e2) {
                    saveBtn = driver.findElement(org.openqa.selenium.By.xpath("//button[text()='Save']"));
                    usedSelector = "button with text 'Save'";
                }
            }
            
            System.out.println("Save button found with: " + usedSelector);
            System.out.println("Save button enabled: " + saveBtn.isEnabled());
            System.out.println("Save button displayed: " + saveBtn.isDisplayed());
            System.out.println("Save button text: " + saveBtn.getText());
            System.out.println("Save button class: " + saveBtn.getAttribute("class"));
        } catch (Exception e) {
            System.out.println("❌ Save button not found: " + e.getMessage());
        }

        // Click save
        System.out.println("\n📍 Clicking Save...");
        try {
            // Take screenshot BEFORE clicking
            try {
                java.io.File screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                java.nio.file.Files.copy(screenshot.toPath(), new java.io.File("screenshot-before-save.png").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("📸 Screenshot saved: screenshot-before-save.png");
            } catch (Exception e) {
                System.out.println("Failed to take screenshot: " + e.getMessage());
            }
            
            myInfoPage.clickSave();
            System.out.println("✅ Save clicked");
            
            // Wait a bit for API
            Thread.sleep(3000);
            
            // Take screenshot AFTER clicking
            try {
                java.io.File screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                java.nio.file.Files.copy(screenshot.toPath(), new java.io.File("screenshot-after-save.png").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("📸 Screenshot saved: screenshot-after-save.png");
            } catch (Exception e) {
                System.out.println("Failed to take screenshot: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("❌ Failed to click save: " + e.getMessage());
        }

        // Check NGAY LẬP TỨC cho success message (không wait 7s)
        System.out.println("\n📍 Checking success message IMMEDIATELY...");
        
        // Try với JavaScript để check trực tiếp
        try {
            Thread.sleep(2000); // Wait 2s cho API response
            
            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            Object toastCount = js.executeScript(
                "return document.querySelectorAll('div.oxd-toast--success, div.oxd-toast, div[class*=toast]').length;");
            System.out.println("Toast elements on page (by JS): " + toastCount);
            
            Object toastVisible = js.executeScript(
                "var toasts = document.querySelectorAll('div.oxd-toast--success, div.oxd-toast, div[class*=toast]');" +
                "for(var i=0; i<toasts.length; i++) {" +
                "  if(toasts[i].offsetParent !== null) return toasts[i].textContent;" +
                "}" +
                "return null;");
            System.out.println("Visible toast text (by JS): " + toastVisible);
            
        } catch (Exception e) {
            System.out.println("Error checking with JS: " + e.getMessage());
        }
        
        boolean success = myInfoPage.isSuccessMessageDisplayed();
        System.out.println("Success message displayed (by Selenium): " + success);

        // Nếu không có success message, hãy verify bằng cách reload page và check data
        if (!success) {
            System.out.println("\n📍 Verifying by checking data after page reload...");
            driver.navigate().refresh();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            String actualFirstName = myInfoPage.getFirstName();
            String actualMiddleName = myInfoPage.getMiddleName();
            String actualLastName = myInfoPage.getLastName();
            
            System.out.println("Expected: " + firstName + " " + middleName + " " + lastName);
            System.out.println("Actual:   " + actualFirstName + " " + actualMiddleName + " " + actualLastName);
            
            boolean dataChanged = actualFirstName.equals(firstName) && 
                                actualMiddleName.equals(middleName) &&
                                actualLastName.equals(lastName);
            System.out.println("Data actually changed: " + dataChanged);
            
            if (dataChanged) {
                System.out.println("✅ DATA VERIFIED - Save worked even without success message!");
            } else {
                System.out.println("❌ DATA NOT CHANGED - Save did NOT work!");
            }
        }

        if (success) {
            String message = myInfoPage.getSuccessMessage();
            System.out.println("Success message text: " + message);
        } else {
            System.out.println("❌ NO SUCCESS MESSAGE FOUND!");
            
            // Search for ANY toast/message elements
            System.out.println("\n📍 Searching for toast elements...");
            try {
                java.util.List<org.openqa.selenium.WebElement> toasts = driver.findElements(
                    org.openqa.selenium.By.cssSelector("div[class*='toast'], div[class*='message'], div[class*='alert']"));
                System.out.println("Found " + toasts.size() + " toast/message elements");
                for (org.openqa.selenium.WebElement toast : toasts) {
                    if (toast.isDisplayed()) {
                        System.out.println("  - Visible toast class: " + toast.getAttribute("class"));
                        System.out.println("    Text: " + toast.getText());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error searching toasts: " + e.getMessage());
            }
            
            // Check if form was actually submitted by checking URL or page state
            System.out.println("\n📍 Current URL after save: " + driver.getCurrentUrl());
        }

        System.out.println("\n══════════════════════════════════════════");
    }
}
