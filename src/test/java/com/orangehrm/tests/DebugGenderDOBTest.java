package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.MyInfoPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Debug test cho Gender và DOB fields
 */
public class DebugGenderDOBTest extends BaseTest {

    @Test
    public void testGenderAndDOB() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  DEBUG GENDER & DOB FIELDS");
        System.out.println("══════════════════════════════════════════\n");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("orangehrm", "OrangeHRM@123");
        System.out.println("✅ Login successful");

        // Navigate to My Info
        MyInfoPage myInfoPage = new MyInfoPage(driver);
        myInfoPage.navigateToMyInfo();
        System.out.println("✅ Navigated to My Info");

        System.out.println("\nCurrent URL: " + driver.getCurrentUrl());
        
        try {
            Thread.sleep(2000); // Wait for page load
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test Gender - Male
        System.out.println("\n📍 Testing Gender = Male");
        try {
            myInfoPage.selectGender("Male");
            System.out.println("✅ Clicked Male gender");
        } catch (Exception e) {
            System.out.println("❌ Failed to click Male: " + e.getMessage());
        }

        // Test Gender - Female
        System.out.println("\n📍 Testing Gender = Female");
        try {
            myInfoPage.selectGender("Female");
            System.out.println("✅ Clicked Female gender");
        } catch (Exception e) {
            System.out.println("❌ Failed to click Female: " + e.getMessage());
        }

        // Test DOB
        System.out.println("\n📍 Testing Date of Birth");
        try {
            myInfoPage.updateDateOfBirth("1990-05-15");
            System.out.println("✅ Updated DOB");
        } catch (Exception e) {
            System.out.println("❌ Failed to update DOB: " + e.getMessage());
        }

        // Click Save
        System.out.println("\n📍 Clicking Save button");
        try {
            myInfoPage.clickSave();
            System.out.println("✅ Clicked Save");
        } catch (Exception e) {
            System.out.println("❌ Failed to click Save: " + e.getMessage());
        }

        // Wait for success message
        System.out.println("\n📍 Waiting for success message...");
        try {
            Thread.sleep(3000);
            boolean success = myInfoPage.isSuccessMessageDisplayed();
            System.out.println("Success message displayed: " + success);
            
            if (success) {
                String message = myInfoPage.getSuccessMessage();
                System.out.println("Success message text: " + message);
            }
        } catch (Exception e) {
            System.out.println("❌ Error checking success: " + e.getMessage());
        }

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  DEBUG COMPLETED");
        System.out.println("══════════════════════════════════════════\n");
    }
}
