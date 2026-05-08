package org.example.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminUsersPage extends BasePage {
    private static final By ADMIN_MENU = By.xpath("//a[.//span[normalize-space()='Admin']]");
    private static final By ADD_BUTTON = By.xpath("//button[contains(normalize-space(), 'Add')]");
    private static final By SEARCH_BUTTON = By.xpath("//button[normalize-space()='Search']");
    private static final By CANCEL_DELETE_BUTTON = By.xpath("//button[normalize-space()='No, Cancel']");
    private static final By CONFIRM_DELETE_BUTTON = By.xpath("//button[contains(normalize-space(), 'Yes, Delete')]");
    private static final By DELETE_DIALOG = By.xpath("//div[@role='dialog' and .//*[normalize-space()='Are you Sure?']]");
    private static final By NO_RECORDS_FOUND = By.xpath("//*[normalize-space()='No Records Found']");

    private final String baseUrl;

    public AdminUsersPage(WebDriver driver, WebDriverWait wait, String baseUrl) {
        super(driver, wait);
        this.baseUrl = baseUrl;
    }

    public void open() {
        wait.until(ExpectedConditions.elementToBeClickable(ADMIN_MENU)).click();
        waitForSystemUsersPage();
    }

    public void openDirectly() {
        driver.get(baseUrl + "/web/index.php/admin/viewSystemUsers");
        waitForSystemUsersPage();
    }

    public AddUserPage clickAddUser() {
        wait.until(ExpectedConditions.elementToBeClickable(ADD_BUTTON)).click();
        return new AddUserPage(driver, wait);
    }

    public void searchByUsername(String username) {
        typeIntoInput("Username", username);
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON)).click();
        waitForPageLoaderToFinish();
        wait.until(driver -> isUserPresent(username) || isNoRecordsFoundVisible());
    }

    public boolean isUserPresent(String username) {
        return driver.findElements(userRow(username))
                .stream()
                .anyMatch(this::isDisplayed);
    }

    public boolean isNoRecordsFoundVisible() {
        return driver.findElements(NO_RECORDS_FOUND)
                .stream()
                .anyMatch(this::isDisplayed);
    }

    public void waitForUserToRemainVisible(String username) {
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(userRow(username))));
    }

    public void startDelete(String username) {
        searchByUsername(username);
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(userRow(username)));
        row.findElements(By.cssSelector(".oxd-table-cell-actions button")).get(0).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_DIALOG));
    }

    public void cancelDelete() {
        wait.until(ExpectedConditions.elementToBeClickable(CANCEL_DELETE_BUTTON)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(DELETE_DIALOG));
    }

    public String deleteUser(String username) {
        startDelete(username);
        return confirmDelete();
    }

    public boolean deleteUserIfPresent(String username) {
        searchByUsername(username);
        if (!isUserPresent(username)) {
            return false;
        }

        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(userRow(username)));
        row.findElements(By.cssSelector(".oxd-table-cell-actions button")).get(0).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(DELETE_DIALOG));
        confirmDelete();
        return true;
    }

    public String latestSuccessMessage() {
        String toastText = wait.until(ExpectedConditions.visibilityOfElementLocated(SUCCESS_TOAST)).getText();
        if (toastText.contains("Successfully Saved")) {
            return "Successfully Saved";
        }
        if (toastText.contains("Successfully Deleted")) {
            return "Successfully Deleted";
        }

        throw new IllegalStateException("Success toast did not contain an expected action message: " + toastText);
    }

    private String confirmDelete() {
        wait.until(ExpectedConditions.elementToBeClickable(CONFIRM_DELETE_BUTTON)).click();
        String successMessage = waitForSuccessMessage("Successfully Deleted");
        waitForPageLoaderToFinish();
        return successMessage;
    }

    private void waitForSystemUsersPage() {
        waitForPageLoaderToFinish();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[normalize-space()='System Users']")));
    }

    private By userRow(String username) {
        String usernameLiteral = xpathLiteral(username);
        return By.xpath("//div[@role='row'][.//div[contains(@class, 'oxd-table-cell')][2]//div[normalize-space()="
                + usernameLiteral + "]]");
    }

    private boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (StaleElementReferenceException ignored) {
            return false;
        }
    }

    @Override
    protected WebElement typeIntoInput(String label, String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputByLabel(label)));
        input.clear();
        input.sendKeys(value);
        return input;
    }
}
