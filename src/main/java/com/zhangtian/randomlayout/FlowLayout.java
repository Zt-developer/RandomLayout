package com.zhangtian.randomlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

/**
 * Created by Fast on 2017/5/9.
 */

public class FlowLayout extends ViewGroup {

    private int horizontalSpace = 15;
    private int verticalSpace = 15;
    private ArrayList<Line> lineList = new ArrayList<>();
    private int noPaddingWidth;
    private Rect[] rects;

    public void setHorizontalSpace(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }
    public void setverticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }



    public FlowLayout(Context context) {
        this(context,null);

    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        noPaddingWidth = width - getPaddingLeft() - getPaddingRight();
        Line line = new Line();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(0,0);

            if(lineList.size() == 0) {
                line.addView(child);
            }else if(line.width + child.getMeasuredWidth() + horizontalSpace  > noPaddingWidth){
                //大于 之后就换一行 找个集合存这个line对象
                lineList.add(line);
                line = new Line();
                line.addView(child);
            }else {
                line.addView(child);
            }
        }
        //最后一行 丢失 手动添加
        lineList.add(line);
        //测量高
        int height = getPaddingBottom() + getPaddingTop();
        for (int i = 0; i < lineList.size(); i++) {
            height += lineList.get(i).height;
        }
        height += (lineList.size() - 1) * verticalSpace;
        setMeasuredDimension(width,height);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        getAllRect();

        //遍历linelist,取出line
        for (int i = 0; i < lineList.size(); i++) {

            Line line = lineList.get(i);//获取line对象


            //规律：从第二行开始，每行的top总是比上一行多一个行高和垂直间距
            if(i>0){
                top += lineList.get(i-1).height + verticalSpace;
            }

            ArrayList<View> viewList = line.viewList;//获取line的子View集合
            //1.计算出右边留白的区域
            int remainSpace = noPaddingWidth - line.width;
            //2.计算每个子View平均分到的值
            float perSpace = remainSpace*1f / viewList.size();
            for (int j = 0; j < viewList.size(); j++) {
                final View view = viewList.get(j);
                //如果要对齐右边
                if(isAlignRight){
                    //3.将perSpace添加到view的宽度上，通过分给左右两边的padding
                    int halfSpace = (int) (perSpace/2);
                    view.setPadding(view.getPaddingLeft()+halfSpace, view.getPaddingTop(),
                            view.getPaddingRight()+halfSpace, view.getPaddingBottom());
                    //主动重新测量，否则padidng的修改不会增大VIew的宽度
                    view.measure(0,0);
                }

                if(j==0){
                    //说明摆放的是第一个View
//                  view.layout(left, top, left+view.getMeasuredWidth(), getBottom());
                    view.layout(left, top, left+view.getMeasuredWidth(), top+view.getMeasuredHeight());
                }else {
                    //说明摆放的是后面的VIew，左边就等于前一个View的right+水平间距
                    View preView = viewList.get(j-1);
                    int currentLeft = preView.getRight()+horizontalSpace;
                    view.layout(currentLeft, preView.getTop(), currentLeft+view.getMeasuredWidth(),
                            preView.getBottom());
                }
//                final int finalJ = j;
                view.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.transtop);
                                animation.setInterpolator(new OvershootInterpolator());
                                view.startAnimation(animation);


                                break;
                            case MotionEvent.ACTION_UP:
                                startEndAnimation(view);


                                break;
                            case MotionEvent.ACTION_MOVE:


                                boolean isInRect = isInRect(event, 2);
                                if (!isInRect) {
                                    startEndAnimation(view);
                                }

                                break;

                        }
                        return true;
                    }
                });
//


            }
        }
    }

    private void startEndAnimation(View view) {
        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.transdown);
        view.startAnimation(animation2);
    }

    public class Line {
        //获取每行的宽度  添加view的同时更新line的宽度
        public ArrayList<View> viewList = new ArrayList<>();
        public int width;
        public int height;
        public void addView(View child) {
            viewList.add(child);
            if (lineList.size() == 0) {
                width = child.getMeasuredWidth();
            }else{
                width += child.getMeasuredWidth() + horizontalSpace;
            }
            height = child.getMeasuredHeight();
        }
    }

    private boolean isAlignRight = true;
    public void isAlign(boolean s) {
        this.isAlignRight = s;
    }

    protected void getAllRect() {
        rects = new Rect[getChildCount()];

        //通过判断此控件s的孩子节点有多少个,判断要生成多少个矩形
        for (int i = 0; i < getChildCount(); i++) {
            //获取每一个孩子节点的view对象
            View view = getChildAt(i);
            Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            rects[i] = rect;

        }
    }

    protected boolean isInRect(MotionEvent event,int j ) {
        int locationX = (int) event.getX();//手指移动到的x轴的坐标
        int locationY = (int) event.getY();//手指移动到的y轴的坐标

        //判断手指的(x,y)落在那个矩形区域内

            if(rects[j].contains(locationX,locationY)){
                return true;
            }

        return false;
    }


}
