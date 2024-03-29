package ru.gdgkazan.popularmoviesclean.data.mapper;

import ru.gdgkazan.popularmoviesclean.data.model.content.Review;
import ru.gdgkazan.popularmoviesclean.data.model.content.Video;
import rx.functions.Func1;

/**
 * @author Artur Vasilov
 */
public class MoviesVideoMapper implements Func1<Video, ru.gdgkazan.popularmoviesclean.domain.model.Video> {

    @Override
    public ru.gdgkazan.popularmoviesclean.domain.model.Video call(Video video) {
        return new ru.gdgkazan.popularmoviesclean.domain.model.Video(video.getKey(), video.getName());
    }
}
