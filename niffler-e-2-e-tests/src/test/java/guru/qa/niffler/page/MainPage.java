package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement header = $("div#spendings h2");
  private final SelenideElement profileIcon = $(".MuiAvatar-root");
  private final SelenideElement myfriendsLink = $("a.nav-link[href='/people/friends']");
  private final SelenideElement allPeopleLink = $("a.nav-link[href='/people/all']");
  private final SelenideElement profilePageLink = $("a[href='/profile']");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public void checkThatHeaderContainsText(String header){

    this.header.shouldHave(text(header)).shouldBe(visible);
  }

  public MainPage clickProfileAvatar(){
     profileIcon.click();
     return this;
  }

  public ProfilePage clickProfilePageLink(){
    profilePageLink.click();
    return new ProfilePage();
  }

  public FriendsPage clickMyFriendLink(){
    myfriendsLink.click();
    return new FriendsPage();
  }
  public FriendsPage clickAllPeopleLink(){
    allPeopleLink.click();
    return new FriendsPage();
  }



}
