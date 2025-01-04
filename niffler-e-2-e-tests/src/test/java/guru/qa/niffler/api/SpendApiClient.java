package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Step;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ParametersAreNonnullByDefault
public class SpendApiClient  implements SpendClient {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    @Step("Создание новой траты")
    public @Nullable SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    @Override
    @Step("Редактирование траты")
    public @Nullable SpendJson updateSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    @Step("Создание новой категории")
    public @Nullable CategoryJson createCategory(CategoryJson category) {
        try {
            Response<CategoryJson> response = spendApi.addCategory(category).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException("Не удалось создать категорию: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании категории", e);
        }
    }

    @Override
    @Step("Обновление категории")
    public @Nullable CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    @Step("Удаление категории")
    public void deleteCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Невозможно удалить категорию через АПИ");
    }

    @Step("Получение траты")
    public @Nullable SpendJson getSpend(UUID id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Nonnull
    @Step("Получение всех трат пользователя: {username}")
    public SpendJson getSpends(String username,@Nullable CurrencyValues filterCurrency,@Nullable Date from,@Nullable Date to) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpends(username, filterCurrency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Удаление траты")
    public SpendJson removeSpends(@Nonnull String username,@Nonnull List<UUID> ids) {
        final Response<SpendJson> response;
        try {
            response = spendApi.deleteSpends(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Nonnull
    @Step("Получение всех категорий пользователя: {username}")
    public CategoryJson getAllCategories(boolean excludeArchived) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.getCategories(excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }
}
