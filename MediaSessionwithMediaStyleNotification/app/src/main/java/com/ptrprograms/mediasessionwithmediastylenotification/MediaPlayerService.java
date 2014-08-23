package com.ptrprograms.mediasessionwithmediastylenotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioTrack;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.MediaSessionToken;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by paulruiz on 8/10/14.
 */
public class MediaPlayerService extends Service {

    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_STOP = "action_stop";

    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaSessionToken mToken;
    private MediaController mController;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleIntent( Intent intent ) {
        Log.e( "MediaPlayerService", "handleIntent" );
        if( intent == null || intent.getAction() == null )
            return;

        String action = intent.getAction();
        Log.e( "MediaPlayerService", "handleIntent: " + action );
        if( action.equalsIgnoreCase( ACTION_PLAY ) ) {
            mController.getTransportControls().play();
            /*
            //TODO Waiting on a response from _anyone_ on getting this to work.
                MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();
                metadataBuilder.putString( MediaMetadata.METADATA_KEY_ARTIST, "some Artist" );
                metadataBuilder.putString( MediaMetadata.METADATA_KEY_ALBUM, "some album" );
                metadataBuilder.putString( MediaMetadata.METADATA_KEY_TITLE, "some title" );
                metadataBuilder.putBitmap( MediaMetadata.METADATA_KEY_ART, BitmapFactory.decodeResource( this.getResources(), R.drawable.ic_launcher ) );
                metadataBuilder.putBitmap( MediaMetadata.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeResource( this.getResources(), R.drawable.ic_launcher ) );
                mSession.setMetadata( metadataBuilder.build() );
            */
            buildNotification( ACTION_PLAY );
        } else if( action.equalsIgnoreCase( ACTION_STOP ) ) {
            mController.getTransportControls().stop();
            buildNotification( ACTION_STOP );
        }
    }

    private void buildNotification( String action ) {
        Log.e( "MediaPlayerService", "buildNotification" );
        if( ACTION_PLAY.equalsIgnoreCase( action ) ) {

            Notification.MediaStyle style = new Notification.MediaStyle();
            style.setMediaSession( mSession.getSessionToken() );

            Notification.Builder builder = new Notification.Builder( this )
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setStyle(style);

            Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );

            intent.setAction( ACTION_STOP );
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
            builder.addAction( R.drawable.ic_launcher, "Play", pendingIntent );
            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            notificationManager.notify( 1, builder.build() );

        } else if( ACTION_STOP.equalsIgnoreCase( action ) ) {


            Notification.MediaStyle style = new Notification.MediaStyle();
            style.setMediaSession( mToken );

            Notification.Builder builder = new Notification.Builder( this )
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setStyle(style);

            Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
            intent.setAction( ACTION_PLAY );
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
            builder.addAction( R.drawable.ic_launcher, "Stop", pendingIntent );

            NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
            notificationManager.notify( 1, builder.build() );

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( mManager == null ) {
            initMediaSessions();
        }

        handleIntent( intent );
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaSessions() {
        mManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        mSession = mManager.createSession("sample session");
        mToken = mSession.getSessionToken();
        mController = MediaController.fromToken( mToken );
        mSession.addTransportControlsCallback( new MediaSession.TransportControlsCallback() {
            @Override
            public void onPlay() {
                super.onPlay();

            }

            @Override
            public void onPause() {
                super.onPause();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
            }

            @Override
            public void onFastForward() {
                super.onFastForward();
            }

            @Override
            public void onRewind() {
                super.onRewind();
            }

            @Override
            public void onStop() {
                super.onStop();
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
            }

            @Override
            public void onSetRating(Rating rating) {
                super.onSetRating(rating);
            }
        });
    }
}
