package com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.qyxmusic.R;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.data.Const;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.model.IDialogButtonClickListener;

/**
 * Created by Administrator on 2017/2/2.
 */
public class ShowDialog {

    public static AlertDialog mAlertDialog;
    public static void showDialog(final Context context, String message, final IDialogButtonClickListener listener)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View mViewDialog=LayoutInflater.from(context).inflate(R.layout.malertdialog,null);
        TextView textView=(TextView)mViewDialog.findViewById(R.id.txt_message) ;
        ImageButton imageButtonSure=(ImageButton)mViewDialog.findViewById(R.id.ibtn_sure);
        ImageButton imageButtonCancel=(ImageButton)mViewDialog.findViewById(R.id.ibtn_cancel);
        textView.setText(message);
        imageButtonSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAlertDialog!=null)
                {
                    mAlertDialog.dismiss();
                }
                if(listener!=null)
                {
                    listener.onClick();
                }
                MyPlayer.playStone(context, MyPlayer.INDEX_STONE_ENTER);
            }
        });
        imageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAlertDialog!=null)
                {
                    mAlertDialog.dismiss();
                }
                MyPlayer.playStone(context, MyPlayer.INDEX_STONE_CANCEL);
            }
        });
        //为dialog显示对话框
        builder.setView(mViewDialog);
        //显示对话框
        mAlertDialog=builder.create();
        mAlertDialog.show();
    }
}
