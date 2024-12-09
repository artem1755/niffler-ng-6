package guru.qa.niffler.test.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

@User(
        username = "duck",
        categories = @Category(
                archived = false
        ),
        spendings = @Spending(
                category = "Обучение2",
                description = "Обучение Advanced 2.0",
                amount = 79990
        )
)
  @Test
  void categoryDescriptionShouldBeChangedFromTable2(SpendJson spend) {
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
            .register(randomUserName(), "qwerty","qwerty")
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
            .register(randomUserName(), "qwerty","qwertyy")
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


@User(
        username = "duck",
        categories = @Category(
                archived = false
        )
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


@User(
        username = "duck",
        categories = @Category(
                archived = true
        )
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
    Configuration.timeout = 10000;
    SpendJson spend = spends[0];
    final String newDescription = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("duck", "12345")
            .editSpending(spend.description())
            .setNewSpendingDescription(newDescription)
            .save();
    new MainPage().checkThatTableContainsSpending(newDescription);
  }

}
