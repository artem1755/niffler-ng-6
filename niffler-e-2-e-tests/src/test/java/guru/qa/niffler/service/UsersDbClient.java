package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.*;

public class UsersDbClient {
    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

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
                                new UdUserDAOJdbc(con).createUser(ue);
                                return ue;
                            },
                            CFG.userdataJdbcUrl()
                    )
            ));
}

    public Optional<UserEntity> findById(UUID id){
        return transaction(connection -> {
                    return new UdUserDAOJdbc(connection).findById(id);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public Optional<UserEntity> findByUsername(String username){
        return transaction(connection -> {
                    return new UdUserDAOJdbc(connection).findByUsername(username);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public Optional<UserEntity> findByUsernameSpring(String username){
        return new UdUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl())).findByUsername(username);
    };

    public void delete(UserEntity user){
         transaction(connection -> {
                     new UdUserDAOJdbc(connection).delete(user);
                },
                CFG.spendJdbcUrl(),
                 TRANSACTION_ISOLATION_LEVEL
        );
    };

    public void deleteSpring(UserEntity user){
        new UdUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl())).delete(user);
    };

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authorityEntities);

        return UserJson.fromEntity(
                new UdUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .createUser(
                                UserEntity.fromJson(user)
                        )
        );
    }
    public Optional<AuthorityEntity> findAuthorityByIdSpring(UUID id){
        return new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).findAuthorityById(id);
    };

    public Optional<AuthUserEntity> findUserByUsernameSpring(String username){
        return new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).findByUsername(username);
    };

    public void deleteSpring(AuthUserEntity user){
         new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).deleteUser(user);
    };

    public List<AuthUserEntity> findAllUsersInAuth() {
        return transaction(connection -> {
                    return new AuthUserDaoJdbc(connection).findAllUsers();
                },
                CFG.authJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public List<AuthUserEntity> findAllUsersInAuthSpring(){
        return new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).findAllUsers();
    };

    public List<AuthorityEntity> findAllAuthorities() {
        return transaction(connection -> {
                    return new AuthAuthorityDaoJdbc(connection).findAllAuthorities();
                },
                CFG.authJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public List<AuthorityEntity> findAllAuthoritiesSpring(){
        return new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).findAllAuthorities();
    };

    public List<UserEntity> findAllUsersFromUserdata() {
        return transaction(connection -> {
                    return new UdUserDAOJdbc(connection).findAllUsers();
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public List<UserEntity> findAllUsersFromUserdataSpring(){
        return new UdUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl())).findAllUsers();
    };


}
