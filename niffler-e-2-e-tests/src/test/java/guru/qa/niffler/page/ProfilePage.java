package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final SelenideElement unarchiveButtonSubmit = $x("//button[text()='Unarchive']");
    private final SelenideElement saveButton = $x("//button[text()='Save changes']");
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement successArchiveMessage = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    private final SelenideElement successUnarchiveMessage = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    private final SelenideElement showArchiveCategoryButton = $x("//input[@type='checkbox']");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement photoInput = $("input[type='file']");
    private final SelenideElement avatar = $("#image__input").parent().$("img");
    private final SelenideElement profileImage = $(".MuiAvatar-img");


    @Step("Архивировать категорию с названием: {categoryName}")
    public ProfilePage clickArchiveButtonForCategoryName(String categoryName) {
        SelenideElement archiveButtonInRow = categoryList
                .find(text(categoryName))
                .parent().$(".MuiIconButton-sizeMedium[aria-label='Archive category']");
        archiveButtonInRow.shouldBe(visible).click();
        return this;
    }

    @Nonnull
    @Step("Разархивировать категорию с названием: {categoryName}")
    public ProfilePage clickUnarchiveButtonForCategoryName(String categoryName) {
        SelenideElement unarchiveButtonInRow = categoryList
                .find(text(categoryName))
                .parent().$("[data-testid='UnarchiveOutlinedIcon']");
        unarchiveButtonInRow.shouldBe(visible).click();
        return this;
    }

    @Nonnull
    @Step("Нажать кнопку для показа архивных категорий")
    public ProfilePage clickShowArchiveCategoryButton() {
        Selenide.executeJavaScript("arguments[0].scrollIntoView(true);", showArchiveCategoryButton);
        Selenide.executeJavaScript("arguments[0].click();", showArchiveCategoryButton);
        return this;
    }

    @Nonnull
    @Step("Подтвердить архивирование категории")
    public ProfilePage clickArchiveButtonSubmit() {
        archiveButtonSubmit.click();
        return this;
    }

    @Nonnull
    @Step("Подтвердить разархивирование категории")
    public ProfilePage clickUnarchiveButtonSubmit() {
        unarchiveButtonSubmit.click();
        return this;
    }

    @Step("Проверить, что активная категория с названием: {value} видна")
    public void shouldVisibleActiveCategory(String value) {
        categoryList.findBy(text(value)).shouldBe(visible);
    }

    @Step("Проверить, что архивная категория с названием: {value} не видна")
    public void shouldNotVisibleArchiveCategory(String value) {
        categoryList.findBy(text(value)).shouldNotBe(visible);
    }

    @Nonnull
    @Step("Ввести имя: {name}")
    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    @Nonnull
    @Step("Нажать кнопку сохранить изменения")
    public ProfilePage clickSaveButton() {
        saveButton.click();
        return this;
    }

    @Step("Проверить имя: {name}")
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    @Step("Загрузить фото из classpath")
    @Nonnull
    public ProfilePage uploadPhotoFromClasspath(String path) {
        photoInput.uploadFromClasspath(path);
        return this;
    }

    @Step("Проверка что фото существует")
    @Nonnull
    public ProfilePage checkPhotoExist() {
        avatar.should(attributeMatching("src", "data:image.*"));
        return this;
    }

    @Step("Проверка что картина профиля совпадает с ожидаемой")
    @Nonnull
    public ProfilePage checkProfileImage(BufferedImage expectedImage) throws IOException {
        profileImage.shouldBe(visible);
        BufferedImage actualImage = ImageIO.read(profileImage.screenshot());
        assertFalse(new ScreenDiffResult(actualImage, expectedImage));
        return this;
    }
}
