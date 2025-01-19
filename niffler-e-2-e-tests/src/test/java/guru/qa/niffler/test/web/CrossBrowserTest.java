package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.CrossBrowserWebTest;
import guru.qa.niffler.jupiter.converters.BrowserArgumentConverter;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;
import guru.qa.niffler.utils.Browser;

@CrossBrowserWebTest
public class CrossBrowserTest {
    @ParameterizedTest
    @EnumSource(Browser.class)
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(
            @ConvertWith(BrowserArgumentConverter.class) SelenideDriver driver) {
        driver.open(LoginPage.URL);
        new LoginPage(driver).setUsername(RandomDataUtils.randomUserName())
                .setPassword("BAD")
                .submitButtonClick()
                .checkFormErrorText("Неверные учетные данные пользователя");
    }
}
