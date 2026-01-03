package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for OrangeHRM My Info / Personal Details Page.
 * Handles UC01 test cases - Employee personal information management.
 */
public class MyInfoPage extends BasePage {
    
    // Candidate navigation locators - OrangeHRM modern UI
    private final By[] MY_INFO_MENU_CANDIDATES = new By[] {
        By.xpath("//span[text()='My Info']/parent::a"),
        By.xpath("//a[contains(@href,'viewMyDetails')]"),
        By.id("menu_pim_viewMyDetails"),
        By.linkText("My Info"),
        By.xpath("//a[contains(text(),'My Info')]"),
        By.cssSelector("a[href*='viewMyDetails']")
    };
    
    private final By[] PERSONAL_DETAILS_TAB_CANDIDATES = new By[] {
        By.linkText("Personal Details"),
        By.xpath("//a[contains(text(),'Personal Details')]"),
        By.cssSelector("a[href*='personalDetails']"),
        By.xpath("//span[text()='Personal Details']")
    };
    
    // Personal Details Section - Form Field Candidates (optimized - fewer candidates)
    private final By[] FIRST_NAME_CANDIDATES = new By[] {
        By.name("firstName"),
        By.cssSelector("input[name='firstName']"),
        By.xpath("//input[@name='firstName']")
    };
    
    private final By[] MIDDLE_NAME_CANDIDATES = new By[] {
        By.id("middleName"),
        By.name("middleName"),
        By.xpath("//input[@name='middleName']"),
        By.cssSelector("input[name='middleName']")
    };
    
    private final By[] LAST_NAME_CANDIDATES = new By[] {
        By.name("lastName"),
        By.cssSelector("input[name='lastName']"),
        By.xpath("//input[@name='lastName']")
    };
    
    private final By[] EMPLOYEE_ID_CANDIDATES = new By[] {
        By.xpath("//label[text()='Employee Id']/following::input[1]"),
        By.name("employeeId"),
        By.cssSelector("input[name='employeeId']")
    };
    
    private final By[] OTHER_ID_CANDIDATES = new By[] {
        By.xpath("//label[text()='Other Id']/following::input[1]"),
        By.id("otherId"),
        By.name("otherId"),
        By.xpath("//input[@name='otherId']"),
        By.cssSelector("input[name='otherId']")
    };
    
    // Driver's License Section
    private final By[] LICENSE_NUMBER_CANDIDATES = new By[] {
        By.xpath("//label[text()=\"Driver's License Number\"]/following::input[1]"),
        By.id("personal_licenNo"),
        By.name("licenseNo"),
        By.xpath("//input[@name='licenseNo']"),
        By.cssSelector("input[name='licenseNo']"),
        By.xpath("//input[contains(@id,'licen')]")
    };
    
    private final By[] LICENSE_EXPIRY_CANDIDATES = new By[] {
        By.cssSelector("input[placeholder='yyyy-mm-dd']"),
        By.id("personal_licExpDate"),
        By.name("licenseExpiry"),
        By.xpath("//input[@name='licenseExpiry']"),
        By.xpath("//input[contains(@id,'licExpDate')]"),
        By.xpath("//label[contains(text(),'Expiry')]/following::input[1]")
    };
    
    // Dropdowns - OrangeHRM sử dụng custom dropdown (không phải <select>)
    private final By[] NATIONALITY_DROPDOWN_CANDIDATES = new By[] {
        By.xpath("//label[text()='Nationality']/following::div[contains(@class,'oxd-select-text')][1]"),
        By.id("personal_cmbNation"),
        By.name("nationality"),
        By.xpath("//select[@name='nationality']"),
        By.cssSelector("select[name='nationality']")
    };
    
    private final By[] MARITAL_STATUS_CANDIDATES = new By[] {
        By.xpath("//label[text()='Marital Status']/following::div[contains(@class,'oxd-select-text')][1]"),
        By.id("personal_cmbMarital"),
        By.name("maritalStatus"),
        By.xpath("//select[@name='maritalStatus']"),
        By.cssSelector("select[name='maritalStatus']")
    };
    
    // Date of Birth
    private final By[] DOB_FIELD_CANDIDATES = new By[] {
        By.xpath("//label[text()='Date of Birth']/following::input[1]"),
        By.id("personal_DOB"),
        By.name("dob"),
        By.name("dateOfBirth"),
        By.xpath("//input[@name='dob']"),
        By.cssSelector("input[name='dob']")
    };
    
// Gender Radio Buttons - Click vào LABEL thay vì input (input không clickable)
    private final By[] MALE_RADIO_CANDIDATES = new By[] {
        By.xpath("//label[text()='Male']"),
        By.xpath("//label[text()='Male']/input"),
        By.id("personal_optGender_1"),
        By.xpath("//input[@value='1' and @type='radio']"),
        By.xpath("//input[@type='radio' and @name='gender'][@value='1']")
    };

