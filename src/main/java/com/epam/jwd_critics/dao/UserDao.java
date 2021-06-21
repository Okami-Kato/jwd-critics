package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.entity.Column;
import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import org.intellij.lang.annotations.Language;
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
    private static final String SELECT_ALL_USERS_BETWEEN = "SELECT U.id, U.first_name, U.last_name, U.email, U.image_path, U.login, U.password, U.rating, UR.role, US.status FROM jwd_critics.user U inner join jwd_critics.user_role UR on U.role_id = UR.id inner join jwd_critics.user_status US on U.status_id = US.id order by UR.id, U.last_name, U.first_name limit ?, ?";
    @Language("SQL")
    private static final String COUNT_USERS = "SELECT COUNT(*) FROM celebrity";
    @Language("SQL")
    private static final String SELECT_USER_BY_ID = "SELECT U.id, U.first_name, U.last_name, U.email, U.image_path, U.login, U.password, U.rating, UR.role, US.status FROM jwd_critics.user U inner join jwd_critics.user_role UR on U.role_id = UR.id inner join jwd_critics.user_status US on U.status_id = US.id WHERE U.id = ?";
    @Language("SQL")
    private static final String SELECT_USER_BY_LOGIN = "SELECT U.id, U.first_name, U.last_name, U.email, U.image_path, U.login, U.password, U.rating, UR.role, US.status FROM jwd_critics.user U inner join jwd_critics.user_role UR on U.role_id = UR.id inner join jwd_critics.user_status US on U.status_id = US.id WHERE U.login = ?";
    @Language("SQL")
    private static final String DELETE_USER_BY_ID = "DELETE FROM jwd_critics.user U WHERE U.id = ?";
    @Language("SQL")
    private static final String INSERT_USER = "INSERT INTO jwd_critics.user (first_name, last_name, email, login, password, rating, role_id, status_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    @Language("SQL")
    private static final String UPDATE_USER = "UPDATE jwd_critics.user U SET U.first_name = ?, U.last_name = ?, U.email = ?, U.login = ?, U.rating = ?, U.role_id = ?, U.status_id = ?, U.image_path = ? WHERE U.id = ?";
    @Language("SQL")
    private static final String UPDATE_PASSWORD = "UPDATE jwd_critics.user U SET U.password = ? WHERE U.id = ?";
    @Language("SQL")
    private static final String LOGIN_EXISTS = "SELECT EXISTS(SELECT login FROM jwd_critics.user WHERE login = ?)";
    @Language("SQL")
    private static final String ID_EXISTS = "SELECT EXISTS(SELECT id FROM jwd_critics.user WHERE id = ?)";

    public static UserDao getInstance() {
        return UserDaoSingleton.INSTANCE;
    }

    @Override
    public List<User> getAllBetween(int begin, int end) throws DaoException {
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = getPreparedStatement(SELECT_ALL_USERS_BETWEEN)) {
            ps.setInt(1, begin);
            ps.setInt(2, end);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildUser(rs));
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
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(buildUser(resultSet));
                } else return Optional.empty();
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
            ps.setInt(6, user.getRating());
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
            ps.setInt(5, user.getRating());
            ps.setInt(6, user.getRole().getId());
            ps.setInt(7, user.getStatus().getId());
            ps.setString(8, user.getImagePath());
            ps.setInt(9, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean idExists(Integer userId) throws DaoException {
        return idExists(userId, ID_EXISTS);
    }

    @Override
    public Optional<User> getEntityByLogin(String login) throws DaoException {
        try (PreparedStatement ps = getPreparedStatement(SELECT_USER_BY_LOGIN)) {
            ps.setString(1, login);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(buildUser(resultSet));
                } else return Optional.empty();
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
        boolean result = false;
        try (PreparedStatement ps = getPreparedStatement(LOGIN_EXISTS)) {
            ps.setString(1, login);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1) != 0;
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        if (resultSet.wasNull()) {
            return null;
        }
        Map<String, String> columnNames = Arrays.stream(User.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toMap(Field::getName, field -> field.getAnnotation(Column.class).name()));
        Field idField = null;
        try {
            idField = User.class.getSuperclass().getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        }
        assert idField != null;
        columnNames.put(idField.getName(), idField.getAnnotation(Column.class).name());
        return User.newBuilder().setId(resultSet.getInt(columnNames.get("id")))
                .setFirstName(resultSet.getString(columnNames.get("firstName")))
                .setLastName(resultSet.getString(columnNames.get("lastName")))
                .setLogin(resultSet.getString(columnNames.get("login")))
                .setPassword(resultSet.getString(columnNames.get("password")))
                .setEmail(resultSet.getString(columnNames.get("email")))
                .setRating(resultSet.getInt(columnNames.get("rating")))
                .setStatus(Status.valueOf(resultSet.getString(columnNames.get("status")).toUpperCase()))
                .setRole(Role.valueOf(resultSet.getString(columnNames.get("role")).toUpperCase()))
                .setImagePath(resultSet.getString(columnNames.get("imagePath")))
                .build();
    }

    private static class UserDaoSingleton {
        private static final UserDao INSTANCE = new UserDao();
    }
}