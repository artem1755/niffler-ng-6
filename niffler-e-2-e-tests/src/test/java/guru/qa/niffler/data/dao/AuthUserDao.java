package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    AuthUserEntity create(AuthUserEntity user);
    Optional<AuthUserEntity> findUserById(UUID id);
    Optional<AuthUserEntity> findByUsername(String username);
    List<AuthUserEntity> findAllUsers();
    void deleteUser(AuthUserEntity user);
}
