package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.user.UserEntity;
import java.nio.charset.StandardCharsets;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("firstname")
    String firstname,
    @JsonProperty("surname")
    String surname,
    @JsonProperty("fullname")
    String fullname,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("photo")
    String photo,
    @JsonProperty("photoSmall")
    String photoSmall){

    public static UserJson fromEntity(UserEntity entity){
       return new UserJson(
               entity.getId(),
               entity.getUsername(),
               entity.getFirstname(),
               entity.getSurname(),
               entity.getFullname(),
               entity.getCurrency(),
               convertBytesToString(entity.getPhoto()),
               convertBytesToString(entity.getPhotoSmall())
       );

    }

    private static String convertBytesToString(byte[] data) {
        return data != null ? new String(data, StandardCharsets.UTF_8) : null;
    }
}
