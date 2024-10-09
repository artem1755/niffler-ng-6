package guru.qa.niffler.jupiter;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuitExtension extends BeforeAllCallback {
    @Override
    default void beforeAll(ExtensionContext context) throws Exception {

    };

    default void beforeSuite(ExtensionContext context){

    };
    default void afterSuite(){

    };
}
