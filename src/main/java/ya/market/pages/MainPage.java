package ya.market.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static ya.market.helpers.Properties.pagesProperties;
import static ya.market.helpers.Properties.testsProperties;

/**
 * класс работает с элементами каталога на главной странице сайта
 */
public class MainPage {

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
     * структура блока категорий
     */
    private Map<String, List<String>> categoryContentFromSectionCatalog;

    /**
     * конструктор класса
     *
     * @param chromeDriver драйвер браузера
     */
    public MainPage(WebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        this.wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(testsProperties.defaultTimeout()));
        this.actions = new Actions(chromeDriver);
        categoryContentFromSectionCatalog = new HashMap<>();
    }

    /**
     * метод выполняет переход в каталог
     */
    public void goToCatalogMenu() {
        String buttonCatalog = "//div[@data-zone-name='catalog']//span[text()='Каталог']";
        wait.until(visibilityOfElementLocated(By.xpath(buttonCatalog)));
        chromeDriver.findElement(By.xpath(buttonCatalog)).click();
    }

    /**
     * метод выбирает раздел каталога
     *
     * @param section параметр названия раздела
     */
    public void hoverFromSectionCatalog(String section) {
        String blockSectionsInDynamicCatalog = "//div[@data-zone-name='catalog-content'] //ul[@role='tablist']";
        wait.until(visibilityOfElementLocated(By.xpath(blockSectionsInDynamicCatalog)));
        String sectionXpath = blockSectionsInDynamicCatalog
                .concat("//span[text()='").concat(section).concat("']");
        checkSectionCatalogInDOM(section, sectionXpath);
        hoverRepeat(section, sectionXpath);
        createListCategoryOfSections();
    }

    /**
     * метод выполняет переход в подкатегорию каталога
     *
     * @param subcategory параметр названия подкатегории
     */
    public void goToSubcategoryFromCatalog(String subcategory) {
        String subcategoryXpath = "//div[@role='tabpanel'] //div[@data-auto='category']//a[text()='"
                .concat(subcategory).concat("']");
        checkSubcategoryInSection(subcategory);
        chromeDriver.findElement(By.xpath(subcategoryXpath)).click();
    }

    /**
     * метод проверяет существование раздела в каталоге
     *
     * @param checking параметр названия запрошенного раздела
     * @param xPath    селектор раздела в DOM-дереве
     */
    private void checkSectionCatalogInDOM(String checking, String xPath) {
        boolean checkPresence = chromeDriver.findElements(By.xpath(xPath)).size() > 0;

        Assert.assertTrue("раздел --" + checking + "-- не найден", checkPresence);
    }

    /**
     * метод наводит курсор на раздел и проверяет это
     *
     * @param section      параметр названия раздела
     * @param sectionXpath селектор xpath раздела
     * @return boolean
     */
    private void hoverRepeat(String section, String sectionXpath) {
        String nameSectionXpath = "//div[@role='heading']/a";
        int loop = pagesProperties.loopCount();
        boolean loopCheck = true;
        while (loop >= 0 && loopCheck) {
            loop--;
            actions.moveToElement(chromeDriver.findElement(By.xpath(sectionXpath))).perform();
            if (chromeDriver.findElement(By.xpath(nameSectionXpath)).getText().contains(section)) {
                break;
            }
            int size = chromeDriver.findElements(By
                    .xpath("//div[@data-apiary-widget-id='/content/header/header/catalogEntrypoint/catalog']" +
                            "//ul[@role='tablist']/li")).size();
            actions.moveToElement(chromeDriver.findElement(By
                    .xpath("(//div[@data-apiary-widget-id='/content/header/header/catalogEntrypoint/catalog']" +
                            "//ul[@role='tablist']/li)[" + size + "]"))).perform();
        }
        Assert.assertTrue("переход в раздел --" + section + "-- не выполнен", loopCheck);
    }

    /**
     * метод составляет карту категорий и их подкатегорий
     */
    private void createListCategoryOfSections() {
        List<WebElement> nameCategoryList = chromeDriver.findElements(By
                .xpath("//div[@role='tabpanel']//div[@data-auto='category']//div[@role='heading']"));
        for (WebElement elementCategory : nameCategoryList) {
            String nameCategory = elementCategory.getText();
            openList(nameCategory);
            String subcategoryXpath = "//div[@role='tabpanel'] //div[@data-auto='category']//a[text()='"
                    .concat(nameCategory).concat("']/parent::div/.. /following-sibling::ul/li//a");
            List<WebElement> nameSubcategory = chromeDriver.findElements(By.xpath((subcategoryXpath)));
            List<String> collect = nameSubcategory.stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
            categoryContentFromSectionCatalog.put(nameCategory, collect);
        }
    }

    /**
     * метод разворачивает список подкатегорий
     */
    private void openList(String nameCategory) {
        String visibilityXpath = "//div[@data-auto='category']/div[@role='heading']//a[text()='"
                .concat(nameCategory).concat("']/ancestor::div[@data-auto='category']//li/span/span[text()='Ещё']");
        actions.moveToElement(chromeDriver.findElement(By.xpath(("//div[@role='tabpanel']" +
                "//div[@data-auto='category']//div[@role='heading']")))).perform();
        boolean checkPresence = chromeDriver.findElements(By.xpath(visibilityXpath)).size() > 0;
        if (checkPresence) {
            actions.moveToElement(chromeDriver.findElement(By.xpath((visibilityXpath)))).perform();
            wait.until(visibilityOfElementLocated(By.xpath(visibilityXpath)));
            chromeDriver.findElement(By.xpath((visibilityXpath))).click();
        }
    }

    /**
     * метод проверяет существование подкатегории в каталоге
     *
     * @param checking параметр назввания запрошенной категории
     */
    private void checkSubcategoryInSection(String checking) {
        boolean checkPresence = categoryContentFromSectionCatalog.values().stream()
                .anyMatch(e -> e.stream()
                        .anyMatch(s -> s.trim().equals(checking.trim())));
        Assert.assertTrue("подкатегория --" + checking + "-- не найдена", checkPresence);
    }
}
