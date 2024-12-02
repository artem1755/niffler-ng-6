package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();
    private final SpendDao spendDao = new SpendDaoSpringJdbc();
    private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );


    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);

            // Если категория не указана, создаем новую категорию
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }

            SpendEntity createdSpend = spendDao.create(spendEntity);

            // Возвращаем результат в формате JSON
            return SpendJson.fromEntity(createdSpend);
        });
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return jdbcTxTemplate.execute(
                () -> spendDao.findSpendById(id)
        );
    }


    public List<SpendEntity> findAllByUsername(String username){
        return jdbcTxTemplate.execute(
                () -> spendDao.findAllByUsername(username)
        );
    };


    public void deleteSpend(SpendJson spend){
        jdbcTxTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendDao.deleteSpend(spendEntity);
            return null;
        });
    };


    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(
                () -> {
                    CategoryEntity ce = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(categoryDao.create(ce));
                }
        );
    }


    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return jdbcTxTemplate.execute(
                () -> categoryDao.findCategoryById(id)
        );
    }


    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName){
        return jdbcTxTemplate.execute(
                () -> categoryDao.findCategoryByUsernameAndCategoryName(username,categoryName)
        );
    }


    public List<CategoryEntity> findAllCategoriesByUsername(String username){
        return jdbcTxTemplate.execute(
                () -> categoryDao.findAllCategoriesByUsername(username)
        );
    };


    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            categoryDao.deleteCategory(categoryEntity);
            return null;
        });
    }
}
