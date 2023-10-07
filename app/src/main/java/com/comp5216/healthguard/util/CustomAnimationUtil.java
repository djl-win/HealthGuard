package com.comp5216.healthguard.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.comp5216.healthguard.R;

/**
 * 自定义动画加载的工具类
 * <p>
 * 调用其中方法来加载自定义动画
 * </p>
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-22
 */
public class CustomAnimationUtil {

    /**
     * 登录按钮的动画
     * @param context 当前的 Activity 或 Context
     * @param buttonToAnimate 要启动动画的按钮
     */
    public static void ButtonsAnimation(Context context, Button buttonToAnimate) {

        // 加载并启动动画
        Animation buttonAnimation = AnimationUtils.loadAnimation(context, R.anim.button_anim);
        buttonToAnimate.startAnimation(buttonAnimation);
    }


}
