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
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.SpendRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient{
    private static final Config CFG = Config.getInstance();
//    private final SpendRepository spendRepository = new SpendRepositoryHibernate();
//    private final SpendRepository spendRepository = new SpendRepositoryJdbc();
      private final SpendRepository spendRepository = new SpendRepositorySpringJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            // Проверка на существование категории по ID
            if (spendEntity.getCategory().getId() == null) {
                CategoryEntity categoryEntity = spendRepository.createCategory(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(spendRepository.create(spendEntity));
        });
    }

    public SpendJson updateSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            SpendEntity updatedEntity = spendRepository.update(spendEntity);
            return SpendJson.fromEntity(updatedEntity);
        });
    }

    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> optionalSpendEntity = spendRepository.findById(id);
            return optionalSpendEntity.map(SpendJson::fromEntity);
        });
    }

    public List<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return xaTransactionTemplate.execute(() -> {
            List<SpendEntity> spendEntities = spendRepository.findByUsernameAndSpendDescription(username, description);
            return spendEntities.stream()
                    .map(SpendJson::fromEntity)
                    .toList();
        });
    }

    public void deleteSpend(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepository.remove(spendEntity);
            return null;
        });
    }

    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            CategoryEntity createdCategoryEntity = spendRepository.createCategory(categoryEntity);
            return CategoryJson.fromEntity(createdCategoryEntity);
        });
    }

    public CategoryJson updateCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            CategoryEntity updatedEntity = spendRepository.updateCategory(categoryEntity);
            return CategoryJson.fromEntity(updatedEntity);
        });
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryById(id);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryByUsernameAndCategoryName(username, name);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    public void deleteCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            spendRepository.removeCategory(categoryEntity);
            return null;
        });
    }
}
