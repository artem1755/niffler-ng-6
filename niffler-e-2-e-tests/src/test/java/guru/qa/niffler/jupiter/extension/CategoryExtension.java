package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;
import java.util.List;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendClient spendClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (ArrayUtils.isNotEmpty(userAnno.categories())) {
                        List<CategoryJson> result = new ArrayList<>();
                        UserJson user = context.getStore(UserExtension.NAMESPACE)
                                .get(context.getUniqueId(), UserJson.class);

                        for (Category categoryAnno : userAnno.categories()) {
                            final String categoryName = "".equals(categoryAnno.name())
                                    ? RandomDataUtils.randomCategory()
                                    : categoryAnno.name();

                            CategoryJson category = new CategoryJson(
                                    null,
                                    categoryName,
                                    user != null ? user.username() : userAnno.username(),
                                    categoryAnno.archived()
                            );

                            CategoryJson createdCategory = spendClient.createCategory(category);
                            result.add(createdCategory);
                        }


                        if (user != null) {
                            user.testData().categories().addAll(result);
                        } else {
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    result
                            );
                        }
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CategoryJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        List<CategoryJson> categories = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class);
        if (categories == null) {
            throw new ParameterResolutionException("No categories found in the store");
        }
        return categories.toArray(new CategoryJson[0]); // Преобразование списка в массив типа CategoryJson[]
    }
}