package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {
    private final SelenideElement myFriendsListHeader = $x("//h2[text()='My friends']");
    private final SelenideElement panelFriends = $("#simple-tabpanel-friends");
    private final SelenideElement friendsRequestListHeader = $x("//h2[text()='Friend requests']");
    private final ElementsCollection requestsRows = $$x("//tbody[@id='requests']/tr");
    private final ElementsCollection allPeopleRows = $$("tbody#all tr");
    private final SelenideElement searchInput = $("input[type='text']");
    private final ElementsCollection friendsRows = $$x("//tbody[@id='friends']/tr");

    private SelenideElement getFriendItem(String friendName) {
        return $x("//p[contains(text(), '" + friendName + "')]");
    }

    public FriendsPage shouldHaveMyFriendsListHeader(String expectedHeaderText) {
        myFriendsListHeader.shouldHave(text(expectedHeaderText)).shouldBe(visible);
        return this;
    }

    public FriendsPage shouldHaveFriendRequestListHeader(String expectedHeaderText) {
        friendsRequestListHeader.shouldHave(text(expectedHeaderText)).shouldBe(visible);
        return this;
    }

    public void shouldBePresentInRequestsTable(String friend) {
        searchFriend(friend);
        requestsRows.findBy(text(friend)).shouldBe(visible);
    }

    public void shouldBePresentInFriendsTable(String friendName) {
        searchFriend(friendName);
        friendsRows.findBy(text(friendName)).shouldBe(visible);
    }

    public FriendsPage shouldBePresentInAllPeopleTableAndCheckStatus(String name, String status) {
        searchFriend(name);
        allPeopleRows.findBy(text(name)).$("span").shouldHave(text(status)).shouldBe(visible);
        return this;
    }

    public void searchFriend(String friendName) {
        searchInput.setValue(friendName).pressEnter();
    }

    public void checkThatShouldHaveEmptyFriendsTable(String emptyMessage) {
        panelFriends.shouldHave(text(emptyMessage));
    }

    public FriendsPage checkThatShouldBeFriendRequestList(String expectedHeaderText) {
        friendsRequestListHeader.shouldHave(text(expectedHeaderText)).shouldBe(visible);
        return this;
    }

    public void checkThatShouldBePresentInRequestsTable(String friendName) {
        requestsRows.findBy(text(friendName)).shouldBe(visible);
    }

    public FriendsPage checkThatShouldBePresentInAllPeopleTableAndCheckStatus(String name, String status) {
        allPeopleRows.filter(text(name)).first().$("span").shouldHave(text(status)).shouldBe(visible);
        return this;
    }


}
