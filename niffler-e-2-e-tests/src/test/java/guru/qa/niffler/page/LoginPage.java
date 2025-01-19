package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

  private final SelenideElement usernameInput;
  private final SelenideElement passwordInput;
  private final SelenideElement submitButton;
  private final SelenideElement createNewAccountButton;
  private final SelenideElement formError;
  private final SelenideElement createNewAccBtn;

  public static final String URL = CFG.authUrl() + "login";


  public LoginPage(SelenideDriver driver) {
    super(driver);
    this.usernameInput = driver.$("input[name='username']");
    this.passwordInput = driver.$("input[name='password']");
    this.submitButton = driver.$("button[type='submit']");
    this.createNewAccountButton = driver.$(".form__register");
    this.formError = driver.$(".form__error");
    this.createNewAccBtn = driver.$(".form__register");
  }

  public LoginPage( ) {
    this.usernameInput = Selenide.$("input[name='username']");
    this.passwordInput = Selenide.$("input[name='password']");
    this.submitButton = Selenide.$("button[type='submit']");
    this.createNewAccountButton = Selenide.$(".form__register");
    this.formError = Selenide.$(".form__error");
    this.createNewAccBtn = Selenide.$(".form__register");
  }




  @Nonnull
  @Step("Ввести имя пользователя: {username}")
  public LoginPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  @Nonnull
  @Step("Ввести пароль")
  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Nonnull
  @Step("Нажать кнопку отправки формы")
  public LoginPage submitButtonClick() {
    submitButton.click();
    return this;
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
//    webdriver().shouldHave(urlContaining("/login"));
  }

  @Step("Нажать на кнопку создать новый аккаунт")
  public RegisterPage clickCreateNewAccBtn(){
    createNewAccBtn.click();
    return new RegisterPage();
  }
}
