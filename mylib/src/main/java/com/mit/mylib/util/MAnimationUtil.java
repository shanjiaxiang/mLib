package com.mit.mylib.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MAnimationUtil {
    public static final long DEFAULT_ANIMATION_DURATION = 1000L;

    private MAnimationUtil() {
        throw new Error("Do not need instantiate!");
    }

    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, long durationMillis, Animation.AnimationListener animationListener) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        rotateAnimation.setDuration(durationMillis);
        if(animationListener != null) {
            rotateAnimation.setAnimationListener(animationListener);
        }

        return rotateAnimation;
    }

    public static RotateAnimation getRotateAnimationByCenter(long durationMillis, Animation.AnimationListener animationListener) {
        return getRotateAnimation(0.0F, 359.0F, 1, 0.5F, 1, 0.5F, durationMillis, animationListener);
    }

    public static RotateAnimation getRotateAnimationByCenter(long duration) {
        return getRotateAnimationByCenter(duration, (Animation.AnimationListener)null);
    }

    public static RotateAnimation getRotateAnimationByCenter(Animation.AnimationListener animationListener) {
        return getRotateAnimationByCenter(1000L, animationListener);
    }

    public static RotateAnimation getRotateAnimationByCenter() {
        return getRotateAnimationByCenter(1000L, (Animation.AnimationListener)null);
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis, Animation.AnimationListener animationListener) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(durationMillis);
        if(animationListener != null) {
            alphaAnimation.setAnimationListener(animationListener);
        }

        return alphaAnimation;
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis) {
        return getAlphaAnimation(fromAlpha, toAlpha, durationMillis, (Animation.AnimationListener)null);
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, Animation.AnimationListener animationListener) {
        return getAlphaAnimation(fromAlpha, toAlpha, 1000L, animationListener);
    }

    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha) {
        return getAlphaAnimation(fromAlpha, toAlpha, 1000L, (Animation.AnimationListener)null);
    }

    public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis, Animation.AnimationListener animationListener) {
        return getAlphaAnimation(1.0F, 0.0F, durationMillis, animationListener);
    }

    public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis) {
        return getHiddenAlphaAnimation(durationMillis, (Animation.AnimationListener)null);
    }

    public static AlphaAnimation getHiddenAlphaAnimation(Animation.AnimationListener animationListener) {
        return getHiddenAlphaAnimation(1000L, animationListener);
    }

    public static AlphaAnimation getHiddenAlphaAnimation() {
        return getHiddenAlphaAnimation(1000L, (Animation.AnimationListener)null);
    }

    public static AlphaAnimation getShowAlphaAnimation(long durationMillis, Animation.AnimationListener animationListener) {
        return getAlphaAnimation(0.0F, 1.0F, durationMillis, animationListener);
    }

    public static AlphaAnimation getShowAlphaAnimation(long durationMillis) {
        return getAlphaAnimation(0.0F, 1.0F, durationMillis, (Animation.AnimationListener)null);
    }

    public static AlphaAnimation getShowAlphaAnimation(Animation.AnimationListener animationListener) {
        return getAlphaAnimation(0.0F, 1.0F, 1000L, animationListener);
    }

    public static AlphaAnimation getShowAlphaAnimation() {
        return getAlphaAnimation(0.0F, 1.0F, 1000L, (Animation.AnimationListener)null);
    }

    public static ScaleAnimation getLessenScaleAnimation(long durationMillis, Animation.AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F);
        scaleAnimation.setDuration(durationMillis);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }

    public static ScaleAnimation getLessenScaleAnimation(long durationMillis) {
        return getLessenScaleAnimation(durationMillis, (Animation.AnimationListener)null);
    }

    public static ScaleAnimation getLessenScaleAnimation(Animation.AnimationListener animationListener) {
        return getLessenScaleAnimation(1000L, animationListener);
    }

    public static ScaleAnimation getAmplificationAnimation(long durationMillis, Animation.AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        scaleAnimation.setDuration(durationMillis);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }

    public static ScaleAnimation getAmplificationAnimation(long durationMillis) {
        return getAmplificationAnimation(durationMillis, (Animation.AnimationListener)null);
    }

    public static ScaleAnimation getAmplificationAnimation(Animation.AnimationListener animationListener) {
        return getAmplificationAnimation(1000L, animationListener);
    }
}
