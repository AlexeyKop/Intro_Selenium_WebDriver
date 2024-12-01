import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.nio.file.Paths;
import java.time.Duration;
import java.util.logging.Logger;

public class PerfectGBTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private static String USERNAME;
    private static String PASSWORD;

    @BeforeAll
    public static void setupClass(){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
        PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));
    }

    @BeforeEach
    public void setupTest(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    void testStandGB() {

            // Открываем страницу логина
            driver.get("https://test-stand.gb.ru/login");

            // Явное ожидание для полей логина и пароля
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#login input[type='text']")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#login input[type='password']")));


            // Вводим данные
            usernameField.sendKeys(USERNAME);
            passwordField.sendKeys(PASSWORD);

            // Кликаем на кнопку логина
//            WebElement loginButton = driver.findElement(By.cssSelector("form#login button"));
//            loginButton.click();
            // "Заинлайнить" переменную (Ctrl + Alt + N)— это значит убрать её объявление и использовать её значение
            // непосредственно в том месте, где она применяется.
            driver.findElement(By.cssSelector("form#login button")).click();


        // Ждем загрузки следующей страницы и появления элемента с именем пользователя
            WebElement usernameLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(), 'Hello')]")));

            // Проверяем текст
            String actualUsername = usernameLink.getText().trim();
            String expectedUsername = String.format("Hello, %s", USERNAME);

            assertEquals(expectedUsername, actualUsername, "The displayed username is incorrect.");

        }
        @AfterEach
        public void teardown() {
            driver.quit();
        }


}
