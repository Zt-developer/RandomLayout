package com.zhangtian.randomlayout;


import android.graphics.Color;

import java.util.Random;


/**
 * Created by Fast on 2017/5/7.
 */

public class ColorUtils {
    public static int getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(200);
        int green = random.nextInt(200);
        int blue = random.nextInt(200);
        return Color.rgb(red, green, blue);

    }

}
