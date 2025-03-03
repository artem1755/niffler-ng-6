package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.utils.OAuthUtils;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import com.github.jknack.handlebars.internal.lang3.StringUtils;
import retrofit2.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthApiClient extends RestClient {
    private final AuthApi authApi;

    private static final Config CFG = Config.getInstance();
    private static final String CLIENT_ID = "client";
    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String SCOPE = "openid";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String REDIRECT_URI = CFG.frontUrl() + "authorized";

    public AuthApiClient() {
        super(CFG.authUrl(), true, ScalarsConverterFactory.create(), HEADERS);
        this.authApi = create(AuthApi.class);
    }

    @Step("Запрос формы регистрации для получения CSRF токена")
    public void requestRegisterForm() {
        final Response<Void> response;
        try {
            response = authApi.requestRegisterForm().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Step("Регистрация нового пользователя c именем: {username}")
    public void registerUser(@Nonnull String username, @Nonnull String password, @Nonnull String passwordSubmit, @Nonnull String _csrf) {
        final Response<Void> response;
        try {
            response = authApi.register(username, password, passwordSubmit, _csrf).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }

    @SneakyThrows
    public String doLogin(String username, String password) {
        final String codeVerifier = OAuthUtils.generateCodeVerifier();
        final String codeChallenge = OAuthUtils.generateCodeChallenge(codeVerifier);

        authorize(codeChallenge);
        String code = login(username, password);
        return token(code, codeVerifier);
    }

    @SneakyThrows
    private void authorize(String codeChallenge) {
        authApi.authorize(
                RESPONSE_TYPE,
                CLIENT_ID,
                SCOPE,
                REDIRECT_URI,
                codeChallenge,
                CODE_CHALLENGE_METHOD
        ).execute();

    }

    @SneakyThrows
    private String login(String username, String password) {
        Response<String> loginResponse = authApi.login(
                username,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        ).execute();
        String url = loginResponse.raw().request().url().toString();
        return StringUtils.substringAfter(url, "code=");
    }

    @SneakyThrows
    private String token(String code, String codeVerifier) {
        Response<String> tokenResponse = authApi.token(
                CLIENT_ID,
                REDIRECT_URI,
                GRANT_TYPE,
                code,
                codeVerifier
        ).execute();
        return new ObjectMapper().readTree(
                tokenResponse.body().getBytes(StandardCharsets.UTF_8)
        ).get("id_token").asText();
    }
}
