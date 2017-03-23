package com.wanban.focuslayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;


public class AnimContainer extends RelativeLayout {

    // 焦点框的rect，用来canvas使用
    private Rect mBound;
    // 焦点框的图片
    private Drawable mDrawable;
    // 当前item的rect，包含宽高信息
    private Rect mRect;
    private int padding;
    // 默认缩小即恢复原大小
    private final float VALUE_ANIMATE_SMALL = 1.0f;
    // 自己定义放大的比例
    private final float VALUE_ANIMATE_BIG = 1.12f;


    public AnimContainer(Context context) {
        super(context);
        init();
    }

    public AnimContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AnimContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        // init focus image bound size
        padding = dpToPx(getResources(), 49);
        // because you will user onDraw(), if not, the method of onDraw is no effect
        setWillNotDraw(false);
        // do not init in onDraw()
        mRect = new Rect();
        mBound = new Rect();
        // init the focusable image
        mDrawable = getResources().getDrawable(R.drawable.bg_shadow_focus);// nav_focused_2,poster_shadow_4
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // 获取当前view的rect
        super.getDrawingRect(mRect);
        mBound.set(-padding + mRect.left, -padding + mRect.top, padding + mRect.right, padding + mRect.bottom);
        mDrawable.setBounds(mBound);
        canvas.save();
        mDrawable.draw(canvas);
        canvas.restore();
        super.onDraw(canvas);
    }


    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            // 自定义你的ViewGroup，需要去重写getChildDrawingOrder方法，去修改child显示次序，以防止放大缩小覆盖问题
            if (getParent() instanceof HLayoutContainer) {
                HLayoutContainer parent = (HLayoutContainer) getParent();
                if (parent != null) {
                    // 传递当前的item的position，我是将position设置到了id中
                    parent.setCurrentPosition(getId());
                    parent.requestLayout();
                    parent.invalidate();
                }
            }
            animateBig();
        } else {
            animateSmall();
        }
    }

    public void animateSmall() {
        this.animate().scaleX(VALUE_ANIMATE_SMALL).scaleY(VALUE_ANIMATE_SMALL).start();
    }

    public void animateBig() {
        this.animate().scaleX(VALUE_ANIMATE_BIG).scaleY(VALUE_ANIMATE_BIG).start();
    }


    private int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }
}