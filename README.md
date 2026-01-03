# OrangeHRM Test Automation Framework

## Overview
This is a comprehensive Selenium automation framework for testing the OrangeHRM application. The framework implements Page Object Model (POM) design pattern and supports data-driven testing using TestNG.

## Technology Stack
- **Language**: Java 17
- **Build Tool**: Maven
- **Test Framework**: TestNG 7.9.0
- **Automation Tool**: Selenium WebDriver 4.17.0
- **Driver Management**: WebDriverManager 5.6.3
- **CSV Parser**: OpenCSV 5.9
- **Logging**: Log4j 2.22.1

## Project Structure
```
orangehrm-test-automation/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/orangehrm/
│   │           ├── base/
│   │           │   └── BasePage.java          # Base page with common methods
│   │           └── pages/
│   │               ├── LoginPage.java         # Login page object
│   │               ├── MyInfoPage.java        # My Info page object (UC01)
│   │               └── LeavePage.java         # Leave page object (UC02)
│   └── test/
│       └── java/
│           └── com/orangehrm/
│               ├── base/
│               │   └── BaseTest.java          # Base test with setup/teardown
│               ├── tests/
│               │   ├── MyInfoTest.java        # UC01 test cases
│               │   └── LeaveTest.java         # UC02 test cases
│               └── utils/
│                   ├── TestData.java          # Test data model
│                   └── CSVDataProvider.java   # CSV data reader
├── testcases_all_ess_detailed.csv             # Test cases data
├── testng.xml                                  # TestNG suite configuration
├── pom.xml                                     # Maven dependencies
└── README.md                                   # This file
```

## Features

### 1. **Multi-Browser Support**
The framework supports testing across 3 browsers:
- Google Chrome
- Mozilla Firefox
- Microsoft Edge

Configure browser in `testng.xml` or pass as parameter:
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### 2. **Page Object Model (POM)**
- **BasePage**: Contains reusable methods (click, sendKeys, select dropdown, etc.)
- **LoginPage**: Handles login functionality
- **MyInfoPage**: Manages personal details (UC01 - 39 test cases)
- **LeavePage**: Manages leave applications (UC02 - 10 test cases)

### 3. **Data-Driven Testing**
Tests read data from `testcases_all_ess_detailed.csv` using TestNG `@DataProvider`:
- **CSVDataProvider**: Parses CSV file and provides test data
- **TestData**: Model class representing each test case
- Supports filtering by Feature ID (UC01, UC02)

### 4. **Test Coverage**

#### UC01: My Info / Personal Details (TC001-TC039)
- **Name Validation**: First, Middle, Last Name with boundary testing
- **Employee ID**: Read-only validation
- **Other ID**: Optional field validation
- **Driver's License**: License number and expiry date validation
- **Nationality & Marital Status**: Dropdown selection validation
- **Date of Birth**: Age validation with boundary testing (18-100 years)
- **Gender**: Radio button selection

#### UC02: Leave Management (TC040-TC049)
- **Decision Table Testing**: 10 rules covering all validation scenarios
- **Leave Balance**: Sufficient/insufficient/equal balance
- **Date Validation**: Past dates, future dates, today (boundary)
- **Date Range**: From Date < To Date validation
- **Overlap Detection**: Detecting overlapping leave requests

## Setup Instructions

### Prerequisites
1. **Java JDK 17+** installed
2. **Maven 3.6+** installed
3. **Browsers** installed (Chrome, Firefox, Edge)

### Installation
1. Clone or extract the project
2. Navigate to project directory:
   ```bash
   cd orangehrm-test-automation
   ```

3. Install dependencies:
   ```bash
   mvn clean install
   ```

### Configuration

#### 1. Update Test Credentials
Edit the following files and replace placeholders with actual credentials:
- `src/test/java/com/orangehrm/tests/MyInfoTest.java`
- `src/test/java/com/orangehrm/tests/LeaveTest.java`

```java
private static final String EMPLOYEE_USERNAME = "YOUR_USERNAME";
private static final String EMPLOYEE_PASSWORD = "YOUR_PASSWORD";
```

#### 2. Update Locators
Replace placeholder locators in page objects with actual locators from the application:
- `src/main/java/com/orangehrm/pages/LoginPage.java`
- `src/main/java/com/orangehrm/pages/MyInfoPage.java`
- `src/main/java/com/orangehrm/pages/LeavePage.java`

Example:
```java
// Replace this:
private final By usernameField = By.id("REPLACE_WITH_ACTUAL_USERNAME_ID");

// With actual locator:
private final By usernameField = By.name("username");
```

#### 3. Update Base URL (Optional)
Edit `src/test/java/com/orangehrm/base/BaseTest.java`:
```java
protected String baseUrl = "https://your-orangehrm-url.com/";
```

## Running Tests

### Option 1: Run All Tests
```bash
mvn test
```

### Option 2: Run Specific Test Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Option 3: Run Specific Browser
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Option 4: Run Specific Test Class
```bash
mvn test -Dtest=MyInfoTest
mvn test -Dtest=LeaveTest
```

