package com.epam.jwd_critics.model.service.impl;

import com.epam.jwd_critics.exception.DaoException;
import com.epam.jwd_critics.exception.ServiceException;
import com.epam.jwd_critics.exception.codes.UserServiceCode;
import com.epam.jwd_critics.model.dao.AbstractMovieReviewDao;
import com.epam.jwd_critics.model.dao.AbstractUserDao;
import com.epam.jwd_critics.model.dao.EntityTransaction;
import com.epam.jwd_critics.model.dao.MovieReviewDao;
import com.epam.jwd_critics.model.dao.UserDao;
import com.epam.jwd_critics.model.entity.MovieReview;
import com.epam.jwd_critics.model.entity.Role;
import com.epam.jwd_critics.model.entity.Status;
import com.epam.jwd_critics.model.entity.User;
import com.epam.jwd_critics.model.service.UserService;
import com.epam.jwd_critics.model.util.PasswordAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AbstractMovieReviewDao reviewDao = MovieReviewDao.getInstance();
    private final AbstractUserDao userDao = UserDao.getInstance();
    private final PasswordAuthenticator passwordAuthenticator = new PasswordAuthenticator();

    private UserServiceImpl() {

    }

    public static UserServiceImpl getInstance() {
        return UserServiceImplSingleton.INSTANCE;
    }

    @Override
    public User login(String login, String password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(reviewDao, userDao);
        User user;
        try {
            user = userDao.getEntityByLogin(login).orElseThrow(() -> new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            if (!passwordAuthenticator.authenticate(password, user.getPassword())) {
                throw new ServiceException(UserServiceCode.INCORRECT_PASSWORD);
            } else if (user.getStatus().equals(Status.BANNED)) {
                throw new ServiceException(UserServiceCode.USER_IS_BANNED);
            } else if (user.getStatus().equals(Status.INACTIVE)) {
                throw new ServiceException(UserServiceCode.USER_IS_INACTIVE);
            }
            updateInfo(user);
            transaction.commit();
            logger.info("{} logged in", user);

        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (ServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
        return user;
    }

    @Override
    public User register(String firstName, String lastName, String email, String login, char[] password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        User userToRegister = User.newBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setLogin(login)
                .build();
        try {
            if (userDao.loginExists(login)) {
                throw new ServiceException(UserServiceCode.LOGIN_EXISTS);
            } else {
                userToRegister.setPassword(passwordAuthenticator.hash(password));
                setDefaultFields(userToRegister);
                userToRegister = userDao.create(userToRegister);
            }
            transaction.commit();
            logger.info("{} registered", userToRegister);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (ServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
        return userToRegister;
    }

    @Override
    public List<User> getAllBetween(int begin, int end) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        List<User> userList;
        try {
            userList = userDao.getAllBetween(begin, end);
            for (User user : userList) {
                updateInfo(user);
            }
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return userList;
    }

    @Override
    public int getCount() throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        int count;
        try {
            count = userDao.getCount();
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return count;
    }

    @Override
    public Optional<User> getEntityById(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        Optional<User> user;
        try {
            user = userDao.getEntityById(id);
            if (user.isPresent())
                updateInfo(user.get());
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
        return user;
    }

    @Override
    public void update(User user) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.idExists(user.getId())) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            userDao.update(user);
            transaction.commit();
            logger.info("User with id {} was updated", user.getId());
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (ServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    public void updatePassword(Integer id, char[] password) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao);
        try {
            if (!userDao.idExists(id)) {
                throw new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST);
            }
            userDao.updatePassword(id, passwordAuthenticator.hash(password));
            transaction.commit();
            logger.info("User with id {} updated password to ", password);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (ServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(userDao, reviewDao);
        try {
            User userToDelete = userDao.getEntityById(id).orElseThrow(() -> new ServiceException(UserServiceCode.USER_DOES_NOT_EXIST));
            userDao.delete(id);
            transaction.commit();
            logger.info("{} was deleted", userToDelete);
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } catch (ServiceException e) {
            transaction.rollback();
            throw e;
        } finally {
            transaction.close();
        }
    }

    private void updateInfo(User user) throws ServiceException {
        EntityTransaction transaction = new EntityTransaction(reviewDao);
        try {
            List<MovieReview> reviews = reviewDao.getMovieReviewsByUserId(user.getId());
            user.setReviews(reviews);
            transaction.commit();
        } catch (DaoException e) {
            transaction.rollback();
            throw new ServiceException(e);
        } finally {
            transaction.close();
        }
    }

    private void setDefaultFields(User user) {
        user.setRating(0);
        user.setRole(Role.USER);
        user.setStatus(Status.INACTIVE);
    }

    private static class UserServiceImplSingleton {
        private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    }
}
