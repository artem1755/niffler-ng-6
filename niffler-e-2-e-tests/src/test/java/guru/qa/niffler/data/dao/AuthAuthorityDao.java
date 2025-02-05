package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
    void create(AuthorityEntity... authority);
    Optional<AuthorityEntity> findAuthorityById(UUID id);
    List<AuthorityEntity> findAllByUserId(UUID userId);
    List<AuthorityEntity> findAllAuthorities();
    void deleteAuthority(AuthorityEntity authority);
}
