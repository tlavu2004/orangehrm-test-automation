package com.orangehrm.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading test data from CSV file.
 * Provides DataProvider methods for TestNG tests.
 */
public class CSVDataProvider {
    
    private static final String CSV_FILE_PATH = "testcases_all_ess_detailed.csv";

    /**
     * Read all test data from CSV file
     * 
     * @return List of TestData objects
     */
    public static List<TestData> readTestData() {
        List<TestData> testDataList = new ArrayList<>();
        
        try {
            // Read with UTF-8 encoding, skipping BOM if present
            java.io.FileInputStream fis = new java.io.FileInputStream(CSV_FILE_PATH);
            java.io.InputStreamReader isr = new java.io.InputStreamReader(fis, java.nio.charset.StandardCharsets.UTF_8);
            java.io.BufferedReader br = new java.io.BufferedReader(isr);
            
            // Skip UTF-8 BOM if present (EF BB BF)
            br.mark(1);
            int firstChar = br.read();
            if (firstChar != 0xFEFF) { // Not BOM
                br.reset();
            }
            
            CSVReader reader = new CSVReader(br);
            List<String[]> records = reader.readAll();
            reader.close();
            
            // Skip header row
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                
                TestData testData = new TestData(
                    record[0],  // Feature ID
                    record[1],  // Test case ID
                    record[2],  // Test Description
                    record[3],  // Test Steps
                    record[4],  // Expected Result
                    record[5],  // Actual Result
                    record[6],  // Status
                    record[7],  // Tester
                    record[8],  // Tested Date
                    record[9]   // Remark
                );
                
                testDataList.add(testData);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read CSV file: " + CSV_FILE_PATH, e);
        }
        
        return testDataList;
    }

    /**
     * Filter test data by Feature ID (UC01, UC02, etc.)
     * 
     * @param featureId Feature ID to filter
     * @return List of TestData objects for the feature
     */
    public static List<TestData> readTestDataByFeature(String featureId) {
        List<TestData> allData = readTestData();
        List<TestData> filteredData = new ArrayList<>();
        
        for (TestData data : allData) {
            if (data.getFeatureId().equalsIgnoreCase(featureId)) {
                filteredData.add(data);
            }
        }
        
        return filteredData;
    }

    /**
     * Filter test data by multiple feature IDs
     * 
     * @param featureIds Array of feature IDs
     * @return List of TestData objects
     */
    public static List<TestData> readTestDataByFeatures(String[] featureIds) {
        List<TestData> allData = readTestData();
        List<TestData> filteredData = new ArrayList<>();
        
        for (TestData data : allData) {
            for (String featureId : featureIds) {
                if (data.getFeatureId().equalsIgnoreCase(featureId)) {
                    filteredData.add(data);
                    break;
                }
            }
        }
        
        return filteredData;
    }

    /**
     * Convert TestData list to Object[][] for TestNG DataProvider
     * 
     * @param testDataList List of test data
     * @return Object[][] for DataProvider
     */
    public static Object[][] convertToDataProviderFormat(List<TestData> testDataList) {
        Object[][] data = new Object[testDataList.size()][1];
        
        for (int i = 0; i < testDataList.size(); i++) {
            data[i][0] = testDataList.get(i);
        }
        
        return data;
    }

    /**
     * Get test data for UC01 (My Info / Personal Details) tests
     * 
     * @return Object[][] for DataProvider
     */
    public static Object[][] getMyInfoTestData() {
        List<TestData> testData = readTestDataByFeature("UC01");
        return convertToDataProviderFormat(testData);
    }

    /**
     * Get test data for UC02 (Leave Management) tests
     * 
     * @return Object[][] for DataProvider
     */
    public static Object[][] getLeaveTestData() {
        List<TestData> testData = readTestDataByFeature("UC02");
        return convertToDataProviderFormat(testData);
    }

    /**
     * Get all test data from CSV
     * 
     * @return Object[][] for DataProvider
     */
    public static Object[][] getAllTestData() {
        List<TestData> testData = readTestData();
        return convertToDataProviderFormat(testData);
    }
}
