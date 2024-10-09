package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;
import java.util.List;

public class SpendApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @SneakyThrows
  public SpendJson createSpend(SpendJson spend) {
    return spendApi.addSpend(spend)
        .execute()
        .body();
  }

  @SneakyThrows
  public SpendJson editSpend(SpendJson spend) {
    return spendApi.editSpend(spend)
            .execute()
            .body();
  }

  @SneakyThrows
  public void removeSpend(String username, List<String> ids) {
     spendApi.removeSpend(username,ids)
            .execute()
            .body();
  }

  @SneakyThrows
  public SpendJson getSpend(String id, String username) {
    return spendApi.getSpend(id ,username)
            .execute()
            .body();
  }

  @SneakyThrows
  public List<SpendJson> getAllSpends(String username, CurrencyValues filterCurrency, Date from, Date to){
    return spendApi.getAllSpends(username ,filterCurrency, from, to)
            .execute()
            .body();
  }

  @SneakyThrows
  public CategoryJson createCategory(CategoryJson category) {
    return spendApi.addCategory(category)
            .execute()
            .body();
  }

  @SneakyThrows
  public CategoryJson editCategory(CategoryJson category) {
    return spendApi.editCategory(category)
            .execute()
            .body();
  }

  @SneakyThrows
  public List<CategoryJson> getAllCategories(String username, Boolean excludeArchived){
    return spendApi.getAllCategories(username,excludeArchived)
            .execute()
            .body();
  }

}
