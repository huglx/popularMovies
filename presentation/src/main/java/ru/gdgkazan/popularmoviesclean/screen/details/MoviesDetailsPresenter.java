package ru.gdgkazan.popularmoviesclean.screen.details;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.popularmoviesclean.R;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import ru.gdgkazan.popularmoviesclean.domain.usecase.MoviesUseCase;
import ru.gdgkazan.popularmoviesclean.screen.movies.MoviesView;
import rx.functions.Action1;

/**
 * @author Artur Vasilov
 */
public class MoviesDetailsPresenter {

    private final MoviesDetailsView mMoviesView;
    private final MoviesUseCase mMoviesUseCase;
    private final LifecycleHandler mLifecycleHandler;


    public MoviesDetailsPresenter(@NonNull MoviesDetailsView moviesView, @NonNull MoviesUseCase moviesUseCase,@NonNull LifecycleHandler lifecycleHandler) {
        mMoviesView = moviesView;
        mMoviesUseCase = moviesUseCase;
        mLifecycleHandler = lifecycleHandler;
    }

    public void init(Movie movie) {
        mMoviesUseCase.movieReviews(movie)
                .doOnSubscribe(mMoviesView::showLoadingIndicator)
                .doAfterTerminate(mMoviesView::hideLoadingIndicator)
                .compose(mLifecycleHandler.load(R.id.movies_request_id))
                .subscribe(mMoviesView::showReview, throwable -> mMoviesView.showError(throwable));

        mMoviesUseCase.movieVideo(movie)
                .doOnSubscribe(mMoviesView::showLoadingIndicator)
                .doAfterTerminate(mMoviesView::hideLoadingIndicator)
                .subscribe(mMoviesView::showVideo, throwable -> mMoviesView.showError(throwable));

    }
}

