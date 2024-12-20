package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;
import guru.qa.niffler.page.component.Calendar;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");
  private final SelenideElement categoryInput = $("#category");
  private final SelenideElement amountInput = $("#amount");

  @Getter
  private final Calendar calendar = new Calendar($("input[name='date']"));

  @Step("Ввести новое описание траты: {description}")
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.setValue(description);
    return this;
  }

  @Step("Ввести название категории: {category}")
  public EditSpendingPage setSpendingCategory(String category) {
    categoryInput.setValue(category);
    return this;
  }

  @Step("Ввести стоимость траты: {amount}")
  public EditSpendingPage setSpendingAmount(String amount) {
    amountInput.setValue(amount);
    return this;
  }

  @Step("Нажать на кнопку сохранить")
  public void save() {
    saveBtn.click();
  }
}
