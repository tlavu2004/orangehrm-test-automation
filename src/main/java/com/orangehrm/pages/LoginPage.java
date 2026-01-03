package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for OrangeHRM Login Page.
 * Handles login functionality.
 */
public class LoginPage extends BasePage {
    
        // Candidate locators for the login form (try each until one is found)
        private final By[] USERNAME_CANDIDATES = new By[] {
            By.id("txtUsername"),
            By.id("username"),
            By.name("username"),
            By.cssSelector("input[type='text'][name='username']"),
            By.cssSelector("input[type='text']")
        };

        private final By[] PASSWORD_CANDIDATES = new By[] {
            By.id("txtPassword"),
            By.id("password"),
            By.name("password"),
            By.cssSelector("input[type='password']")
        };

        private final By[] LOGIN_BUTTON_CANDIDATES = new By[] {
            By.id("btnLogin"),
            By.cssSelector("button[type='submit']"),
            By.xpath("//button[contains(., 'Login') or contains(., 'Log In')]")
        };

        private final By[] ERROR_CANDIDATES = new By[] {
            By.cssSelector("div.alert, div.message, div.error"),
            By.xpath("//p[contains(@class,'error') or contains(@class,'message')]")
        };
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Find first candidate locator that exists and is displayed (no explicit wait)
    private By findFirstVisible(By[] candidates) {
        for (By locator : candidates) {
            try {
                java.util.List<org.openqa.selenium.WebElement> elems = driver.findElements(locator);
                if (elems != null && !elems.isEmpty()) {
                    for (org.openqa.selenium.WebElement e : elems) {
                        try {
                            if (e.isDisplayed()) {
                                return locator;
                            }
                        } catch (Exception ignored) {}
                    }
                }
            } catch (Exception ignored) {}
        }
        // fallback - return first candidate so BasePage's waits will throw clear error
        return candidates[0];
    }

    /**
     * Perform login with username and password
     * 
     * @param username Employee username
     * @param password Employee password
     */
    public void login(String username, String password) {
        By user = findFirstVisible(USERNAME_CANDIDATES);
        By pass = findFirstVisible(PASSWORD_CANDIDATES);
        By btn = findFirstVisible(LOGIN_BUTTON_CANDIDATES);

        sendKeys(user, username);
        sendKeys(pass, password);
        clickElement(btn);
    }

    /**
     * Check if login was successful by verifying URL change or dashboard element
     * 
     * @return true if login successful
     */
    public boolean isLoginSuccessful() {
        try {
            // Wait for URL to change (max 3 seconds)
            org.openqa.selenium.support.ui.WebDriverWait shortWait = 
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3));
            shortWait.until(driver -> 
                driver.getCurrentUrl().contains("dashboard") || 
                driver.getCurrentUrl().contains("index") ||
                driver.getCurrentUrl().contains("viewMyDetails") ||
                driver.getCurrentUrl().contains("pim")
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error message text if login fails
     * 
     * @return Error message text
     */
    public String getErrorMessage() {
        By err = findFirstVisible(ERROR_CANDIDATES);
        return getText(err);
    }

    /**
     * Check if error message is displayed
     * 
     * @return true if error message is visible
     */
    public boolean isErrorMessageDisplayed() {
        By err = findFirstVisible(ERROR_CANDIDATES);
        return isElementDisplayed(err);
    }
}
