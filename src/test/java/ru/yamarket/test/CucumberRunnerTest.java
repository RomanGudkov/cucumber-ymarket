package ru.yamarket.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@CucumberOptions(
        //Если есть нереализованные строки в фичах, тест упадет
        strict = false,
        // повышение читаемости вывода в консоли, заменяет нечитаемые символы
        monochrome = true,
        // плагины для форматирования вывода, отчетов
        plugin = {"pretty", "io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm", "json:target/cucumber-report/report.json"},
        // указатель на корневой пакет, где лежат файлы .features
        features = "src/test/java/features",
        // указатель на корневой пакет, где лежат реализации шагов (stepdefs)
        glue = {"/ya/market/stepdefs", "ya/market/hooks"},
        //Указатель тегов для запуска тестов. Префиксы ~/not исключают тест из списка запускаемых тестов, например ~@fail или not в зависимости от версиии cucumber;
        tags = "@excluded"
)

@RunWith(Cucumber.class)
public class CucumberRunnerTest {
}
