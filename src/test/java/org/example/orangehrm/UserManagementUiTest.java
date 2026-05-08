package org.example.orangehrm;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.orangehrm.pages.AddUserPage;
import org.example.orangehrm.pages.AdminUsersPage;
import org.example.orangehrm.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ui")
@EnabledIfSystemProperty(named = "ui.tests.enabled", matches = "true")
class UserManagementUiTest {
    private static final String USER_PASSWORD = "StrongPass123!";
    private static final String EMPLOYEE_HINT = "a";

    private final UiTestConfig config = UiTestConfig.fromSystemProperties();
    private final List<String> createdUsernames = new ArrayList<>();

    private WebDriver driver;
    private WebDriverWait wait;
    private AdminUsersPage adminUsersPage;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000");
        if (config.headless()) {
            options.addArguments("--headless=new");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get(config.baseUrl());
        new LoginPage(driver, wait).login(config.username(), config.password());

        adminUsersPage = new AdminUsersPage(driver, wait, config.baseUrl());
        adminUsersPage.open();
    }

    @AfterEach
    void tearDown() {
        try {
            cleanupCreatedUsers();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test
    void addUserSuccessfully() {
        String username = registerUniqueUsername();

        createEnabledEssUser(username);

        adminUsersPage.searchByUsername(username);
        assertTrue(adminUsersPage.isUserPresent(username));
    }

    @Test
    void addUserShowsValidationMessagesWhenRequiredFieldsAreMissing() {
        AddUserPage addUserPage = adminUsersPage.clickAddUser();

        List<String> messages = addUserPage.submitEmptyFormAndReadValidationMessages();

        assertTrue(messages.stream().filter("Required"::equals).count() >= 5);
        assertTrue(messages.contains("Passwords do not match"));
    }

    @Test
    void deleteUserSuccessfully() {
        String username = registerUniqueUsername();
        createEnabledEssUser(username);

        String deleteMessage = adminUsersPage.deleteUser(username);

        assertEquals("Successfully Deleted", deleteMessage);
        adminUsersPage.searchByUsername(username);
        assertFalse(adminUsersPage.isUserPresent(username));
        assertTrue(adminUsersPage.isNoRecordsFoundVisible());
        createdUsernames.remove(username);
    }

    @Test
    void cancelDeleteKeepsUserInTheSystem() {
        String username = registerUniqueUsername();
        createEnabledEssUser(username);

        adminUsersPage.startDelete(username);
        adminUsersPage.cancelDelete();

        adminUsersPage.waitForUserToRemainVisible(username);
    }

    private void createEnabledEssUser(String username) {
        AddUserPage addUserPage = adminUsersPage.clickAddUser();
        String message = addUserPage.createUser("ESS", "Enabled", EMPLOYEE_HINT, username, USER_PASSWORD);
        assertEquals("Successfully Saved", message);
        adminUsersPage.openDirectly();
    }

    private String registerUniqueUsername() {
        String username = "qauser" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        createdUsernames.add(username);
        return username;
    }

    private void cleanupCreatedUsers() {
        if (driver == null || createdUsernames.isEmpty()) {
            return;
        }

        adminUsersPage.openDirectly();
        for (String username : List.copyOf(createdUsernames)) {
            if (adminUsersPage.deleteUserIfPresent(username)) {
                createdUsernames.remove(username);
            }
        }
    }
}
