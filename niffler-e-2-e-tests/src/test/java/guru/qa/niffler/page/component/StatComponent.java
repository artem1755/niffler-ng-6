package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.*;
import static java.util.Objects.requireNonNull;

public class StatComponent<T extends BasePage<?>> extends BaseComponent<T> {
    public StatComponent(SelenideElement stat, T page) {
        super(stat, page);
    }

    private final SelenideElement chart = $("canvas[role='img']");
    private final ElementsCollection bubbles = self.$("#legend-container").$$("li");

    @Step("Получить снимок экрана статистической диаграммы")
    @Nonnull
    public BufferedImage chartScreenshot() throws IOException {
        return ImageIO.read(requireNonNull(chart.screenshot()));
    }

    @Step("Проверить, что bubbles статистики содержат цвета {expectedColors}")
    @Nonnull
    public StatComponent checkBubbles(Color... expectedColors) {
        bubbles.should(color(expectedColors));
        return this;
    }

    @Step("Проверить, что bubbles статистики содержат цвета и текст {expectedBubbles}")
    @Nonnull
    public StatComponent checkBubbles(Bubble... expectedBubbles) {
        bubbles.should(statBubbles(expectedBubbles));
        return this;
    }

    @Step("Проверить, что bubbles статистики содержат цвета и текст {expectedBubbles}")
    @Nonnull
    public StatComponent checkBubblesInAnyOrder(Bubble... expectedBubbles) {
        bubbles.should(statBubblesInAnyOrder(expectedBubbles));
        return this;
    }

    @Step("Проверить, что bubbles статистики содержат цвета и текст среди других bubbles. {expectedBubbles}")
    @Nonnull
    public StatComponent checkBubblesContains(Bubble... expectedBubbles) {
        bubbles.should(statBubblesContains(expectedBubbles));
        return this;
    }

}
