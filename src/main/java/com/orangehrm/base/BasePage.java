package com.orangehrm.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Base Page class containing common methods used across all page objects.
 * Follows Page Object Model (POM) design pattern.
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 2; // Reduced to 2s for maximum speed

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    /**
     * Wait for element to be clickable and click
     */
    protected void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        // Scroll vào view trước khi click - use center to avoid header overlay
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
            Thread.sleep(300); // Wait for scroll
            
            // Try JavaScript click if regular click fails for submit buttons
            if (locator.toString().contains("submit")) {
                try {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    return;
                } catch (Exception e) {
                    // Fallback to regular click
                }
            }
        } catch (Exception ignored) {}
        element.click();
    }

    /**
     * Wait for element to be visible and send keys
     */
    protected void sendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from an element
     */
    protected String getText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }

    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Select dropdown by visible text
     */
    protected void selectDropdownByVisibleText(By locator, String visibleText) {
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Select select = new Select(dropdown);
        select.selectByVisibleText(visibleText);
    }

    /**
     * Select dropdown by value
     */
    protected void selectDropdownByValue(By locator, String value) {
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        Select select = new Select(dropdown);
        select.selectByValue(value);
    }

    /**
     * Select OrangeHRM custom dropdown (không phải <select> tag)
     * OrangeHRM sử dụng div.oxd-select-text, click để mở dropdown rồi chọn option
     * 
     * @param dropdownLocator Locator của div.oxd-select-text
     * @param optionText Text của option cần chọn (ví dụ: "Vietnamese", "Single")
     */
    protected void selectOrangeHRMDropdown(By dropdownLocator, String optionText) {
        // Use longer wait for dropdowns - 5 seconds
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        // Click vào dropdown để mở
        WebElement dropdown = longWait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));
        dropdown.click();
        
        // Chờ dropdown options xuất hiện và chọn option theo text
        // OrangeHRM dropdown options có class "oxd-select-option"
        By optionLocator = By.xpath("//div[contains(@class,'oxd-select-option') and contains(text(),'" + optionText + "')]");
        WebElement option = longWait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
    }

    /**
     * Clear input field
     */
    protected void clearField(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
    }

    /**
     * Wait for element to be present
     */
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Get attribute value from element
     */
    protected String getAttribute(By locator, String attribute) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getAttribute(attribute);
    }

    /**
     * Check if element is enabled
     */
    protected boolean isElementEnabled(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for page title to contain specific text
     */
    protected void waitForPageTitle(String title) {
        wait.until(ExpectedConditions.titleContains(title));
    }
}
