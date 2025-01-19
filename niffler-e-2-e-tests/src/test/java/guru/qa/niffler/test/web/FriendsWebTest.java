package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtills;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();
    SelenideDriver driver = new SelenideDriver(SelenideUtills.chromeConfig);


    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .shouldHaveMyFriendsListHeader("My friends")
//                .shouldBePresentInFriendsTable(user.testData().friends().get(0));
                .shouldBePresentInFriendsTable(user.testData().friendsUsernames()[0]);
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .shouldHaveEmptyFriendsTable("There are no users yet");
    }

    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .shouldFriendRequestList("Friend requests")
                .shouldBePresentInRequestsTable(user.testData().incomeInvitationsUsernames()[0]);
    }

    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toAllPeoplesPage()
                .shouldBePresentInAllPeopleTableAndCheckStatus(user.testData().outcomeInvitationsUsernames()[0], "Waiting...");
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void acceptInvitation(UserJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .acceptFriend()
                .shouldHaveMyFriendsListHeader("My friends")
                .checkUnfriendButtonIsVisible();
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void declineInvitation(UserJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .declineFriend()
                .shouldHaveEmptyFriendsTable("There are no users yet");
    }
}
