package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

public class JdbcTestNew {
    @Test
    void test1() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-qwerty12345",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        null
                )
        );
        System.out.println(spend);
    }

    @Test
    void test2() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithSpringJdbcTransaction(
                new UserJson(
                        null,
                        randomUserName(),
                        "Joe",
                        "Jhones2",
                        "Joe Jones2",
                        RUB,
                        "photo",
                        "photoSmall"
                )
        );
        System.out.println(user);
    }

    @Test
    void test3() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithoutSpringJdbcTransaction(
                new UserJson(
                        null,
                        randomUserName(),
                        "Joe",
                        "Jhones2",
                        "Joe Jones2",
                        RUB,
                        "photo",
                        "photoSmall"
                )
        );
        System.out.println(user);
    }

    @Test
    void test4() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithJdbcTransaction(
                new UserJson(
                        null,
                        randomUserName(),
                        "Joe",
                        "Jhones2",
                        "Joe Jones2",
                        RUB,
                        "photo",
                        "photoSmall"
                )
        );
        System.out.println(user);
    }

    @Test
    void jdbcWithoutTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithoutJdbcTransaction(
                new UserJson(
                        null,
                        "nigga42233334234",
                        "Joe",
                        "Jhones2",
                        "Joe Jones2",
                        RUB,
                        "photo",
                        "photoSmall"
                )
        );
        System.out.println(user);
    }


}



