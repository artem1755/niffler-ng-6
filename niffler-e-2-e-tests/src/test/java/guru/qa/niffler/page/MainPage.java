package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import guru.qa.niffler.page.component.Header;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement headerh2 = $("div#spendings h2");
  private final SelenideElement profileIcon = $(".MuiAvatar-root");
  private final SelenideElement myfriendsLink = $("a.nav-link[href='/people/friends']");
  private final SelenideElement allPeopleLink = $("a.nav-link[href='/people/all']");
  private final SelenideElement profilePageLink = $("a[href='/profile']");
  private final SelenideElement statComponent = $("#stat");
  private final SelenideElement spendingTable = $("#spendings");
  private final SelenideElement searchInput = $("input[type='text']");

  @Getter
  private final Header header = new Header();

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Step("Проверка отображения заголовка")
  public void checkThatHeaderContainsText(String headertext){
    this.headerh2.shouldHave(text(headertext)).shouldBe(visible);
  }

  @Step("Нажать на аватар профиля")
  public MainPage clickProfileAvatar(){
     profileIcon.click();
     return this;
  }

  @Step("Нажать на кнопку страницы профиля")
  public ProfilePage clickProfilePageLink(){
    profilePageLink.click();
    return new ProfilePage();
  }

  @Step("Нажать на кнопку страницы друзей")
  public FriendsPage clickMyFriendLink(){
    myfriendsLink.click();
    return new FriendsPage();
  }

  @Step("Нажать на кнопку списка всех пользователей")
  public FriendsPage clickAllPeopleLink(){
    allPeopleLink.click();
    return new FriendsPage();
  }

  @Step("Проверка, что страница загрузилась")
  public MainPage checkThatPageLoaded() {
    statComponent.should(visible).shouldHave(text("Statistics"));
    spendingTable.should(visible).shouldHave(text("History of Spendings"));
    return this;
  }

  @Step("Проверка, что страница трат содержит трату")
  public void checkThatTableContainsSpending(String spendingDescription) {
    searchSpend(spendingDescription);
    tableRows.find(text(spendingDescription)).should(visible);
  }

  @Step("Поиск траты")
  public void searchSpend(String spendingDescription) {
    searchInput.setValue(spendingDescription).pressEnter();
  }

}
