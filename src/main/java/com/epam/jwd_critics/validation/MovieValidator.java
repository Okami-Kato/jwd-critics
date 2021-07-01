package com.epam.jwd_critics.validation;

import com.epam.jwd_critics.controller.command.Parameter;
import com.epam.jwd_critics.entity.AgeRestriction;
import com.epam.jwd_critics.entity.Country;
import com.epam.jwd_critics.entity.Genre;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MovieValidator {
    private static final String NAME_REGEX = "^[\\w\\s.,?!@'#$:;*+-=%]{1,150}$";
    private static final String RELEASE_DATE_REGEX = "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
            + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
            + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
            + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$";
    private static final String RUNTIME_REGEX = "^[0-9]+:[0-5][0-9]$";
    private static final String COUNTRY_REGEX = "^[A-Za-z]{3,56}$";
    private static final String AGE_RESTRICTION_REGEX = "^[\\w_-]{1,10}$";

    private static final String NAME_MESSAGE = "Movie name must be 1-150 characters. Can contain letters, numbers and special characters .,?!@'#$:;*+-=%";
    private static final String RELEASE_DATE_MESSAGE = "Movie release date must be of pattern mm-dd-yyyy";
    private static final String RUNTIME_MESSAGE = "Movie runtime must be of pattern hh:mm";
    private static final String COUNTRY_DOES_NOT_EXIST_MESSAGE = "Country %s does no exist";
    private static final String COUNTRY_MESSAGE = "Country name must be 3-56 characters. Country name must be a word";
    private static final String AGE_RESTRICTION_DOES_NOT_EXIST_MESSAGE = "Age restriction %s does no exist";
    private static final String AGE_RESTRICTION_MESSAGE = "Age restriction name must be 1-10 characters. Age restriction must be a sequence of characters and symbols -_";
    private static final String SUMMARY_MESSAGE = "Summary must be 100-10000 characters";
    private static final String GENRE_MESSAGE = "GenreId must be and integer";
    private static final String GENRE_DOES_NOT_EXIST_MESSAGE = "Genre with id %s does not exist";

    public Optional<ConstraintViolation> validateName(String name) {
        if (name.matches(NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.MOVIE_NAME, NAME_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateReleaseDate(String releaseDate) {
        if (releaseDate.matches(RELEASE_DATE_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.MOVIE_RELEASE_DATE, RELEASE_DATE_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateRuntime(String runtime) {
        if (runtime.matches(RUNTIME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.MOVIE_RUNTIME, RUNTIME_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateCountry(String country) {
        if (country.matches(COUNTRY_REGEX)) {
            try {
                Country.valueOf(country.toUpperCase());
                return Optional.empty();
            } catch (IllegalArgumentException e) {
                return Optional.of(new ConstraintViolation(Parameter.MOVIE_COUNTRY, String.format(COUNTRY_DOES_NOT_EXIST_MESSAGE, country)));
            }
        } else {
            return Optional.of(new ConstraintViolation(Parameter.MOVIE_COUNTRY, COUNTRY_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateAgeRestriction(String ageRestriction) {
        if (ageRestriction.matches(AGE_RESTRICTION_REGEX)) {
            try {
                AgeRestriction.valueOf(ageRestriction.toUpperCase().replace("-", "_"));
                return Optional.empty();
            } catch (IllegalArgumentException e) {
                return Optional.of(new ConstraintViolation(Parameter.MOVIE_AGE_RESTRICTION, String.format(AGE_RESTRICTION_DOES_NOT_EXIST_MESSAGE, ageRestriction)));
            }
        } else {
            return Optional.of(new ConstraintViolation(Parameter.MOVIE_AGE_RESTRICTION, AGE_RESTRICTION_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateSummary(String summary) {
        if (summary.length() > 100 && summary.length() < 10000) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(Parameter.MOVIE_SUMMARY, SUMMARY_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateGenreId(String genreIdStr) {
        try {
            int genreId = Integer.parseInt(genreIdStr);
            Optional<Genre> genre = Arrays.stream(Genre.values()).filter(g -> g.getId() == genreId).findFirst();
            if (genre.isPresent())
                return Optional.empty();
            else
                return Optional.of(new ConstraintViolation(Parameter.GENRES, String.format(GENRE_DOES_NOT_EXIST_MESSAGE, genreIdStr)));

        } catch (NumberFormatException e) {
            return Optional.of(new ConstraintViolation(Parameter.GENRES, GENRE_MESSAGE));
        }
    }

    public Set<ConstraintViolation> validateMovieData(String name, String releaseDate, String runtime, String country, String ageRestriction, String summary, String[] genreIds) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateName(name).ifPresent(violations::add);
        validateReleaseDate(releaseDate).ifPresent(violations::add);
        validateRuntime(runtime).ifPresent(violations::add);
        validateCountry(country).ifPresent(violations::add);
        validateAgeRestriction(ageRestriction).ifPresent(violations::add);
        validateSummary(summary).ifPresent(violations::add);
        for (String genreId : genreIds) {
            validateGenreId(genreId).ifPresent(violations::add);
        }
        return violations;
    }
}