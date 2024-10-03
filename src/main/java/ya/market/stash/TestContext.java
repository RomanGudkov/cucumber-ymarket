package ya.market.stash;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс управляет хранилищем данных, которые передаются между шагами теста
 */
public class TestContext {

    /**
     * Коллекция, куда кладутся данные, формата ключ-объект
     */
    private final Map<String, Object> stash;

    /**
     * Инстранс класс
     */
    private static TestContext INSTANCE;

    private TestContext() {
        stash = new HashMap<>();
    }

    /**
     * Реализован паттерн SingleTon для содержания едиснтвенного,уникального объекта класса
     * Это необходимо для обращения к единому хранилищу данных
     */
    public static TestContext getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestContext();
        }
        return INSTANCE;
    }

    /**
     * Метод кладет объект в хранилище
     * @param key - ключ
     * @param value - объект
     */
    public void put(String key, Object value) {
        stash.put(key, value);
    }

    /**
     * метод возвращает объект по ключу
     * @param key ключ
     * @return значение объекта
     */
    public Object get(String key) {
        return stash.get(key);
    }
}