### Option 5: Run from IDE
1. Import project as Maven project
2. Right-click `testng.xml` → Run As → TestNG Suite
3. Or right-click individual test class → Run As → TestNG Test

## Test Execution Flow

### My Info Tests (UC01)
1. Login as Employee
2. Navigate to My Info → Personal Details
3. Execute test scenarios:
   - Name validation (9 tests)
   - Employee ID & Other ID (5 tests)
   - Driver's License (5 tests)
   - Nationality & Marital Status (7 tests)
   - Date of Birth (9 tests)
   - Gender (4 tests)
4. Verify success/error messages

### Leave Tests (UC02)
1. Login as Employee
2. Navigate to Leave → Apply
3. Execute Decision Table scenarios:
   - R1: All conditions valid
   - R2: Insufficient balance
   - R3: Invalid date order
   - R4: Past date
   - R5: Overlapping leave
   - R6-R10: Edge cases and boundaries
4. Verify success/error messages and leave status

## Locator Strategy

### Recommended Approach
Use this priority order for locators:
1. **ID**: `By.id("elementId")`
2. **Name**: `By.name("elementName")`
3. **CSS Selector**: `By.cssSelector(".className")`
4. **XPath**: `By.xpath("//tag[@attribute='value']")`

### Finding Locators
1. Open OrangeHRM application
2. Right-click element → Inspect
3. In DevTools:
   - Look for `id` attribute (most stable)
   - Use `name` attribute for form fields
   - Create CSS selectors for complex elements
   - Use XPath as last resort

Example for OrangeHRM login page:
```java
private final By usernameField = By.name("username");
private final By passwordField = By.name("password");
private final By loginButton = By.xpath("//button[@type='submit']");
```

## Troubleshooting

### Issue 1: WebDriver not found
**Solution**: WebDriverManager handles this automatically. Ensure internet connection is available on first run.

### Issue 2: Element not found
**Solution**: Update locators with actual values from the application DOM.

### Issue 3: CSV file not found
**Solution**: Ensure `testcases_all_ess_detailed.csv` is in the project root directory.

### Issue 4: Test timeout
**Solution**: Increase timeout in `BasePage.java`:
```java
private static final int DEFAULT_TIMEOUT = 20; // Increase from 10 to 20
```

### Issue 5: Browser not opening
**Solution**: 
- Check browser is installed
- Update WebDriverManager version in `pom.xml`
- Clear Maven cache: `mvn clean`

## Reports

### TestNG HTML Reports
After test execution, reports are generated at:
```
target/surefire-reports/index.html
```

Open in browser to view:
- Test execution summary
- Pass/Fail status
- Execution time
- Error messages and stack traces

## Best Practices

1. **Explicit Waits**: Framework uses explicit waits (WebDriverWait) instead of implicit waits
2. **Page Factory**: Not used intentionally to keep code simple and maintainable
3. **Assertions**: Use meaningful assertion messages for debugging
4. **Logging**: Add Log4j logging for better traceability
5. **Screenshots**: Capture on failure (can be added as TestNG listener)

## Extending the Framework

### Adding New Page Objects
1. Create new class extending `BasePage`
2. Define locators as private final fields
3. Implement page-specific methods
4. Use inherited methods from `BasePage`

### Adding New Tests
1. Create new class extending `BaseTest`
2. Initialize page objects in `@BeforeMethod`
3. Create test methods with `@Test` annotation
4. Use `@DataProvider` for data-driven tests

### Adding Test Listeners
Create custom listener for:
- Screenshots on failure
- Custom logging
- Reporting integration

Example:
```java
public class TestListener extends TestListenerAdapter {
    @Override
    public void onTestFailure(ITestResult result) {
        // Take screenshot
        // Log failure
    }
}
```

## Known Limitations

1. **Placeholder Locators**: All locators need to be replaced with actual values
2. **Test Credentials**: Must be configured before running
3. **CSV Path**: Hardcoded to root directory
4. **Parallel Execution**: Limited due to shared state (can be improved)

## CSV Data Format

The framework expects CSV with the following columns:
```
Feature ID, Test case ID, Test Description, Test Steps, Expected Result, 
Actual Result, Status, Tester, Tested Date, Remark
```

Example:
```csv
UC01,TC001,Valid name update,"1. Login...",Success message,Pass,...
UC02,TC040,Leave application,"1. Apply leave...",Approved,Pass,...
```

## Contact & Support

For issues or questions:
1. Check the Troubleshooting section
2. Review TestNG documentation: https://testng.org/
3. Review Selenium documentation: https://www.selenium.dev/

## Version History

**Version 1.0** (Current)
- Initial framework setup
- Page Object Model implementation
- Multi-browser support (Chrome, Firefox, Edge)
- Data-driven testing with CSV
- 49 test cases implemented
- UC01: Personal Details (39 tests)
- UC02: Leave Management (10 tests)

## License

This framework is created for educational and testing purposes.

---

**Note**: Remember to replace all placeholder locators (marked with `REPLACE_WITH_ACTUAL_*`) and test credentials before running the tests.
