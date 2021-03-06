package com.epam.jwd_critics.entity;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Movie extends AbstractBaseEntity {
    private static final String DEFAULT_MOVIE_IMAGE = "movie-posters/default_movie.png";

    @Column(name = "name")
    private String name;

    @Column(name = "summary")
    private String summary = "";

    @Column(name = "runtime")
    private Duration runtime;

    @Column(name = "country")
    private Country country;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "rating")
    private int rating;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "restriction")
    private AgeRestriction ageRestriction;


    private List<Genre> genres;
    private Map<Position, List<Celebrity>> staff;

    private Movie() {

    }

    public static MovieBuilder newBuilder() {
        return new Movie().new MovieBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Duration getRuntime() {
        return runtime;
    }

    public void setRuntime(Duration runtime) {
        this.runtime = runtime;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public AgeRestriction getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(AgeRestriction ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public Map<Position, List<Celebrity>> getStaff() {
        return staff;
    }

    public void setStaff(Map<Position, List<Celebrity>> staff) {
        this.staff = staff;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return rating == movie.rating && reviewCount == movie.reviewCount && name.equals(movie.name) && summary.equals(movie.summary) && runtime.equals(movie.runtime) && country == movie.country && imagePath.equals(movie.imagePath) && releaseDate.equals(movie.releaseDate) && ageRestriction == movie.ageRestriction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, summary, runtime, country, imagePath, rating, reviewCount, releaseDate, ageRestriction);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", runtime=" + runtime +
                ", country=" + country +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                ", releaseDate=" + releaseDate +
                ", ageRestriction=" + ageRestriction +
                ", genres=" + genres +
                ", staff=" + staff +
                '}';
    }

    public class MovieBuilder {

        private MovieBuilder() {
            // private constructor
        }

        public MovieBuilder setId(int id) {
            Movie.this.id = id;
            return this;
        }

        public MovieBuilder setName(String name) {
            Movie.this.name = name;
            return this;
        }

        public MovieBuilder setSummary(String summary) {
            Movie.this.summary = summary;
            return this;
        }

        public MovieBuilder setRuntime(Duration runtime) {
            Movie.this.runtime = runtime;
            return this;
        }

        public MovieBuilder setCountry(Country country) {
            Movie.this.country = country;
            return this;
        }

        public MovieBuilder setRating(int rating) {
            Movie.this.rating = rating;
            return this;
        }

        public MovieBuilder setReviewCount(int reviewCount) {
            Movie.this.reviewCount = reviewCount;
            return this;
        }

        public MovieBuilder setReleaseDate(LocalDate releaseDate) {
            Movie.this.releaseDate = releaseDate;
            return this;
        }

        public MovieBuilder setAgeRestriction(AgeRestriction ageRestriction) {
            Movie.this.ageRestriction = ageRestriction;
            return this;
        }

        public MovieBuilder setGenres(List<Genre> genres) {
            Movie.this.genres = genres;
            return this;
        }

        public MovieBuilder setStaff(Map<Position, List<Celebrity>> staff) {
            Movie.this.staff = staff;
            return this;
        }

        public MovieBuilder setImagePath(String imagePath) {
            Movie.this.imagePath = imagePath;
            return this;
        }

        public Movie build() {
            Movie movie = new Movie();
            movie.id = Movie.this.id;
            movie.name = Movie.this.name;
            movie.summary = Movie.this.summary;
            movie.runtime = Movie.this.runtime;
            movie.country = Movie.this.country;
            movie.rating = Movie.this.rating;
            movie.reviewCount = Movie.this.reviewCount;
            movie.releaseDate = Movie.this.releaseDate;
            movie.ageRestriction = Movie.this.ageRestriction;
            movie.genres = Movie.this.genres;
            movie.staff = Movie.this.staff;
            movie.imagePath = (Movie.this.imagePath == null || Movie.this.imagePath.equals("")) ? (DEFAULT_MOVIE_IMAGE) : (Movie.this.imagePath);
            return movie;
        }
    }
}
