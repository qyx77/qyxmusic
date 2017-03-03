package com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.myui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.administrator.qyxmusic.R;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.model.IWordButtonClickListener;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.model.WordButton;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.utils.GetInflatorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/1/25.
 */
public class MyGridView extends GridView {

    public List<WordButton> wordButtonList=new ArrayList<WordButton>();

    private MyGridViewAdapter myGridViewAdapter;

    //点击事件的接口
    IWordButtonClickListener myIWordButtonClickListener;

    private Context context;
    //实现底下排列好的文字框的动画
    Animation scaleAnimation;
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        myGridViewAdapter=new MyGridViewAdapter();
        Collections.shuffle(wordButtonList);
        this.setAdapter(myGridViewAdapter);
    }

    public void updateDate(List<WordButton> wordButtonList)
    {
        this.wordButtonList=wordButtonList;
        Collections.shuffle(wordButtonList);
        this.setAdapter(myGridViewAdapter);
    }
    public class MyGridViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return wordButtonList.size();
        }

        @Override
        public Object getItem(int i) {
            return wordButtonList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final WordButton holder;
            if(view==null)
            {
                //加载动画
                scaleAnimation= AnimationUtils.loadAnimation(context,R.anim.scale1);
                //设置动画的延迟时间
                scaleAnimation.setStartOffset(i*300);
                view= GetInflatorUtils.getView(context,R.layout.self_ui_gridview_item);
                holder=wordButtonList.get(i);
                holder.mIndex=i;
                holder.mViewButton=(Button)view.findViewById(R.id.item_btn);
                holder.mViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myIWordButtonClickListener.onWordButtonClick(holder);
                    }
                });
                view.setTag(holder);
            }
            else
            {
                holder=(WordButton) view.getTag();
            }
            holder.mViewButton.setText(holder.mWordString);
            view.startAnimation(scaleAnimation);
            return view;
        }
    }
    /**
     * 注册监听接口
     * @param iWordButtonClickListener
     */
    public void registOnWordButtonClick(IWordButtonClickListener iWordButtonClickListener)
    {
        myIWordButtonClickListener=iWordButtonClickListener;
    }
}
