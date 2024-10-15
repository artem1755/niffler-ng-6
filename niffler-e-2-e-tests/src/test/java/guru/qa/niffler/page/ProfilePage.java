package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement saveChangesBtn = $("button[type='submit']");
    private final SelenideElement showArchivedCheckbox = $x("//input[@type='checkbox']");
    private final SelenideElement archivedBtnConfirm = $x("//button[text()='Archive']");
    private final SelenideElement unArchivedBtnConfirm = $x("//button[text()='Unarchive']");

    private final SelenideElement successMessageBlock = $x("/html/body/div[1]/div/div/div[2]/div");

    private final ElementsCollection categories = $$x("/html/body/div[1]/main/div/div");
    private SelenideElement categoryDiv ;

    public ProfilePage clickShowArchivedCheckbox(){
        showArchivedCheckbox.click();
        return this;
    }

    public ProfilePage searchCategoryBlock(String categoryText) {
        categoryDiv = $x("//div//span[text()='"+categoryText+"']/../../..");
        return this;
    }

    public ProfilePage clickArchivedBtn() {
        categoryDiv.$("button[aria-label='Archive category']").click();
        return this;
    }

    public ProfilePage clickUnarchivedBtn() {
        categoryDiv.$("button[aria-label='Unarchive category']").click();
        return this;
    }

    public ProfilePage clickArchivedBtnConfirm(){
        archivedBtnConfirm.click();
        return this;
    }

    public ProfilePage clickUnarchivedBtnConfirm(){
        unArchivedBtnConfirm.click();
        return this;
    }

    public ProfilePage checkThatSuccessMessageBlockHasText(String successMes){
        successMessageBlock.shouldHave(text(successMes)).shouldBe(visible);
        return this;
    }

    public void checkThatCategoryIsInTheList(String category){
        SelenideElement element = categories
                .filterBy(text(category))
                .find(visible);
        element.shouldBe(visible);
    }


}
