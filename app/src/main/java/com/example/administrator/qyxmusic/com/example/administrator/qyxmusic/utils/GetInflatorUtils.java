package com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.administrator.qyxmusic.R;

/**
 * Created by Administrator on 2017/1/25.
 */
public class GetInflatorUtils {
    public static View getView(Context context,int layoutId)
    {
        LayoutInflater layoutInflater=(LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(layoutId,null);
        return view;
    }
}
