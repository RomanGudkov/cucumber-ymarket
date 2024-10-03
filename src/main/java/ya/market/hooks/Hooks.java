package ya.market.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ya.market.stash.Context;
import ya.market.stash.TestContext;

import java.time.Duration;

import static ya.market.helpers.Properties.testsProperties;

/**
 * Класс задает методы, которые могут выполняться в различных точках цикла выполнения Cucumber.
 * Обычно они используются для настройки и демонтажа среды до и после каждого сценария
 */
public class Hooks {

    /**
     * Инициализация и настройка драйвера
     * value указывает, для каких тегов выполнить метод
     */
    @Before(value = "@search")
    public void initializeWebDriver() {
        System.setProperty(testsProperties.webdriver(), System.getenv(testsProperties.webdriverPath()));
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadTimeout(Duration.ofSeconds(testsProperties.defaultTimeout()));
        options.setScriptTimeout(Duration.ofSeconds(testsProperties.defaultTimeout()));
        ChromeDriver chromeDriver = new ChromeDriver(options);
        TestContext testContext = TestContext.getInstance();
        chromeDriver.manage().window().maximize();
        testContext.put(String.valueOf(Context.CHROMEDRIVER), chromeDriver);
    }

    /**
     * Завершение работы драйвера
     */
    @After(value = "@search")
    public void quitWebDriver() {
        ChromeDriver chromeDriver = (ChromeDriver) TestContext.getInstance().get(String.valueOf(Context.CHROMEDRIVER));
        chromeDriver.quit();
    }

    /**
     * Получение методанных сценария по завершении теста
     *
     * @param scenario - объект метаданных сценария
     */
    @Before
    public void getScenarioInfo(Scenario scenario) {
        System.out.println("____________________________");
        System.out.println(scenario.getName());
        System.out.println(scenario.getStatus());
        System.out.println(scenario.getSourceTagNames());
        System.out.println("____________________________");
    }
}