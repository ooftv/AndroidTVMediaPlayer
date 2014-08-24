package com.ptrprograms.animations.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.ptrprograms.animations.R;

/**
 * Created by paulruiz on 8/18/14.
 */
public class RevealAnimationActivity extends Activity {

    ImageView imageView;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView( R.layout.activity_base_animation );
        activity = this;
        imageView = (ImageView) findViewById( R.id.image );
        Button button = (Button) findViewById( R.id.button );
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), SharedElementAnimationActivity.class );
                ((ViewGroup) imageView.getParent()).setTransitionGroup(false);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation( activity, imageView, "image" );
                startActivity( intent, options.toBundle() );

            }
        });
    }
}
