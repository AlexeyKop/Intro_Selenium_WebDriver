import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.logging.Logger;

public class GeekBrainsTests {

    @Test
    void loginTest() {
        // Указываем путь к ChromeDriver
        String pathToChromeDriver = Paths.get("src", "main", "resources", "chromedriver.exe").toAbsolutePath().toString();
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);

        Logger logger = Logger.getLogger(GeekBrainsTests.class.getName());
        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();

            // Открываем страницу логина
            driver.get("https://test-stand.gb.ru/login");

            // Явное ожидание для полей логина и пароля
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#login input[type='text']")));
            WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form#login input[type='text']")));
//            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#login input[type='password']")));
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form#login input[type='password']")));

            // Получаем данные логина и пароля
            final String USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
            final String PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));

            if (USERNAME == null || PASSWORD == null) {
                throw new IllegalArgumentException("USERNAME or PASSWORD is null. Please set the necessary properties or environment variables.");
            }

            logger.info("USERNAME: " + USERNAME);

            // Вводим данные
            usernameField.sendKeys(USERNAME);
            passwordField.sendKeys(PASSWORD);

            // Кликаем на кнопку логина
            WebElement loginButton = driver.findElement(By.cssSelector("form#login button"));
            loginButton.click();
            // ожидаем исчезновение кнопки loginButton: LOGIN
            wait.until(ExpectedConditions.invisibilityOf(loginButton));

            // Ждем загрузки следующей страницы и появления элемента с именем пользователя
            WebElement usernameLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(), 'Hello')]")));

            // Проверяем текст
            String actualUsername = usernameLink.getText().trim();
            String expectedUsername = String.format("Hello, %s", USERNAME);

            logger.info("Actual username displayed: " + actualUsername);
            Assertions.assertEquals(expectedUsername, actualUsername, "The displayed username is incorrect.");

        } finally {
            driver.quit();
        }
    }

}
