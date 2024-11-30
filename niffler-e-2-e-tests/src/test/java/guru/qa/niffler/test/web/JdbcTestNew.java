package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;


public class JdbcTestNew {

    SpendDbClient spendDbClient = new SpendDbClient();

    @Test
    void test1(){
                SpendJson spend = spendDbClient.create(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-313",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx-3",
                        "duck"
                )
        );

        System.out.println(spend);
//        SpendJson spend = spendDbClient.create(spend);
    }



//    @Test
//    void txTest() {
//        SpendDbClient spendDbClient = new SpendDbClient();
//
//        SpendJson spend = spendDbClient.createSpend(
//                new SpendJson(
//                        null,
//                        new Date(),
//                        new CategoryJson(
//                                null,
//                                "cat-name-tx-3",
//                                "duck",
//                                false
//                        ),
//                        CurrencyValues.RUB,
//                        1000.0,
//                        "spend-name-tx-3",
//                        "duck"
//                )
//        );
//
//        System.out.println(spend);
//    }
//
//
//    static UsersDbClient usersDbClient = new UsersDbClient();
//
//    @ValueSource(strings = {
//            "valentin-12",
//            "valentin-13"
//    })
//    @ParameterizedTest
//    void springJdbcTest(String uname) {
//
//        UserJson user = usersDbClient.createUser(
//                uname,
//                "12345"
//        );

//        usersDbClient.addIncomeInvitation(user, 1);
//        usersDbClient.addOutcomeInvitation(user, 1);

    //    @Test
//    void test1(){
//        UserJson user = new UserJson(
//                null,
//                "test1111",
//                null,
//                null,
//                null,
//                CurrencyValues.RUB,
//                null,
//                null,
//                null
//        );
//
//
//        UsersDbClient usersDbClient1 = new UsersDbClient();
//        usersDbClient1.create(UserEntity.fromJson(user));
//    }


    }





