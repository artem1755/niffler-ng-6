package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.SelenideUtills;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest {
    RegisterPage registerPage = new RegisterPage();
    private static final Config CFG = Config.getInstance();
    final String REGISTERED_USER_NAME = "duck";
    final String EXPECTED_REGISTRATION_MESSAGE = "Congratulations! You've registered!";
    final String USERNAME_ALREADY_EXISTS_ERROR_TEXT = "Username `duck` already exists";
    final String PASSWORDS_NOT_EQUAL_ERROR_TEXT = "Passwords should be equal";
    String password = RandomDataUtils.randomPassword();
    SelenideDriver driver = new SelenideDriver(SelenideUtills.chromeConfig);

    @Test
    void shouldRegisterNewUser() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .submitCreateNewAccount();
        registerPage.setUsername(RandomDataUtils.randomUserName())
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessRegisterNewUser(EXPECTED_REGISTRATION_MESSAGE);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .submitCreateNewAccount();
        registerPage.setUsername(REGISTERED_USER_NAME)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkFormErrorText(USERNAME_ALREADY_EXISTS_ERROR_TEXT);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String password = RandomDataUtils.randomPassword();

        driver.open(CFG.frontUrl(), LoginPage.class)
                .submitCreateNewAccount();
        registerPage.setUsername(RandomDataUtils.randomUserName())
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkFormErrorText(PASSWORDS_NOT_EQUAL_ERROR_TEXT);
    }
}
