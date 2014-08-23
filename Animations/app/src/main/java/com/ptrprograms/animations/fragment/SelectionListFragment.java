package com.ptrprograms.animations.fragment;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ptrprograms.animations.activity.BaseAnimationActivity;
import com.ptrprograms.animations.activity.ExplodeAnimationActivity;
import com.ptrprograms.animations.activity.ExplosionRevealActivity;
import com.ptrprograms.animations.activity.FadeAnimationActivity;
import com.ptrprograms.animations.activity.SlidingAnimationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulruiz on 8/18/14.
 */
public class SelectionListFragment extends ListFragment {

    private final String CATEGORY_EXPLOSION_REVEAL = "Explosion Reveal";
    private final String CATEGORY_RIPPLES = "Ripples";
    private final String CATEGORY_ACTIVITY_TRANSITION_EXPLODE = "Activity Transition: Explode";
    private final String CATEGORY_ACTIVITY_TRANSITION_SLIDE = "Activity Transition: Slide";
    private final String CATEGORY_ACTIVITY_TRANSITION_FADE = "Activity Transition: Fade";
    /*
    private final String CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_BOUNDS = "Shared Element Transition: Change Bounds";
    private final String CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_CLIP_BOUNDS = "Shared Element Transition: Change Clip Bounds";
    private final String CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_TRANSFORM = "Shared Element Transition: Change Transform";
    private final String CATEGORY_SHARED_ELEMENT_TRANSITION_MOVE_IMAGE = "Shared Element Transition: Move Image";
    private final String CATEGORY_SCENE_TRANSITION = "Scene Transitions";
    */
    private ArrayAdapter<String> mAdapter;

    public static SelectionListFragment getInstance() {
        SelectionListFragment fragment = new SelectionListFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_list_item_1, getCategories() );
        setListAdapter(mAdapter);
    }

    private List<String> getCategories() {
        List<String> categories = new ArrayList<String>();

        categories.add( CATEGORY_EXPLOSION_REVEAL );
        categories.add( CATEGORY_RIPPLES );
        categories.add( CATEGORY_ACTIVITY_TRANSITION_EXPLODE );
        categories.add( CATEGORY_ACTIVITY_TRANSITION_SLIDE );
        categories.add( CATEGORY_ACTIVITY_TRANSITION_FADE );
        /*
        categories.add( CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_BOUNDS );
        categories.add( CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_CLIP_BOUNDS );
        categories.add( CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_TRANSFORM );
        categories.add( CATEGORY_SHARED_ELEMENT_TRANSITION_MOVE_IMAGE );
        categories.add( CATEGORY_SCENE_TRANSITION );
        */

        return categories;
    }

    @Override
    public void onPause() {
        super.onPause();
        getListView().setDividerHeight( 0 );
    }

    @Override
    public void onResume() {
        super.onResume();
        getListView().setDividerHeight( 1 );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        getActivity().getWindow().setExitTransition( null );
        getActivity().getWindow().setEnterTransition( null );

        String text = ( (TextView) v ).getText().toString();
        Intent intent = null;
        if( text.equalsIgnoreCase( CATEGORY_EXPLOSION_REVEAL ) ) {
            intent = new Intent( getActivity(), ExplosionRevealActivity.class );
        }
        else if( text.equalsIgnoreCase( CATEGORY_RIPPLES ) ) {

        }
        else if( text.equalsIgnoreCase( CATEGORY_ACTIVITY_TRANSITION_EXPLODE ) ) {
            getActivity().getWindow().setExitTransition( new Explode() );
            getActivity().getWindow().setEnterTransition( new Explode() );
            intent = new Intent( getActivity(), ExplodeAnimationActivity.class );
        }
        else if( text.equalsIgnoreCase( CATEGORY_ACTIVITY_TRANSITION_SLIDE ) ) {
            getActivity().getWindow().setExitTransition( new Slide() );
            getActivity().getWindow().setEnterTransition( new Slide() );
            intent = new Intent( getActivity(), SlidingAnimationActivity.class );
        }
        else if( text.equalsIgnoreCase( CATEGORY_ACTIVITY_TRANSITION_FADE ) ) {
            getActivity().getWindow().setExitTransition( new Fade() );
            getActivity().getWindow().setEnterTransition( new Fade() );
            intent = new Intent( getActivity(), FadeAnimationActivity.class );
        }
        /*
        else if( text.equalsIgnoreCase( CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_BOUNDS ) ) {

        }
        else if( text.equalsIgnoreCase( CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_CLIP_BOUNDS ) ) {

        }
        else if( text.equalsIgnoreCase( CATEGORY_SHARED_ELEMENT_TRANSITION_CHANGE_TRANSFORM ) ) {

        }
        else if( text.equalsIgnoreCase( CATEGORY_SHARED_ELEMENT_TRANSITION_MOVE_IMAGE ) ) {
            intent = new Intent( getActivity(), BaseAnimationActivity.class );
        }
        */

        if( intent != null )
            startActivity( intent );
    }
}
