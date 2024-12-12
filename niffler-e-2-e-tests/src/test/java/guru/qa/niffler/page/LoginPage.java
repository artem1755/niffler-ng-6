package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createNewAccBtn = $(".form__register");

  @Step("Авторизация с именем пользователя: {username} и паролем")
  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  @Step("Нажать на кнопку создать новый аккаунт")
  public RegisterPage clickCreateNewAccBtn(){
    createNewAccBtn.click();
    return new RegisterPage();
  }

  @Step("Проверить, что пользователь на странице логина")
  public void checkIsStillOnLoginPage() {
    Assertions.assertTrue(
            WebDriverRunner.url().contains("/login"), "Пользователь не на странице логина");
  }

  @Step("Успешная авторизация с именем пользователя: {username} и паролем")
  public MainPage successLogin(String username, String password) {
    login(username, password);
    return new MainPage();
  }
}
