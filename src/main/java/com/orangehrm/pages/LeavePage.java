package com.orangehrm.pages;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for OrangeHRM Leave Management Page.
 * Handles UC02 test cases - Leave application and approval workflow.
 */
public class LeavePage extends BasePage {
    
    // Candidate navigation locators
    private final By[] LEAVE_MENU_CANDIDATES = new By[] {
        By.id("menu_leave_viewLeaveModule"),
        By.linkText("Leave"),
        By.xpath("//a[contains(text(),'Leave')]"),
        By.cssSelector("a[href*='leave']"),
        By.xpath("//span[text()='Leave']/parent::a")
    };
    
    private final By[] APPLY_BUTTON_CANDIDATES = new By[] {
        By.linkText("Apply"),
        By.xpath("//a[contains(text(),'Apply')]"),
        By.cssSelector("a[href*='applyLeave']"),
        By.xpath("//span[text()='Apply']")
    };
    
    private final By[] LEAVE_LIST_MENU_CANDIDATES = new By[] {
        By.linkText("Leave List"),
        By.xpath("//a[contains(text(),'Leave List')]"),
        By.cssSelector("a[href*='leaveList']")
    };
    
    // Leave Application Form - Candidate Locators (MODERN UI)
    private final By[] LEAVE_TYPE_DROPDOWN_CANDIDATES = new By[] {
        By.xpath("//label[text()='Leave Type']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text')]"),
        By.xpath("//label[contains(text(),'Leave Type')]/following::div[contains(@class,'oxd-select-text')][1]"),
        By.cssSelector("div.oxd-select-text"),
        By.id("applyleave_txtLeaveType"), // Old UI fallback
        By.name("leaveType")
    };
    
    private final By[] FROM_DATE_FIELD_CANDIDATES = new By[] {
        By.xpath("//label[text()='From Date']/parent::div/following-sibling::div//input"),
        By.xpath("//label[contains(text(),'From')]/following::input[1]"),
        By.cssSelector("input[placeholder*='yyyy-mm-dd']"),
        By.id("applyleave_txtFromDate"), // Old UI fallback
        By.name("fromDate")
    };
    
    private final By[] TO_DATE_FIELD_CANDIDATES = new By[] {
        By.xpath("//label[text()='To Date']/parent::div/following-sibling::div//input"),
        By.xpath("//label[contains(text(),'To')]/following::input[1]"),
        By.cssSelector("input[placeholder*='yyyy-mm-dd']"),
        By.id("applyleave_txtToDate"), // Old UI fallback
        By.name("toDate")
    };
    
    private final By[] COMMENT_FIELD_CANDIDATES = new By[] {
        By.xpath("//label[text()='Comments']/parent::div/following-sibling::div//textarea"),
        By.xpath("//label[contains(text(),'Comment')]/following::textarea[1]"),
        By.cssSelector("textarea.oxd-textarea"),
        By.id("applyleave_txtComment"), // Old UI fallback
        By.name("comment")
    };
    
    private final By[] APPLY_SUBMIT_BUTTON_CANDIDATES = new By[] {
        By.xpath("//button[@type='submit' and contains(text(),'Apply')]"),
        By.xpath("//button[contains(@class,'oxd-button') and contains(text(),'Apply')]"),
        By.cssSelector("button[type='submit']"),
        By.id("applyBtn"), // Old UI fallback
        By.xpath("//input[@type='submit' and @value='Apply']")
    };
    
    // Leave Balance Display
    // Số dư hiện ra (VD: 10.00 Day(s)) sau khi chọn loại nghỉ phép. Class này đặc thù cho balance text.
    private final By leaveBalanceDisplay = By.xpath("//p[contains(@class, 'orangehrm-leave-balance-text')]");

    // Messages and Validations (Dùng chung Toast của hệ thống)
    private final By successMessage = By.xpath("//div[contains(@class, 'oxd-toast--success')]");
    private final By errorMessage = By.xpath("//div[contains(@class, 'oxd-toast--error')]");

    // Các lỗi logic nghiệp vụ
    // Lỗi khi xin nghỉ lố số dư (thường hiện popup hoặc text đỏ)
    private final By balanceErrorMessage = By.xpath("//div[contains(@class, 'oxd-toast--content') and contains(., 'Balance not sufficient')]");

    // Lỗi chọn ngày (VD: To Date nhỏ hơn From Date) - Hiện ngay dưới ô input date
    private final By dateRangeErrorMessage = By.xpath("//span[contains(@class, 'oxd-input-group__message') and contains(., 'To date should be after')]");

    // Lỗi chọn ngày quá khứ (nếu hệ thống chặn)
    private final By pastDateErrorMessage = By.xpath("//span[contains(@class, 'oxd-input-group__message') and contains(., 'date')]"); 

    // Lỗi trùng lịch (Overlap) - Thường hiện pop-up modal hoặc Toast error
    private final By overlapErrorMessage = By.xpath("//div[contains(@class, 'oxd-toast--content') and contains(., 'Overlap')]");

    // Leave List Page (Bảng danh sách đơn nghỉ)
    // Cột trạng thái trong bảng (tìm cell chứa text trạng thái)
    // Cấu trúc bảng OrangeHRM: div role='table' -> div role='row' -> div role='cell'
    private final By leaveStatusColumn = By.xpath("//div[@role='cell'][6]"); // Thường cột Status là cột thứ 6 hoặc dùng //div[contains(@class,'oxd-table-cell')][contains(.,'Status')]

