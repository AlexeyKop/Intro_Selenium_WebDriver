import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class AddGroupTest {

    private static WebDriver driver;
    private WebDriverWait wait;
    private static String USERNAME;
    private static String PASSWORD;

    @BeforeAll
    public static void setUpDriver() {
        // Устанавливаем путь к драйверу (предполагается, что он добавлен в PATH)
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        // Инициализируем WebDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        USERNAME = System.getProperty("geekbrains_username", System.getenv("geekbrains_username"));
        PASSWORD = System.getProperty("geekbrains_password", System.getenv("geekbrains_password"));
    }

    @BeforeEach
    public void setUp() {
        // Инициализируем явные ожидания
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Переходим на страницу входа
        driver.get("https://test-stand.gb.ru/login");
    }

    @Test
    public void testAddGroup() {
        // Вводим логин и пароль
//        driver.findElement(By.id("login")).sendKeys("GB202407d2a3f1");
//        driver.findElement(By.id("password")).sendKeys("b29dc00096");

//        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
//        System.out.println("Количество iframe на странице: " + iframes.size());
//
//        driver.switchTo().frame("frame_id_or_name");
        // WebElement createButton = driver.findElement(By.id("create-btn"));

        // Явное ожидание для полей логина и пароля
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#login input[type='text']")));
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#login input[type='password']")));


        // Вводим данные
        usernameField.sendKeys(USERNAME);
        passwordField.sendKeys(PASSWORD);

        driver.findElement(By.cssSelector("form#login button")).click();

//        WebElement createButton = driver.findElement(By.id("create-btn"));
//        if (createButton != null) {
//            System.out.println("Элемент с ID create-btn найден!");
//        }


        WebElement createButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-btn")));
        createButton.click();

        //driver.findElement(By.id("create-btn")).click();

        // Явное ожидание, чтобы убедиться, что вход был успешным и мы на главной странице
        wait.until(titleContains("Student"));

//        // Нажимаем на кнопку для добавления новой группы
//        driver.findElement(By.cssSelector("button.add-group")).click();

        // Явное ожидание, чтобы модальное окно появилось
        wait.until(visibilityOfElementLocated(By.id("titleId-title")));

        // Вводим уникальное имя для группы
//        String groupName = "Group-" + System.currentTimeMillis();
//        driver.findElement(By.className("mdc-text-field__input")).sendKeys(groupName);

        // Генерируем уникальное имя
        String groupName = "Group-" + System.currentTimeMillis();

        // Ищем поле ввода "First Name" через XPath
        driver.findElement(By.xpath("//label[.//span[text()='Fist Name']]/input")).sendKeys(groupName);
        driver.findElement(By.xpath("//label[.//span[text()='Login']]/input")).sendKeys(groupName);


//        driver.findElement(By.linkText("Login")).sendKeys(groupName);
//
//        String loginXpath = "//mdc-floating-label mdc-floating-label--float-above//@style[contains(text(),'Login')]//mdc-text-field__input";
//        driver.loginXpath.sendKeys(groupName);

        // Нажимаем кнопку SAVE
        //driver.findElement(By.className("mdc-button__label")).click();

        WebElement element = driver.findElement(By.className("mdc-button__label"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);


        String groupXpath = "//mdc-data-table__content//@class[contains(text(),'" + groupName + "')]";

        System.out.println("Searching for group: " + groupName);



        // Явное ожидание, чтобы группа появилась в таблице
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(groupXpath)));



// Ожидаем появления текста группы в таблице
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        WebElement groupCell = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='mdc-data-table__cell']//td[contains(., '" + groupName + "')]")
        ));

// Проверяем, что элемент найден
        Assertions.assertNotNull(groupCell, "Group should be present in the table.");


        // Проверяем, что группа отображается в таблице
        WebElement group = driver.findElement(By.xpath(groupXpath));
        Assertions.assertNotNull(group, "Group should be present in the table.");


        // Сохраняем скриншот для отчетности
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(screenshot.toPath(), new File("src/test/resources/screenshot.png").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        // Закрываем браузер после выполнения теста
        driver.quit();
    }

    @AfterAll
    public static void tearDownDriver() {
        // Закрытие драйвера
        if (driver != null) {
            driver.quit();
        }
    }
}
