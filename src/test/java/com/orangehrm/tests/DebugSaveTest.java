package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.MyInfoPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.util.List;

/**
 * Debug test to find correct Save button and Success message locators
 */
public class DebugSaveTest extends BaseTest {
    
    @Test
    public void debugSaveButton() throws InterruptedException {
        System.out.println("\n========== DEBUG SAVE BUTTON ==========");
        
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("orangehrm", "OrangeHRM@123");
        Thread.sleep(2000);
        
        MyInfoPage myInfoPage = new MyInfoPage(driver);
        myInfoPage.navigateToMyInfo();
        Thread.sleep(2000);
        
        // Update name
        myInfoPage.updateFirstName("TestName");
        Thread.sleep(1000);
        
        // Try to find Save button
        System.out.println("\n1. Looking for Save button...");
        String[] saveSelectors = {
            "button[type='submit']",
            "button.oxd-button",
            "//button[contains(@class, 'oxd-button')]",
            "//button[contains(text(), 'Save')]",
            "//button[@type='submit']"
        };
        
        for (String selector : saveSelectors) {
            try {
                By by = selector.startsWith("//") ? By.xpath(selector) : By.cssSelector(selector);
                List<WebElement> buttons = driver.findElements(by);
                System.out.println("  - Selector '" + selector + "': Found " + buttons.size() + " buttons");
                
                for (int i = 0; i < Math.min(buttons.size(), 3); i++) {
                    WebElement btn = buttons.get(i);
                    System.out.println("    * Button " + (i+1) + ":");
                    System.out.println("      - Text: " + btn.getText());
                    System.out.println("      - Type: " + btn.getAttribute("type"));
                    System.out.println("      - Class: " + btn.getAttribute("class"));
                    System.out.println("      - Is displayed: " + btn.isDisplayed());
                    System.out.println("      - Is enabled: " + btn.isEnabled());
                }
            } catch (Exception e) {
                System.out.println("  - Selector '" + selector + "': Error - " + e.getMessage());
            }
        }
        
        // Try clicking the first visible Save button
        System.out.println("\n2. Attempting to click Save button...");
        try {
            List<WebElement> saveButtons = driver.findElements(By.cssSelector("button[type='submit']"));
            if (!saveButtons.isEmpty()) {
                WebElement saveBtn = saveButtons.get(0);
                System.out.println("Clicking button with text: " + saveBtn.getText());
                saveBtn.click();
                Thread.sleep(3000);
                System.out.println("Button clicked successfully");
            }
        } catch (Exception e) {
            System.out.println("Click failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Try to find success message
        System.out.println("\n3. Looking for Success message...");
        String[] successSelectors = {
            "div.oxd-toast--success",
            "div[class*='toast'][class*='success']",
            "//div[contains(@class, 'oxd-toast--success')]",
            "//div[contains(@class, 'toast') and contains(@class, 'success')]",
            "//p[contains(@class, 'oxd-text') and contains(@class, 'toast-message')]"
        };
        
        for (String selector : successSelectors) {
            try {
                By by = selector.startsWith("//") ? By.xpath(selector) : By.cssSelector(selector);
                List<WebElement> messages = driver.findElements(by);
                System.out.println("  - Selector '" + selector + "': Found " + messages.size() + " messages");
                
                for (WebElement msg : messages) {
                    if (msg.isDisplayed()) {
                        System.out.println("    * Message text: " + msg.getText());
                        System.out.println("    * Message class: " + msg.getAttribute("class"));
                    }
                }
            } catch (Exception e) {
                System.out.println("  - Selector '" + selector + "': Error - " + e.getMessage());
            }
        }
        
        // Print all divs that might contain success message
        System.out.println("\n4. Looking for any toast/message divs...");
        try {
            List<WebElement> allDivs = driver.findElements(By.cssSelector("div[class*='toast'], div[class*='message'], div[class*='alert']"));
            System.out.println("Found " + allDivs.size() + " potential message containers");
            for (int i = 0; i < Math.min(allDivs.size(), 5); i++) {
                WebElement div = allDivs.get(i);
                if (div.isDisplayed()) {
                    System.out.println("  - Div " + (i+1) + ":");
                    System.out.println("    * Class: " + div.getAttribute("class"));
                    System.out.println("    * Text: " + div.getText());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n========== DEBUG SAVE BUTTON END ==========\n");
    }
}
