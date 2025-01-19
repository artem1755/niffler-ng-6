package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.utils.RandomDataUtils.randomName;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProfileTest {
    private static final Config CFG = Config.getInstance();

    @User(
            username = "duck",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson[] categories) throws InterruptedException {
        CategoryJson category = categories[0];
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .getHeader()
                .toProfilePage()
                .clickArchiveButtonForCategoryName(category.name())
                .clickArchiveButtonSubmit()
                .checkAlert(category.name())
                .shouldNotVisibleArchiveCategory(category.name());
    }

    @User(
            username = "duck",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson[] categories) throws InterruptedException {
        CategoryJson category = categories[0];
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .getHeader()
                .toProfilePage()
                .clickShowArchiveCategoryButton()
                .clickUnarchiveButtonForCategoryName(category.name())
                .clickUnarchiveButtonSubmit()
                .checkAlert(category.name())
                .clickShowArchiveCategoryButton()
                .shouldVisibleActiveCategory(category.name());
    }

    @User
    @Test
    void changeNameAndCheckAlertTest(UserJson user) {
        String name = randomName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .setName(name)
                .clickSaveButton()
                .checkAlert("Profile successfully updated")
                .checkName(name);
    }


    @User
    @Test
    void shouldUpdateProfileWithAllFieldsSet(UserJson user) {
        final String newName = randomName();

        ProfilePage profilePage = Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .uploadPhotoFromClasspath("img/cat.png")
                .setName(newName)
                .clickSaveButton()
                .checkAlert("Profile successfully updated");

        Selenide.refresh();

        profilePage.checkName(newName)
                .checkPhotoExist();
    }



    @User
    @ScreenShotTest(value = "img/profile-expected.png")
    void checkProfileImageTest(UserJson user, BufferedImage expectedProfileImage) throws IOException {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .toProfilePage()
                .uploadPhotoFromClasspath("img/cat.png")
                .clickSaveButton()
                .checkProfileImage(expectedProfileImage);
    }
}
