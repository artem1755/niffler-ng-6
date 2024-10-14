package guru.qa.niffler.jupiter;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({SpendingExtension.class})
public @interface Category {

    String name();
    String username();
    boolean archived();
}
