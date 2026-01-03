package com.orangehrm.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import java.net.URL;
import java.time.Duration;

/**
 * Base Test class containing setup and teardown methods.
 * Supports multi-browser testing (Chrome, Firefox, Edge).
 * Supports both local and remote (Docker) execution.
 * 
 * To run with Docker Selenium containers:
 * mvn test -Dbrowser=chrome -Dremote=true
 */
public class BaseTest {
    protected WebDriver driver;
    // Default URLs: local vs containerized browsers
    private final String DEFAULT_LOCAL_URL = "http://localhost:8080/";
    private final String DEFAULT_REMOTE_URL = "http://host.docker.internal:8080/";
    protected String baseUrl; // will be set at runtime (can be overridden with -Dapp.url)
    
    @BeforeClass
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        // Configure UTF-8 encoding for console output (MUST be first)
        try {
            // Force UTF-8 for all output streams
            System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
            System.setErr(new java.io.PrintStream(System.err, true, java.nio.charset.StandardCharsets.UTF_8));
            
            // Set system properties for encoding
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("console.encoding", "UTF-8");
            System.setProperty("sun.jnu.encoding", "UTF-8");
            
            // Force default charset to UTF-8
            java.lang.reflect.Field charset = java.nio.charset.Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Silently continue if charset override fails
        }
        
        driver = initializeDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1)); // Reduced to 1s for maximum speed
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10)); // Reduced to 10s
        driver.get(baseUrl);
    }

    /**
     * Initialize WebDriver based on browser parameter.
     * Supports Chrome, Firefox, and Edge browsers.
     * Supports both local and remote (Docker) execution.
     * 
     * @param browser Browser name (chrome/firefox/edge)
     * @return WebDriver instance
     */
    private WebDriver initializeDriver(String browser) {
        // Check if remote execution is requested
        String remote = System.getProperty("remote", "false");
        boolean isRemote = Boolean.parseBoolean(remote);

        // Allow overriding application URL via -Dapp.url
        String appUrlProp = System.getProperty("app.url", "");
        if (appUrlProp != null && !appUrlProp.isBlank()) {
            baseUrl = appUrlProp;
        } else {
            baseUrl = isRemote ? DEFAULT_REMOTE_URL : DEFAULT_LOCAL_URL;
        }

        if (isRemote) {
            return initializeRemoteDriver(browser);
        } else {
            return initializeLocalDriver(browser);
        }
    }
    
    /**
     * Initialize local WebDriver (browser installed on machine)
     */
    private WebDriver initializeLocalDriver(String browser) {
        WebDriver driver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");
                firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                break;
                
            default:
                throw new IllegalArgumentException("Browser " + browser + " is not supported. Use chrome, firefox, or edge.");
        }
        
        return driver;
    }
    
    /**
     * Initialize RemoteWebDriver (browser in Docker container)
     * Chrome: http://localhost:4444/wd/hub
     * Firefox: http://localhost:4445/wd/hub
     * Edge: http://localhost:4446/wd/hub
     */
    private WebDriver initializeRemoteDriver(String browser) {
        try {
            URL remoteUrl;
            
            switch (browser.toLowerCase()) {
                case "chrome":
                    remoteUrl = new URL("http://localhost:4444/wd/hub");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-notifications");
                    chromeOptions.addArguments("--disable-popup-blocking");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    // Add headless mode if requested via -Dheadless=true
                    if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                        chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--disable-gpu");
                    }
                    return new RemoteWebDriver(remoteUrl, chromeOptions);
                    
                case "firefox":
                    remoteUrl = new URL("http://localhost:4445/wd/hub");
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                    if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                        firefoxOptions.addArguments("--headless");
                    }
                    return new RemoteWebDriver(remoteUrl, firefoxOptions);
                    
                case "edge":
                    remoteUrl = new URL("http://localhost:4446/wd/hub");
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--disable-notifications");
                    edgeOptions.addArguments("--no-sandbox");
                    edgeOptions.addArguments("--disable-dev-shm-usage");
                    if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
                        edgeOptions.addArguments("--headless=new");
                    }
                    return new RemoteWebDriver(remoteUrl, edgeOptions);
                    
                default:
                    throw new IllegalArgumentException("Browser " + browser + " is not supported. Use chrome, firefox, or edge.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize RemoteWebDriver: " + e.getMessage(), e);
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Get current WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
}
