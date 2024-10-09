package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.RegisterPage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

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
            .register("person123", "person1","person1")
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
            .register("username", "person1","person12")
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

}
