package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.color;
import static java.util.Objects.requireNonNull;

public class StatComponent<T extends BasePage<?>> extends BaseComponent<T> {
    public StatComponent(SelenideElement stat, T page) {
        super(stat, page);
    }

    private final SelenideElement chart = $("canvas[role='img']");
    private final ElementsCollection bubbles = self.$("#legend-container").$$("li");

    @Step("Get screenshot of stat chart")
    @Nonnull
    public BufferedImage chartScreenshot() throws IOException {
        return ImageIO.read(requireNonNull(chart.screenshot()));
    }

    @Step("Check that stat bubbles contains colors {expectedColors}")
    @Nonnull
    public StatComponent checkBubbles(Color... expectedColors) {
        bubbles.should(color(expectedColors));
        return this;
    }

}
