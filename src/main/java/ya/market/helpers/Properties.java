package ya.market.helpers;

import org.aeonbits.owner.ConfigFactory;

/**
 * Класс управления глобальными настройками (пропертями)
 */
public class Properties {

    /**
     * переменная интерфейса к проперти значениям
     */
    public static TestsProperties testsProperties = ConfigFactory.create(TestsProperties.class);

    /**
     * переменная интерфейса к проперти значениям
     */
    public static PagesProperties pagesProperties = ConfigFactory.create(PagesProperties.class);
}
