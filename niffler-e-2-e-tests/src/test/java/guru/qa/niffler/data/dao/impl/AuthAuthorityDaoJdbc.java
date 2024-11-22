package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private static final Config CFG = Config.getInstance();
    private final Connection connection;
    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

//    @Override
//    public AuthorityEntity create(AuthorityEntity[] authority) {
//        try (PreparedStatement ps = connection.prepareStatement(
//                "INSERT INTO user (user_id, authority) " +
//                        "VALUES ( ?, ?)",
//                Statement.RETURN_GENERATED_KEYS
//        )) {
//            ps.setObject(1, authority.getId());
//            ps.setObject(1, authority.getAuthority());
//            ps.executeUpdate();
//            final UUID generatedKey;
//            try (ResultSet rs = ps.getGeneratedKeys()) {
//                if (rs.next()) {
//                    generatedKey = rs.getObject("id", UUID.class);
//                } else {
//                    throw new SQLException("Can`t find id in ResultSet");
//                }
//            }
//            authority.setId(generatedKey);
//            return authority;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUserId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthorityEntity> findAuthorityById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<AuthorityEntity> findByUserId(String userId) {
        return null;
    }

    @Override
    public void deleteAuthority(AuthorityEntity authority) {

    }
}
