package ru.gdgkazan.popularmoviesclean.screen.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;
import ru.arturvasilov.rxloader.RxUtils;
import ru.gdgkazan.popularmoviesclean.R;
import ru.gdgkazan.popularmoviesclean.data.repository.RepositoryProvider;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import ru.gdgkazan.popularmoviesclean.domain.model.Video;
import ru.gdgkazan.popularmoviesclean.domain.usecase.MoviesUseCase;
import ru.gdgkazan.popularmoviesclean.screen.general.LoadingDialog;
import ru.gdgkazan.popularmoviesclean.screen.general.LoadingView;
import ru.gdgkazan.popularmoviesclean.screen.movies.MoviesPresenter;
import ru.gdgkazan.popularmoviesclean.utils.Images;
import ru.gdgkazan.popularmoviesclean.utils.Videos;

public class MovieDetailsActivity extends AppCompatActivity implements MoviesDetailsView{

    private static final String MAXIMUM_RATING = "10";

    public static final String IMAGE = "image";
    public static final String EXTRA_MOVIE = "extraMovie";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.image)
    ImageView mImage;

    @BindView(R.id.title)
    TextView mTitleTextView;

    @BindView(R.id.overview)
    TextView mOverviewTextView;

    @BindView(R.id.rating)
    TextView mRatingTextView;

    @BindView(R.id.review)
    TextView mReviewTextView;

    @BindView(R.id.contentPanel)
    LinearLayout mLinear;

    private LoadingView mLoadingView;


    public static void navigate(@NonNull AppCompatActivity activity, @NonNull View transitionImage,
                                @NonNull Movie movie) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareWindowForAnimation();
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());


        ViewCompat.setTransitionName(findViewById(R.id.app_bar), IMAGE);
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this, getSupportLoaderManager());
        MoviesUseCase moviesUseCase = new MoviesUseCase(RepositoryProvider.getMoviesRepository(), RxUtils.async(), RxUtils.async(), RxUtils.async());
        MoviesDetailsPresenter presenter = new MoviesDetailsPresenter(this, moviesUseCase, lifecycleHandler);
        presenter.init(movie);
        //presenter.initVideo(movie);

        showMovie(movie);

        /**
         * TODO : task
         *
         * Load movie trailers and reviews and display them
         *
         * 1) See http://docs.themoviedb.apiary.io/#reference/movies/movieidtranslations/get?console=1
         * http://docs.themoviedb.apiary.io/#reference/movies/movieidtranslations/get?console=1
         * for API documentation
         *
         * 2) Add requests to repository
         *
         * 3) Add new UseCase for loading Movie details
         *
         * 4) Use MVP pattern for the Movie details screen
         *
         * 5) Execute requests in parallel and show loading progress until both of them are finished
         *
         * 6) Handle lifecycle changes any way you like
         *
         * 7) Save trailers and videos to Realm and use cached version when error occurred
         */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareWindowForAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void showMovie(@NonNull Movie movie) {
        String title = getString(R.string.movie_details);
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        Images.loadMovie(mImage, movie, Images.WIDTH_780);

        String year = movie.getReleasedDate().substring(0, 4);
        mTitleTextView.setText(getString(R.string.movie_title, movie.getTitle(), year));
        mOverviewTextView.setText(movie.getOverview());

        String average = String.valueOf(movie.getVoteAverage());
        average = average.length() > 3 ? average.substring(0, 3) : average;
        average = average.length() == 3 && average.charAt(2) == '0' ? average.substring(0, 1) : average;
        mRatingTextView.setText(getString(R.string.rating, average, MAXIMUM_RATING));
    }

    @Override
    public void showReview(@NonNull List<Review> reviews) {
        Log.i("movieReviews", reviews.size() +"");


        if(reviews.size() == 0|| reviews == null){
            Log.i("movieReviews","ret");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < reviews.size(); ++i) {
            Review currentReview = reviews.get(i);
            stringBuilder.append(currentReview.getAuthor());
            stringBuilder.append("\n");
            stringBuilder.append(currentReview.getContent());
            stringBuilder.append("\n\n");
            Log.i("movieReviews", currentReview.getContent());
        }
        mReviewTextView.setText(stringBuilder.toString());

    }

    @Override
    public void showVideo(@NonNull List<Video> videos) {
        if (videos == null || videos.size() == 0)
            return;
        for (int i = 0; i < videos.size(); ++i) {
            TextView textView = new TextView(this);
            textView.setText(videos.get(i).getName());
            textView.setTextColor(Color.BLUE);
            textView.setTextSize(18);
            textView.setTag(videos.get(i));
            textView.setOnClickListener((View content)-> {
                Video video = (Video) content.getTag();
                Videos.browseVideo(this, video);
            });
            mLinear.addView(textView);
        }
    }

    @Override
    public void showError(Throwable error) {
        Log.i("movieReviews", error.getMessage());
    }

    @Override
    public void showLoadingIndicator() {
        mLoadingView.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        mLoadingView.hideLoadingIndicator();
        Log.i("movieReviews", "CHELn");

    }
}
