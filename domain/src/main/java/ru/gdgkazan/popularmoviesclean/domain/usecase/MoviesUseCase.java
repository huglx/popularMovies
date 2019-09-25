package ru.gdgkazan.popularmoviesclean.domain.usecase;

import java.util.List;

import ru.gdgkazan.popularmoviesclean.domain.MoviesRepository;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import ru.gdgkazan.popularmoviesclean.domain.model.Video;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public class MoviesUseCase {

    private final MoviesRepository mRepository;
    private final Observable.Transformer<List<Movie>, List<Movie>> mAsyncTransformer;
    private final Observable.Transformer<List<Review>, List<Review>> mAsyncTransformerReview;
    private final Observable.Transformer<List<Video>, List<Video>> mAsyncTransformerVideo;



    public MoviesUseCase(MoviesRepository repository,
                         Observable.Transformer<List<Movie>, List<Movie>> asyncTransformer,
                         Observable.Transformer<List<Review>, List<Review>> AsyncTransformerReview,
                         Observable.Transformer<List<Video>, List<Video>> AsyncTransformerVideo) {
        mRepository = repository;
        mAsyncTransformer = asyncTransformer;
        mAsyncTransformerReview = AsyncTransformerReview;
        mAsyncTransformerVideo = AsyncTransformerVideo;
    }

    public Observable<List<Movie>> popularMovies() {
        return mRepository.popularMovies()
                .compose(mAsyncTransformer);
    }

    public Observable<List<Review>> movieReviews(Movie movie) {
        return mRepository.movieReviews(movie)
                .compose(mAsyncTransformerReview);
    }

    public Observable<List<Video>> movieVideo(Movie movie) {
        return mRepository.movieVideo(movie)
                .compose(mAsyncTransformerVideo);
    }
}


