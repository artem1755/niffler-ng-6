package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(UsersQueueExtension.class)
public class TestClass {

    @Test
    void testnumber0(@UsersQueueExtension.UserType(empty = true)UsersQueueExtension.StaticUser user,
                     @UsersQueueExtension.UserType(empty = false)UsersQueueExtension.StaticUser user2){
        try {
            Thread.sleep(1000);
            System.out.println(user.username());
            System.out.println(user2.username());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void testnumber1(@UsersQueueExtension.UserType(empty = false)UsersQueueExtension.StaticUser user){
        try {
            Thread.sleep(1000);
            System.out.println(user.username());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void testnumber2(@UsersQueueExtension.UserType(empty = false)UsersQueueExtension.StaticUser user){
        try {
            Thread.sleep(1000);
            System.out.println(user.username());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
