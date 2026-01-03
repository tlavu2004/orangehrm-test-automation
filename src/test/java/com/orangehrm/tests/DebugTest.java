package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.MyInfoPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Debug test to inspect OrangeHRM UI and find correct locators
 */
public class DebugTest extends BaseTest {
    
    @Test(priority = 1)
    public void debugLoginAndMyInfo() {
        System.out.println("\n========== DEBUG TEST START ==========");
        
        // Step 1: Debug Login
        System.out.println("\n1. Testing Login Page...");
        LoginPage loginPage = new LoginPage(driver);
        
        // Print page source to understand structure
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());
        
        // Try to find username field
        System.out.println("\n2. Looking for username field...");
        String[] usernameSelectors = {
            "input[name='username']",
            "input[placeholder*='username' i]",
            "input[type='text']",
            "#txtUsername"
        };
        
        for (String selector : usernameSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                System.out.println("  - Selector '" + selector + "': Found " + elements.size() + " elements");
                if (!elements.isEmpty()) {
                    WebElement el = elements.get(0);
                    System.out.println("    * Tag: " + el.getTagName());
                    System.out.println("    * Name: " + el.getAttribute("name"));
                    System.out.println("    * Class: " + el.getAttribute("class"));
                    System.out.println("    * Placeholder: " + el.getAttribute("placeholder"));
                }
            } catch (Exception e) {
                System.out.println("  - Selector '" + selector + "': Error - " + e.getMessage());
            }
        }
        
        // Try to login
        System.out.println("\n3. Attempting to login...");
        try {
            loginPage.login("orangehrm", "OrangeHRM@123");
            Thread.sleep(3000); // Wait to see result
            System.out.println("Login URL after: " + driver.getCurrentUrl());
            System.out.println("Login successful: " + loginPage.isLoginSuccessful());
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
        
        // Step 2: Debug My Info Page
        if (loginPage.isLoginSuccessful()) {
            System.out.println("\n4. Testing My Info Page...");
            MyInfoPage myInfoPage = new MyInfoPage(driver);
            
            try {
                myInfoPage.navigateToMyInfo();
                Thread.sleep(2000);
                System.out.println("My Info URL: " + driver.getCurrentUrl());
                
                // Try to find first name field
                System.out.println("\n5. Looking for First Name field...");
                String[] nameSelectors = {
                    "input[name='firstName']",
                    "input[placeholder*='first' i]",
                    "//input[@name='firstName']"
                };
                
                for (String selector : nameSelectors) {
                    try {
                        By by = selector.startsWith("//") ? By.xpath(selector) : By.cssSelector(selector);
                        List<WebElement> elements = driver.findElements(by);
                        System.out.println("  - Selector '" + selector + "': Found " + elements.size() + " elements");
                        if (!elements.isEmpty()) {
                            WebElement el = elements.get(0);
                            System.out.println("    * Current value: " + el.getAttribute("value"));
                            System.out.println("    * Is displayed: " + el.isDisplayed());
                            System.out.println("    * Is enabled: " + el.isEnabled());
                        }
                    } catch (Exception e) {
                        System.out.println("  - Selector '" + selector + "': Error - " + e.getMessage());
                    }
                }
                
                // Try to update name
                System.out.println("\n6. Attempting to update name...");
                try {
                    myInfoPage.updateFirstName("Test");
                    myInfoPage.updateLastName("User");
                    myInfoPage.clickSave();
                    Thread.sleep(2000);
                    System.out.println("Save successful: " + myInfoPage.isSuccessMessageDisplayed());
                } catch (Exception e) {
                    System.out.println("Update failed: " + e.getMessage());
                    e.printStackTrace();
                }
                
            } catch (Exception e) {
                System.out.println("My Info navigation failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("\n========== DEBUG TEST END ==========\n");
    }
}
