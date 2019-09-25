package ru.gdgkazan.popularmoviesclean.domain;

import java.util.List;

import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public interface MoviesRepository {

    Observable<List<ru.gdgkazan.popularmoviesclean.domain.model.Movie>> popularMovies();

    Observable<List<ru.gdgkazan.popularmoviesclean.domain.model.Review>> movieReviews(ru.gdgkazan.popularmoviesclean.domain.model.Movie movie);

    Observable<List<ru.gdgkazan.popularmoviesclean.domain.model.Video>> movieVideo(ru.gdgkazan.popularmoviesclean.domain.model.Movie movie);


}
