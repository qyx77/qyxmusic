package com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.utils;

import android.content.Context;

import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.data.Const;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/2/5.
 */
public class SaveData {
    public static void SaveDate(Context context)
    {
        try {
            FileOutputStream fos=context.openFileOutput(Const.FILE_NAME_SAVE_DATA,context.MODE_PRIVATE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
