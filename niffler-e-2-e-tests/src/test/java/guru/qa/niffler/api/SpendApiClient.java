package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(Config.getInstance().spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendJson createSpend(SpendJson spend) {
      try {
          return spendApi.addSpend(spend)
              .execute()
              .body();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  public SpendJson editSpend(SpendJson spend) {
      try {
          return spendApi.editSpend(spend)
                  .execute()
                  .body();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  public void removeSpend(String username, List<String> ids) {
      try {
          spendApi.removeSpend(username,ids)
                 .execute()
                 .body();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  public SpendJson getSpend(String id, String username) {
      try {
          return spendApi.getSpend(id ,username)
                  .execute()
                  .body();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  public List<SpendJson> getAllSpends(String username, CurrencyValues filterCurrency, Date from, Date to){
      try {
          return spendApi.getAllSpends(username ,filterCurrency, from, to)
                  .execute()
                  .body();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  public CategoryJson createCategory(CategoryJson category) {
      try {
          return spendApi.addCategory(category)
                  .execute()
                  .body();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }


  public CategoryJson editCategory(CategoryJson category) {
      try {
          return spendApi.editCategory(category)
                  .execute()
                  .body();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  public List<CategoryJson> getAllCategories(String username, Boolean excludeArchived){
      try {
          return spendApi.getAllCategories(username,excludeArchived)
                  .execute()
                  .body();
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

}
