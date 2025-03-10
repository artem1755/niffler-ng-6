package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.SelenideUtills;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

public class LoginTest {
    private static final Config CFG = Config.getInstance();
    private static final String STATISTICS_TEXT = "Statistics";
    private static final String HISTORY_OF_SPENDING_TEXT = "History of Spendings";
    private static final String FAILED_LOGIN_MESSAGE = "Неверные учетные данные пользователя";
    SelenideDriver chrome = new SelenideDriver(SelenideUtills.chromeConfig);

    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();

    @User(
            categories = {
                    @Category(name = "cat_1", archived = false),
                    @Category(name = "cat_2", archived = true),
            },
            spendings = {
                    @Spending(
                            category = "cat_3",
                            description = "test_spend",
                            amount = 100
                    )
            }
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        chrome.open(LoginPage.URL);
                new LoginPage(chrome).login(user.username(), user.testData().password());
        new MainPage().checkStatisticsHeaderContainsText(STATISTICS_TEXT)
                .checkHistoryOfSpendingHeaderContainsText(HISTORY_OF_SPENDING_TEXT);
    }


    @User(
            categories = {
                    @Category(name = "cat_1", archived = false),
                    @Category(name = "cat_2", archived = true),
            },
            spendings = {
                    @Spending(
                            category = "cat_3",
                            description = "test_spend",
                            amount = 100
                    )
            }
    )
    @Test
    void extTest(UserJson user) {

        chrome.open(LoginPage.URL);
        new LoginPage(chrome).login(user.username(), user.testData().password());
        new MainPage().checkStatisticsHeaderContainsText(STATISTICS_TEXT)
                .checkHistoryOfSpendingHeaderContainsText(HISTORY_OF_SPENDING_TEXT);
    }


    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        SelenideDriver firefox = new SelenideDriver(SelenideUtills.firefoxConfig);
        browserExtension.drivers().addAll(List.of(chrome,firefox));

        firefox.open("https://habr.com/");
        chrome.open(LoginPage.URL);
        new LoginPage(chrome).setUsername(RandomDataUtils.randomUserName())
                .setPassword("BAD")
                .submitButtonClick()
                .checkFormErrorText(FAILED_LOGIN_MESSAGE);
    }
}
