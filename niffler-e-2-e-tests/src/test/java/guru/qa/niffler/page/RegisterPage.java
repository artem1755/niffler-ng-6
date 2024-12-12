package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement successRegistrationMessage = $("p.form__paragraph");
    private final SelenideElement formError = $("span.form__error");


    @Step("Регистраиця по имени пользователя и паролю")
    public RegisterPage register(String username, String password, String passwordSubmit) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(passwordSubmit);
        submitButton.click();
        return new RegisterPage();
    }

    @Step("Проверка, что отображается сообщение об успехе")
    public void checkThatParagrapthContainsSuccessMessage(String successMessage){
        successRegistrationMessage.shouldHave(text(successMessage)).shouldBe(visible);
    }

    @Step("Проверка, что форма содержит сообщение об ошибке")
    public void checkThatFormContainsErrorMessage(String errorMassage){
        formError.shouldHave(text(errorMassage)).shouldBe(visible);
    }
}
