package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {
    private final SelenideElement myFriendsListHeader = $x("//h2[text()='My friends']");
    private final SelenideElement panelFriends = $("#simple-tabpanel-friends");
    private final SelenideElement friendsRequestListHeader = $x("//h2[text()='Friend requests']");
    private final ElementsCollection requestsRows = $$x("//tbody[@id='requests']/tr");
    private final ElementsCollection allPeopleRows = $$("tbody#all tr");
    private final SelenideElement searchInput = $("input[type='text']");
    private final ElementsCollection friendsRows = $$x("//tbody[@id='friends']/tr");
    private final SelenideElement acceptButton = $(byText("Accept"));
    private final SelenideElement unfriendButton = $("button[class*='MuiButton-containedSecondary']");
    private final SelenideElement declineButton = $(byText("Decline"));
    private final SelenideElement confirmDeclineButton = $(".MuiPaper-root button.MuiButtonBase-root.MuiButton-containedPrimary");


    @Step("Проверка отображения заголовка списка друзей")
    public FriendsPage shouldHaveMyFriendsListHeader(String expectedHeaderText) {
        myFriendsListHeader.shouldHave(text(expectedHeaderText)).shouldBe(visible);
        return this;
    }

    @Step("Проверка наличия запроса в друзья")
    public FriendsPage shouldHaveFriendRequestListHeader(String expectedHeaderText) {
        friendsRequestListHeader.shouldHave(text(expectedHeaderText)).shouldBe(visible);
        return this;
    }

    @Step("Проверка наличия друга {friendName} в списке запросов")
    public void shouldBePresentInRequestsTable(String friend) {
        searchFriend(friend);
        requestsRows.findBy(text(friend)).shouldBe(visible);
    }

    @Step("Проверка наличия друга {friendName} в списке друзей")
    public void shouldBePresentInFriendsTable(String friendName) {
        searchFriend(friendName);
        friendsRows.findBy(text(friendName)).shouldBe(visible);
    }

    @Step("Проверка наличия {name} с статусом {status} в таблице \"all people\"")
    public FriendsPage shouldBePresentInAllPeopleTableAndCheckStatus(String name, String status) {
        searchFriend(name);
        allPeopleRows.findBy(text(name)).$("span").shouldHave(text(status)).shouldBe(visible);
        return this;
    }

    @Step("Найти друга по имени")
    public void searchFriend(String friendName) {
        searchInput.setValue(friendName).pressEnter();
    }

    @Step("Проверка что в списке друзей будет пусто")
    public void checkThatShouldHaveEmptyFriendsTable(String emptyMessage) {
        panelFriends.shouldHave(text(emptyMessage));
    }

    @Step("Принять заявку в друзья")
    public FriendsPage acceptFriend() {
        acceptButton.click();
        return this;
    }

    @Step("Проверить наличие кнопки 'Удалить из друзей'")
    public void checkUnfriendButtonIsVisible() {
        unfriendButton.shouldBe(visible);
    }

    @Step("Отклонить заявку в друзья")
    public FriendsPage declineFriend() {
        declineButton.click();
        confirmDeclineButton.click();
        return this;
    }

}
