package guru.qa.niffler.test.web;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Order(Integer.MAX_VALUE)
public class LastTest {
    @User
    @Test
    void shouldReturnNotEmptyUsersListTest(UserJson user) {
        UserApiClient usersApiClient = new UserApiClient();
        List<UserJson> response = usersApiClient.allUsers(user.username(), null);
        assertFalse(response.isEmpty(), "Список пользователей должен быть не пустым");
    }
}
