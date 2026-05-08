Each new exercise will be added to a new branch.

## Running tests

Run the CourseService unit tests:

```bash
mvn test
```

Run the live OrangeHRM Selenium tests:

```bash
mvn -Pui-tests -Dui.headless=true test
```

When running the Selenium class directly from IntelliJ, add this VM option:

```bash
-Dui.tests.enabled=true
```

Useful UI overrides:

```bash
-Dui.headless=false
-Dui.baseUrl=https://opensource-demo.orangehrmlive.com
-Dui.username=Admin
-Dui.password=admin123
```
