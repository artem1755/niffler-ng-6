package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SpendingTable<T extends BasePage<?>> extends BaseComponent<T> {

    private static final ElementsCollection timePeriods = $$("[role='option']");

    private static final String spendingRow = "tbody tr";
    private static final String spendingColumn = "td:nth-child(4)";
    private final ElementsCollection tableRows = self.$("tbody").$$("tr");
    private final SelenideElement popup = $("div[role='dialog']");
    private final SelenideElement deleteBtn = self.$("#delete");

    public SpendingTable(SelenideElement spends, T page) {
        super(spends, page);
    }

    @Step("Выбор периода для отображения трат: {period}")
    public SpendingTable<T> selectPeriod(String period) {
        self.$("#period").click();
        timePeriods.find(text(period)).click();
        return this;
    }

    @Step("Изменения описания траты на: {spendingDescription}")
    public EditSpendingPage editSpending(String description) {
        self.$$(spendingRow).find(text(description)).$(" [aria-label='Edit spending']").shouldBe(visible).click();
        return new EditSpendingPage();
    }


    @Step("Delete spending with description {0}")
    @Nonnull
    public SpendingTable deleteSpending(String description) {
        searchSpendingByDescription(description);
        SelenideElement row = tableRows.find(text(description));
        row.$$("td").get(0).click();
        deleteBtn.click();
        popup.$(byText("Delete")).click(usingJavaScript());
        return this;
    }

    @Step("Поиск траты с описанием: {description}")
    public SpendingTable<T> searchSpendingByDescription(String description) {
        $(".MuiTableContainer-root").$(spendingRow).$$("tr").find(text(description)).shouldBe(visible);
        return this;
    }

    @Step("Проверка, что таблица содержит траты: {expectedSpends}")
    public SpendingTable<T> checkTableContains(String... expectedSpends) {
        self.$(spendingRow).$("td").$$(spendingColumn).shouldHave(textsInAnyOrder(expectedSpends));
        return this;
    }

    @Step("Проверка, что количество трат равно: {expectedSize}")
    public SpendingTable<T> checkTableSize(int expectedSize) {
        self.$(spendingRow).$$("tr").shouldHave(size(expectedSize));
        return this;
    }
}
