package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    private final SelenideElement showArchiveCategoryButton = $x("//input[@type='checkbox']");
    private final SelenideElement archivedBtnConfirm = $x("//button[text()='Archive']");
    private final SelenideElement unArchivedBtnConfirm = $x("//button[text()='Unarchive']");
    private final SelenideElement successSaveChangesMessage = $x("//div[text()='Profile successfully updated']");
    private final ElementsCollection categories = $$x("/html/body/div[1]/main/div/div");
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement successUnarchiveMessage = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    private final SelenideElement successArchiveMessage = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");

    public ProfilePage clickShowArchivedCheckbox(){
        showArchiveCategoryButton.click();
        return this;
    }


    @Step("Архивировать категорию с названием: {categoryName}")
    public ProfilePage clickArchiveButtonForCategoryName(String categoryName) {
        SelenideElement archiveButtonInRow = categoryList
                .find(text(categoryName))
                .parent().$(".MuiIconButton-sizeMedium[aria-label='Archive category']");
        archiveButtonInRow.shouldBe(visible).click();
        return this;
    }

    @Step("Разархивировать категорию с названием: {categoryName}")
    public ProfilePage clickUnarchiveButtonForCategoryName(String categoryName) {
        SelenideElement unarchiveButtonInRow = categoryList
                .find(text(categoryName))
                .parent().$("[data-testid='UnarchiveOutlinedIcon']");
        unarchiveButtonInRow.shouldBe(visible).click();
        return this;
    }

    @Step("Нажать кнопку для показа архивных категорий")
    public ProfilePage clickShowArchiveCategoryButton() {
        Selenide.executeJavaScript("arguments[0].scrollIntoView(true);", showArchiveCategoryButton);
        Selenide.executeJavaScript("arguments[0].click();", showArchiveCategoryButton);
        return this;
    }

    @Step("Нажать кнопку подтверждения архивирования категории")
    public ProfilePage clickArchivedBtnConfirm(){
        archivedBtnConfirm.click();
        return this;
    }

    @Step("Нажать кнопку подтверждения разархивирования категории")
    public ProfilePage clickUnarchivedBtnConfirm(){
        unArchivedBtnConfirm.click();
        return this;
    }

    @Step("Проверить успешное сообщение об архивировании категории: {value}")
    public ProfilePage shouldBeVisibleArchiveSuccessMessage(String value) {
        successArchiveMessage.shouldHave(text("Category " + value + " is archived")).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что архивная категория с названием: {value} не видна")
    public ProfilePage shouldNotVisibleArchiveCategory(String value) {
        categoryList.findBy(text(value)).shouldNotBe(visible);
        return this;
    }

    @Step("Проверить, что архивная категория {category} есть в списке")
    public void checkThatCategoryIsInTheList(String category){
        SelenideElement element = categories
                .filterBy(text(category))
                .find(visible);
        element.shouldBe(visible);
    }

    @Step("Ввести имя: {name}")
    public ProfilePage setName(String name){
        nameInput.setValue(name);
        return this;
    }

    @Step("Нажать кнопку сохранить изменения")
    public ProfilePage clickSaveButton(){
        saveChangesBtn.click();
        return this;
    }

    @Step("Проверить успешное сообщение об обновлении профиля")
    public ProfilePage shouldBeVisibleSaveChangesSuccessMessage() {
        successSaveChangesMessage.shouldHave(text("Profile successfully updated")).shouldBe(visible);
        return this;
    }

    @Step("Проверить имя: {name}")
    public void checkName(String name) {
        nameInput.shouldHave(value(name));
    }

    @Step("Проверить успешное сообщение об разархивировании категории: {value}")
    public ProfilePage shouldBeVisibleUnarchiveSuccessMessage(String value) {
        successUnarchiveMessage.shouldHave(text("Category " + value + " is unarchived")).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что активная категория с названием: {value} видна")
    public ProfilePage shouldVisibleActiveCategory(String value) {
        categoryList.findBy(text(value)).shouldBe(visible);
        return this;
    }

}
