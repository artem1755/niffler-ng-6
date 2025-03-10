package guru.qa.niffler.jupiter.converters;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.utils.SelenideUtills;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import guru.qa.niffler.utils.Browser;

public class BrowserArgumentConverter implements ArgumentConverter {
    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof Browser)) {
            throw new ArgumentConversionException("Expected a Browser enum");
        }

        Browser browser = (Browser) source;
        SelenideConfig config;
        switch (browser) {
            case CHROME:
                config = SelenideUtills.chromeConfig;
                break;
            case FIREFOX:
                config = SelenideUtills.firefoxConfig;
                break;
            default:
                throw new ArgumentConversionException("Unknown browser type");
        }

        // Создаем SelenideDriver с выбранной конфигурацией
        return new SelenideDriver(config);
    }
}
