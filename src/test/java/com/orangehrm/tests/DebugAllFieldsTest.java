package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.MyInfoPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.util.List;

/**
 * Debug test để kiểm tra TẤT CẢ các fields trong My Info page
 */
public class DebugAllFieldsTest extends BaseTest {
    
    @Test
    public void debugAllMyInfoFields() throws InterruptedException {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║  DEBUG TẤT CẢ CÁC FIELDS TRONG MY INFO PAGE       ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");
        
        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("orangehrm", "OrangeHRM@123");
        Thread.sleep(2000);
        
        // Navigate to My Info
        MyInfoPage myInfoPage = new MyInfoPage(driver);
        myInfoPage.navigateToMyInfo();
        Thread.sleep(2000);
        
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("\n");
        
        // ============= 1. FIRST NAME =============
        System.out.println("【1】 FIRST NAME FIELD:");
        testField("First Name", new String[]{
            "input[name='firstName']",
            "input.oxd-input:nth-of-type(1)"
        });
        
        // ============= 2. MIDDLE NAME =============
        System.out.println("\n【2】 MIDDLE NAME FIELD:");
        testField("Middle Name", new String[]{
            "input[name='middleName']",
            "//input[@name='middleName']"
        });
        
        // ============= 3. LAST NAME =============
        System.out.println("\n【3】 LAST NAME FIELD:");
        testField("Last Name", new String[]{
            "input[name='lastName']",
            "input.oxd-input:nth-of-type(3)"
        });
        
        // ============= 4. EMPLOYEE ID =============
        System.out.println("\n【4】 EMPLOYEE ID FIELD:");
        testField("Employee ID", new String[]{
            "input[name='employeeId']",
            "//label[text()='Employee Id']/following::input[1]",
            "//div[contains(@class,'oxd-input-group')]//input[@class='oxd-input oxd-input--active']"
        });
        
        // ============= 5. OTHER ID =============
        System.out.println("\n【5】 OTHER ID FIELD:");
        testField("Other ID", new String[]{
            "input[name='otherId']",
            "//label[text()='Other Id']/following::input[1]"
        });
        
        // ============= 6. DRIVER LICENSE =============
        System.out.println("\n【6】 DRIVER LICENSE NUMBER:");
        testField("Driver License", new String[]{
            "input[name='licenseNo']",
            "//label[text()=\"Driver's License Number\"]/following::input[1]",
            "//label[contains(text(),'License')]/following::input[1]"
        });
        
        // ============= 7. LICENSE EXPIRY DATE =============
        System.out.println("\n【7】 LICENSE EXPIRY DATE:");
        testField("License Expiry", new String[]{
            "input[name='licenseExpiry']",
            "input[placeholder='yyyy-mm-dd']",
            "//label[text()='License Expiry Date']/following::input[1]"
        });
        
        // ============= 8. NATIONALITY DROPDOWN =============
        System.out.println("\n【8】 NATIONALITY DROPDOWN:");
        testDropdown("Nationality", new String[]{
            "//label[text()='Nationality']/following::div[contains(@class,'oxd-select-text')][1]",
            "div.oxd-select-text",
            "//div[contains(@class,'oxd-select-wrapper')]//div[@class='oxd-select-text-input']"
        });
        
        // ============= 9. MARITAL STATUS DROPDOWN =============
        System.out.println("\n【9】 MARITAL STATUS DROPDOWN:");
        testDropdown("Marital Status", new String[]{
            "//label[text()='Marital Status']/following::div[contains(@class,'oxd-select-text')][1]",
            "(//div[contains(@class,'oxd-select-text')])[2]"
        });
        
        // ============= 10. DATE OF BIRTH =============
        System.out.println("\n【10】 DATE OF BIRTH FIELD:");
        testField("Date of Birth", new String[]{
            "//label[text()='Date of Birth']/following::input[1]",
            "(//input[contains(@class,'oxd-input') and @placeholder='yyyy-mm-dd'])[1]",
            "input[placeholder='yyyy-mm-dd']"
        });
        
        // ============= 11. GENDER RADIO BUTTONS =============
        System.out.println("\n【11】 GENDER RADIO BUTTONS:");
        System.out.println("Looking for MALE radio...");
        testRadioButton("Male", new String[]{
            "//label[text()='Male']/input",
            "//label[text()='Male']",
            "//input[@type='radio' and @value='1']",
            "//span[text()='Male']/preceding::input[@type='radio'][1]"
        });
        
        System.out.println("\nLooking for FEMALE radio...");
        testRadioButton("Female", new String[]{
            "//label[text()='Female']/input",
            "//label[text()='Female']",
            "//input[@type='radio' and @value='2']",
            "//span[text()='Female']/preceding::input[@type='radio'][1]"
        });
        
        // ============= 12. SAVE BUTTON =============
        System.out.println("\n【12】 SAVE BUTTON:");
        testButton("Save", new String[]{
            "button[type='submit']",
            "//button[@type='submit' and contains(@class,'oxd-button')]",
            "//button[contains(text(),'Save')]"
        });
        
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║  DEBUG HOÀN TẤT - XEM KẾT QUẢ BÊN TRÊN           ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");
    }
    
