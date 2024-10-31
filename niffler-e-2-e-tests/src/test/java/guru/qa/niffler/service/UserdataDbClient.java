package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public class UserdataDbClient {
    private final UserDao userDao = new UserdataUserDAOJdbc();
    public UserEntity createUser(UserEntity user){
        return userDao.createUser(user);
    };

    public Optional<UserEntity> findById(UUID id){
        return userDao.findById(id);
    };

    public Optional<UserEntity> findByUsername(String username){
        return userDao.findByUsername(username);
    };

    public void delete(UserEntity user){
        userDao.delete(user);
    };

}
