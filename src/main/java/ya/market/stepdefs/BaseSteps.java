package ya.market.stepdefs;

import org.openqa.selenium.WebDriver;
import ya.market.stash.Context;
import ya.market.stash.TestContext;

/**
 * Базовый класс Steps
 */
public class BaseSteps {

    /**
     * Класс-хранилище тестовых данных
     */
    public TestContext testContext;

    /**
     * Хромдрайвер
     */
    public WebDriver chromeDriver;

    /**
     * метод конструктора класса
     */
    public BaseSteps() {
        this.testContext = TestContext.getInstance();
        // драйвер берется из хранилища в классе контекста
        this.chromeDriver = (WebDriver) this.testContext.get(Context.CHROMEDRIVER.toString());
    }
}