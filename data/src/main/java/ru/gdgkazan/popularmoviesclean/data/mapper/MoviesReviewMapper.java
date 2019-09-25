package ru.gdgkazan.popularmoviesclean.data.mapper;

import ru.gdgkazan.popularmoviesclean.data.model.content.Review;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import rx.functions.Func1;

/**
 * @author Artur Vasilov
 */
public class MoviesReviewMapper implements Func1<ru.gdgkazan.popularmoviesclean.data.model.content.Review, ru.gdgkazan.popularmoviesclean.domain.model.Review> {

    @Override
    public ru.gdgkazan.popularmoviesclean.domain.model.Review call(ru.gdgkazan.popularmoviesclean.data.model.content.Review review) {
        return new ru.gdgkazan.popularmoviesclean.domain.model.Review(review.getAuthor(), review.getContent());
    }
}
