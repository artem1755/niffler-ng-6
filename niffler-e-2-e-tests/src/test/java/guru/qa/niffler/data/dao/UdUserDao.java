package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UdUserDao {
    UserEntity create(UserEntity user);
    Optional<UserEntity> findById(UUID id);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAllUsers();
    void delete(UserEntity user);
}