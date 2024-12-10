package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class JdbcTestNew {

    SpendDbClient spendDbClient = new SpendDbClient();

    @Test
    void test1() {
                SpendJson spend = spendDbClient.create(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-04",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-un4",
                        "duck"
                )
        );
    }

    @Test
    void test2() {
        Optional<SpendJson> sj = spendDbClient.findSpendById(UUID.fromString("e1bd750e-ab45-11ef-897c-0242ac110002"));
        SpendJson obj = sj.get();
        System.out.println(obj);
    }

    @Test
    void test3() {
        List<SpendJson> list = spendDbClient.findSpendByUsernameAndDescription("duck", "spend-name-tx-7");
        list.stream().forEach(x -> System.out.println(x));
    }

    @Test
    void test4() {
        spendDbClient.deleteSpend(new SpendJson(
                UUID.fromString("323b492c-9569-11ef-87f3-0242ac110002"),
                new Date(),
                new CategoryJson(
                        UUID.fromString("3237ea3e-9569-11ef-a7ad-0242ac110002"),
                        "cat-name-tx-313",
                        "duck",
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                "spend-name-tx-3N",
                "duck665"
        ));
    }

    @Test
    void test5() {
        spendDbClient.updateSpend(new SpendJson(
                UUID.fromString("34882300-b02e-11ef-8e92-0242ac110002"),
                new Date(),
                new CategoryJson(
                        UUID.fromString("e2e50b8e-b024-11ef-ba53-0242ac110002"),
                        "cat-name-tx-313",
                        "duck",
                        false
                ),
                CurrencyValues.RUB,
                1000.0,
                "spend-name-tx-3N",
                "duck1222"
        ));
    }

    @Test
    void test6() {
        spendDbClient.createCategory(
                new CategoryJson(
                        null,
                        "newcat3",
                        "duck",
                        false
                )
        );
    }

    @Test
    void test7() {
        spendDbClient.updateCategory(
                new CategoryJson(
                        UUID.fromString("1b89614c-b02f-11ef-ba9d-0242ac110002"),
                        "34",
                        "duck2",
                        false
                )
        );
    }

    @Test
    void test8() {
        Optional<CategoryJson> catOpt = spendDbClient.findCategoryById(UUID.fromString("d282b256-b023-11ef-aac8-0242ac110002"));
        System.out.println(catOpt.get());
    }

    @Test
    void test9() {
        Optional<CategoryJson> catOpt = spendDbClient.findCategoryByUsernameAndCategoryName("artem","22");
        System.out.println(catOpt.get());
    }

    @Test
    void test10() {
        spendDbClient.deleteCategory(
                new CategoryJson(
                        UUID.fromString("1b89614c-b02f-11ef-ba9d-0242ac110002"),
                        "newcat",
                        "duck",
                        false
                )
        );
    }

    @Test
    void test11(){
        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.createUser("someuser3","31234114");
    }

}





