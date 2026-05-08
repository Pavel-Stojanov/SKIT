package org.example.orangehrm;

public record UiTestConfig(String baseUrl, String username, String password, boolean headless) {
    private static final String DEFAULT_BASE_URL = "https://opensource-demo.orangehrmlive.com";
    private static final String DEFAULT_USERNAME = "Admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    public static UiTestConfig fromSystemProperties() {
        return new UiTestConfig(
                stripTrailingSlash(System.getProperty("ui.baseUrl", DEFAULT_BASE_URL)),
                System.getProperty("ui.username", DEFAULT_USERNAME),
                System.getProperty("ui.password", DEFAULT_PASSWORD),
                Boolean.parseBoolean(System.getProperty("ui.headless", "true"))
        );
    }

    private static String stripTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return DEFAULT_BASE_URL;
        }

        String trimmed = value.trim();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}
