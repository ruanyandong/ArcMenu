package com.example.ai.satellitemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
/**
 * 实现方式：补间动画
 */
//========！！！！！注意：卫星菜单的布局本身就是呈现卫星行的，只是把菜单隐藏了,卫星菜单的位置始终没改变！！！！！==========
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    private static final int POS_LEFT_TOP = 0;
    private static final int POS_LEFT_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;

    private Position mPosition = Position.RIGHT_BOTTOM;

    private int mRadius;

    private Status mCurrentStatus = Status.CLOSE;

    private OnMenuItemListener mItemListener;
    /**
     * 菜单主按钮
     */
    private View mCButtom;

    public enum Status {
        OPEN, CLOSE
    }

    /**
     * 菜单的位置枚举类
     */
    public enum Position {

        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM

    }

    /**
     * 点击子菜单项的回调接口
     */
    public interface OnMenuItemListener {

        void onClick(View view, int pos);

    }

    public void setItemListener(OnMenuItemListener mItemListener) {
        this.mItemListener = mItemListener;
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 这个方法的作用是 把Android系统中的非标准度量尺寸转变为标准度量尺寸 (Android系统中的标准尺寸是px, 即像素)

         Android系统中的尺寸单位有:

         标准单位: px (px是安卓系统内部使用的单位, dp是与设备无关的尺寸单位 )

         非标准单位: dp, in, mm, pt, sp

         TypedValue.applyDimension()方法的功能就是把非标准尺寸转换成标准尺寸, 如:

         dp->px:  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());

         in->px: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, 20, context.getResources().getDisplayMetrics());

         mm->px: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 20, context.getResources().getDisplayMetrics());

         pt->px: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, 20, context.getResources().getDisplayMetrics());

         sp->px: TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics());
         */

        /**
         * 默认值是100dp
         */
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                100, getResources().getDisplayMetrics());
        // 获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);
        int pos = a.getInt(R.styleable.ArcMenu_position, POS_RIGHT_BOTTOM);

        switch (pos) {
            case POS_LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case POS_LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        100, getResources().getDisplayMetrics()));
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            // 测量child
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

    }

    /**
     * ========！！！！！注意：卫星菜单的布局本身就是呈现卫星行的，只是把菜单隐藏了,卫星菜单的位置始终没改变！！！！！==========
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCButton();
            int count = getChildCount();
            for (int i = 0; i < count - 1; i++) {
                View child = getChildAt(i + 1);
                child.setVisibility(View.GONE);
                int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
                int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();
                // 如果菜单位置在底部(左下，右下)
                if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
                    ct = getMeasuredHeight() - cHeight - ct;
                }
                // 如果菜单位置在底部(右上，右下)
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }
                child.layout(cl, ct, cl + cWidth, ct + cHeight);

            }
        }
    }

    /**
     * 定位主菜单按钮
     */
    private void layoutCButton() {
        mCButtom = getChildAt(0);
        mCButtom.setOnClickListener(this);
        /**
         * 定位一个button，需要知道 上下左右的位置
         */
        int l = 0;
        int t = 0;
        int width = mCButtom.getMeasuredWidth();
        int height = mCButtom.getMeasuredHeight();
        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        mCButtom.layout(l, t, l + width, t + height);
    }


    @Override
    public void onClick(View v) {
        /**
         * 主菜单按钮旋转
         */
        rotateCButton(v, 0f, 360f, 300);

        toggleMenu(300);
    }

    /**
     * 切换菜单
     */
    public void toggleMenu(int duration) {
        // 为menuItem添加平移动画和旋转动画
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
            AnimationSet animationSet = new AnimationSet(true);
            Animation tranAnim = null;

            /**
             * 补间动画View本身的位置是不会变的
             */
            // 当前是关闭状态
            if (mCurrentStatus == Status.CLOSE) {
                if (mPosition == Position.LEFT_TOP) {
                    tranAnim = new TranslateAnimation(-cl, 0, -ct, 0);
                } else if (mPosition == Position.RIGHT_TOP) {
                    tranAnim = new TranslateAnimation(cl, 0, -ct, 0);
                } else if (mPosition == Position.LEFT_BOTTOM) {
                    tranAnim = new TranslateAnimation(-cl, 0, ct, 0);
                } else if (mPosition == Position.RIGHT_BOTTOM) {
                    tranAnim = new TranslateAnimation(cl, 0, ct, 0);
                }

                childView.setClickable(true);
                childView.setFocusable(true);
            } else {
                if (mPosition == Position.LEFT_TOP) {
                    tranAnim = new TranslateAnimation(cl, 0, ct, 0);
                } else if (mPosition == Position.RIGHT_TOP) {
                    tranAnim = new TranslateAnimation(0, cl, 0, -ct);
                } else if (mPosition == Position.LEFT_BOTTOM) {
                    tranAnim = new TranslateAnimation(0, -cl, 0, ct);
                } else if (mPosition == Position.RIGHT_BOTTOM) {
                    tranAnim = new TranslateAnimation(0, cl, 0, ct);
                }

                childView.setClickable(false);
                childView.setFocusable(false);
            }

            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            tranAnim.setStartOffset((i * 100) / count);
            tranAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE) {
                        childView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            // 旋转动画
            RotateAnimation rotateAnimation = new RotateAnimation(0, 720,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(duration);
            rotateAnimation.setFillAfter(true);

            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(tranAnim);

            childView.startAnimation(animationSet);

            final int pos = i + 1;
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mItemListener != null) {
                        mItemListener.onClick(v, pos);
                    }

                    menuItemAnim(pos - 1);
                    changeStatus();
                }
            });

        }
        // 切换菜单状态
        changeStatus();

    }

    /**
     * 添加Item的点击动画
     *
     * @param pos
     */
    private void menuItemAnim(int pos) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);

            if (i == pos) {
                childView.startAnimation(scaleBigAnim(300));
            } else {
                childView.startAnimation(scaleSmallAnim(300));
            }

            childView.setClickable(false);
            childView.setFocusable(false);
        }
    }

    /**
     * @param duration
     * @return
     */
    private Animation scaleSmallAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    /**
     * 为当前点击的Item设置变大和透明度降低的动画
     *
     * @param duration
     * @return
     */
    private Animation scaleBigAnim(int duration) {

        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);
        return animationSet;
    }

    private void changeStatus() {
        mCurrentStatus = mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE;
    }

    public boolean isOpen() {
        return mCurrentStatus == Status.OPEN;
    }

    private void rotateCButton(View view, float start, float end, int duration) {

        RotateAnimation anim = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setDuration(duration);
        // 转完后应用
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

}
