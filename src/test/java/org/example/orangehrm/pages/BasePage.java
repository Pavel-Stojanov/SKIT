package org.example.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

abstract class BasePage {
    protected static final By SUCCESS_TOAST = By.cssSelector(".oxd-toast-content--success, .oxd-toast--success");
    private static final By PAGE_LOADER = By.cssSelector(".oxd-loading-spinner");

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected static String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }

        String[] parts = value.split("'", -1);
        StringBuilder expression = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                expression.append(", \"'\", ");
            }
            expression.append("'").append(parts[i]).append("'");
        }
        expression.append(")");
        return expression.toString();
    }

    protected WebElement typeIntoInput(String label, String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputByLabel(label)));
        input.sendKeys(value);
        return input;
    }

    protected void selectDropdownOption(String label, String option) {
        wait.until(ExpectedConditions.elementToBeClickable(dropdownByLabel(label))).click();
        wait.until(ExpectedConditions.elementToBeClickable(dropdownOption(option))).click();
    }

    protected void waitForPageLoaderToFinish() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(PAGE_LOADER));
    }

    protected String waitForSuccessMessage(String expectedMessage) {
        By messageLocator = By.xpath("//*[contains(@class, 'oxd-toast') and contains(normalize-space(.), "
                + xpathLiteral(expectedMessage) + ")]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(messageLocator));
        return expectedMessage;
    }

    protected By inputByLabel(String label) {
        return By.xpath("//label[normalize-space()=" + xpathLiteral(label)
                + "]/ancestor::div[contains(@class, 'oxd-input-group')]//input");
    }

    private By dropdownByLabel(String label) {
        return By.xpath("//label[normalize-space()=" + xpathLiteral(label)
                + "]/ancestor::div[contains(@class, 'oxd-input-group')]//div[contains(@class, 'oxd-select-text')]");
    }

    private By dropdownOption(String option) {
        return By.xpath("//div[@role='listbox']//span[normalize-space()=" + xpathLiteral(option) + "]");
    }
}
