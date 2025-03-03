package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendClient spendClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (ArrayUtils.isNotEmpty(userAnno.spendings())) {
                        List<SpendJson> result = new ArrayList<>();
                        UserJson user = context.getStore(UserExtension.NAMESPACE)
                                .get(context.getUniqueId(), UserJson.class);

                        for (Spending spendAnno : userAnno.spendings()) {
                            SpendJson spend = new SpendJson(
                                    null,
                                    new Date(),
                                    new CategoryJson(
                                            null,
                                            spendAnno.category(),
                                            user != null ? user.username() : userAnno.username(),
                                            false
                                    ),
                                    CurrencyValues.RUB,
                                    spendAnno.amount(),
                                    spendAnno.description(),
                                    user != null ? user.username() : userAnno.username()
                            );

                            SpendJson createdSpend = spendClient.createSpend(spend);
                            result.add(createdSpend);
                        }


                        if (user != null) {
                            user.testData().spendings().addAll(result);
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
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
    }

    @Override
    public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        List<SpendJson> spendList = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class);
        if (spendList == null) {
            return new SpendJson[0]; // Возвращаем пустой массив, если данных нет
        }
        return spendList.toArray(new SpendJson[0]); // Преобразование списка в массив типа SpendJson[]
    }

}
