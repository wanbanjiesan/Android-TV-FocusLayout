package com.wanban.demo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wanban.focuslayout.HLayoutContainer;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private HLayoutContainer hLayoutContainer;
    private ArrayList<Rect> childLocationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hLayoutContainer = (HLayoutContainer) findViewById(R.id.view_hlayout);

        // demo 布局是前8个item是不规则大小，后面的就是规则的大小
        int childCount = 20;
        MyListAdapter focusAdapter = new MyListAdapter(this, childCount);
        layoutChild(childCount);
        hLayoutContainer.setAdapter(focusAdapter, childLocationInfo);
        hLayoutContainer.post(new Runnable() {
            @Override
            public void run() {
                hLayoutContainer.getChildAt(0).requestFocus();
            }
        });
    }

    // 设置child的布局 本例只是demo，安各位的需求类似的设置即可
    // 设置child的布局 本例只是demo，安各位的需求类似的设置即可
    private void layoutChild(int count) {
        childLocationInfo = new ArrayList<>();
        int itemRowAndColumnSpace = 36;
        int paddingLeft = 0;
        int paddingTop = 0;
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
        int top1 = top0 + imgTopH + itemRowAndColumnSpace;
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
        child2.left = child1.right + itemRowAndColumnSpace;
        child2.right = child2.left + img2W;
        child2.top = top1;
        child2.bottom = bottom1;
        childLocationInfo.add(child2);

        Rect child3 = new Rect();
        child3.left = child0.right + itemRowAndColumnSpace;
        child3.right = child3.left + img3W;
        child3.top = top0;
        child3.bottom = bottom0;
        childLocationInfo.add(child3);


        Rect child4 = new Rect();
        child4.left = child0.right + itemRowAndColumnSpace;
        child4.right = child4.left + img2W;
        child4.top = top1;
        child4.bottom = bottom1;
        childLocationInfo.add(child4);


        Rect child5 = new Rect();
        child5.left = child3.right + itemRowAndColumnSpace;
        child5.right = child5.left + img3W;
        child5.top = top0;
        child5.bottom = bottom0;
        childLocationInfo.add(child5);

        Rect child7 = new Rect();
        child7.left = child5.right + itemRowAndColumnSpace;
        child7.right = child7.left + img3W;
        child7.top = top0;
        child7.bottom = bottom0;
        childLocationInfo.add(child7);

        Rect child6 = new Rect();
        child6.left = child4.right + itemRowAndColumnSpace;
        child6.right = child6.left + img2W;
        child6.top = top1;
        child6.bottom = bottom1;
        childLocationInfo.add(child6);

        // 固定位置child的数量
        int localChildCount = 8;
        int childRecycleCount = count - localChildCount;
        if (childRecycleCount > 0) {
            // 固定大小的child
            int recycleLeft = childLocationInfo.get(childLocationInfo.size() - 1).right + itemRowAndColumnSpace;
            int recycleWidth =  255;
            int recycleHeight =  363;
            for (int i = 0; i < childRecycleCount; i++) {
                Rect child = new Rect();
                child.left = recycleLeft;
                child.right = child.left + recycleWidth;
                child.top = (i % 2 == 0 ? 0 : 1) * (recycleHeight + itemRowAndColumnSpace);
                child.bottom = child.top + recycleHeight;
                recycleLeft += (i % 2 == 0 ? 0 : 1) * (recycleWidth + itemRowAndColumnSpace);
                childLocationInfo.add(child);
            }
        }
    }
}
