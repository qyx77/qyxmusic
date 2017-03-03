package com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Administrator on 2017/1/27.
 */
public class MyPlayer {
    public final static int INDEX_STONE_ENTER =0;
    public final static int INDEX_STONE_CANCEL=1;
    public final static int INDEX_STONE_COIN  =2;
    //音效文件
    public static final String[] SONG_NAMES={"enter.mp3","cancel.mp3","coin.mp3"};
    //音效
    public static MediaPlayer[] mToneMediaPlayer=new MediaPlayer[SONG_NAMES.length];
    /**
     * 音乐文件
     */
    static MediaPlayer myMediaPlayer;
    //播放音效
    public static void playStone(Context context,int index)
    {
        if(mToneMediaPlayer[index]==null)
        {
            mToneMediaPlayer[index]=new MediaPlayer();
            //强制重置
           // mToneMediaPlayer[index].reset();
            //加载声音文件
            AssetManager assetManager=context.getAssets();
            try {
                AssetFileDescriptor assetFileDescriptor=assetManager.openFd(SONG_NAMES[index]);
                mToneMediaPlayer[index].setDataSource(assetFileDescriptor.getFileDescriptor(),
                        assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
                mToneMediaPlayer[index].prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mToneMediaPlayer[index].start();

    }
    //播放音乐
    public static void playSong(Context context,String fileName)
    {
       if (myMediaPlayer==null)
       {
           myMediaPlayer=new MediaPlayer();
       }
        //强制重置
        myMediaPlayer.reset();
        //加载声音文件
        AssetManager assetManager=context.getAssets();
        try {
            AssetFileDescriptor assetFileDescriptor=assetManager.openFd(fileName);
            //getFileDescriptor():Returns the FileDescriptor that can be used to read the data in the file
            //getStartOffset():Returns the byte offset where this asset entry's data starts.
            //getLength():Returns the total number of bytes of this asset entry's data.
            myMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
            myMediaPlayer.prepare();
            myMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void stopSong()
    {
        if(myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
        }
    }
}
