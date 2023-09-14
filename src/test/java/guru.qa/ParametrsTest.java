package guru.qa;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;

public class ParametrsTest extends BaseTest{

    SelenideElement inputSearch = $("[data-qa='header-search-input']"),
                    titleText = $(By.xpath("//h1[contains(@class, 'title')]")),
                    lkCancelButton = $(By.xpath("//div[contains(@class, 'sla-notification__cancel')]")),
                    cityButton = $("[data-qa='button-confirm-region']"),
                    localeButton = $(By.xpath("(//div[contains(@class, 'GCM-Menu-ItemLabel')])[7]"));


    ElementsCollection  itemSearch = $$(By.xpath("//div[contains(@class, 'catalog-2-level-product-card')]")),
                        localeList = $$(By.xpath("//div[contains(@class, 'GCM-Menu-ItemLabel')]")),
                        menuList = $$(By.xpath("//li[contains(@class, 'header-nav__item')]"));



    void openMetroPage(){
        open("https://online.metro-cc.ru/");
    }

    void openWarTunderPage(){
        open("https://warthunder.ru/");
    }

    void closedModal(){

        if (cityButton.isDisplayed()) {cityButton.click();}
        if (lkCancelButton.isDisplayed()) {lkCancelButton.click();}

    }

    @CsvSource(value =  {

            "Рис длиннозерный",
            "Добрый Cola"

    }, delimiter = '|')


    @Tags({
            @Tag("smoke"),
            @Tag("web")
    })
    @ParameterizedTest(name = "В поисковой выдаче интернет-магазина METRO присутствует {0}")
    @DisplayName(("Проверка поисковой выдачи"))
    void succsesSearchItemFirsTest(String searchQuery){

        openMetroPage();
        closedModal();
        inputSearch.setValue(searchQuery).pressEnter();
        titleText.shouldHave(Condition.text(searchQuery));

    }


    @Tags({
            @Tag("smoke"),
            @Tag("web")
    })
    @CsvFileSource(resources = "/succsesSearchItemTest.csv")
    @ParameterizedTest(name = "В поисковой выдаче интернет-магазина METRO присутствует {0}")
    @DisplayName(("Проверка поисковой выдачи"))
    void succsesSearchItemSecondTest(String searchQuery){

        openMetroPage();
        closedModal();
        inputSearch.setValue(searchQuery).pressEnter();
        titleText.shouldHave(Condition.text(searchQuery));

    }

    @Tags({
            @Tag("smoke"),
            @Tag("web")
    })
    @CsvFileSource(resources = "/succsesSearchItemTest.csv")
    @ParameterizedTest(name = "Поисковая выдача не пустая для запроса товара - {0}")
    @DisplayName(("Проверка поисковой выдачи"))
    void SearchResultNotEmptyTest(String searchQuery){

        openMetroPage();
        closedModal();
        inputSearch.setValue(searchQuery).pressEnter();
        itemSearch.shouldHave(CollectionCondition.sizeGreaterThan(0));

    }


    static Stream<Arguments> warTunderLocaleTestBase(){
        return Stream.of(
                Arguments.of("DEUTSCH", List.of("SPIEL", "MEDIEN", "AUSBILDUNGSKURSE", "WERKSTATT", "COMMUNITY", "ESPORT", "Jetzt anmelden!")),
                Arguments.of("ENGLISH", List.of("GAME", "MEDIA", "TUTORIALS", "WORKSHOP", "COMMUNITY", "ESPORT", "Register now!"))
        );
    }

    @Tags({
            @Tag("smoke"),
            @Tag("web")
    })
    @MethodSource("warTunderLocaleTestBase")
    @ParameterizedTest(name = "Меню хедера на {0} языке имеет заголовки и кнопку: {1}")
    void warTunderLocaleTest(String locale, List<String> expectedButtons){

        openWarTunderPage();
        localeButton.click();
        localeList.find(Condition.text(locale)).click();
        menuList.filter(Condition.visible).should(CollectionCondition.texts(expectedButtons));

    }

}
