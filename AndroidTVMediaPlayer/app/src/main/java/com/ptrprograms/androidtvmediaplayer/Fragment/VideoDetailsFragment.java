package com.ptrprograms.androidtvmediaplayer.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.DetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.ptrprograms.androidtvmediaplayer.Activity.PlayerActivity;
import com.ptrprograms.androidtvmediaplayer.Model.Movie;
import com.ptrprograms.androidtvmediaplayer.Presenter.DetailsDescriptionPresenter;
import com.ptrprograms.androidtvmediaplayer.R;
import com.ptrprograms.androidtvmediaplayer.Util.PicassoBackgroundManagerTarget;
import com.ptrprograms.androidtvmediaplayer.Util.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VideoDetailsFragment extends DetailsFragment {

    public static final String EXTRA_MOVIE = "extra_movie";

    private static final int ACTION_WATCH_TRAILER = 1;

    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;

    private Movie selectedMovie;

    private Target mBackgroundTarget;
    private DisplayMetrics mMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedMovie = (Movie) getActivity().getIntent().getSerializableExtra( EXTRA_MOVIE );

        initBackground();
        new DetailRowBuilderTask().execute( selectedMovie );

    }

    private void initBackground() {
        BackgroundManager backgroundManager = BackgroundManager.getInstance(getActivity());
        backgroundManager.attach(getActivity().getWindow());
        mBackgroundTarget = new PicassoBackgroundManagerTarget( backgroundManager );

        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        if( selectedMovie != null && !TextUtils.isEmpty( selectedMovie.getBackgroundImageUrl() ) ) {
            try {
                updateBackground(new URI(selectedMovie.getBackgroundImageUrl()));
            } catch (URISyntaxException e) { }
        }
    }

    protected void updateBackground(URI uri) {
        if( uri.toString() == null ) {
            try {
                uri = new URI("");
            } catch( URISyntaxException e ) {}
        }

        Picasso.with(getActivity())
                .load( uri.toString() )
                .error( getResources().getDrawable( R.drawable.default_background ) )
                .resize( mMetrics.widthPixels, mMetrics.heightPixels )
                .into( mBackgroundTarget );
    }



    private class DetailRowBuilderTask extends AsyncTask<Movie, Integer, DetailsOverviewRow> {
        @Override
        protected DetailsOverviewRow doInBackground(Movie... movies) {
            selectedMovie = movies[0];
            DetailsOverviewRow row = null;
            try {
                row = new DetailsOverviewRow(selectedMovie);
                Bitmap poster = Picasso.with( getActivity() )
                        .load( selectedMovie.getCardImageUrl() )
                        .resize(Utils.dpToPx(DETAIL_THUMB_WIDTH, getActivity().getApplicationContext()),
                                Utils.dpToPx(DETAIL_THUMB_HEIGHT, getActivity().getApplicationContext()))
                        .centerCrop()
                        .get();
                row.setImageBitmap(getActivity(), poster);
            } catch (IOException e) {
                getActivity().finish();
                return null;
            } catch( NullPointerException e ) {
                getActivity().finish();
                return null;
            }

            row.addAction( new Action( ACTION_WATCH_TRAILER, getResources().getString(
                    R.string.watch_trailer_1 ), "" ) );

            return row;
        }

        @Override
        protected void onPostExecute(DetailsOverviewRow detailRow) {
            if( detailRow == null )
                return;

            ClassPresenterSelector ps = new ClassPresenterSelector();
            DetailsOverviewRowPresenter dorPresenter =
                    new DetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
            // set detail background and style
            dorPresenter.setBackgroundColor(getResources().getColor(R.color.detail_background));
            dorPresenter.setStyleLarge(true);
            dorPresenter.setOnActionClickedListener(new OnActionClickedListener() {
                @Override
                public void onActionClicked(Action action) {
                    if (action.getId() == ACTION_WATCH_TRAILER) {
                        Intent intent = new Intent(getActivity(), PlayerActivity.class);
                        intent.putExtra(getResources().getString(R.string.movie), selectedMovie);
                        intent.putExtra(getResources().getString(R.string.should_start), true);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ps.addClassPresenter(DetailsOverviewRow.class, dorPresenter);
            ps.addClassPresenter(ListRow.class,
                    new ListRowPresenter());


            ArrayObjectAdapter adapter = new ArrayObjectAdapter(ps);
            adapter.add(detailRow);
            loadRelatedMedia( adapter );
            setAdapter(adapter);
        }

        private void loadRelatedMedia( ArrayObjectAdapter adapter ) {
            /*
            Collections.shuffle( mMovies );
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
            for (int j = 0; j < NUM_COLS; j++) {
                listRowAdapter.add( mMovies.get( j % mMovies.size() ));
            }

            HeaderItem header = new HeaderItem(0, subcategories[0], null);
            adapter.add(new ListRow(header, listRowAdapter));
            */
        }

    }

}