    private final By[] FEMALE_RADIO_CANDIDATES = new By[] {
        By.xpath("//label[text()='Female']"),
        By.xpath("//label[text()='Female']/input"),
        By.id("personal_optGender_2"),
        By.xpath("//input[@value='2' and @type='radio']"),
        By.xpath("//input[@type='radio' and @name='gender'][@value='2']")
    };
    
    private final By[] SAVE_BUTTON_CANDIDATES = new By[] {
        By.id("btnSave"),
        By.xpath("//input[@id='btnSave']"),
        By.xpath("//input[@type='button' and @value='Save']"),
        By.cssSelector("input[type='button'][value='Save']"),
        By.xpath("//button[text()='Save']"),
        By.xpath("//button[contains(@class,'save')]"),
        By.xpath("(//button[@type='submit'])[1]")
    };
    
    // Buttons and Messages
    // Nút Save trong OrangeHRM thường là nút submit trong form
    private final By saveButton = By.cssSelector("button[type='submit']"); 

    // Thông báo màu xanh lá cây hiện ra ở góc phải
    private final By successMessage = By.cssSelector("div.oxd-toast--success");

    // Thông báo màu đỏ hiện ra ở góc phải
    private final By errorMessage = By.cssSelector("div.oxd-toast--error");

    // Dòng chữ đỏ "Required" xuất hiện ngay dưới ô input khi bỏ trống
    private final By requiredFieldError = By.xpath("//span[contains(@class, 'oxd-input-group__message') and text()='Required']");
    
    public MyInfoPage(WebDriver driver) {
        super(driver);
    }

