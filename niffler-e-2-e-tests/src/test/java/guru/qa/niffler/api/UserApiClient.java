package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class UserApiClient implements UsersClient {

    private final Retrofit userRetrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final UserApi userApi = userRetrofit.create(UserApi.class);

    private final Retrofit authRetrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().authUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final AuthApi authApi = authRetrofit.create(AuthApi.class);


    @Override
    public UserJson createUser(String username, String password) {
        return null;
    }

    @Override
    public void createIncomeInvitations(UserJson targetUser, int count) {

    }

    @Override
    public void createOutcomeInvitations(UserJson targetUser, int count) {

    }

    @Override
    public void createFriends(UserJson targetUser, int count) {

    }
}
