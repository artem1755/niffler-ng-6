package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome)
    {};


    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();


    static {
        EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("duck", "12345", "dima", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("dima", "12345", null, "bee", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("barsik", "12345", null, null, "bill"));
    }


    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY,
            WITH_FRIEND,
            WITH_INCOME_REQUEST,
            WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class) && p.getType().isAssignableFrom(StaticUser.class))
                .forEach(p -> {
                    UserType ut = p.getAnnotation(UserType.class);
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    Queue<StaticUser> queue;

                    switch (ut.value()) {
                        case EMPTY:
                            queue = EMPTY_USERS;
                            break;
                        case WITH_FRIEND:
                            queue = WITH_FRIEND_USERS;
                            break;
                        case WITH_INCOME_REQUEST:
                            queue = WITH_INCOME_REQUEST_USERS;
                            break;
                        case WITH_OUTCOME_REQUEST:
                            queue = WITH_OUTCOME_REQUEST_USERS;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + ut.value());
                    }

                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(queue.poll());
                    }

                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );

                    user.ifPresentOrElse(
                            u -> {
                                @SuppressWarnings("unchecked")
                                Map<UserType, StaticUser> userMap = (Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                        .getOrComputeIfAbsent(
                                                context.getUniqueId(),
                                                key -> new HashMap<>()
                                        );
                                userMap.put(ut, u);
                            },
                            () -> {
                                throw new IllegalStateException("Can't obtain user after 30s.");
                            }
                    );
                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );

        // Проверяем, что userMap не пустая
        if (map != null) {
            for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
                UserType key = e.getKey();
                StaticUser user = e.getValue();

                // Получаем значение типа пользователя
                UserType.Type userType = key.value();

                // В зависимости от типа пользователя добавляем его в соответствующую очередь
                switch (userType) {
                    case EMPTY:
                        EMPTY_USERS.add(user);
                        break;
                    case WITH_FRIEND:
                        WITH_FRIEND_USERS.add(user);
                        break;
                    case WITH_INCOME_REQUEST:
                        WITH_INCOME_REQUEST_USERS.add(user);
                        break;
                    case WITH_OUTCOME_REQUEST:
                        WITH_OUTCOME_REQUEST_USERS.add(user);
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected user type: " + userType);
                }
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserType, StaticUser> userMap = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        // Получаем аннотацию UserType для параметра
        UserType ut = parameterContext.getParameter().getAnnotation(UserType.class);

        // Извлекаем соответствующего пользователя из Map
        if (userMap != null) {
            return userMap.get(ut);
        }

        throw new ParameterResolutionException("No users available for the given UserType.");
        }
}
