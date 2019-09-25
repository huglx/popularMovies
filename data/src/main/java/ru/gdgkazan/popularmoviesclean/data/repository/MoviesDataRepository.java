package ru.gdgkazan.popularmoviesclean.data.repository;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.gdgkazan.popularmoviesclean.data.cache.MoviesCacheTransformer;
import ru.gdgkazan.popularmoviesclean.data.cache.MoviesReviewCacheTransformer;
import ru.gdgkazan.popularmoviesclean.data.cache.MoviesVideoCacheTransformer;
import ru.gdgkazan.popularmoviesclean.data.mapper.MoviesMapper;
import ru.gdgkazan.popularmoviesclean.data.mapper.MoviesReviewMapper;
import ru.gdgkazan.popularmoviesclean.data.mapper.MoviesVideoMapper;
import ru.gdgkazan.popularmoviesclean.data.model.response.MoviesResponse;
import ru.gdgkazan.popularmoviesclean.data.model.response.ReviewsResponse;
import ru.gdgkazan.popularmoviesclean.data.model.response.VideosResponse;
import ru.gdgkazan.popularmoviesclean.data.network.ApiFactory;
import ru.gdgkazan.popularmoviesclean.domain.MoviesRepository;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import ru.gdgkazan.popularmoviesclean.domain.model.Video;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public class MoviesDataRepository implements MoviesRepository {

    @Override
    public Observable<List<Movie>> popularMovies() {
        return ApiFactory.getMoviesService()
                .popularMovies()
                .map(MoviesResponse::getMovies)
                .compose(new MoviesCacheTransformer())
                .flatMap(Observable::from)
                .map(new MoviesMapper())
                .toList();
    }

    @Override
    public Observable<List<Review>> movieReviews(Movie movie) {
        Log.i("Debug", ""+movie.getmId());
        return ApiFactory.getMoviesService()
                .movieReviews(movie.getmId())
                .map(ReviewsResponse::getReviews)
                .compose(new MoviesReviewCacheTransformer())
                .flatMap(Observable::from)
                .map(new MoviesReviewMapper())
                .toList();

    }

    @Override
    public Observable<List<Video>> movieVideo(Movie movie) {
        return ApiFactory.getMoviesService()
                .movieVideos(movie.getmId())
                .map(VideosResponse::getVideos)
                .compose(new MoviesVideoCacheTransformer())
                .flatMap(Observable::from)
                .map(new MoviesVideoMapper())
                .toList();
    }
}

