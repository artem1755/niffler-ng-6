package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();
  Faker faker = new Faker();



  @Spending(
      username = "duck",
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
  )
  @Test
  void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
    final String newDescription = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("duck", "12345")
        .editSpending(spend.description())
        .setNewSpendingDescription(newDescription)
        .save();

    new MainPage().checkThatTableContainsSpending(newDescription);
  }

  @Test
  void shouldRegisterNewUser(){
    final String successMessage = "Congratulations! You've registered!";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .clickCreateNewAccBtn()
            .register("user_"+faker.artist().name(), "qwerty","qwerty")
            .checkThatParagrapthContainsSuccessMessage(successMessage);
  }

  @Test
  void shouldNotRegisterUserWithExistingUsername(){
    final String username = "duck";
    final String errorMassage = String.format("Username `%s` already exists",username);

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .clickCreateNewAccBtn()
            .register(username, "person1","person1")
            .checkThatFormContainsErrorMessage(errorMassage);
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){
    final String errorMassage = String.format("Passwords should be equal");

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .clickCreateNewAccBtn()
            .register("user_"+faker.artist().name(), "qwerty","qwertyy")
            .checkThatFormContainsErrorMessage(errorMassage);
  }

  @Test
  void mainPageShouldBeDisplaydAfterSuccessLogin(){
    final String h2 = "History of Spendings";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("duck", "12345")
            .checkThatHeaderContainsText(h2);
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithBadCredentials(){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("user11", "password");

    new LoginPage().checkIsStillOnLoginPage();
  }


  @Category(
          username = "duck",
          archived = false
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("duck", "12345");

    new MainPage()
            .clickProfileAvatar()
            .clickProfilePageLink()
            .searchCategoryBlock(category.name())
            .clickArchivedBtn()
            .clickArchivedBtnConfirm()
            .checkThatSuccessMessageBlockHasText("Category " + category.name() +" is archived")
            .clickShowArchivedCheckbox()
            .checkThatCategoryIsInTheList(category.name());
  }



//  @DisabledByIssue("2")
  @Category(
          username = "duck",
          archived = true
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("duck", "12345");

    new MainPage()
            .clickProfileAvatar()
            .clickProfilePageLink()
            .clickShowArchivedCheckbox()
            .searchCategoryBlock(category.name())
            .clickUnarchivedBtn()
            .clickUnarchivedBtnConfirm()
            .checkThatSuccessMessageBlockHasText("Category " + category.name() +" is unarchived")
            .clickShowArchivedCheckbox()
            .checkThatCategoryIsInTheList(category.name());
  }

}
