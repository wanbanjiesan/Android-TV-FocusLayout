package com.wanban.focuslayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Scroller;
import java.util.ArrayList;

/**
 * 显示首页的推荐布局
 *
 * @author wanbanjiesan
 */
public class HLayoutContainer extends ViewGroup {

    // item之间的间距
    private int padding;
    private int screenWidth;
    private OnItemClickedListener onItemClickedListener;
    /**
     * 设置滚动的时间
     */
    private final int SCROLL_DURATION = 450;
    // 滑动辅助类
    private Scroller mScroller;
    // 右边界阈值
    private int rightOffset;
    // child的位置
    private int position = 0;
    // child的布局rect，measure和layout都依赖rect来设置
    private ArrayList<Rect> childLocationInfo;
    // 计算得到的view的最大宽度
    private int layoutWidth;

    private int offset;


    public HLayoutContainer(Context context) {
        this(context, null);
    }

    public HLayoutContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取屏幕宽度
        DisplayMetrics metric = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metric);
        screenWidth = metric.widthPixels; // 屏幕宽度（像素）
        padding = 36;
        rightOffset = 60;
        offset = 10;
        mScroller = new Scroller(context);
        // 由于动画会导致view出现覆盖，所以我们要打开order开关来主动改变view的绘制顺序
        setChildrenDrawingOrderEnabled(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount <= 0) {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        // 计算item的宽高等
        int layoutHeight = 762 + getPaddingTop() + getPaddingBottom();
        int layoutWidth = getPaddingLeft();
        int childWidth;
        int childHeight;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 固定位置的配置
            Rect rect = childLocationInfo.get(i);
            childWidth = MeasureSpec.makeMeasureSpec(rect.right - rect.left, MeasureSpec.EXACTLY);
            childHeight = MeasureSpec.makeMeasureSpec(rect.bottom - rect.top, MeasureSpec.EXACTLY);
            child.measure(childWidth, childHeight);
            // 计算layout的总的宽度，最后一个item的right
            if (i == childCount - 1) {
                layoutWidth = rect.right + getPaddingRight() + rightOffset;
            }
        }
        this.layoutWidth = layoutWidth;
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.EXACTLY));
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!changed) {
            return;
        }
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }
        // 根据rect layout child
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 固定位置的配置
            Rect rect = childLocationInfo.get(i);

            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }


    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i == childCount - 1) {// 这是最后一个需要刷新的item
            return position;
        }
        if (i == position) {// 这是原本要在最后一个刷新的item
            return childCount - 1;
        }
        return i;// 正常次序的item
    }

    // 跟AnimContainer配合，改变当前的item绘制顺序
    public void setCurrentPosition(int pos) {
        this.position = pos;
    }


    // 设置child的布局 本例只是demo，安各位的需求类似的设置即可
    private void layoutChild(int count) {
        childLocationInfo = new ArrayList<>();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        // 第一张大图的宽
        int img0W =  801;
        // 第一行img的高度
        int imgTopH =  450;
        // 第一张大图的高度
        int img1W =  270;
        // 第二行img的高度
        int imgBottomH =  276;
        int img2W =  492;
        int img3W =  315;
        // 第一行img的top
        int top0 = paddingTop;
        // 第二行img的top
        int top1 = top0 + imgTopH + padding;
        int bottom0 = top0 + imgTopH;
        int bottom1 = top1 + imgBottomH;

        // 第1个child
        Rect child0 = new Rect();
        child0.left = paddingLeft;
        child0.right = child0.left + img0W;
        child0.top = top0;
        child0.bottom = bottom0;
        childLocationInfo.add(child0);
        // 第2个child
        Rect child1 = new Rect();
        child1.left = paddingLeft;
        child1.right = child1.left + img1W;
        child1.top = top1;
        child1.bottom = bottom1;
        childLocationInfo.add(child1);

        Rect child2 = new Rect();
        child2.left = child1.right + padding;
        child2.right = child2.left + img2W;
        child2.top = top1;
        child2.bottom = bottom1;
        childLocationInfo.add(child2);

        Rect child3 = new Rect();
        child3.left = child0.right + padding;
        child3.right = child3.left + img3W;
        child3.top = top0;
        child3.bottom = bottom0;
        childLocationInfo.add(child3);


        Rect child4 = new Rect();
        child4.left = child0.right + padding;
        child4.right = child4.left + img2W;
        child4.top = top1;
        child4.bottom = bottom1;
        childLocationInfo.add(child4);


        Rect child5 = new Rect();
        child5.left = child3.right + padding;
        child5.right = child5.left + img3W;
        child5.top = top0;
        child5.bottom = bottom0;
        childLocationInfo.add(child5);

        Rect child7 = new Rect();
        child7.left = child5.right + padding;
        child7.right = child7.left + img3W;
        child7.top = top0;
        child7.bottom = bottom0;
        childLocationInfo.add(child7);

        Rect child6 = new Rect();
        child6.left = child4.right + padding;
        child6.right = child6.left + img2W;
        child6.top = top1;
        child6.bottom = bottom1;
        childLocationInfo.add(child6);

        // 固定位置child的数量
        int localChildCount = 8;
        int childRecycleCount = count - localChildCount;
        if (childRecycleCount > 0) {
            // 固定大小的child
            int recycleLeft = childLocationInfo.get(childLocationInfo.size() - 1).right + padding;
            int recycleWidth =  255;
            int recycleHeight =  363;
            for (int i = 0; i < childRecycleCount; i++) {
                Rect child = new Rect();
                child.left = recycleLeft;
                child.right = child.left + recycleWidth;
                child.top = (i % 2 == 0 ? 0 : 1) * (recycleHeight + padding) + getPaddingTop();
                child.bottom = child.top + recycleHeight;
                recycleLeft += (i % 2 == 0 ? 0 : 1) * (recycleWidth + padding);
                childLocationInfo.add(child);
            }
        }
    }

    // 判断当前item是否在第一行
    public boolean isAtFirstRow(Rect rect) {
        boolean temp = false;
        if (rect != null && rect.top <= getPaddingTop() + padding) {
                temp = true;
        }
        return temp;
    }



    private boolean isFirstColumn(Rect rect) {
        boolean temp = false;
        if (rect != null && rect.left == getPaddingLeft()) {
            temp = true;
        }
        return temp;
    }

    // 恢复到初始状态
    public void reSetLayout() {
        if (mScroller != null) {
            mScroller.setFinalX(0);
        }
    }

    // 将view移动到最末尾
    public void scrollToLast() {
        int scrollX;
        if (layoutWidth < screenWidth) {
            scrollX = getWidth() - layoutWidth;
        } else {
            scrollX = getWidth() - screenWidth;
        }
        mScroller.setFinalX(scrollX);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final View view = getFocusedChild();
        if (view == null) {
            return false;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
            // focusSearch是个神奇的方法，建议做tv的都去查查
            View child = focusSearch(getFocusedChild(), FOCUS_RIGHT);
            if (child != null) {
                // id 即position
                int id = child.getId();
                if (id < childLocationInfo.size()) {
                    Rect rect = childLocationInfo.get(id);
                    // 当前屏幕显示右边缘在window的x距离  比如不滑动默认是1080 但是由于按右键，进行了滑动后，边缘的x=1080+item.width
                    int screenScroll = screenWidth + mScroller.getCurrX();
                    // 判断当前的view的右边界是否超出屏幕外，如果在外部需要进行滑动，让其全部显示在屏幕内部
                    if (rect != null && rect.right > screenScroll) {
                        // 需要移动超出屏幕的部分+阴影补偿
                        int moveDistance = rect.right - screenScroll + rightOffset;
                        // 如果到达边界，则进行精确
                        if (screenScroll + moveDistance > getWidth()) {
                            moveDistance = getWidth() - screenScroll;
                        }
                        gridSmoothScrollBy(moveDistance);
                    }
                }
            }

            return false;
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {
            View child = focusSearch(getFocusedChild(), FOCUS_LEFT);
            if (child != null) {
                int id = child.getId();
                if (id < childLocationInfo.size()) {
                    Rect rect = childLocationInfo.get(id);
                    int realScreenBound = screenWidth + mScroller.getCurrX();
                    if (rect != null) {
                        int bound = rect.left - (realScreenBound - screenWidth);
                        // offset补偿值，可能会跟阴影有关
                        if (bound < offset) {
                            // 如果是最后左边的view，直接滑动到最左边
                            if (isFirstColumn(rect)) {
                                gridSmoothScrollBy(-mScroller.getCurrX());
                            } else {
                                int moveDistance = realScreenBound - screenWidth - rect.left + rightOffset;
                                gridSmoothScrollBy(-moveDistance);
                            }
                        }
                    }
                }
            }

            return false;
        }
        return super.dispatchKeyEvent(event);
    }



    // 给child添加paddingLeft和PaddingTop
    private void setChildRectPadding(){
        for(Rect rect : childLocationInfo){
            rect.left += getPaddingLeft();
            rect.right += getPaddingLeft();
            rect.top += getPaddingTop();
            rect.bottom += getPaddingTop();
        }
    }

    public void setAdapter(BaseAdapter adapter, ArrayList<Rect> childLocationInfo) {
        this.childLocationInfo = childLocationInfo;
        setChildRectPadding();
        removeAllViews();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            AnimContainer view = (AnimContainer) adapter.getView(i, null, this);
            if (view != null) {
                final int index = i;
                // 将position设置到id，方便以后查询用，如果有用到setId的请注意
                view.setId(i);
                // 看各位喜欢事件是否放到view还是adapter
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (onItemClickedListener != null) {
                            onItemClickedListener.onItemClicked(v, index);
                        }
                    }
                });
                // 防止最左边tab按左键移动时，焦点跑出layout
                if (isFirstColumn(childLocationInfo.get(i))) {
                    view.setNextFocusLeftId(view.getId());
                }
                this.addView(view);
            }
        }

    }

    /**
     * 调用此方法设置滚动的相对偏移
     */
    private void gridSmoothScrollBy(int dx) {
        // 设置mScroller的滚动偏移量
        if (mScroller.computeScrollOffset()) {
            mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), dx, 0, SCROLL_DURATION);
        } else {
            mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, 0, SCROLL_DURATION);
        }
        invalidate();// 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }


    @Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 必须调用该方法，否则不一定能看到滚动效果
            invalidate();
        }
        super.computeScroll();

    }


    /**
     * 个人喜欢在view上添加onclick事件，看个人喜欢，如果喜欢的在adapter也可以
     */
    public interface OnItemClickedListener {
        void onItemClicked(View view, int position);
    }

    public OnItemClickedListener getOnItemClickedListener() {
        return onItemClickedListener;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    private int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, res.getDisplayMetrics());
    }

}
