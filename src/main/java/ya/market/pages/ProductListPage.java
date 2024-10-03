package ya.market.pages;

import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ya.market.helpers.LetterTranslate;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static ya.market.helpers.Properties.pagesProperties;
import static ya.market.helpers.Properties.testsProperties;

/**
 * класс работает с элементами на странице категории товара
 */

public class ProductListPage {

    /**
     * переменная для хранения названия товара
     */
    private String productName;

    /**
     * селектор блока фильтров
     */
    private final String blockFilters = "//div[@data-grabber='SearchFilters']";

    /**
     * драйвера для доступа к Chrome browser
     */
    private WebDriver chromeDriver;

    /**
     * ожидает выполнение заданного сценария
     */
    private WebDriverWait wait;

    /**
     * осуществляет симуляцию действий мыши
     */
    private Actions actions;

    /**
     * конструктор класса
     *
     * @param chromeDriver драйвер браузера
     */
    public ProductListPage(WebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(testsProperties.defaultTimeout()));
        this.actions = new Actions(chromeDriver);
    }

    /**
     * метод проверяет переход на страницу
     *
     * @param head параметр названия страницы на которую перешли
     */
    public void checkingTransitionOnPage(String head) {
        String headSection = "//div[@data-zone-name='searchTitle']//h1";
        wait.until(visibilityOfElementLocated(By.xpath(headSection)));
        boolean checkPresence = chromeDriver.findElement(By
                .xpath(headSection)).getText().contains(head);
        Assert.assertTrue("переход в --" + head + "-- не выполнен", checkPresence);
    }

    /**
     * метод организует проверку наличия фильтра
     *
     * @param head парааметр названия фильтра
     */
    public void rangeFilter(String head) {
        wait.until(visibilityOfElementLocated((By.xpath(blockFilters))));
        checkNameFilter(head, "//div[@data-filter-type='range']//h4");
    }

    /**
     * метод задает низ диапазона
     *
     * @param head      парааметр названия фильтра
     * @param valueFrom параметр значения нижнего диапазона
     */
    public void setFromOnRangeFilter(int valueFrom, String head) {
        String fieldFromXpath = blockFilters.concat("//div[@data-filter-type='range']//h4[text()='")
                .concat(head).concat("']/../../following-sibling::div//span[@data-auto='filter-range-min']//input");
        WebElement fromElement = chromeDriver.findElement(By.xpath(fieldFromXpath));
        checkAddingValueToField(head, valueFrom, fieldFromXpath, fromElement);
    }

    /**
     * метод задает верх диапазона
     *
     * @param head    парааметр названия фильтра
     * @param valueTo параметр значения верхнего диапазона
     */
    public void setToOnRangeFilter(int valueTo, String head) {
        String fieldToXpath = blockFilters.concat("//div[@data-filter-type='range']//h4[text()='")
                .concat(head).concat("']/../../following-sibling::div//span[@data-auto='filter-range-max']//input");
        WebElement toElement = chromeDriver.findElement(By.xpath(fieldToXpath));
        checkAddingValueToField(head, valueTo, fieldToXpath, toElement);
    }

    /**
     * метод работает с фильтром по критерию
     *
     * @param head параметр названия фильтра
     */
    public void enumFilter(String head) {
        wait.until(visibilityOfElementLocated((By.xpath(blockFilters))));
        checkNameFilter(head, "//div[@data-filter-type='enum']//h4");
    }

    /**
     * метод проверяет наличие элемента по его названию
     *
     * @param value     параметр названия фильтра
     * @param fileXpath параметр Xpath селектора
     */
    private void checkNameFilter(String value, String fileXpath) {
        List<WebElement> element = chromeDriver.findElements(By.xpath(fileXpath));
        boolean checkPresence = element.stream()
                .anyMatch(e -> e.getText().trim().equals(value));
        Assert.assertTrue("фильтр --" + value + "-- не найден", checkPresence);
    }

    /**
     * метод устанавливает значение в поле
     *
     * @param head       параметр названия фильтра
     * @param value      параметр устанавлтваемого значения
     * @param fieldXpath параметр Xpath селектора
     * @param element    параметр элемента на странице
     */
    private void checkAddingValueToField(String head, int value, String fieldXpath, WebElement element) {
        int loop = pagesProperties.loopCount();
        boolean loopCheck = false;

        while (loop >= 0 && !loopCheck) {
            element.clear();
            element.sendKeys(String.valueOf(value));
            wait.until(visibilityOfElementLocated((By.xpath("//div[@data-auto='SerpStatic-loader']"))));
            wait.until(invisibilityOfElementLocated(By.xpath("//div[@data-auto='SerpStatic-loader']")));
            String valueOnField = chromeDriver.findElement(By
                    .xpath(fieldXpath)).getAttribute("value");
            loopCheck = String.valueOf(value).equals(valueOnField);
            loop--;
        }
        Assert.assertTrue("значение --" + value + "-- для фильтра --" + head + "-- не задано", loopCheck);
    }

    /**
     * метод проверяет наличие кнопки раскрытия списка критериев
     *
     * @param head     параметр названия фильтра
     * @param criteria параметр список критериев
     */
    public void checkHiddenFields(String head, List<String> criteria) {
        if (chromeDriver.findElements(By
                .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                        "/ancestor::legend/following-sibling::div//div[@data-baobab-name='showMoreFilters']" +
                        "/button")).size() > 0) {
            criteriaCalculate(head, criteria);
        } else {
            checkFieldFilter(head, criteria);
            for (String criteriaItem : criteria) {
                String checkboxXpath = "//div[@data-filter-type='enum']//h4[text()='"
                        + head + "']/../../following-sibling::div//span[text()='" + criteriaItem + "'] /../..";
                checkCheckboxStatus(head, criteriaItem, checkboxXpath);
            }
        }
    }

    /**
     * метод считает количество карточек товара на странице
     *
     * @param product    параметр наименования товара
     * @param countCheck параметр количества отображаемых товаров
     */
    public void cardCountOnPage(String product, String countCheck) {
        wait.until((visibilityOfElementLocated(By
                .xpath("//div[@id='/content/page/fancyPage/searchSerpStatic']/parent::div"))));
        boolean checking = chromeDriver.findElements(By
                .xpath("//div[@data-apiary-widget-name='@light/Organic']")).size()
                > Integer.parseInt(countCheck);
        Assert.assertTrue("карточек --" + product + "-- на странице меньше --" + countCheck, checking);
    }

    /**
     * метод проверяет результат запроса согласно фильтру
     *
     * @param valueFrom нижнее значение
     * @param valueTo   верхнее значения
     * @param criteria  список критериев
     */
    public void checkParameterInProductCard(int valueFrom, int valueTo, List<String> criteria) {
        wait.until(visibilityOfElementLocated(By.xpath("//*[@id='/content/page/fancyPage/searchSerpStatic']/..")));
        int numberBlock = 1;
        boolean lastPage;
        while (true) {
            actions.moveToElement(chromeDriver.findElement(By
                    .xpath("//*[@data-auto='pagination-page']/.."))).perform();
            String valueOf = String.valueOf(numberBlock + 1);
            lastPage = chromeDriver.findElements(By.xpath("//*[@data-auto='pagination-page']/a[text()]"))
                    .stream().anyMatch(e -> e.getText().contains(valueOf));
            if (lastPage) {
                wait.until(visibilityOfElementLocated(By.xpath("(//*[@id='/marketfrontSerpLayout'])["
                        + (numberBlock) + "]")));
                wait.until(visibilityOfElementLocated(By
                        .xpath("//*[@id='/content/page/fancyPage/searchPagerPagination']")));
                numberBlock++;
                continue;
            }
            break;
        }
        Assert.assertTrue("Просмотрено страниц --" + (numberBlock + 1) + "--, но остались не просмотренные", !lastPage);
        checkingPriceInCard(valueFrom, valueTo);
        checkingCriteriaInCard(criteria);
    }

    /**
     * метод выполняет переход к первому товару в списке
     */
    public void getNameFromFirstProduct() {
        wait.until(visibilityOfElementLocated(By.xpath("//div[@data-auto='SerpList']")));
        actions.moveToElement(chromeDriver.findElement(By
                .xpath("(//div[@data-apiary-widget-name='@light/Organic']" +
                        "//span[@itemprop='name'])[1]")));
        wait.until(visibilityOfElementLocated(By
                .xpath("(//div[@data-apiary-widget-name='@light/Organic']"
                        + "//span[@itemprop='name'])[1]")));
    }

    /**
     * метод получает название товара и добавляет в строку поиска
     */
    public void addValueToSearch() {
        String searchField = "//input[@id='header-search']";
        productName = chromeDriver.findElement(By
                .xpath("(//div[@data-apiary-widget-name='@light/Organic']" +
                        "//span[@itemprop='name'])[1]")).getText();
        actions.moveToElement(chromeDriver.findElement(By.xpath(searchField)));
        wait.until(visibilityOfElementLocated(By.xpath(searchField)));
        chromeDriver.findElement(By.xpath(searchField)).click();
        chromeDriver.findElement(By.xpath(searchField)).sendKeys(productName);
        String actualValue = chromeDriver.findElement(By.xpath(searchField)).getAttribute("value");
        boolean isMatch = actualValue != null && actualValue.equals(productName);
        Assert.assertTrue("строка поиска не заполнена значением " + productName, isMatch);
    }

    /**
     * метод кликает по кнопке поиска
     */
    public void clickOnFind() {
        chromeDriver.findElement(By
                .xpath("//span[text()='Найти']/parent::button[@data-auto='search-button']")).click();
    }

    /**
     * метод сравниввает результаты поиска с запросом
     */
    public void checkingResult() {
        wait.until(visibilityOfElementLocated(By.xpath("//div[@data-auto='SerpList']")));
        List<WebElement> elements = chromeDriver.findElements(By
                .xpath("//div[@data-apiary-widget-name='@light/Organic']//span[@itemprop='name']"));
        boolean nameContain = elements.stream()
                .anyMatch(e -> e.getText().contains(productName));
        Assert.assertTrue("в результатах поиска не найдено --" + productName + "--", nameContain);
    }

    /**
     * метод добавлят поля в фильтр
     *
     * @param head     параметр названия фильтра
     * @param criteria параметр списка критериев
     */
    private void criteriaCalculate(String head, List<String> criteria) {
        int position = 0;

        for (String criteriaItem : criteria) {
            openMoreParameters(head);
            while (true) {
                actions.moveToElement(chromeDriver.findElement(By
                        .xpath("//div[@data-item-index='" + position + "']"))).perform();
                WebElement criteriaName = chromeDriver.findElement(By
                        .xpath("//div[@data-item-index='" + position
                                + "']//span/span/following-sibling::span"));
                String checkboxXpath = "//div[@data-item-index='" + position + "']//label";
                boolean checkboxMarked = chromeDriver.findElement(By.xpath(checkboxXpath))
                        .getAttribute("aria-checked").equals("false");
                boolean itemEquals = criteriaName.getText().equals(criteriaItem);
                if (itemEquals && checkboxMarked) {
                    chromeDriver.findElement(By.xpath(checkboxXpath)).click();
                    wait.until(visibilityOfElementLocated((By
                            .xpath("//div[@data-auto='SerpStatic-loader']"))));
                    wait.until(invisibilityOfElementLocated(By
                            .xpath("//div[@data-auto='SerpStatic-loader']")));
                    checkCheckboxStatus(criteriaItem);
                    position = 0;
                    break;
                }
                if (chromeDriver.findElements(By
                        .xpath("//div[@data-item-index='" + (position + 1) + "']")).size() > 0) {
                    position++;
                    continue;
                }
                Assert.assertTrue("критерий --" + criteriaItem + "-- не найден", false);
            }
        }
    }

    /**
     * метод разворачивает скрытый список критериев
     *
     * @param head параметр название фильта
     */
    private void openMoreParameters(String head) {
        int loop = pagesProperties.loopCount();
        boolean loopCheck = false;
        while (loop >= 0 && !loopCheck) {
            loop--;
            WebElement elementXpath = chromeDriver.findElement(By
                    .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                            "/ancestor::legend/following-sibling::div//div[@data-baobab-name='showMoreFilters']/button"));
            if (elementXpath.getAttribute("aria-expanded").equals("false")) {
                chromeDriver.findElement(By
                        .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                                "/ancestor::legend/following-sibling::div//button")).click();
                wait.until(invisibilityOfElementLocated((By
                        .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                                "/ancestor::legend/parent::fieldset/div/ul"))));
                wait.until(visibilityOfElementLocated((By
                        .xpath("//div[@data-filter-type='enum']//h4[text()='" + head + "']" +
                                "/ancestor::legend/following-sibling::div//div[@data-test-id='virtuoso-item-list']"))));
                continue;
            }
            loopCheck = true;
        }
    }

    /**
     * метод проверяет наличие элемента по его названию, список
     *
     * @param head  параметр названия фильтра
     * @param value параметр список критериев
     */
    private void checkFieldFilter(String head, List<String> value) {
        String nameCriteriaXpath = "//div[@data-filter-type='enum']//h4[text()='".concat(head)
                .concat("']/../../following-sibling::div//span[text()]");
        List<WebElement> element = chromeDriver.findElements(By.xpath(nameCriteriaXpath));
        for (String itemValue : value) {
            boolean checkPresence = element.stream()
                    .anyMatch(e -> e.getText().equals(itemValue));
            Assert.assertTrue("критерий --" + itemValue + "-- не найден", checkPresence);
        }
    }

    /**
     * метод добавлят поля в фильтр
     *
     * @param criteriaItem параметр название критерия
     */
    private void checkCheckboxStatus(String criteriaItem) {
        boolean checkboxControl = chromeDriver.findElement(By
                        .xpath("//span[text()='" + criteriaItem + "']/ancestor::label"))
                .getAttribute("aria-checked").equals("true");
        Assert.assertTrue("критерий --" + criteriaItem + "-- не отмечен", checkboxControl);
    }

    /**
     * метод добавлят поля в фильтр
     *
     * @param head          параметр названия фильтра
     * @param criteriaItem  параметр название критерия
     * @param checkboxXpath параметр селектор xpath
     */
    private void checkCheckboxStatus(String head, String criteriaItem, String checkboxXpath) {
        if (chromeDriver.findElement(By.xpath(checkboxXpath)).getAttribute("aria-checked").equals("false")) {
            chromeDriver.findElement(By.xpath(checkboxXpath)).click();
            wait.until(visibilityOfElementLocated((By.xpath("//div[@data-auto='SerpStatic-loader']"))));
            wait.until(invisibilityOfElementLocated(By
                    .xpath("//div[@data-auto='SerpStatic-loader']")));
        }
        boolean checkPresence = chromeDriver.findElement(By.xpath(checkboxXpath))
                .getAttribute("aria-checked").equals("true");
        Assert.assertTrue("для --" + head + "-- критерий --" + criteriaItem + "-- не отмечен", checkPresence);
    }

    /**
     * метод сравнивает стоимость товара с установленной в фильтре
     *
     * @param valueFrom параметр значения -от-
     * @param valueTo   параметр значения -до-
     */
    private void checkingPriceInCard(int valueFrom, int valueTo) {
        List<WebElement> elements = chromeDriver.findElements(By
                .xpath("//div[@data-apiary-widget-name='@light/Organic']//span[@itemprop='name']" +
                        "/ancestor::div[@data-baobab-name='title']/parent::div/following-sibling::div" +
                        "//span[contains(text(),'Цена')]"));
        List<String> collect = elements.stream()
                .map(WebElement::getText)
                .map(e -> {
                    String elementPrice = e.replaceAll("[^\\d*₽+]", "");
                    String twoPrice = elementPrice.replaceAll("₽", " ");
                    if (twoPrice.isEmpty()) return String.valueOf(0);
                    int spaceIndex = twoPrice.indexOf(" ");
                    return twoPrice.substring(0, spaceIndex);
                })
                .mapToInt(e -> Integer.parseInt(e))
                .filter(e -> e < valueFrom || e > valueTo)
                .mapToObj(e -> String.valueOf(e))
                .collect(Collectors.toList());
        Assert.assertTrue("из " + elements.size() + " цен "
                + collect.size() + " не соответствуют установкам диапазона фильтраб а именно " + collect + " ₽", collect.size() == 0);
    }

    /**
     * метод проверяет заголовок товара по выбранным в фильтре критериям
     *
     * @param criteria параметр список критериев фильтра
     * @return число
     */
    private void checkingCriteriaInCard(List<String> criteria) {
        List<WebElement> elements = chromeDriver.findElements(By
                .xpath("//div[@data-apiary-widget-name='@light/Organic']//span[@itemprop='name']"));
        List<String> collect = elements.stream()
                .filter(e -> !criteria.stream().anyMatch(c -> {
                    String webText = e.getText().toLowerCase();
                    if (Pattern.compile("([a-z]+[а-я])|\\s([а-я][a-z]+)|([a-z]+[а-я][a-z]+)").matcher(webText).find()
                    ) {
                        webText = LetterTranslate.convertCyrillicToLatin(webText);
                    }
                    return webText.contains(c.toLowerCase());
                }))
                .map(WebElement::getText)
                .collect(Collectors.toList());
        Assert.assertTrue("из " + elements.size() + " заголовков "
                + collect.size() + " не соответствуют установкам " + criteria + " такие, как \n \t" + collect, collect.size() == 0);
    }
}