    // Find first visible locator from candidates
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
        return candidates[0];
    }

    /**
     * Navigate to My Info section
     */
    public void navigateToMyInfo() {
        try {
            By menu = findFirstVisible(MY_INFO_MENU_CANDIDATES);
            clickElement(menu);
        } catch (Exception e) {
            // Nếu click menu thất bại, navigate trực tiếp bằng URL
            String currentUrl = driver.getCurrentUrl();
            String baseUrl = currentUrl.substring(0, currentUrl.indexOf("/web/") + 5);
            driver.get(baseUrl + "index.php/pim/viewMyDetails");
        }
    }

    /**
     * Navigate to Personal Details tab
     */
    public void navigateToPersonalDetails() {
        try {
            By tab = findFirstVisible(PERSONAL_DETAILS_TAB_CANDIDATES);
            clickElement(tab);
        } catch (Exception e) {
            // Personal Details là tab mặc định khi vào My Info
            // Nếu không tìm thấy tab, có thể đã ở trang này rồi
        }
    }

    /**
     * Update employee name (First, Middle, Last)
     * 
     * @param firstName Employee first name
     * @param middleName Employee middle name
     * @param lastName Employee last name
     */
    public void updateName(String firstName, String middleName, String lastName) {
        if (firstName != null && !firstName.isEmpty()) {
            By firstNameLoc = findFirstVisible(FIRST_NAME_CANDIDATES);
            sendKeys(firstNameLoc, firstName);
        }
        if (middleName != null && !middleName.isEmpty()) {
            By middleNameLoc = findFirstVisible(MIDDLE_NAME_CANDIDATES);
            sendKeys(middleNameLoc, middleName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            By lastNameLoc = findFirstVisible(LAST_NAME_CANDIDATES);
            sendKeys(lastNameLoc, lastName);
        }
    }

    /**
     * Update first name only
     */
    public void updateFirstName(String firstName) {
        By loc = findFirstVisible(FIRST_NAME_CANDIDATES);
        sendKeys(loc, firstName);
    }

    /**
     * Update last name only
     */
    public void updateLastName(String lastName) {
        By loc = findFirstVisible(LAST_NAME_CANDIDATES);
        sendKeys(loc, lastName);
    }

    /**
     * Clear first name field
     */
    public void clearFirstName() {
        By loc = findFirstVisible(FIRST_NAME_CANDIDATES);
        clearField(loc);
    }

    /**
     * Clear last name field
     */
    public void clearLastName() {
        By loc = findFirstVisible(LAST_NAME_CANDIDATES);
        clearField(loc);
    }

    /**
     * Update Other ID
     */
    public void updateOtherId(String otherId) {
        By loc = findFirstVisible(OTHER_ID_CANDIDATES);
        sendKeys(loc, otherId);
    }

    /**
     * Update Driver's License information
     * 
     * @param licenseNumber License number
     * @param expiryDate Expiry date in yyyy-MM-dd format
     */
    public void updateDriverLicense(String licenseNumber, String expiryDate) {
        if (licenseNumber != null && !licenseNumber.isEmpty()) {
            By licenseLoc = findFirstVisible(LICENSE_NUMBER_CANDIDATES);
            sendKeys(licenseLoc, licenseNumber);
        }
        if (expiryDate != null && !expiryDate.isEmpty()) {
            By expiryLoc = findFirstVisible(LICENSE_EXPIRY_CANDIDATES);
            sendKeys(expiryLoc, expiryDate);
        }
    }

    /**
     * Select nationality from dropdown
     * Handles cases where dropdown options might not be available
     * 
     * @param nationality Nationality value
     */
    public void selectNationality(String nationality) {
        try {
            By loc = findFirstVisible(NATIONALITY_DROPDOWN_CANDIDATES);
            selectOrangeHRMDropdown(loc, nationality);
        } catch (Exception e) {
            // Dropdown options not available - skip silently per Option B
            System.out.println("Warning: Could not select nationality '" + nationality + "' - options not available");
        }
    }

    /**
     * Select marital status from dropdown
     * Handles cases where dropdown options might not be available
     * 
     * @param maritalStatus Marital status (Single/Married/Other)
     */
    public void selectMaritalStatus(String maritalStatus) {
        try {
            By loc = findFirstVisible(MARITAL_STATUS_CANDIDATES);
            selectOrangeHRMDropdown(loc, maritalStatus);
        } catch (Exception e) {
            // Dropdown options not available - skip silently per Option B
            System.out.println("Warning: Could not select marital status '" + maritalStatus + "' - options not available");
        }
    }

    /**
     * Update date of birth
     * 
     * @param dateOfBirth Date in yyyy-MM-dd format
     */
    public void updateDateOfBirth(String dateOfBirth) {
        By loc = findFirstVisible(DOB_FIELD_CANDIDATES);
        sendKeys(loc, dateOfBirth);
    }

    /**
     * Select gender
     * 
     * @param gender "Male" or "Female"
     */
    public void selectGender(String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            By loc = findFirstVisible(MALE_RADIO_CANDIDATES);
            clickElement(loc);
        } else if (gender.equalsIgnoreCase("Female")) {
            By loc = findFirstVisible(FEMALE_RADIO_CANDIDATES);
            clickElement(loc);
        }
    }

    /**
     * Click Save button
     */
    public void clickSave() {
        By loc = findFirstVisible(SAVE_BUTTON_CANDIDATES);
        clickElement(loc);
    }

    /**
     * Check if success message is displayed
     * 
     * @return true if success message appears
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            // Wait up to 5 seconds for toast message to appear
            org.openqa.selenium.support.ui.WebDriverWait longWait = 
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5));
            longWait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(successMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get success message text
     * 
     * @return Success message text
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }

    /**
     * Check if error message is displayed
     * 
     * @return true if error message appears
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    /**
     * Get error message text
     * 
     * @return Error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Check if "Required" field error is displayed
     * 
     * @return true if required error appears
     */
    public boolean isRequiredErrorDisplayed() {
        return isElementDisplayed(requiredFieldError);
    }

    /**
     * Check if Employee ID field is read-only
     * 
     * @return true if field is disabled/read-only
     */
    public boolean isEmployeeIdReadOnly() {
        By loc = findFirstVisible(EMPLOYEE_ID_CANDIDATES);
        return !isElementEnabled(loc);
    }

    /**
     * Get Employee ID value
     * 
     * @return Employee ID
     */
    public String getEmployeeId() {
        By loc = findFirstVisible(EMPLOYEE_ID_CANDIDATES);
        return getAttribute(loc, "value");
    }

    /**
     * Get First Name value
     * 
     * @return First name
     */
    public String getFirstName() {
        By loc = findFirstVisible(FIRST_NAME_CANDIDATES);
        return getAttribute(loc, "value");
    }

    /**
     * Get Last Name value
     * 
     * @return Last name
     */
    public String getLastName() {
        By loc = findFirstVisible(LAST_NAME_CANDIDATES);
        return getAttribute(loc, "value");
    }
    
    /**
     * Get Middle Name value
     * 
     * @return Middle name
     */
    public String getMiddleName() {
        By loc = findFirstVisible(MIDDLE_NAME_CANDIDATES);
        return getAttribute(loc, "value");
    }
}
