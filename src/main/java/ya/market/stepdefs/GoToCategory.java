package ya.market.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ya.market.pages.MainPage;
import ya.market.pages.ProductListPage;
import ya.market.stash.Context;

import java.util.List;

import static ya.market.helpers.Properties.testsProperties;

/**
 * Класс реализации шагов
 */
public class GoToCategory extends BaseSteps {
    @Given("перехожу на главную страницу ya.market")
    public void перехожуНаГлавнуюСтраницуYaMarket() {
        chromeDriver.get(testsProperties.urlTest());
        MainPage mainPage = new MainPage(chromeDriver);
        testContext.put(Context.CURRENT_PAGE.name(), mainPage);
    }

    @When("кликаю по кнопке -Каталог-")
    public void кликаюПоКнопкеКаталог() {
        MainPage mainPage = (MainPage) testContext.get(Context.CURRENT_PAGE.name());
        mainPage.goToCatalogMenu();
        testContext.put(Context.CURRENT_PAGE.name(), mainPage);
    }

    @When("навожу курсор на название {string}")
    public void навожуКурсорНаНзваниеРаздел(String section) {
        MainPage mainPage = (MainPage) testContext.get(Context.CURRENT_PAGE.name());
        mainPage.hoverFromSectionCatalog(section);
        testContext.put(Context.CURRENT_PAGE.name(), mainPage);
    }

    @When("выбираю название {string} и кликаю по нему")
    public void выбираюНазваниеКатегорииИКликаюПоНему(String category) {
        MainPage mainPage = (MainPage) testContext.get(Context.CURRENT_PAGE.name());
        mainPage.goToSubcategoryFromCatalog(category);
        ProductListPage productPage = new ProductListPage(chromeDriver);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @When("проверяю, что нахожусь в выбранной {string}")
    public void проверяюЧтоНахожусьВВыбраннойКатегории(String category) {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        productPage.checkingTransitionOnPage(category);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @Given("выбираю фильтр {string}")
    public void выбираюФильтр(String filterName) {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        productPage.rangeFilter(filterName);
        testContext.put(Context.NAME_FILTER.name(), filterName);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @When("устанавливаю нижнее {int}")
    public void устанавливаюНижнееЗначение
            (int parameterFrom) {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        String filterName = testContext.get(Context.NAME_FILTER.name()).toString();
        productPage.setFromOnRangeFilter(parameterFrom, filterName);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @When("устанавливаю верхний {int}")
    public void устанавливаюВерхнийПараметр(int parameterOn) {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        String filterName = testContext.get(Context.NAME_FILTER.name()).toString();
        productPage.setToOnRangeFilter(parameterOn, filterName);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @Given("для фильтра {string}")
    public void дляФильтра(String filterName) {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        productPage.enumFilter(filterName);
        testContext.put(Context.NAME_FILTER.name(), filterName);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @When("задаю критерии и сохраняю как -заданные критерии-")
    public void задаюКритерииИСохраняюКакЗаданныеКритерии(List<String> parameter) {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        String filterName = testContext.get(Context.NAME_FILTER.name()).toString();
        productPage.checkHiddenFields(filterName, parameter);
        testContext.put("заданные критерии", parameter);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @Then("для {string} считаю {string} количество отображаемых элементов")
    public void дляСчитаюКоличествоОтображаемыхЭлементов(String product, String countCheck) {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        productPage.cardCountOnPage(product, countCheck);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @Then("проверяю, что {int}, {int} и {string} соответствуют фильтру")
    public void проверяюЧтоЗначениеПараметрИСоответствуютФильтру(int valueFrom, int valueTo, String criteriaList) {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        List<String> parameterList = (List<String>) testContext.get(criteriaList);
        productPage.checkParameterInProductCard(valueFrom, valueTo, parameterList);
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @Given("перехожу к началу списка товаров")
    public void перехожуКНачалуСпискаТоваров() {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        productPage.getNameFromFirstProduct();
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @When("получаю название первого товара и добавляю в строку поиска")
    public void получаюНазваниеПервогоТовараИДобавляюВСтрокуПоиска() {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        productPage.addValueToSearch();
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @When("выполняю поиск и получаю карточку товара")
    public void выполняюПоискИПолучаюКарточкуТовара() {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        productPage.clickOnFind();
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }

    @Then("сравниваю название в карточке с названием из запроса")
    public void сравниваюНазваниеВКарточккеСНазваниемИзЗапроса() {
        ProductListPage productPage = (ProductListPage) (testContext.get(Context.CURRENT_PAGE.name()));
        productPage.checkingResult();
        testContext.put(Context.CURRENT_PAGE.name(), productPage);
    }
}