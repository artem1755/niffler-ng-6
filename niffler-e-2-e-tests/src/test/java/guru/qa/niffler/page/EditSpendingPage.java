package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;
import guru.qa.niffler.page.component.Calendar;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement categoryInput = $("#category");
  private final SelenideElement calendarInput = $("input[name='date']");

  @Getter
  private final Calendar<EditSpendingPage> calendar = new Calendar<>(calendarInput, this);


  @Nonnull
  @Step("Установить новое описание траты: {description}")
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  @Step("Ввести стоимость траты: {amount}")
  public EditSpendingPage setSpendingAmount(String amount) {
    amountInput.setValue(amount);
    return this;
  }

  @Step("Ввести название категории: {category}")
  public EditSpendingPage setSpendingCategory(String category) {
    categoryInput.setValue(category);
    return this;
  }

  @Step("Сохранить изменения по трате")
  public void save() {
    saveBtn.click();
  }

  @Step("Установить новую сумму траты: {0}")
  @Nonnull
  public EditSpendingPage setNewSpendingAmount(double amount) {
    String formattedAmount = amount % 1 == 0 ? String.format("%.0f", amount) : String.valueOf(amount);
    amountInput.clear();
    amountInput.setValue(formattedAmount);
    return this;
  }
}