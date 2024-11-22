package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UsersDbClient {
    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

public UserJson createUser(UserJson user) {
    return UserJson.fromEntity(
            xaTransaction(
                    TRANSACTION_ISOLATION_LEVEL,
                    new Databases.XaFunction<>(
                            con -> {
                                AuthUserEntity authUser = new AuthUserEntity();
                                authUser.setUsername(user.username());
                                authUser.setPassword("12345");
                                authUser.setEnabled(true);
                                authUser.setAccountNonExpired(true);
                                authUser.setAccountNonLocked(true);
                                authUser.setCredentialsNonExpired(true);

                                new AuthUserDaoJdbc(con).create(authUser);
                                new AuthAuthorityDaoJdbc(con).create(
                                        Arrays.stream(Authority.values())
                                                .map(a -> {
                                                            AuthorityEntity ae = new AuthorityEntity();
                                                            ae.setUserId(authUser.getId());
                                                            ae.setAuthority(a);
                                                            return ae;
                                                        }
                                                ).toArray(AuthorityEntity[]::new));
                                return null;
                            },
                            CFG.authJdbcUrl()
                    ),
                    new Databases.XaFunction<>(
                            con -> {
                                UserEntity ue = new UserEntity();
                                ue.setUsername(user.username());
                                ue.setFullname(user.fullname());
                                ue.setCurrency(user.currency());
                                new UserdataUserDAOJdbc(con).createUser(ue);
                                return ue;
                            },
                            CFG.userdataJdbcUrl()
                    )
            ));
}

    public Optional<UserEntity> findById(UUID id){
        return transaction(connection -> {
                    return new UserdataUserDAOJdbc(connection).findById(id);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public Optional<UserEntity> findByUsername(String username){
        return transaction(connection -> {
                    return new UserdataUserDAOJdbc(connection).findByUsername(username);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public void delete(UserEntity user){
         transaction(connection -> {
                     new UserdataUserDAOJdbc(connection).delete(user);
                },
                CFG.spendJdbcUrl(),
                 TRANSACTION_ISOLATION_LEVEL
        );
    };

}
