package com.lecaoliem.songlist.Service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.lecaoliem.songlist.Activity.PlayBackInfoListener;
import com.lecaoliem.songlist.Adapter.PlayAdapter;
import com.lecaoliem.songlist.Model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, PlayAdapter {

    static MediaPlayer mediaPlayer;
    ArrayList<Song> songList;
    PlayBackInfoListener mPlayBackInfoListener;
    String TAG = "Music Service";
    int songPosition;
    long idSongCurrent;
    final IBinder musicBind = new MusicBinder();
    // Cờ kiểm tra bài hát có thay đổi
    public boolean isSongChanged = false;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize position
        songPosition = -1;
        //create player
        mediaPlayer = new MediaPlayer();

        initMusicPlayer();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(songList,new Random(System.nanoTime()));
    }

    public void initMusicPlayer() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setSong(int songIndex) {
        if (songIndex != songPosition)
            isSongChanged = true;
        songPosition = songIndex;
    }

    public Song getSongCurrent() {
        if (songList.size() != 0)
            return songList.get(songPosition);
        return null;
    }
    public ArrayList<Song> getArraySong(){
        return songList;
    }
    public void setList(ArrayList<Song> theSongs) {
        songList = theSongs;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void initMaxDuration() {
        if (mPlayBackInfoListener != null) {
            mPlayBackInfoListener.setDurationMax(mediaPlayer.getDuration());
        }
    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }


    @Override
    public void loadMedia() {
        reset();
        //get song
        Song playSong = songList.get(songPosition);
        // set cờ lại ban đầu
        isSongChanged = false;

        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(playSong.getmLinkData()));
            //mediaPlayer.setDataSource(getApplicationContext(), trackUri);
        } catch (IOException e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        //
        mediaPlayer.prepareAsync(); // prepare async to not block main thread
        Log.e("Music Service", "Load Media");
    }

    @Override
    public void release() {
        // Sẽ gọi hàm release khi Service hủy
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            Log.e("Music Service", "Play");
            mediaPlayer.start();
        }
    }

    @Override
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    @Override
    public void previous() {
        Log.e(TAG, "Previous");
        if (songPosition > 0)
            songPosition--;
        else
            songPosition = songList.size() - 1;
        loadMedia();
    }

    @Override
    public void next() {
        Log.e(TAG, "Next");
        if (songPosition < songList.size() - 1)
            songPosition++;
        else
            songPosition = 0;
        loadMedia();
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.e(TAG, "Pause");
           /* if (mPlayBackInfoListener != null) {
                mPlayBackInfoListener.onStateChanged(PlayBackInfoListener.State.PAUSED);
            }
            logToUI("playbackPause()");*/
        }
    }

    @Override
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }

    }



    @Override
    public void setInforMusicCallBack() {
        // Thiết lập ban đầu cho SeekBar và hiển thị thời gian bài hát
        if (mPlayBackInfoListener != null) {
            //mPlayBackInfoListener.setDurationMax(mediaPlayer.getDuration());
            mPlayBackInfoListener.setInforSong(songList.get(songPosition), mediaPlayer.getDuration());
            mPlayBackInfoListener.onPositionChanged(0);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        // TODO The MediaPlayer has moved to the Error state, must be reset!
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.e(TAG, "Prepared");

        //  start playback
        mediaPlayer.start();

        // Thiết lập ban đầu cho SeekBar và hiển thị thời gian bài hát
        if (mPlayBackInfoListener != null) {
            //mPlayBackInfoListener.setDurationMax(mediaPlayer.getDuration());
            mPlayBackInfoListener.setInforSong(songList.get(songPosition), mediaPlayer.getDuration());
            mPlayBackInfoListener.onPositionChanged(0);
            mPlayBackInfoListener.onPrepared();
        }


    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        // TODO Xử lý sự kiện khi play xong 1 bài nhạc
        // stopUpdatingCallbackWithPosition(true);
        Log.e(TAG, "Completed");
        if (songPosition < songList.size() - 1) {
            songPosition++;
            loadMedia();
        } else {
            songPosition = 0;
            loadMedia();
        }
    }

    public void setPlaybackInfoListener(PlayBackInfoListener listener) {
        mPlayBackInfoListener = listener;
    }

}