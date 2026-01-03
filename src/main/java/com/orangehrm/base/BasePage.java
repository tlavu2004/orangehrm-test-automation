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
    private static final int DEFAULT_TIMEOUT = 5; // Increased to 5s for more stable waits

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    /**
     * Wait for element to be clickable and click
     */
    protected void clickElement(By locator) {
        // Wait for any loaders/overlays to disappear before interacting
        waitForLoaderToDisappear();
        waitForOverlaysToDisappear();

        int attempts = 0;
        while (attempts < 3) {
            attempts++;
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

                // Scroll into view
                try {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
                    Thread.sleep(250);
                } catch (Exception ignored) {}

                // Try regular click
                try {
                    element.click();
                    return;
                } catch (org.openqa.selenium.ElementClickInterceptedException intercepted) {
                    // Try Actions click
                    try {
                        org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
                        actions.moveToElement(element).pause(java.time.Duration.ofMillis(150)).click().perform();
                        return;
                    } catch (Exception e) {
                        // Try JS click as last resort
                        try {
                            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                            return;
                        } catch (Exception jsEx) {
                            if (attempts >= 3) {
                                throw intercepted;
                            }
                        }
                    }
                }
            } catch (org.openqa.selenium.StaleElementReferenceException | org.openqa.selenium.NoSuchElementException e) {
                if (attempts >= 3) throw e;
            } catch (org.openqa.selenium.TimeoutException te) {
                // element not clickable - re-wait for overlays and retry
                waitForLoaderToDisappear();
                waitForOverlaysToDisappear();
                if (attempts >= 3) throw te;
            }
        }
    }

    /**
     * Wait for OrangeHRM form loader/overlay to disappear if present.
     */
    private void waitForLoaderToDisappear() {
        try {
            By loader = By.cssSelector("div.oxd-form-loader");
            WebDriverWait longWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            longWait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
        } catch (Exception ignored) {
            // ignore timeout or absence of loader
        }
    }

    /**
     * Wait for common overlays (modals, loading masks) to disappear to avoid click interception.
     */
    private void waitForOverlaysToDisappear() {
        try {
            By overlays = By.cssSelector(".oxd-overlay, .oxd-loading, .modal-backdrop, div[role='presentation']");
            WebDriverWait longWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(8));
            longWait.until(ExpectedConditions.invisibilityOfElementLocated(overlays));
        } catch (Exception ignored) {
            // ignore if overlays not present
        }
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
