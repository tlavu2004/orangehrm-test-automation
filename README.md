# OrangeHRM Test Automation

Automated test suite for OrangeHRM Employee Self-Service (My Info) and Leave Management modules using Selenium WebDriver, TestNG, and Maven.

**Repository:** https://github.com/tlavu2004/orangehrm-test-automation

## Overview
A comprehensive automation framework implementing Page Object Model (POM) with data-driven testing capabilities. The framework is designed for stability across multiple browsers and supports execution on Selenium Grid (Docker).

## Technology Stack
- **Language**: Java 17
- **Build Tool**: Maven 3.6+
- **Test Framework**: TestNG 7.9.0
- **Automation**: Selenium WebDriver 4.17.0
- **Driver Management**: WebDriverManager 5.6.3
- **Data Source**: CSV (OpenCSV 5.9)
- **Execution**: Local WebDriver or RemoteWebDriver (Selenium Grid)

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

## Key Features

### 1. **Cross-Browser Testing (3+ Browsers)**
Supports testing across multiple browsers with enhanced stability:
- **Chrome** (default)
- **Firefox** (with ElementClickIntercepted handling)
- **Microsoft Edge**

All browsers tested and verified on Selenium Grid.

**Local execution:**
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
``` with Robustness**
- **BasePage**: Enhanced with retry logic, loader/overlay waits, and multiple click strategies (regular → Actions → JS fallback)
- **LoginPage**: Handles authentication with modern OrangeHRM UI
- **MyInfoPage**: Manages personal details with 12 field types and custom dropdown handling (UC01 - 39 test cases)
- **LeavePage**: Manages leave applications with decision table testingue -Dheadless=true
mvn clean test -Dbrowser=firefox -Dremote=true -Dheadless=true
mvn clean test -Dbrowser=edge -Dremote=true -Dheadless=true
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
3. **Docker** (for Selenium Grid and OrangeHRM containers)
4. **Git** (for cloning repository)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/tlavu2004/orangehrm-test-automation.git
   cd orangehrm-test-automation
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

3. Start Selenium Grid and OrangeHRM (Docker):
   ``Default Test Credentials
The framework uses default OrangeHRM credentials (can be overridden):
```bash
mvn test -Dtest.username=yourUsername -Dtest.password=yourPassword
```

Default credentials (Docker setup):
- Username: `orangehrm`
- Password: `OrangeHRM@123`

#### Base URL Configuration
Default: `http://localhost:8080` (Docker OrangeHRM container)

To override, edit `src/test/java/com/orangehrm/base/BaseTest.java`:
```java
protected String baseUrl = "https://your-orangehrm-instanceal locators from the application:
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
```Quick Start (Recommended)
Run all 49 tests on Chrome via Selenium Grid:
```bash
mvn clean test -Dbrowser=chrome -Dremote=true -Dheadless=true
```

### Run Specific Browser
```bash
# Chrome
mvn clean test -Dbrowser=chrome -Dremote=true -Dheadless=true

# Firefox
mvn clean test -Dbrowser=firefox -Dremote=true -Dheadless=true

# Edge
mvn clean test -Dbrowser=edge -Dremote=true -Dheadless=true
```

### Run Specific Test Suite
```bash
# My Info tests only (39 tests)
mvn test -Dtest=MyInfoTest -Dbrowser=chrome -Dremote=true

# Leave tests only (10 tests)
mvn test -Dtest=LeaveTest -Dbrowser=chrome -Dremote=true
```

### Local Execution (Without Docker)
```bash
mvn test -Dbrowser=chrome
```

### Run from IDE
1. Import project as Maven project in IntelliJ/Eclipse
2. Right-click `testng.xml` → Run As → TestNG Suite
3. Or right-click
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
**Solution**:Docker containers not starting
**Solution**: 
```bash
# Check Docker is running
docker ps

# Restart containers
docker compose -f docker-compose.selenium.yml down
docker compose -f docker-compose.selenium.yml up -d

# Check logs
docker compose -f docker-compose.selenium.yml logs -f
```

### Issue 2: Tests fail with "Element not clickable" (Firefox)
**Solution**: Framework already handles this with:
- Loader/overlay waits (`waitForLoaderToDisappear`, `waitForOverlaysToDisappear`)
- Retry logic with Actions and JS click fallbacks
- If still occurs, increase wait in `BasePage.java` (currently 5s)

### Issue 3: Connection refused to Selenium Grid
**Solution**: 
```bash
# Verify Grid is accessible
curl http://localhost:4444/status

# Check if OrangeHRM is running
curl http://localhost:8080
```

### Issue 4: CSV file not found
**Solution**: Ensure `testcases_all_ess_detailed.csv` is in project root directory.

### Issue 5: Tests pass locally but fail on Grid
**Solution**: 
- Ensure OrangeHRM container is in same Docker network as Grid
- Check `docker-compose.selenium.yml` has `selenium-net` network defined
- Verify BaseTest uses correct remote URL based on `-Dremote=true
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
- CImplementation Highlights

### Cross-Browser Stability
- **Firefox fix**: Enhanced `BasePage.clickElement()` with overlay detection and retry strategies
- **Edge compatibility**: Tested and verified on Selenium Grid standalone-edge image
- **Chrome baseline**: Primary browser for development and debugging

### Modern OrangeHRM UI Support
- ATest Results

**Current Status:** ✅ 49/49 tests passing (100%)
- UC01 (My Info): 39/39 ✅
- UC02 (Leave Management): 10/10 ✅

**Browsers Verified:**
- Chrome standalone ✅
- Firefox standalone ✅
- Edge standalone ✅

**Execution Time:** ~9-10 minutes (full suite on Grid)

## Documentation

- **Detailed Report**: [docs/Automation_Test_Report.md](docs/Automation_Test_Report.md)
- **Docker Compose**: [docker-compose.selenium.yml](docker-compose.selenium.yml)
- **Test Data**: [testcases_all_ess_detailed.csv](testcases_all_ess_detailed.csv)

## Resources

- **Repository**: https://github.com/tlavu2004/orangehrm-test-automation
- **TestNG Documentation**: https://testng.org/
- **Selenium Documentation**: https://www.selenium.dev/
- **OrangeHRM**: https://www.orangehrm.com/

## Version History

**Version 1.1** (2026-01-03)
- Enhanced cross-browser stability (Firefox ElementClickIntercepted fix)
- Improved BasePage with loader/overlay detection
- Added retry logic with Actions and JS fallback
- Increased default timeout to 5s for reliability
- All 49 tests passing on Chrome, Firefox, Edge

**Version 1.0** (Initial)
- Page Object Model implementation
- Multi-browser support (Chrome, Firefox, Edge)
- Data-driven testing with CSV
- 49 test cases implemented

## License

This framework is created for educational purposes as part of Software Testing coursework.

---

**Author:** Trương Lê Anh Vũ (22120443)  
**Repository:** https://github.com/tlavu2004/orangehrm-test-automation
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
