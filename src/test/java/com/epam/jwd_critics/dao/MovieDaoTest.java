package com.epam.jwd_critics.dao;

import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Genre;
import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.Position;
import com.epam.jwd_critics.exception.DaoException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieDaoTest {
    private static final Logger logger = LoggerFactory.getLogger(MovieDaoTest.class);
    private static AbstractMovieDao movieDao;
    private static EntityTransaction transaction;
    private static Movie movie;

    @BeforeAll
    public static void initialize() {
        movieDao = new MovieDao();
        transaction = new EntityTransaction(movieDao);
        movie = Movie.newBuilder()
                .setName("Test Movie")
                .setSummary("Just a test movie")
                .setRuntime(Duration.parse("PT2H22M"))
                .setCountry(Country.USA)
                .setReleaseDate(LocalDate.parse("2021-09-20"))
                .setRating(0)
                .setReviewCount(0)
                .setAgeRestriction(AgeRestriction.R)
                .build();
    }

    @AfterAll
    public static void clear() {
        transaction.rollback();
        transaction.close();
    }

    @BeforeEach
    public void initializeTest() {
        try {
            movie = movieDao.create(movie);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testFindEntityById() {
        try {
            Movie actualResult = movieDao.getEntityById(movie.getId()).get();
            assertEquals(movie, actualResult);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testUpdate() {
        try {
            String oldSummary = movie.getSummary();
            movie.setSummary("new summary");
            movieDao.update(movie);
            Movie actualResult = movieDao.getEntityById(movie.getId()).get();
            assertEquals(movie, actualResult);
            movie.setSummary(oldSummary);
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void testGenres() {
        Assertions.assertDoesNotThrow(() -> movieDao.addGenre(movie.getId(), Genre.ACTION));
        Assertions.assertDoesNotThrow(() -> movieDao.addGenre(movie.getId(), Genre.HORROR));
        Assertions.assertThrows(DaoException.class, () -> movieDao.addGenre(movie.getId(), Genre.HORROR));
        Assertions.assertDoesNotThrow(() -> movieDao.removeGenre(movie.getId(), Genre.ACTION));
        Assertions.assertDoesNotThrow(() -> movieDao.removeGenre(movie.getId(), Genre.ACTION));
    }

    @Test
    public void testStaff() {
        Assertions.assertDoesNotThrow(() -> movieDao.assignCelebrityOnPosition(movie.getId(), 1, Position.ACTOR));
        Assertions.assertDoesNotThrow(() -> movieDao.assignCelebrityOnPosition(movie.getId(), 1, Position.DIRECTOR));
        Assertions.assertThrows(DaoException.class, () -> movieDao.assignCelebrityOnPosition(movie.getId(), 1, Position.ACTOR));
        Assertions.assertDoesNotThrow(() -> movieDao.removeCelebrityFromPosition(movie.getId(), 1, Position.ACTOR));
        Assertions.assertDoesNotThrow(() -> movieDao.removeCelebrityFromPosition(movie.getId(), 1, Position.ACTOR));
    }

    @Test
    public void testFindMoviesByCelebrityId() {
        //System.out.println(movieDao.findMoviesByCelebrityId(6));
    }

    @AfterEach
    public void endTest() {
        try {
            movieDao.delete(movie.getId());
        } catch (DaoException e) {
            logger.error(e.getMessage(), e);
        }
    }
}