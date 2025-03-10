package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    public static final Faker faker = new Faker();

    public static String randomUserName() {
        return faker.name().username();
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomSurName() {
        return faker.name().lastName();
    }

    public static String randomCategory() {
        return faker.commerce().department();
    }

    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    public static String randomPassword() {
        return faker.internet().password(8, 16, true, true, true);
    }

}
