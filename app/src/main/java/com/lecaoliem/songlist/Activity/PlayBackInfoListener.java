package com.lecaoliem.songlist.Activity;
import android.support.annotation.IntDef;

import com.lecaoliem.songlist.Activity.MainActivity;
import com.lecaoliem.songlist.Model.Song;
import com.lecaoliem.songlist.Service.MusicService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Allows {@link MusicService} to report media playback duration and progress updates to
 * the {@link MainActivity}.
 */

public abstract class PlayBackInfoListener {

    @IntDef({State.INVALID, State.PLAYING, State.PAUSED, State.RESET, State.COMPLETED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int INVALID = -1;
        int PLAYING = 0;
        int PAUSED = 1;
        int RESET = 2;
        int COMPLETED = 3;
    }

    public static String convertStateToString(@State int state) {
        String stateString;
        switch (state) {
            case State.COMPLETED:
                stateString = "COMPLETED";
                break;
            case State.INVALID:
                stateString = "INVALID";
                break;
            case State.PAUSED:
                stateString = "PAUSED";
                break;
            case State.PLAYING:
                stateString = "PLAYING";
                break;
            case State.RESET:
                stateString = "RESET";
                break;
            default:
                stateString = "N/A";
        }
        return stateString;
    }

    public void setDurationMax(int duration) {
    }

    public void onPositionChanged(int position) {
    }

    public void onPrepared(){

    }

    public void onPlaybackCompleted() {
    }

    public void setInforSong(Song song,int duration){}
}
