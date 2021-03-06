package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Column;
import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDao extends AbstractUserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Language("SQL")
    private static final String SELECT_ALL_USERS_BETWEEN = "SELECT U.id, U.first_name, U.last_name, U.email, U.image_path, U.login, U.password, UR.role, US.status FROM jwd_critics.user U inner join jwd_critics.user_role UR on U.role_id = UR.id inner join jwd_critics.user_status US on U.status_id = US.id order by UR.id, U.last_name, U.first_name limit ?, ?";
    @Language("SQL")
    private static final String COUNT_USERS = "SELECT COUNT(*) FROM user";
    @Language("SQL")
    private static final String SELECT_USER_BY_ID = "SELECT U.id, U.first_name, U.last_name, U.email, U.image_path, U.login, U.password, UR.role, US.status FROM jwd_critics.user U inner join jwd_critics.user_role UR on U.role_id = UR.id inner join jwd_critics.user_status US on U.status_id = US.id WHERE U.id = ?";
    @Language("SQL")
    private static final String SELECT_USER_BY_LOGIN = "SELECT U.id, U.first_name, U.last_name, U.email, U.image_path, U.login, U.password, UR.role, US.status FROM jwd_critics.user U inner join jwd_critics.user_role UR on U.role_id = UR.id inner join jwd_critics.user_status US on U.status_id = US.id WHERE U.login = ?";
    @Language("SQL")
    private static final String SELECT_USER_BY_EMAIL = "SELECT U.id, U.first_name, U.last_name, U.email, U.image_path, U.login, U.password, UR.role, US.status FROM jwd_critics.user U inner join jwd_critics.user_role UR on U.role_id = UR.id inner join jwd_critics.user_status US on U.status_id = US.id WHERE U.email = ?";
    @Language("SQL")
    private static final String DELETE_USER_BY_ID = "DELETE FROM jwd_critics.user U WHERE U.id = ?";
    @Language("SQL")
    private static final String INSERT_USER = "INSERT INTO jwd_critics.user (first_name, last_name, email, login, password, image_path, role_id, status_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    @Language("SQL")
    private static final String UPDATE_USER = "UPDATE jwd_critics.user U SET U.first_name = ?, U.last_name = ?, U.email = ?, U.login = ?, U.role_id = ?, U.status_id = ?, U.image_path = ? WHERE U.id = ?";
    @Language("SQL")
    private static final String UPDATE_PASSWORD = "UPDATE jwd_critics.user U SET U.password = ? WHERE U.id = ?";
    @Language("SQL")
    private static final String LOGIN_EXISTS = "SELECT EXISTS(SELECT login FROM jwd_critics.user WHERE login = ?)";
    @Language("SQL")
    private static final String EMAIL_EXISTS = "SELECT EXISTS(SELECT email FROM jwd_critics.user WHERE email = ?)";
    @Language("SQL")
    private static final String RECOVERY_KEY_EXISTS = "SELECT EXISTS(SELECT recovery_key FROM jwd_critics.user_recovery_key WHERE user_id = ?)";
    @Language("SQL")
    private static final String ID_EXISTS = "SELECT EXISTS(SELECT id FROM jwd_critics.user WHERE id = ?)";
    @Language("SQL")
    private static final String INSERT_ACTIVATION_KEY = "INSERT INTO jwd_critics.user_activation_key (user_id, activation_key) VALUES (?, ?)";
    @Language("SQL")
    private static final String SELECT_ACTIVATION_KEY = "SELECT activation_key from jwd_critics.user_activation_key where user_id = ?";
    @Language("SQL")
    private static final String DELETE_ACTIVATION_KEY = "DELETE FROM jwd_critics.user_activation_key where user_id = ?";
    @Language("SQL")
    private static final String INSERT_RECOVERY_KEY = "INSERT INTO jwd_critics.user_recovery_key (user_id, recovery_key) VALUES (?, ?)";
    @Language("SQL")
    private static final String SELECT_RECOVERY_KEY = "SELECT recovery_key from jwd_critics.user_recovery_key where user_id = ?";
    @Language("SQL")
    private static final String DELETE_RECOVERY_KEY = "DELETE FROM jwd_critics.user_recovery_key where user_id = ?";

    @Override
    public List<User> getAllBetween(int begin, int end) throws DaoException {
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_ALL_USERS_BETWEEN)) {
            ps.setInt(1, begin);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            User user = buildUser(rs);
            while (user != null) {
                list.add(user);
                user = buildUser(rs);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return list;
    }

    @Override
    public int getCount() throws DaoException {
        return getCount(COUNT_USERS);
    }

    @Override
    public Optional<User> getEntityById(Integer userId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_USER_BY_ID)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(buildUser(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Integer userId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_USER_BY_ID)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User create(User user) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getLogin());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getImagePath());
            ps.setInt(7, user.getRole().getId());
            ps.setInt(8, user.getStatus().getId());
            user.setId(executeQueryAndGetGeneratesKeys(ps));
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void update(User user) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(UPDATE_USER)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getLogin());
            ps.setInt(5, user.getRole().getId());
            ps.setInt(6, user.getStatus().getId());
            ps.setString(7, user.getImagePath());
            ps.setInt(8, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean idExists(Integer userId) throws DaoException {
        return existsQuery(ID_EXISTS, userId);
    }

    @Override
    public Optional<User> getEntityByLogin(String login) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_USER_BY_LOGIN)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(buildUser(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> getEntityByEmail(String email) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_USER_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return Optional.ofNullable(buildUser(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void updatePassword(Integer id, String password) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(UPDATE_PASSWORD)) {
            ps.setString(1, password);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean loginExists(String login) throws DaoException {
        return existsQuery(LOGIN_EXISTS, login);
    }

    @Override
    public boolean emailExists(String email) throws DaoException {
        return existsQuery(EMAIL_EXISTS, email);
    }

    @Override
    public void insertActivationKey(Integer userId, String key) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(INSERT_ACTIVATION_KEY)) {
            ps.setInt(1, userId);
            ps.setString(2, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<String> selectActivationKey(Integer userId) throws DaoException {
        return selectKey(userId, SELECT_ACTIVATION_KEY);
    }

    @Override
    public void deleteActivationKey(Integer userId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_ACTIVATION_KEY)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void insertRecoveryKey(Integer userId, String key) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(INSERT_RECOVERY_KEY)) {
            ps.setInt(1, userId);
            ps.setString(2, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<String> selectRecoveryKey(Integer userId) throws DaoException {
        return selectKey(userId, SELECT_RECOVERY_KEY);
    }

    @Override
    public void deleteRecoveryKey(Integer userId) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(DELETE_RECOVERY_KEY)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private boolean existsQuery(String query, String key) throws DaoException {
        boolean result = false;
        try (PreparedStatement ps = getPreparedStatement(query)) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1) != 0;
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @NotNull
    private Optional<String> selectKey(int userId, String selectActivationKey) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(selectActivationKey)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString(1));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private User buildUser(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        Map<String, String> columnNames = Arrays.stream(User.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(Column.class).name()));
        Field idField;
        try {
            idField = User.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        columnNames.put(idField.getName(), idField.getAnnotation(Column.class).name());
        return User.newBuilder().setId(rs.getInt(columnNames.get("id")))
                .setFirstName(rs.getString(columnNames.get("firstName")))
                .setLastName(rs.getString(columnNames.get("lastName")))
                .setLogin(rs.getString(columnNames.get("login")))
                .setPassword(rs.getString(columnNames.get("password")))
                .setEmail(rs.getString(columnNames.get("email")))
                .setStatus(Status.valueOf(rs.getString(columnNames.get("status")).toUpperCase()))
                .setRole(Role.valueOf(rs.getString(columnNames.get("role")).toUpperCase()))
                .setImagePath(rs.getString(columnNames.get("imagePath")))
                .build();
    }
}
