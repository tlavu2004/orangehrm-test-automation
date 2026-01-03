package com.orangehrm.utils;

/**
 * Test Data model representing a test case from CSV.
 * Maps to the structure of testcases_all_ess_detailed.csv
 */
public class TestData {
    private String featureId;
    private String testCaseId;
    private String testDescription;
    private String testSteps;
    private String expectedResult;
    private String actualResult;
    private String status;
    private String tester;
    private String testedDate;
    private String remark;

    // Constructor
    public TestData(String featureId, String testCaseId, String testDescription, 
                   String testSteps, String expectedResult, String actualResult,
                   String status, String tester, String testedDate, String remark) {
        this.featureId = featureId;
        this.testCaseId = testCaseId;
        this.testDescription = testDescription;
        this.testSteps = testSteps;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
        this.status = status;
        this.tester = tester;
        this.testedDate = testedDate;
        this.remark = remark;
    }

    // Getters and Setters
    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public String getTestSteps() {
        return testSteps;
    }

    public void setTestSteps(String testSteps) {
        this.testSteps = testSteps;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    public String getTestedDate() {
        return testedDate;
    }

    public void setTestedDate(String testedDate) {
        this.testedDate = testedDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "featureId='" + featureId + '\'' +
                ", testCaseId='" + testCaseId + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
