package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final Faker faker = new Faker();

    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson category = new CategoryJson(
                            null,
                            faker.aviation().airport(),
                            anno.username(),
                            false
                    );

                    CategoryJson createCategory = spendApiClient.createCategory(category);

                    if (anno.archived()){
                        CategoryJson archivedCategory = new CategoryJson(
                                createCategory.id(),
                                createCategory.name(),
                                createCategory.username(),
                                true
                        );
                        createCategory = spendApiClient.editCategory(archivedCategory);

                    }

                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createCategory
                    );

                });
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);

    }
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        if (category.archived()){
            CategoryJson archivedCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            spendApiClient.editCategory(archivedCategory);
        }
    }
}