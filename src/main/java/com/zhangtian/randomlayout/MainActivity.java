package com.zhangtian.randomlayout;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.parent)
    ConstraintLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ArrayList<String> lists = new ArrayList<>();

        lists.add("小黑");
        lists.add("敌法");
        lists.add("GA");
        lists.add("影魔");
        lists.add("卡尔");
        lists.add("地卜");
        lists.add("白牛");
        lists.add("lion");
        lists.add("火女");
        lists.add("冰女");
        lists.add("骨法");
        lists.add("小黑1");
        lists.add("敌法1");
        lists.add("GA1");
        lists.add("影魔1");
        lists.add("卡尔1");
        lists.add("地卜1");
        lists.add("白牛1");
        lists.add("lion1");
        lists.add("火女1");
        lists.add("冰女1");
        lists.add("骨法1");


        StellarMap stellarMap = new StellarMap(this);
        int padding = getResources().getDimensionPixelSize(R.dimen.dp12);
        stellarMap.setInnerPadding(padding, padding, padding, padding);

        StellAdapter adapter = new StellAdapter(lists);
        stellarMap.setAdapter(adapter);
        //设置默认显示第几组,否则刚进来不显示数据
        stellarMap.setGroup(0, true);
        //设置在SteallarMap中显示的View的密度,只要x,y相乘大于每页n个就行，不要搞特别大
        stellarMap.setRegularity(4, 4);
        parent.addView(stellarMap);

    }

    class StellAdapter implements StellarMap.Adapter {

        private final ArrayList<String> lists;

        public StellAdapter(ArrayList<String> list) {
            this.lists = list;
        }

        /**
         * 返回有多少组的数据
         *
         * @return
         */
        @Override
        public int getGroupCount() {
            return 3;
        }

        /**
         * 返回指定group有多少个数据,当前项目中每组是11个数据
         *
         * @param group
         * @return
         */
        @Override
        public int getCount(int group) {
            return lists.size() / getGroupCount();
        }

        /**
         * 返回每一个View对象
         *
         * @param group       当前是第几组
         * @param position    当前group中的位置
         * @param convertView
         * @return
         */
        @Override
        public View getView(int group, int position, View convertView) {
            TextView textview = new TextView(MainActivity.this);
            int index = group * getCount(group) + position;
            textview.setText(lists.get(index));
            Random random = new Random();
            textview.setTextSize(random.nextInt(14) + 10);
            textview.setTextColor(ColorUtils.getRandomColor());

            return textview;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        /**
         * 当缩放动画执行完，下一组加载哪一个组的数据
         *
         * @param group    当前是第几组
         * @param isZoomIn
         * @return 返回的值表示下一组要加载的数据
         */
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {

            return (group + 1) % getGroupCount();
        }
    }
}
