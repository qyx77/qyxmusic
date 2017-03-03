package com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.model;

/**
 * Created by Administrator on 2017/1/26.
 */
public class Song {

    private String mSongName;
    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String SongName) {
        this.mSongName = SongName;
        this.mSongLength=SongName.length();
    }
    private String mSongFileName;
    public String getSongFileName() {
        return mSongFileName;
    }

    public void setSongFileName(String songFileName) {
        this.mSongFileName = songFileName;
    }
    private int mSongLength;

    public int getSongLength() {
        return mSongLength;
    }

    public char[] getSongNameCharacters(){
        return mSongName.toCharArray();
    }


}
