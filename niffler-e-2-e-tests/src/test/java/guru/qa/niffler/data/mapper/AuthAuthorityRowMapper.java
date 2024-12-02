package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityRowMapper  implements RowMapper<AuthorityEntity>  {
    public static final AuthAuthorityRowMapper instance = new AuthAuthorityRowMapper();

    private AuthAuthorityRowMapper() {
    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthUserEntity user = new AuthUserEntity();
        user.setId(rs.getObject("user_id", UUID.class));

        AuthorityEntity result = new AuthorityEntity();
        result.setId(rs.getObject("id", UUID.class));
        Authority authority = Authority.valueOf(rs.getString("authority"));
        result.setAuthority(authority);
        result.setUser(user);
        return result;
    }
}
