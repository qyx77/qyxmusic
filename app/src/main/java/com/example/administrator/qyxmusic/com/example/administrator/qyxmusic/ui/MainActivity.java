package com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.qyxmusic.R;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.data.Const;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.model.IDialogButtonClickListener;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.model.IWordButtonClickListener;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.model.Song;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.model.WordButton;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.myui.MyGridView;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.utils.GetInflatorUtils;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.utils.MyPlayer;
import com.example.administrator.qyxmusic.com.example.administrator.qyxmusic.utils.ShowDialog;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements IWordButtonClickListener{


    /**删除弹出框*/
    public static final int ID_DIALOG_DELETE_WORD=1;
    /**提示弹出框取消按钮*/
    public static final int ID_DIALOG_Light_WORD=2;
    /**金币弹出框按钮*/
    public static final int ID_DIALOG_COINS_WORD=3;
    /**答案状态 -- 正确*/
    public static final int STATUS_ANSWER_RIGHT=1;
    /**答案状态 -- 错误*/
    public static final int STATUS_ANSWER_WRONG=2;
    /**答案状态 -- 不完整*/
    public static final int STATUS_ANSWER_LACK=3;
    private Animation mPanAnimation;
    private LinearInterpolator mPanLin;

    private Animation mBarInAnimation;
    private LinearInterpolator mBarInLin;

    private Animation mBarOutAnimation;
    private LinearInterpolator mBarOutLin;
    //转盘、拨杆
    ImageView iv_game_disc,iv_game_pin;
    //播放音乐按钮,删除按钮,提示按钮,下一首按钮
    ImageButton bt_startPlay,deleteCoins,lightCoins,bt_next;
    //初始化下面字的个数
    public final static int TEXT_SIZE=24;
    //所有字符数据,选择的数据
    public List<WordButton> allWordsButton,myButtonSelect;
    //自己定义的GridView
    MyGridView myGridView;
    //最下方词的线性布局
    LinearLayout bottomLinearLayout;
    //当前关卡
    int currentStateIndex=-1;
    //当前歌曲
    Song currentSong;
    //闪烁次数
    public final static int staticSparkTimes=6;
    //过关页面
    LinearLayout passLinearLayout;
    //当前金币数量
    public int currentCoins=Const.currentCoinCount;
    //显示金币的控件,当前关数，当前歌曲
    TextView mViewCurrentCoins,mPassViewCurrentStage,mPassViewCurrentSongName,mViewCurrentStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bt_startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_game_pin.startAnimation(mBarInAnimation);
                MyPlayer.playSong(MainActivity.this,currentSong.getSongFileName());
            }
        });
        initCurrentStageDate();
    }
    public void init()
    {
        //显示当前关数
        mViewCurrentStage=(TextView) findViewById(R.id.txt_title_num);
        //下一关按钮
        bt_next=(ImageButton) findViewById(R.id.ima_next);
        //当前关卡的TextView
        mPassViewCurrentStage=(TextView) findViewById(R.id.txt_main_currentStage);
        //当前歌曲的TextView
        mPassViewCurrentSongName=(TextView) findViewById(R.id.txt_SongName);
        //初始化最下方词的线性布局
        bottomLinearLayout=(LinearLayout) findViewById(R.id.bottom);
        //初始化自己定义的GridView
        myGridView=(MyGridView) findViewById(R.id.myGridView);
        //注册监听器
        myGridView.registOnWordButtonClick(this);
        //开始音乐按钮
        bt_startPlay=(ImageButton) findViewById(R.id.bt_startPlay);
        //转盘
        iv_game_disc=(ImageView) findViewById(R.id.iv_game_disc);
        //拨杆
        iv_game_pin=(ImageView)findViewById(R.id.iv_game_pin);
        //当前金币数量
        mViewCurrentCoins=(TextView)findViewById(R.id.show_currentCoins);
        mViewCurrentCoins.setText(String.valueOf(currentCoins));
        //删除按钮
        deleteCoins=(ImageButton)findViewById(R.id.deleteCoins);
        deleteCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(ID_DIALOG_DELETE_WORD);
               // MyPlayer.playStone(MainActivity.this,MyPlayer.INDEX_STONE_COIN);
            }
        });
        //提示按钮
        lightCoins=(ImageButton) findViewById(R.id.bt_lightCoins);
        lightCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(ID_DIALOG_Light_WORD);

            }
        });
        //转盘动画
        mPanAnimation= AnimationUtils.loadAnimation(this,R.anim.retort1);
        mPanLin=new LinearInterpolator();
        mPanAnimation.setInterpolator(mPanLin);
        mPanAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                 bt_startPlay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                 bt_startPlay.setVisibility(View.VISIBLE);
                 iv_game_pin.setAnimation(mBarOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //拨杆进入动画
        mBarInAnimation= AnimationUtils.loadAnimation(this,R.anim.retatein);
        //true表示保持播完的动画
        mBarInAnimation.setFillAfter(true);
        mBarInLin=new LinearInterpolator();
        mBarInAnimation.setInterpolator(mBarInLin);
        mBarInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                  iv_game_disc.startAnimation(mPanAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //拨杆出来动画
        mBarOutAnimation= AnimationUtils.loadAnimation(this,R.anim.rotateout);
        mBarOutAnimation.setFillAfter(true);
        mBarOutLin=new LinearInterpolator();
        mBarOutAnimation.setInterpolator(mBarOutLin);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyPlayer.stopSong();
        iv_game_pin.clearAnimation();
        iv_game_disc.clearAnimation();

    }
    //初始化当前关卡
    private void initCurrentStageDate()
    {
        //初始化歌曲信息
        currentSong=LoadStateSongInfo(++currentStateIndex);
        //设置标题的当前关卡数
        mViewCurrentStage.setText((currentStateIndex+1)+"");
        //初始化所有选中数据
        myButtonSelect=initWordSelect();

        //初始化数据
        allWordsButton=initAllWord();
        //更新数据
        myGridView.updateDate(allWordsButton);
        //开始自动播放音乐和开始动画
        iv_game_pin.startAnimation(mBarInAnimation);
        MyPlayer.playSong(MainActivity.this,currentSong.getSongFileName());
    }
    //初始化所有待选文字框的数据
    private List<WordButton> initAllWord()
    {
        List<WordButton> wordButtonList=new ArrayList<WordButton>();
        //获得所有待选文字
        String[] words=generateWords();
        for (int i=0;i<TEXT_SIZE;i++)
        {
            WordButton wordButton=new WordButton();
            wordButton.mWordString=words[i];
            wordButton.mIndex=i;
            wordButtonList.add(wordButton);
        }
        return wordButtonList;
    }
    public Song LoadStateSongInfo( int myCurrentStage)
    {
        Song song=new Song();

        String[] stage=Const.SONG_INFO[myCurrentStage];
        song.setSongName(stage[Const.SONG_NAME]);
        song.setSongFileName(stage[Const.SONG_FILE_NAME]);

        return song;
    }
    //初始化所有选中文字框
    private List<WordButton> initWordSelect()
    {
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.word_select_container);
        linearLayout.removeAllViews();
        List<WordButton> wordButtonList=new ArrayList<WordButton>();
        for(int i=0;i<currentSong.getSongLength();i++)
        {
            final WordButton holder=new WordButton();

            View view= GetInflatorUtils.getView(MainActivity.this,R.layout.self_ui_gridview_item);

            holder.mViewButton=(Button)view.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mViewButton.setLayoutParams(new ViewGroup.LayoutParams(80,80));
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearSelectWord(holder);
                }
            });
            wordButtonList.add(holder);

            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(100,100);
            linearLayout.addView(view,layoutParams);
        }
        return wordButtonList;
    }

    /**
     * 当待选文字框点击时触发的事件
     * @param wordButton
     */
    @Override
    public void onWordButtonClick(WordButton wordButton) {
        for(int i=0;i<currentSong.getSongLength();i++)
        {
            if(myButtonSelect.get(i).mWordString.length()==0)
            {
                myButtonSelect.get(i).mWordString=wordButton.mWordString;
                myButtonSelect.get(i).mViewButton.setText(wordButton.mWordString);
                myButtonSelect.get(i).mIndex=wordButton.mIndex;
                myButtonSelect.get(i).mIsVisiable=true;
                myButtonSelect.get(i).mViewButton.setTextColor(Color.WHITE);
                setButtonVisibility(wordButton,View.INVISIBLE);
                break;
            }
        }
        int checkResult=checkAnswer();
        if(checkResult==STATUS_ANSWER_RIGHT)
        {
            handlePassEvent();
        }
        else if(checkResult==STATUS_ANSWER_WRONG)
        {
            sparkTheWords();
        }
        else if(checkResult==STATUS_ANSWER_LACK)
        {
            for (int i=0;i<myButtonSelect.size();i++)
            {
                myButtonSelect.get(i).mViewButton.setTextColor(Color.WHITE);
            }
        }
    }

    /**
     * 清除选择的文字框
     * @param wordButton
     */
    public void clearSelectWord(WordButton wordButton)
    {
        wordButton.mViewButton.setText("");
        wordButton.mWordString="";
        allWordsButton.get(wordButton.mIndex).mViewButton.setVisibility(View.VISIBLE);
        allWordsButton.get(wordButton.mIndex).mIsVisiable=true;
        for (int i=0;i<myButtonSelect.size();i++)
        {
            myButtonSelect.get(i).mViewButton.setTextColor(Color.WHITE);
        }
    }

    /**
     * 设置待选文字框的可见性
     * @param wordButton
     * @param visibility
     */
    private void setButtonVisibility(WordButton wordButton,int visibility)
    {

        wordButton.mViewButton.setVisibility(visibility);
        wordButton.mIsVisiable=(visibility==View.VISIBLE)?true:false;
    }

    /**
     * 生成随机汉字
     * @return
     */
    public String getRandomChar()
    {
        String str="";
        int hignPos;
        int lowPos;

        Random random=new Random();

        hignPos=(176)+Math.abs(random.nextInt(39));
        lowPos=(161)+Math.abs(random.nextInt(93));

        byte[] bytes=new byte[2];
        bytes[0]=(Integer.valueOf(hignPos)).byteValue();
        bytes[1]=(Integer.valueOf(lowPos)).byteValue();

        try {
            str=new String(bytes,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
       // return  str.charAt(0);

    }
    /**
     * 生成所有待选文字的文字内容
     */
    public String[] generateWords()
    {
        String[] words=new String[TEXT_SIZE];
        Random random=new Random();
        for (int i=0;i<currentSong.getSongLength();i++)
        {
            words[i]=currentSong.getSongNameCharacters()[i]+"";
        }
        for(int i=currentSong.getSongLength();i<TEXT_SIZE;i++)
        {
            words[i]=getRandomChar();
        }
        //打乱次序
       /* for(int i=TEXT_SIZE-1;i>=0;i--)
        {
            int index=random.nextInt(i+1);
            String temp=words[index];
            words[index]=words[i];
            words[i]=temp;
        }*/
        return words;
    }

    /**
     * 检查答案是否正确
     * @return
     */
    public int checkAnswer(){
        //先检查网络
        for (int i=0;i<myButtonSelect.size();i++)
        {
            if(myButtonSelect.get(i).mWordString.length()==0)
            { return STATUS_ANSWER_LACK;}
        }
        StringBuffer stringBuffer=new StringBuffer();
        for (int i=0;i<myButtonSelect.size();i++)
        {
            stringBuffer.append(myButtonSelect.get(i).mWordString);
        }
        return stringBuffer.toString().equals(currentSong.getSongName())?
                STATUS_ANSWER_RIGHT:STATUS_ANSWER_WRONG;
    }

    /**
     * 文字闪烁
     */
    public  void sparkTheWords()
    {
        TimerTask timerTask=new TimerTask() {
            boolean myChange;
            int sparkTimes=0;
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if((++sparkTimes)>staticSparkTimes)
                        {
                            return;
                        }
                        //执行交替闪烁逻辑
                        for (int i=0;i<myButtonSelect.size();i++)
                        {
                            myButtonSelect.get(i).mViewButton.setTextColor(
                                    myChange?Color.RED:Color.WHITE);
                        }
                        myChange=!myChange;
                    }
                });
            }
        };
        Timer timer=new Timer();
        timer.schedule(timerTask,1,70);
    }
    public void handlePassEvent()
    {
        if(isAllPassed())
        {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,AllPass.class);
            MainActivity.this.startActivity(intent);
        }
        else
        {
            passLinearLayout=(LinearLayout) findViewById(R.id.ll_pass);
            passLinearLayout.setVisibility(View.VISIBLE);
            //设置金币声音
            MyPlayer.playStone(MainActivity.this,MyPlayer.INDEX_STONE_COIN);
            //停止动画播放
            iv_game_disc.clearAnimation();
            iv_game_pin.clearAnimation();
            //停止播放当前音乐
            MyPlayer.stopSong();
            //设置当前关卡文字
            if(mPassViewCurrentStage!=null)
            {
                mPassViewCurrentStage.setText((currentStateIndex+1)+"");
            }
            //设置当前歌曲名称
            if(mPassViewCurrentSongName!=null)
            {
                mPassViewCurrentSongName.setText(currentSong.getSongName());
            }
            if(bt_next!=null)
            {
                bt_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passLinearLayout.setVisibility(View.GONE);
                        initCurrentStageDate();
                    }
                });
            }
        }

    }
    private boolean isAllPassed()
    {
        return currentStateIndex==Const.SONG_INFO.length-1;
    }
    /**
     * 提示正确答案
     */
    private void hanleLightAnswer()
    {
            WordButton wordButton=findWriteAnswer();
            if(wordButton==null)
            {
                sparkTheWords();
            }
            else
            {
                onWordButtonClick(wordButton);
                if(!(handleCoins(-80)))
                {
                    //.......提示对话框
                    showAlertDialog(ID_DIALOG_COINS_WORD);
                }
            }
    }

    /**
     * 找到正确的答案的WordButton
     * @return
     */
    private WordButton findWriteAnswer()
    {
        int index=findWriteAnswerIndex();
        WordButton wordButton=new WordButton();
        if(index==-1)
        {
            return null;
        }
        else
        {
            for(int i=0;i<TEXT_SIZE;i++)
            {
                if(allWordsButton.get(i).mWordString.equals(currentSong.getSongNameCharacters()[index]+""))
                {
                    wordButton=allWordsButton.get(i);
                    return wordButton;
                   // break;
                }

            }
        }
        return null;
    }

    /**
     * 寻找正确答案在选择框的位置
     * @return
     */
    private int findWriteAnswerIndex()
    {
        for (int i=0;i<currentSong.getSongLength();i++)
        {
            if(myButtonSelect.get(i).mWordString.length()==0)
            {
                return i;
            }
        }
        return -1;
    }
    /**
     * 处理删除错误答案按钮
     */
    private void handleDeleteWord()
    {
        if(!(handleCoins(-30)))
        {
            //.......提示对话框
            showAlertDialog(ID_DIALOG_COINS_WORD);
        }
        else
        {
            WordButton wordButton=findNoAnswerWord();
            setButtonVisibility(wordButton,View.GONE);
        }
    }

    /**
     * 寻找一个不是答案的WordButton的按钮
     * @return
     */
    private WordButton findNoAnswerWord()
    {
        Random random=new Random();
        WordButton wordButton;
        while (true)
        {
            int index=random.nextInt(TEXT_SIZE);
            wordButton=allWordsButton.get(index);
            if(wordButton.mIsVisiable&&!(isTheRightAnswer(wordButton)))
            {
                return wordButton;
            }
        }
    }
    private boolean isTheRightAnswer(WordButton wordButton)
    {
        for(int i=0;i<currentSong.getSongLength();i++)
        {
            if(wordButton.mWordString.equals(currentSong.getSongNameCharacters()[i]+""))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理金币
     * @param coins
     * @return
     */
    private boolean handleCoins(int coins)
    {
        if(currentCoins+coins>=0)
        {
            currentCoins+=coins;
            mViewCurrentCoins.setText(String.valueOf(currentCoins));
            return true;
        }
        else
        {
            return false;
        }
    }
    //删除对话框
    private IDialogButtonClickListener mBtnOkDeleteCoinsListener =
            new IDialogButtonClickListener() {
                @Override
                public void onClick() {
                    handleDeleteWord();
                }
            };
    //提示对话框
    private IDialogButtonClickListener mBtnOKLightCoinsListener =
            new IDialogButtonClickListener() {
                @Override
                public void onClick() {
                    hanleLightAnswer();
                }
            };
    //金币不足对话框
    private IDialogButtonClickListener mBtnOKLackCoinsListener =
            new IDialogButtonClickListener() {
                @Override
                public void onClick() {

                }
            };
    private void showAlertDialog(int id)
    {
        switch (id)
        {
            case ID_DIALOG_DELETE_WORD:
                ShowDialog.showDialog(MainActivity.this,
                        "确定花30金币去掉一个错误答案吗？",mBtnOkDeleteCoinsListener);
                break;
            case ID_DIALOG_Light_WORD:
                ShowDialog.showDialog(MainActivity.this,
                        "确定花80金币去提示一个正确答案吗？",mBtnOKLightCoinsListener);
                break;
            case ID_DIALOG_COINS_WORD:
                ShowDialog.showDialog(MainActivity.this,
                        "金币不足，去商店补充？",mBtnOKLackCoinsListener);
                break;
        }

    }
}
