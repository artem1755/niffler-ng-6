package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
            },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public SpendJson createSpendSpring(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
        return SpendJson.fromEntity(new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).create(spendEntity));
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return transaction(connection-> {
                    return new SpendDaoJdbc(connection).findSpendById(id);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<SpendEntity> findSpendByIdSpring(UUID id) {
        return new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findSpendById(id);
    }

    public List<SpendEntity> findAllByUsername(String username){
        return transaction(connection-> {
                    return new SpendDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public List<SpendEntity> findAllByUsernameSpring(String username){
        return new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAllByUsername(username);

    };

    public void deleteSpend(SpendJson spend){
         transaction(connection-> {
                    new SpendDaoJdbc(connection).deleteSpend(SpendEntity.fromJson(spend));
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
         );
    };

    public void deleteSpendSpring(SpendJson spend){
         new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).deleteSpend(SpendEntity.fromJson(spend));
    };

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection-> {
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(category)));
        },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public CategoryJson createCategorySpring(CategoryJson category) {
        return CategoryJson.fromEntity(new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).create(CategoryEntity.fromJson(category)));
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return transaction(connection-> {
                    return new CategoryDaoJdbc(connection).findCategoryById(id);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<CategoryEntity> findCategoryByIdSpring(UUID id) {
         return new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findCategoryById(id);
    }



    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName){
        return transaction(connection-> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username,categoryName);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryNameSpring(String username, String categoryName){
        return new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findCategoryByUsernameAndCategoryName(username,categoryName);
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username){
        return transaction(connection-> {
                    return new CategoryDaoJdbc(connection).findAllCategoriesByUsername(username);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public List<CategoryEntity> findAllCategoriesByUsernameSpring(String username){
        return new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAllCategoriesByUsername(username);
    };

    public void deleteCategory(CategoryJson category){
         transaction(connection-> {
                     new CategoryDaoJdbc(connection).deleteCategory(CategoryEntity.fromJson(category));
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
         );
    };


    public List<CategoryEntity> findAllCategories(){
        return transaction(connection-> {
                    return new CategoryDaoJdbc(connection).findAllCategories();
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public List<CategoryEntity> findAllCategoriesSpring(){
        return new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAllCategories();
    };

    public List<SpendEntity> findAllSpends(){
        return transaction(connection-> {
                    return new SpendDaoJdbc(connection).findAllSpends();
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_ISOLATION_LEVEL
        );
    };

    public List<SpendEntity> findAllSpendsSpring(){
        return new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAllSpends();
    };





}
