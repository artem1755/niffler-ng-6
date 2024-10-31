package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.*;

public class SpendDbClient {
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);

        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(
                spendDao.create(spendEntity)
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findSpendById(id);
    }

    public List<SpendEntity> findAllByUsername(String username){
        return spendDao.findAllByUsername(username);
    };

    public void deleteSpend(SpendJson spend){
        spendDao.deleteSpend(SpendEntity.fromJson(spend));
    };

    public CategoryJson createCategory(CategoryJson category) {
        return CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(category)));
    }
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName){
        return categoryDao.findCategoryByUsernameAndCategoryName(username,categoryName);
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username){
        return categoryDao.findAllCategoriesByUsername(username);
    };

    public void deleteCategory(CategoryJson category){
        categoryDao.deleteCategory(CategoryEntity.fromJson(category));
    };

}