    // Trạng thái cụ thể để verify
    private final By pendingApprovalStatus = By.xpath("//div[contains(@class, 'oxd-table-cell')]//div[contains(text(), 'Pending Approval')]");
    public LeavePage(WebDriver driver) {
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
     * Navigate to Leave menu
     */
    public void navigateToLeave() {
        By menu = findFirstVisible(LEAVE_MENU_CANDIDATES);
        clickElement(menu);
    }

    /**
     * Click Apply button to open leave application form
     */
    public void clickApply() {
        By btn = findFirstVisible(APPLY_BUTTON_CANDIDATES);
        clickElement(btn);
    }

    /**
     * Navigate to Leave List to view submitted leaves
     */
    public void navigateToLeaveList() {
        By menu = findFirstVisible(LEAVE_LIST_MENU_CANDIDATES);
        clickElement(menu);
    }

    /**
     * Select leave type from dropdown
     * Modern UI uses custom div dropdowns, not standard select
     * 
     * @param leaveType Type of leave (e.g., "CAN - Fam", "CAN - BERL")
     */
    public void selectLeaveType(String leaveType) {
        By loc = findFirstVisible(LEAVE_TYPE_DROPDOWN_CANDIDATES);
        // Use OrangeHRM custom dropdown method instead of standard select
        selectOrangeHRMDropdown(loc, leaveType);
    }

    /**
     * Enter from date for leave application
     * 
     * @param fromDate Date in yyyy-MM-dd format
     */
    public void enterFromDate(String fromDate) {
        By loc = findFirstVisible(FROM_DATE_FIELD_CANDIDATES);
        sendKeys(loc, fromDate);
    }

    /**
     * Enter to date for leave application
     * 
     * @param toDate Date in yyyy-MM-dd format
     */
    public void enterToDate(String toDate) {
        By loc = findFirstVisible(TO_DATE_FIELD_CANDIDATES);
        sendKeys(loc, toDate);
    }

    /**
     * Enter comment/reason for leave
     * 
     * @param comment Leave comment
     */
    public void enterComment(String comment) {
        By loc = findFirstVisible(COMMENT_FIELD_CANDIDATES);
        sendKeys(loc, comment);
    }

    /**
     * Click Apply button to submit leave request
     */
    public void submitLeaveApplication() {
        By loc = findFirstVisible(APPLY_SUBMIT_BUTTON_CANDIDATES);
        clickElement(loc);
    }

    /**
     * Complete leave application process
     * 
     * @param leaveType Type of leave
     * @param fromDate Start date (yyyy-MM-dd)
     * @param toDate End date (yyyy-MM-dd)
     * @param comment Leave reason
     */
    public void applyLeave(String leaveType, String fromDate, String toDate, String comment) {
        selectLeaveType(leaveType);
        enterFromDate(fromDate);
        enterToDate(toDate);
        enterComment(comment);
        submitLeaveApplication();
    }

    /**
     * Get current leave balance for selected leave type
     * 
     * @return Leave balance as string
     */
    public String getLeaveBalance() {
        return getText(leaveBalanceDisplay);
    }

    /**
     * Check if success message is displayed after leave submission
     * 
     * @return true if success message appears
     */
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }

    /**
     * Get success message text
     * 
     * @return Success message text (e.g., "Successfully Saved")
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }

    /**
     * Check if any error message is displayed
     * 
     * @return true if error message appears
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

    /**
     * Get general error message text
     * 
     * @return Error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Check if balance insufficient error is displayed
     * 
     * @return true if balance error appears
     */
    public boolean isBalanceErrorDisplayed() {
        return isElementDisplayed(balanceErrorMessage);
    }

    /**
     * Get balance error message
     * 
     * @return Balance error text (e.g., "Balance not sufficient")
     */
    public String getBalanceErrorMessage() {
        return getText(balanceErrorMessage);
    }

    /**
     * Check if date range error is displayed
     * 
     * @return true if date range error appears
     */
    public boolean isDateRangeErrorDisplayed() {
        return isElementDisplayed(dateRangeErrorMessage);
    }

    /**
     * Get date range error message
     * 
     * @return Date range error text (e.g., "To Date should be after From Date")
     */
    public String getDateRangeErrorMessage() {
        return getText(dateRangeErrorMessage);
    }

    /**
     * Check if past date error is displayed
     * 
     * @return true if past date error appears
     */
    public boolean isPastDateErrorDisplayed() {
        return isElementDisplayed(pastDateErrorMessage);
    }

    /**
     * Get past date error message
     * 
     * @return Past date error text
     */
    public String getPastDateErrorMessage() {
        return getText(pastDateErrorMessage);
    }

    /**
     * Check if overlap error is displayed
     * 
     * @return true if overlap error appears
     */
    public boolean isOverlapErrorDisplayed() {
        return isElementDisplayed(overlapErrorMessage);
    }

    /**
     * Get overlap error message
     * 
     * @return Overlap error text (e.g., "Overlapping leave request found")
     */
    public String getOverlapErrorMessage() {
        return getText(overlapErrorMessage);
    }

    /**
     * Check if leave appears in leave list with Pending Approval status
     * 
     * @return true if pending status is found
     */
    public boolean isPendingApprovalStatusDisplayed() {
        return isElementDisplayed(pendingApprovalStatus);
    }

    /**
     * Get leave status from leave list
     * 
     * @return Leave status text
     */
    public String getLeaveStatus() {
        return getText(leaveStatusColumn);
    }
}
