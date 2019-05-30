package com.realmo.utils;

import android.view.animation.Interpolator;

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBar
 * @email momo.weiye@gmail.com
 * @time 2019/5/29 16:01
 * @describe 弹簧插值器
 */
public class SpringInterpolator implements Interpolator {

    private final float factor;

    public SpringInterpolator() {
        factor = 0.4f;
    }

    public SpringInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2,-10*input)*Math.sin((input-factor/4)*(2*Math.PI)/factor)+1);
    }
}
