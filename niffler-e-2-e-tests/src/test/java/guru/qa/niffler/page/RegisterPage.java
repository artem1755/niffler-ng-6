package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitPasswordInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successRegisterMessage = $(".form__paragraph_success");
    private final SelenideElement formError = $(".form__error");


    @Nonnull
    @Step("Ввести имя пользователя: {username}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return new RegisterPage();
    }

    @Nonnull
    @Step("Ввести пароль: {password}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return new RegisterPage();
    }

    @Nonnull
    @Step("Повторить пароль: {submitPassword}")
    public RegisterPage setPasswordSubmit(String submitPassword) {
        submitPasswordInput.setValue(submitPassword);
        return new RegisterPage();
    }

    @Nonnull
    @Step("Отправить форму регистрации")
    public RegisterPage submitRegistration() {
        submitButton.click();
        return new RegisterPage();
    }

    @Step("Проверить успешное сообщение о регистрации: {value}")
    public void checkSuccessRegisterNewUser(String value) {
        successRegisterMessage.shouldHave(text(value)).shouldBe(visible);
    }

    @Step("Проверить сообщение об ошибке: {value}")
    public void checkFormErrorText(String value) {
        formError.shouldHave(text(value)).shouldBe(visible);
    }

    @Step("Регистраиця по имени пользователя и паролю")
    public RegisterPage register(String username, String password, String passwordSubmit) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitPasswordInput.setValue(passwordSubmit);
        submitButton.click();
        return new RegisterPage();
    }

    @Step("Проверка, что форма содержит сообщение об ошибке")
    public void checkThatFormContainsErrorMessage(String errorMassage){
        formError.shouldHave(text(errorMassage)).shouldBe(visible);
    }

    @Step("Проверка, что отображается сообщение об успехе")
    public void checkThatParagrapthContainsSuccessMessage(String successMessage){
        successRegisterMessage.shouldHave(text(successMessage)).shouldBe(visible);
    }
}
