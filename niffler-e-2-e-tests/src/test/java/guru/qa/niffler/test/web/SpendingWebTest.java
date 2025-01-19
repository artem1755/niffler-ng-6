package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import guru.qa.niffler.utils.SelenideUtills;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;
import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();
    SelenideDriver driver = new SelenideDriver(SelenideUtills.chromeConfig);


    @User(
            username = "duck",
            categories = @Category(
                    archived = false
            ),
            spendings = @Spending(
                    category = "Обучение31",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChanged(SpendJson[] spends) {
        SpendJson spend = spends[0];
        final String newDescription = "Обучение Niffler Next Generation5";

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @Test
    void shouldRegisterNewUser() {
        final String successMessage = "Congratulations! You've registered!";

        driver.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccBtn()
                .register(randomUserName(), "qwerty", "qwerty")
                .checkThatParagrapthContainsSuccessMessage(successMessage);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        final String username = "duck";
        final String errorMassage = String.format("Username `%s` already exists", username);

        driver.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccBtn()
                .register(username, "person1", "person1")
                .checkThatFormContainsErrorMessage(errorMassage);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        final String errorMassage = String.format("Passwords should be equal");

        driver.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccBtn()
                .register(randomUserName(), "qwerty", "qwertyy")
                .checkThatFormContainsErrorMessage(errorMassage);
    }


    @Test
    void mainPageShouldBeDisplaydAfterSuccessLogin() {
        final String h2 = "History of Spendings";

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .checkThatHeaderContainsText(h2);
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .login("user11", "password");

        new LoginPage(driver).checkIsStillOnLoginPage();
    }

    @User(
            username = "duck",
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson[] spends) {
//        Configuration.timeout = 10000;
        SpendJson spend = spends[0];
        final String newDescription = "Обучение Niffler Next Generation";

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();
        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void addSpendTest(UserJson user) {
        String category = randomUserName();
        String description = randomSentence(2);

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .addSpendingPage()
                .setSpendingCategory(category)
                .setNewSpendingDescription(description)
                .setSpendingAmount("10")
                .getCalendar()
                .selectDateInCalendar(new Date());
        new MainPage().checkThatTableContainsSpending(description);
    }

    @User
    @Test
    void addSpendAndCheckAlertTest(UserJson user) {
        String category = RandomDataUtils.randomCategory();
        String description = randomSentence(2);

        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .addSpendingPage()
                .setSpendingCategory(category)
                .setNewSpendingDescription(description)
                .setSpendingAmount("10")
                .getCalendar()
                .selectDateInCalendar(new Date())
                .checkAlert("New spending is successfully created");
        new MainPage().checkThatTableContainsSpending(description);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest("img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
        StatComponent statComponent = driver.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getStatComponent();

        assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");

        statComponent.checkBubbles(Color.yellow);
    }


    @User(
            categories = {
                    @Category(name = "Обучение1"),
                    @Category(name = "Продукты1", archived = true)
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 3100
                    ),
                    @Spending(
                            category = "Продукты",
                            description = "Покупка продуктов",
                            amount = 1000
                    )
            }
    )
    @ScreenShotTest(value = "img/archived-stat.png")
    void checkStatComponentAfterArchivedCategoryTest(UserJson user, BufferedImage expectedStatisticImage) throws IOException, InterruptedException {
        driver.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password());

        new MainPage().checkStatisticCells(List.of("Обучение 3100 ₽", "Продукты 1000 ₽"));
        Thread.sleep(10000);
        new MainPage().checkStatisticImage(expectedStatisticImage);
    }


    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/edit-stat.png")
    void checkStatComponentAfterEditSpendTest(UserJson user, BufferedImage expectedStatisticImage) throws IOException, InterruptedException {
        driver.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getSpendingTable()
                .editSpending("Обучение Advanced 2.0")
                .setNewSpendingAmount(80000)
                .save();
        Thread.sleep(5000);
        new MainPage().checkStatisticCells(List.of("Обучение 80000 ₽"));
        Thread.sleep(3000);
        new MainPage().checkStatisticImage(expectedStatisticImage);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Advanced",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/clear-stat.png")
    void checkStatComponentAfterDeleteSpendTest(UserJson user, BufferedImage expectedStatisticImage) throws IOException, InterruptedException {
//        Configuration.timeout = 10000;
        driver.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .deleteSpending("Advanced");
        Thread.sleep(3000);
        new MainPage().checkStatisticImage(expectedStatisticImage);
    }


    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void checkStatBubbleContent(UserJson user) throws InterruptedException {
        StatComponent statComponent = driver.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getStatComponent();
        Thread.sleep(3000);
        Bubble bubble = new Bubble(Color.yellow, "Обучение 79990 ₽");
        statComponent.checkBubbles(bubble);
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 1000
                    ),
                    @Spending(
                            category = "Развлечения",
                            description = "Поход в кино",
                            amount = 100
                    )
            }
    )
    @Test
    void checkStatBubblesInAnyOrder(UserJson user) throws InterruptedException {
        StatComponent statComponent = driver.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getStatComponent();
        Thread.sleep(3000);
        Bubble bubble1 = new Bubble(Color.yellow, "Обучение 1000 ₽");
        Bubble bubble2 = new Bubble(Color.green, "Развлечения 100 ₽");
        statComponent.checkBubblesInAnyOrder(bubble2, bubble1);
    }


    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 1000
                    ),
                    @Spending(
                            category = "Развлечения",
                            description = "Поход в кино",
                            amount = 100
                    )
            }
    )
    @Test
    void checkStatBubbleContainsAmongOtherBubbles(UserJson user) throws InterruptedException {
        StatComponent statComponent = driver.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getStatComponent();
        Thread.sleep(3000);
        Bubble bubble = new Bubble(Color.yellow, "Обучение 1000 ₽");
        statComponent.checkBubblesContains(bubble);
    }


    @User(
            categories = {
                    @Category(name = "Обучение1"),
                    @Category(name = "Развлечения2")
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 1000
                    ),
                    @Spending(
                            category = "Развлечения",
                            description = "Поход в кино",
                            amount = 100
                    )
            }
    )
    @Test
    void checkSpendsExistInTable(UserJson user) throws InterruptedException {
        SpendingTable spendingTable = driver.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getSpendingTable();
        Thread.sleep(3000);
        // Извлекаем список SpendJson и передаем его в checkSpendingTable
        List<SpendJson> expectedSpends = user.testData().spends();
        spendingTable.checkSpendingTable(expectedSpends.toArray(new SpendJson[0]));
    }

}
