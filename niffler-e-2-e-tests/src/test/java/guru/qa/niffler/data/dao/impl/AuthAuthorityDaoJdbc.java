package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUser().getId());
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
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"authority\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    AuthUserEntity user = new AuthUserEntity();
                    user.setId(rs.getObject("user_id", UUID.class));
                    ae.setUser(user);
//                    ae.setUserId(rs.getObject("user_id", UUID.class));
                    String authorityStr = rs.getString("authority");
                    Authority authority = Authority.valueOf(authorityStr);
                    ae.setAuthority(authority);
                    return Optional.of(ae);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAllByUserId(UUID userId) {

        List<AuthorityEntity> authorityEntities = new ArrayList<>();

        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement("SELECT * FROM \"authority\" WHERE user_id = ?")) {
            ps.setObject(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    AuthUserEntity user = new AuthUserEntity();
                    user.setId(rs.getObject("user_id", UUID.class));
                    ae.setUser(user);
                    String authorityStr = rs.getString("authority");
                    Authority authority = Authority.valueOf(authorityStr);
                    ae.setAuthority(authority);
                    authorityEntities.add(ae);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorityEntities;
    }

    @Override
    public List<AuthorityEntity> findAllAuthorities() {

        List<AuthorityEntity> authorityEntities = new ArrayList<>();

        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement("SELECT * FROM \"authority\"")) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    AuthUserEntity user = new AuthUserEntity();
                    user.setId(rs.getObject("user_id", UUID.class));
                    ae.setUser(user);
                    String authorityStr = rs.getString("authority");
                    Authority authority = Authority.valueOf(authorityStr);
                    ae.setAuthority(authority);
                    authorityEntities.add(ae);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorityEntities;
    }

    @Override
    public void deleteAuthority(AuthorityEntity authority) {
        try (
                PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                        "DELETE FROM \"authority\" WHERE id = ?"
                )) {
            ps.setObject(1, authority.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
