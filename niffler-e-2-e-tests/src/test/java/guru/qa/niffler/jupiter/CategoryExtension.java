package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final Faker faker = new Faker();

    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
            CategoryJson category = new CategoryJson(
                    null,
                    anno.name()+"_"+faker.aviation().airport(),
                    anno.username(),
                    false
            );
            context.getStore(NAMESPACE).put(
                    context.getUniqueId(),
                    spendApiClient.createCategory(category)
            );

                    if (anno.archived(){
                        CategoryJson archivedCategory = new CategoryJson(
                                null,
                                anno.name()+"_"+faker.aviation().airport(),
                                anno.username(),
                                false
                        );
                    }
        });



    }


}
