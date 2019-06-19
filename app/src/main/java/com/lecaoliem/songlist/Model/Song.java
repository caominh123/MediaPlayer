package com.lecaoliem.songlist.Model;

import java.io.Serializable;

public class Song implements Serializable {
    private int mId;
    private String mSongTitle;
    private String mArtistName;
    private String mLinkData;
    private String mSize;
    private int mImage;
    private long mIdSong;

    public Song(String mSongTitle, String mArtistName, String mLinkData, String mSize,long mIdSong) {
        this.mSongTitle = mSongTitle;
        this.mArtistName = mArtistName;
        this.mLinkData = mLinkData;
        this.mSize = mSize;
        this.mIdSong = mIdSong;
    }

    public Song(int mId, String mSongTitle, String mArtistName, String mLinkData, String mSize,long mIdSong) {
        this.mId = mId;
        this.mSongTitle = mSongTitle;
        this.mArtistName = mArtistName;
        this.mLinkData = mLinkData;
        this.mSize = mSize;
        this.mIdSong = mIdSong;
    }

    public Song(int mId, String mSongTitle, String mArtistName, String mLinkData, String mSize) {
        this.mId = mId;
        this.mSongTitle = mSongTitle;
        this.mArtistName = mArtistName;
        this.mLinkData = mLinkData;
        this.mSize = mSize;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmSongTitle() {
        return mSongTitle;
    }

    public void setmSongTitle(String mSongTitle) {
        this.mSongTitle = mSongTitle;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public void setmArtistName(String mArtistName) {
        this.mArtistName = mArtistName;
    }

    public String getmLinkData() {
        return mLinkData;
    }

    public void setmLinkData(String mLinkData) {
        this.mLinkData = mLinkData;
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    public long getmIdSong() {
        return mIdSong;
    }

    public void setmIdSong(long mIdSong) {
        this.mIdSong = mIdSong;
    }
}
