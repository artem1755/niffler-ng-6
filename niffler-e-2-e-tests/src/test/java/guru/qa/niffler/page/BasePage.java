package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {
    protected static final Config CFG = Config.getInstance();
    protected final SelenideElement alert;
    @Getter
    protected final Header<T> header;

    @SuppressWarnings("unchecked")
    public BasePage(SelenideDriver driver) {
        this.header = new Header<>(driver.$("#root header"), (T) this);
        alert = driver.$x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    }

    public BasePage() {
        this.header = new Header<>(Selenide.$("#root header"), (T) this);
        alert = Selenide.$x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    }


    @SuppressWarnings("unchecked")
    @Step("Проверка всплывающего сообщения: {message}")
    public T checkAlert(String message) {
        alert.shouldHave(text(message));
        return (T) this;
    }
}
