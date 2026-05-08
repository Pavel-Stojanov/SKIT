package org.example.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AddUserPage extends BasePage {
    private static final By SAVE_BUTTON = By.xpath("//button[normalize-space()='Save']");
    private static final By VALIDATION_MESSAGES = By.cssSelector(".oxd-input-field-error-message");
    private static final By EMPLOYEE_SUGGESTIONS = By.xpath("//div[@role='listbox']//span[normalize-space()]");

    public AddUserPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[normalize-space()='Add User']")));
    }

    public String createUser(String role, String status, String employeeHint, String username, String password) {
        selectDropdownOption("User Role", role);
        selectDropdownOption("Status", status);
        selectFirstEmployeeSuggestion(employeeHint);
        typeIntoInput("Username", username);
        typeIntoInput("Password", password);
        typeIntoInput("Confirm Password", password);
        clickSave();
        String successMessage = waitForSuccessMessage("Successfully Saved");
        waitForPageLoaderToFinish();
        return successMessage;
    }

    public List<String> submitEmptyFormAndReadValidationMessages() {
        clickSave();
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(VALIDATION_MESSAGES, 0));
        return driver.findElements(VALIDATION_MESSAGES)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    private void selectFirstEmployeeSuggestion(String employeeHint) {
        typeIntoInput("Employee Name", employeeHint);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(EMPLOYEE_SUGGESTIONS, 0));
        driver.findElements(EMPLOYEE_SUGGESTIONS)
                .stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No employee suggestions were available"))
                .click();
    }

    private void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(SAVE_BUTTON)).click();
    }
}
