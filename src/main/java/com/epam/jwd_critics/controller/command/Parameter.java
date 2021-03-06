package com.epam.jwd_critics.controller.command;

public enum Parameter {
    COMMAND("command"),
    LANG("lang"),
    PREVIOUS_PAGE("previousPage"),
    CURRENT_PICTURE("currentPicture"),

    NEW_MOVIES_PAGE("newMoviesPage"),
    NEW_REVIEWS_PAGE("newReviewsPage"),
    NEW_USERS_PAGE("newUsersPage"),
    NEW_CELEBRITIES_PAGE("newCelebritiesPage"),

    MOVIE_ID("movieId"),
    USER_ID("userId"),
    REVIEW_ID("reviewId"),
    CELEBRITY_ID("celebrityId"),
    POSITION_ID("positionId"),
    MOVIE_REVIEW_ID("movieReviewId"),

    MOVIE_REVIEW_TEXT("movieReviewText"),
    MOVIE_REVIEW_SCORE("movieReviewScore"),

    IMAGE_PATH("imagePath"),

    NEW_STATUS("newStatus"),
    NEW_PASSWORD("newPassword"),
    CONFIRM_NEW_PASSWORD("confirmedNewPassword"),

    CELEBRITY_POSITIONS("celebrityPositions"),

    MOVIE_NAME("movieName"),
    MOVIE_RELEASE_DATE("movieReleaseDate"),
    MOVIE_RUNTIME("movieRuntime"),
    MOVIE_COUNTRY("movieCountryId"),
    MOVIE_AGE_RESTRICTION("movieAgeRestrictionId"),
    MOVIE_SUMMARY("movieSummary"),
    MOVIE_GENRES("movieGenreId"),

    ACTIVATION_KEY("activationKey"),
    RECOVERY_KEY("recoveryKey"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    PASSWORD("password"),
    LOGIN("login");

    private final String name;

    Parameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
