package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createNewAccountButton = $(".form__register");
  private final SelenideElement formError = $(".form__error");
  private final SelenideElement createNewAccBtn = $(".form__register");

  public static final String URL = CFG.authUrl() + "login";

  @Nonnull
  @Step("Ввести имя пользователя: {username}")
  public LoginPage setUsername(String username) {
    usernameInput.setValue(username);
    return new LoginPage();
  }

  @Nonnull
  @Step("Ввести пароль")
  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return new LoginPage();
  }

  @Nonnull
  @Step("Нажать кнопку отправки формы")
  public LoginPage submitButtonClick() {
    submitButton.click();
    return new LoginPage();
  }

  @Step("Перейти на страницу создания нового аккаунта")
  public void submitCreateNewAccount() {
    createNewAccountButton.click();
    new RegisterPage();
  }

  @Nonnull
  @Step("Авторизация с именем пользователя: {username} и паролем")
  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  @Step("Проверка текста ошибки: {value}")
  public void checkFormErrorText(String value) {
    formError.shouldHave(text(value)).shouldBe(visible);
  }

  @Step("Проверить, что пользователь на странице логина")
  public void checkIsStillOnLoginPage() {
    webdriver().shouldHave(urlContaining("/login"));
  }

  @Step("Нажать на кнопку создать новый аккаунт")
  public RegisterPage clickCreateNewAccBtn(){
    createNewAccBtn.click();
    return new RegisterPage();
  }
}
