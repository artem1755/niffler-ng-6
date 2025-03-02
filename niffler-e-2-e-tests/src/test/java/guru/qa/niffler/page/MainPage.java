package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statisticsHeader = $x("//h2[text()='Statistics']");
  private final SelenideElement historyOfSpendingHeader = $x("//h2[text()='History of Spendings']");
  private final SelenideElement searchInput = $("input[type='text']");
  private final SelenideElement historyOfSpendingsTitle = $("div#spendings h2");
  private final ElementsCollection statisticCells = $$("#legend-container li");
  private final SelenideElement statisticCanvas = $("canvas[role='img']");
  private final SelenideElement popup = $("div[role='dialog']");
  private final SelenideElement deleteBtn = $("#delete");
  private final SelenideElement stat = $("#stat");
  private final SelenideElement errorContainer = $(".form__error");;

  @Getter
  private final SpendingTable<MainPage> spendingTable = new SpendingTable<>($(".MuiTableContainer-root"), this);
  @Getter
  private final SearchField<MainPage> searchField = new SearchField<>(searchInput, this);
  @Getter
  private final StatComponent<MainPage> statComponent = new StatComponent<>(stat, this);



  @Nonnull
  @Step("Редактировать трату с описанием: {spendingDescription}")
  public EditSpendingPage editSpending(String spendingDescription) {
    searchField.clearIfNotEmpty()
            .search(spendingDescription);
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Step("Проверить, что таблица содержит трату с описанием: {spendingDescription}")
  public void checkThatTableContainsSpending(String spendingDescription) {
    searchField.clearIfNotEmpty()
            .search(spendingDescription);
    tableRows.find(text(spendingDescription)).should(visible);
  }

  @Nonnull
  @Step("Проверить, что заголовок статистики содержит текст: {value}")
  public MainPage checkStatisticsHeaderContainsText(String value) {
    statisticsHeader.shouldHave(text(value)).shouldBe(visible);
    return this;
  }

  @Step("Проверить, что заголовок истории трат содержит текст: {value}")
  public void checkHistoryOfSpendingHeaderContainsText(String value) {
    historyOfSpendingHeader.shouldHave(text(value)).shouldBe(visible);
  }

  @Step("Проверка отображения заголовка")
  public void checkThatHeaderContainsText(String headertext){
    this.historyOfSpendingsTitle.shouldHave(text(headertext)).shouldBe(visible);
  }

  @Step("Проверка, что ячейка статистики содержит текст {texts}")
  @Nonnull
  public MainPage checkStatisticCells(List<String> texts) {
    for (String text : texts) {
      statisticCells.findBy(text(text)).shouldBe(visible);
    }
    return this;
  }

  @Step("Проверка, что статистическое изображение соответствует ожидаемому изображению.")
  @Nonnull
  public MainPage checkStatisticImage(BufferedImage expectedImage) throws IOException {
    statisticCanvas.shouldBe(visible);
    BufferedImage actualImage = ImageIO.read(statisticCanvas.screenshot());
    assertFalse(new ScreenDiffResult(actualImage, expectedImage));
    return this;
  }

  @Step("Удалить трату")
  public void deleteSpending(String description){
    SelenideElement row = tableRows.find(text(description));
    row.$$("td").get(0).shouldBe(visible).click();
    deleteBtn.click();
    popup.$(byText("Delete")).click(usingJavaScript());
  }

  @Step("Проверка ошибки на странице: {error}")
  @Nonnull
  public void checkError(String error) {
    errorContainer.shouldHave(text(error));
//    return this;
  }



}
