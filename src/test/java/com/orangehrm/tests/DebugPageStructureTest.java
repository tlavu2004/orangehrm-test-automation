package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Debug HTML structure của Personal Details page
 */
public class DebugPageStructureTest extends BaseTest {

    @Test
    public void testPageStructure() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  DEBUG PAGE STRUCTURE");
        System.out.println("══════════════════════════════════════════\n");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("orangehrm", "OrangeHRM@123");
        System.out.println("✅ Login method called");
        
        try {
            Thread.sleep(5000); // Wait for login redirect
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Login successful: " + loginPage.isLoginSuccessful());

        // Navigate trực tiếp đến My Info Personal Details
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after login: " + currentUrl);
        
        String baseUrl = currentUrl.substring(0, currentUrl.indexOf("/web/") + 5);
        String targetUrl = baseUrl + "index.php/pim/viewMyDetails";
        System.out.println("Target URL: " + targetUrl);
        
        driver.get(targetUrl);
        System.out.println("✅ Navigated to: " + driver.getCurrentUrl());

        try {
            Thread.sleep(5000); // Wait 5 seconds for page load
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Page title: " + driver.getTitle());

        // Check các labels có chữ "Gender" hoặc "Date"
        System.out.println("\n📍 Tìm tất cả labels chứa 'Gender':");
        List<WebElement> genderLabels = driver.findElements(By.xpath("//label[contains(text(),'Gender') or contains(text(),'gender')]"));
        System.out.println("Found " + genderLabels.size() + " labels");
        for (WebElement label : genderLabels) {
            System.out.println("  - Text: " + label.getText());
            System.out.println("    HTML: " + label.getAttribute("outerHTML"));
        }

        System.out.println("\n📍 Tìm tất cả labels chứa 'Date of Birth':");
        List<WebElement> dobLabels = driver.findElements(By.xpath("//label[contains(text(),'Date of Birth') or contains(text(),'Birth')]"));
        System.out.println("Found " + dobLabels.size() + " labels");
        for (WebElement label : dobLabels) {
            System.out.println("  - Text: " + label.getText());
            System.out.println("    HTML: " + label.getAttribute("outerHTML"));
        }

        System.out.println("\n📍 Tìm tất cả input type='radio':");
        List<WebElement> radios = driver.findElements(By.xpath("//input[@type='radio']"));
        System.out.println("Found " + radios.size() + " radios");
        for (WebElement radio : radios) {
            System.out.println("  - Value: " + radio.getAttribute("value"));
            System.out.println("    Name: " + radio.getAttribute("name"));
            System.out.println("    Visible: " + radio.isDisplayed());
            System.out.println("    HTML: " + radio.getAttribute("outerHTML"));
        }

        System.out.println("\n📍 Tìm tất cả input placeholder='yyyy-mm-dd':");
        List<WebElement> datePickers = driver.findElements(By.cssSelector("input[placeholder='yyyy-mm-dd']"));
        System.out.println("Found " + datePickers.size() + " date pickers");
        for (WebElement dp : datePickers) {
            System.out.println("  - Value: " + dp.getAttribute("value"));
            System.out.println("    Name: " + dp.getAttribute("name"));
            System.out.println("    Visible: " + dp.isDisplayed());
        }

        // In ra page source (chỉ phần có "Gender" hoặc "Date")
        String pageSource = driver.getPageSource();
        String[] lines = pageSource.split("\n");
        System.out.println("\n📍 HTML lines containing 'Gender' or 'Date of Birth':");
        for (String line : lines) {
            if (line.contains("Gender") || line.contains("Date of Birth") || line.contains("radio")) {
                System.out.println(line.trim());
            }
        }

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  DEBUG COMPLETED");
        System.out.println("══════════════════════════════════════════\n");
    }
}
