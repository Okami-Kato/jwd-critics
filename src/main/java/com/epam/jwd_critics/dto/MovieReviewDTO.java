package com.epam.jwd_critics.dto;

import com.epam.jwd_critics.entity.Movie;
import com.epam.jwd_critics.entity.MovieReview;
import com.epam.jwd_critics.entity.User;

import java.util.Objects;

public class MovieReviewDTO {
    private int score;
    private String text;
    private String imagePath;
    private String title;
    private int userId;
    private int movieId;
    private int id;

    public MovieReviewDTO(MovieReview review) {
        this.score = review.getScore();
        this.text = review.getText();
        this.userId = review.getUserId();
        this.movieId = review.getMovieId();
        this.id = review.getId();
    }

    public MovieReviewDTO(MovieReview review, User user) {
        this(review);
        this.imagePath = user.getImagePath();
        this.title = user.getFirstName();
    }

    public MovieReviewDTO(MovieReview review, Movie movie) {
        this(review);
        this.imagePath = movie.getImagePath();
        this.title = movie.getName();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieReviewDTO that = (MovieReviewDTO) o;
        return score == that.score && userId == that.userId && movieId == that.movieId && id == that.id && text.equals(that.text) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, text, title, userId, movieId, id);
    }

    @Override
    public String toString() {
        return "MovieReviewDTO{" +
                "score=" + score +
                ", text='" + text + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                ", movieId=" + movieId +
                ", id=" + id +
                '}';
    }
}
