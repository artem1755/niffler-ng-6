package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import java.util.List;

public class RandomDataUtils {

    public static final Faker faker = new Faker();

    public static String randomUserName(){
        return faker.name().username();
    }

    public static String randomName(){
        return faker.name().firstName();

    }

    public static String randomSurName(){
        return faker.name().lastName();
    }

    public static String randomCategory(){
        return faker.commerce().department();
    }

    public static String randomSentence(int wordsCount){
         return faker.lorem().sentence(wordsCount);
    }
}
