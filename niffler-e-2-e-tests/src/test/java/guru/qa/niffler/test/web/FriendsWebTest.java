package guru.qa.niffler.test.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();


    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Configuration.timeout = 10000; // Устанавливаем глобальный тайм-аут


        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickProfileAvatar()
                .clickMyFriendLink()
                .shouldHaveFriendRequestListHeader("Friend requests")
                .shouldBePresentInRequestsTable(user.testData().incomeInvites().get(0));
    }

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserJson user) {
        Configuration.timeout = 10000; // Устанавливаем глобальный тайм-аут

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickProfileAvatar()
                .clickMyFriendLink()
                .shouldHaveMyFriendsListHeader("My friends")
                .shouldBePresentInFriendsTable(user.testData().friends().get(0));
    }

    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Configuration.timeout = 10000; // Устанавливаем глобальный тайм-аут

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickProfileAvatar()
                .clickAllPeopleLink()
                .shouldBePresentInAllPeopleTableAndCheckStatus(user.testData().outcomeInvites().get(0),"Waiting...");
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void acceptInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickProfileAvatar()
                .clickMyFriendLink()
                .acceptFriend()
                .shouldHaveMyFriendsListHeader("My friends")
                .checkUnfriendButtonIsVisible();
    }

    @User(
            incomeInvitations = 1
    )
    @Test
    void declineInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .clickProfileAvatar()
                .clickMyFriendLink()
                .declineFriend()
                .checkThatShouldHaveEmptyFriendsTable("There are no users yet");
    }

}
