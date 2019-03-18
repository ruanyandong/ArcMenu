package com.ai.satelliteview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 属性动画实现
 */
public class MainActivity extends AppCompatActivity implements View .OnClickListener{

    private ImageView mainMenu;
    private ImageView itemOne;
    private ImageView itemTwo;
    private ImageView itemThree;
    private ImageView itemFour;
    private ImageView itemFive;

    private boolean mIsMenuOpen = false;//菜单是否打开

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mainMenu = findViewById(R.id.menu);
        itemOne = findViewById(R.id.item_one);
        itemTwo = findViewById(R.id.item_two);
        itemThree = findViewById(R.id.item_three);
        itemFour = findViewById(R.id.item_four);
        itemFive = findViewById(R.id.item_five);

        mainMenu.setOnClickListener(this);
        itemOne.setOnClickListener(this);
        itemTwo.setOnClickListener(this);
        itemThree.setOnClickListener(this);
        itemFour.setOnClickListener(this);
        itemFive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!mIsMenuOpen){
            mIsMenuOpen = true;
            openMenu();
        }else {
            Toast.makeText(this,"你点击了 "+v,Toast.LENGTH_SHORT).show();
            mIsMenuOpen = false;
            closeMenu();
        }
    }

    private void closeMenu() {
        doAnimateClose(itemOne,0,5,300);
        doAnimateClose(itemTwo,1,5,300);
        doAnimateClose(itemThree,2,5,300);
        doAnimateClose(itemFour,3,5,300);
        doAnimateClose(itemFive,4,5,300);
    }

    private void doAnimateClose(ImageView view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE){
            view.setVisibility(View.VISIBLE);
        }

        double degree = Math.PI/(2*(total-1))*index;
        int translationX = -(int)(radius*Math.sin(degree));
        int translationY = -(int)(radius*Math.cos(degree));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view,"translationX",translationX,0),
                ObjectAnimator.ofFloat(view,"translationY",translationY,0),
                ObjectAnimator.ofFloat(view,"scaleX",1f,0f),
                ObjectAnimator.ofFloat(view,"scaleY",1f,0f),
                ObjectAnimator.ofFloat(view,"alpha",1f,0f)
        );
        animatorSet.setDuration(500).start();
    }

    private void openMenu() {
        doAnimateOpen(itemOne,0,5,300);
        doAnimateOpen(itemTwo,1,5,300);
        doAnimateOpen(itemThree,2,5,300);
        doAnimateOpen(itemFour,3,5,300);
        doAnimateOpen(itemFive,4,5,300);
    }

    private void doAnimateOpen(ImageView view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE){
            view.setVisibility(View.VISIBLE);
        }
        // 度数转换为弧度
        double degree = Math.toRadians(90)/(total-1)*index;
        int translationX = -(int)(radius*Math.sin(degree));
        int translationY = -(int)(radius*Math.cos(degree));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view,"translationX",0,translationX),
                ObjectAnimator.ofFloat(view,"translationY",0,translationY),
                ObjectAnimator.ofFloat(view,"scaleX",0f,1f),
                ObjectAnimator.ofFloat(view,"scaleY",0f,1f),
                ObjectAnimator.ofFloat(view,"alpha",0f,1f)
        );
        animatorSet.setDuration(500).start();
    }
}
