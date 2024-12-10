package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.util.List;

public interface UsersClient {
    UserJson createUser(String username, String password);

    List<String> createIncomeInvitations(UserJson targetUser, int count);

    List<String> createOutcomeInvitations(UserJson targetUser, int count);

    List<String> createFriends(UserJson targetUser, int count);
}
