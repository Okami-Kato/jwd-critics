package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.Role;
import com.epam.jwd_critics.entity.Status;
import com.epam.jwd_critics.entity.User;
import com.epam.jwd_critics.exception.DaoException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoTest.class);
    private static AbstractUserDao userDao;
    private static EntityTransaction transaction;
    private static User user;

    @BeforeAll
    public static void initialize() {
        userDao = new UserDao();
        transaction = new EntityTransaction(userDao);
        user = User.newBuilder()
                .setFirstName("Test")
                .setLastName("Testovich")
                .setEmail("test@email.com")
                .setLogin("test")
                .setPassword("test")
                .setStatus(Status.ACTIVE)
                .setRole(Role.ADMIN)
                .build();
    }

    @BeforeEach
    public void initializeTest() throws DaoException {
        user = userDao.create(user);
    }

    @Test
    public void testFindEntityById() throws DaoException {
        User actualResult = userDao.getEntityById(user.getId()).get();
        assertEquals(user, actualResult);
    }

    @Test
    public void testUpdate() throws DaoException {
        String oldEmail = user.getEmail();
        user.setEmail("testemail");
        userDao.update(user);
        User actualResult = userDao.getEntityById(user.getId()).get();
        assertEquals(user, actualResult);
        user.setEmail(oldEmail);
    }

    @AfterEach
    public void endTest() throws DaoException {
        userDao.delete(user.getId());
    }

    @AfterAll
    public static void clear() {
        transaction.rollback();
        transaction.close();
    }
}