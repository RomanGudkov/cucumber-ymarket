package ya.market.helpers;

import org.aeonbits.owner.Config;

/**
 * Интерфейс для доступа к глобальным переменным проперти
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"file:src/main/resources/test.properties"})
public interface TestsProperties extends Config {

    /**
     * метод возвращает значение ожидания ответа страницы в секундах
     *
     * @return число
     */
    @Key("default.timeout")
    int defaultTimeout();

    /**
     * метод возвращает параметр используемого драйвера
     *
     * @return строка
     */
    @Key("webdriver")
    String webdriver();

    /**
     * метод возвращает параметр пути к драйверу
     *
     * @return строка
     */
    @Key("webdriver.path")
    String webdriverPath();

    /**
     * метод возвращает тестовый url
     *
     * @return строка
     */
    @Key("ya.market.url")
    String urlTest();
}