    private void testField(String fieldName, String[] selectors) {
        for (String selector : selectors) {
            try {
                By by = selector.startsWith("//") ? By.xpath(selector) : By.cssSelector(selector);
                List<WebElement> elements = driver.findElements(by);
                
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    WebElement el = elements.get(0);
                    System.out.println("  ✅ FOUND: " + selector);
                    System.out.println("     - Value: " + el.getAttribute("value"));
                    System.out.println("     - Enabled: " + el.isEnabled());
                    System.out.println("     - Class: " + el.getAttribute("class"));
                    return; // Found it!
                } else {
                    System.out.println("  ❌ NOT VISIBLE: " + selector);
                }
            } catch (Exception e) {
                System.out.println("  ❌ ERROR: " + selector + " - " + e.getMessage());
            }
        }
        System.out.println("  ⚠️  NO WORKING SELECTOR FOUND FOR " + fieldName);
    }
    
    private void testDropdown(String dropdownName, String[] selectors) {
        for (String selector : selectors) {
            try {
                By by = selector.startsWith("//") ? By.xpath(selector) : By.cssSelector(selector);
                List<WebElement> elements = driver.findElements(by);
                
                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    WebElement el = elements.get(0);
                    System.out.println("  ✅ FOUND: " + selector);
                    System.out.println("     - Text: " + el.getText());
                    System.out.println("     - Class: " + el.getAttribute("class"));
                    System.out.println("     - Tag: " + el.getTagName());
                    return; // Found it!
                } else {
                    System.out.println("  ❌ NOT VISIBLE: " + selector);
                }
            } catch (Exception e) {
                System.out.println("  ❌ ERROR: " + selector + " - " + e.getMessage());
            }
        }
        System.out.println("  ⚠️  NO WORKING SELECTOR FOUND FOR " + dropdownName);
    }
    
    private void testRadioButton(String radioName, String[] selectors) {
        for (String selector : selectors) {
            try {
                By by = selector.startsWith("//") ? By.xpath(selector) : By.cssSelector(selector);
                List<WebElement> elements = driver.findElements(by);
                
                if (!elements.isEmpty()) {
                    WebElement el = elements.get(0);
                    System.out.println("  ✅ FOUND: " + selector);
                    System.out.println("     - Type: " + el.getTagName());
                    System.out.println("     - Value: " + el.getAttribute("value"));
                    System.out.println("     - Checked: " + el.isSelected());
                    return; // Found it!
                } else {
                    System.out.println("  ❌ NOT FOUND: " + selector);
                }
            } catch (Exception e) {
                System.out.println("  ❌ ERROR: " + selector + " - " + e.getMessage());
            }
        }
        System.out.println("  ⚠️  NO WORKING SELECTOR FOUND FOR " + radioName);
    }
    
    private void testButton(String buttonName, String[] selectors) {
        for (String selector : selectors) {
            try {
                By by = selector.startsWith("//") ? By.xpath(selector) : By.cssSelector(selector);
                List<WebElement> buttons = driver.findElements(by);
                
                if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
                    WebElement btn = buttons.get(0);
                    System.out.println("  ✅ FOUND: " + selector);
                    System.out.println("     - Text: " + btn.getText());
                    System.out.println("     - Enabled: " + btn.isEnabled());
                    return; // Found it!
                } else {
                    System.out.println("  ❌ NOT VISIBLE: " + selector);
                }
            } catch (Exception e) {
                System.out.println("  ❌ ERROR: " + selector + " - " + e.getMessage());
            }
        }
        System.out.println("  ⚠️  NO WORKING SELECTOR FOUND FOR " + buttonName);
    }
}
