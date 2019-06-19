package com.lecaoliem.songlist.Adapter;

public interface PlayAdapter {

    void loadMedia();

    void release();

    boolean isPlaying();

    void play();

    void reset();

    void pause();

    void seekTo(int position);

    void  shuffle();

    void previous();

    void next();

    int getCurrentPosition();
    public void setInforMusicCallBack();

}
