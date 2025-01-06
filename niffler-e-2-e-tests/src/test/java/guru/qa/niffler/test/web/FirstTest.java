package guru.qa.niffler.test.web;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
public class FirstTest {

    @User
    @Test
    void shouldReturnEmptyUsersListTest(UserJson user) {
        UserApiClient usersApiClient = new UserApiClient();
        List<UserJson> response = usersApiClient.allUsers(user.username(), null);
        assertTrue(response.isEmpty(), "Список пользователей должен быть пустым");
    }
}
